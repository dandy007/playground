"""Iterative web research agent using headless Chrome and OpenRouter LLM."""

import argparse
import json
import os
from urllib.parse import quote_plus

BASE_DIR = os.path.dirname(__file__)

from dotenv import load_dotenv
load_dotenv(os.path.join(BASE_DIR, ".env"))

import requests
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager


# ----------------------- Google search helpers -----------------------

def _create_driver() -> webdriver.Chrome:
    """Create headless Chrome driver."""
    opts = Options()
    opts.add_argument("--headless=new")
    opts.add_argument("--disable-gpu")
    opts.add_argument("--no-sandbox")
    driver_path = ChromeDriverManager().install()
    service = Service(driver_path)
    return webdriver.Chrome(service=service, options=opts)


def google_search(query: str, limit: int = 5):
    """Return a list of Google text search results."""
    driver = _create_driver()
    try:
        driver.get(f"https://www.google.com/search?q={quote_plus(query)}")
        soup = BeautifulSoup(driver.page_source, "html.parser")
        out = []
        for g in soup.select("div.g"):
            title = g.find("h3")
            link = g.find("a")
            snippet = g.find("span", class_="aCOpRe")
            if title and link:
                out.append(
                    {
                        "title": title.get_text(),
                        "link": link["href"],
                        "snippet": snippet.get_text() if snippet else "",
                    }
                )
            if len(out) >= limit:
                break
        return out
    finally:
        driver.quit()


def google_image_search(query: str, limit: int = 5):
    """Return a list of image urls and alt texts from Google image search."""
    driver = _create_driver()
    try:
        driver.get(f"https://www.google.com/search?q={quote_plus(query)}&tbm=isch")
        soup = BeautifulSoup(driver.page_source, "html.parser")
        imgs = []
        for img in soup.select("img"):
            src = img.get("src")
            alt = img.get("alt", "")
            if src:
                imgs.append({"src": src, "alt": alt})
            if len(imgs) >= limit:
                break
        return imgs
    finally:
        driver.quit()


def fetch_page_text(url: str, limit: int = 5000) -> str:
    """Fetch page HTML and return plain text up to limit characters."""
    try:
        r = requests.get(url, timeout=10)
        r.raise_for_status()
        soup = BeautifulSoup(r.text, "html.parser")
        text = soup.get_text(separator=" ", strip=True)
        return text[:limit]
    except Exception:
        return ""


# ----------------------- OpenRouter LLM -----------------------

def query_llm(messages, model="openrouter/auto"):

    """Call OpenRouter API with chat messages."""
    api_key = os.getenv("OPENROUTER_API_KEY")
    if not api_key:
        raise RuntimeError("OPENROUTER_API_KEY not set")
    headers = {
        "Authorization": f"Bearer {api_key}",
        "Content-Type": "application/json",
    }
    payload = {
        "model": model,
        "messages": messages,
    }
    resp = requests.post(
        "https://openrouter.ai/api/v1/chat/completions", json=payload, headers=headers
    )
    resp.raise_for_status()
    data = resp.json()
    return data["choices"][0]["message"]["content"].strip()


def load_system_prompt(model: str) -> str:
    """Load system prompt text for the given model."""
    fname = model.replace("/", "_") + ".txt"
    path = os.path.join(BASE_DIR, "system_prompts", fname)
    try:
        with open(path, "r", encoding="utf-8") as f:
            return f.read().strip()
    except FileNotFoundError:
        return "You are a helpful assistant."


# ----------------------- Research agent logic -----------------------

def iterative_research(prompt: str, rounds: int = 3, model: str = "openrouter/auto"):

    """Perform iterative research guided by an LLM."""
    history = []
    system_prompt = load_system_prompt(model)
    messages = [
        {"role": "system", "content": system_prompt},
        {"role": "user", "content": prompt},
    ]

    for _ in range(rounds):
        reply = query_llm(messages, model=model)
        if reply.startswith("DONE:"):
            return reply[len("DONE:") :].strip(), history
        query = reply.strip()
        text_results = google_search(query)
        image_results = google_image_search(query)
        # fetch text from first result for more context
        if text_results:
            text_results[0]["content"] = fetch_page_text(text_results[0]["link"])
        history.append({"query": query, "texts": text_results, "images": image_results})
        messages.append({"role": "assistant", "content": json.dumps(history)})
    # final summary
    final = query_llm(messages + [{"role": "assistant", "content": json.dumps(history)}], model=model)
    return final, history


# ----------------------- CLI -----------------------

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("prompt", help="research goal prompt")
    parser.add_argument("--rounds", type=int, default=3, help="max research rounds")
    parser.add_argument("--model", default="openrouter/auto", help="LLM model to use")

    args = parser.parse_args()

    summary, history = iterative_research(args.prompt, rounds=args.rounds, model=args.model)
    print("=== Summary ===")
    print(summary)
    print("=== History ===")
    print(json.dumps(history, indent=2))


if __name__ == "__main__":
    main()

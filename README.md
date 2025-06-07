# Research Agent

This project provides a simple Python agent that performs iterative web research
using Google search in headless Chrome and an LLM accessed via OpenRouter.

## Setup

1. Create and activate a Python virtual environment:

```bash
python3 -m venv .venv
source .venv/bin/activate
```

2. Install dependencies inside the virtual environment:

```bash
pip install -r requirements.txt
```

3. Create a `.env` file with your OpenRouter API key:

```bash
cp .env.example .env
echo "OPENROUTER_API_KEY=YOUR_KEY" >> .env  # edit with your key
```
The script automatically loads variables from `.env`.

4. Install Google Chrome or Chromium and ensure `chromedriver` is accessible on
your PATH. The script uses `webdriver-manager` to download a matching driver
when first executed.
5. Optionally edit the system prompt for your chosen model in the

   `system_prompts/` directory. The default prompt for `openrouter/auto`
   is stored in `system_prompts/openrouter_auto.txt`.

## Usage

Run the script with a research prompt:

```bash
python research_agent.py "history of artificial intelligence" --rounds 3 \
  --model openrouter/auto

```

The agent will iteratively generate search queries, collect text and image
results, and return a final summary along with a JSON log of the performed
searches. Use `--model` to choose a different OpenRouter model and provide a
matching prompt file in `system_prompts/`.

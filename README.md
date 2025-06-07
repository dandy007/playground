# Research Agent

This project provides a simple Python agent that performs iterative web research
using Google search in headless Chrome and an LLM accessed via OpenRouter.

## Setup

1. Install dependencies:

```bash
pip install -r requirements.txt
```

2. Install Google Chrome or Chromium and ensure `chromedriver` is accessible on
your PATH. The script uses `webdriver-manager` to download a matching driver
when first executed.

3. Set the environment variable `OPENROUTER_API_KEY` with your OpenRouter API
key.

4. Optionally edit the system prompt for your chosen model in the
   `system_prompts/` directory. The default prompt for `openrouter/llama3-70b`
   is stored in `system_prompts/openrouter_llama3-70b.txt`.

## Usage

Run the script with a research prompt:

```bash
python research_agent.py "history of artificial intelligence" --rounds 3 \
  --model openrouter/llama3-70b
```

The agent will iteratively generate search queries, collect text and image
results, and return a final summary along with a JSON log of the performed
searches. Use `--model` to choose a different OpenRouter model and provide a
matching prompt file in `system_prompts/`.

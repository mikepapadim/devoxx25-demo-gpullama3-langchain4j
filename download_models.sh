#!/bin/bash

# Directory where models will be stored
MODEL_DIR="models"

# List of model URLs to download
MODEL_URLS=(
  "https://huggingface.co/beehive-lab/Llama-3.2-8B-Instruct-GGUF-FP16/resolve/main/beehive-llama-3.2-8b-instruct-fp16.gguf"
  "https://huggingface.co/beehive-lab/Llama-3.2-1B-Instruct-GGUF-FP16/resolve/main/beehive-llama-3.2-1b-instruct-fp16.gguf"
  "https://huggingface.co/hdnh2006/DeepSeek-R1-Distill-Qwen-1.5B-GGUF/resolve/main/DeepSeek-R1-Distill-Qwen-1.5B-F16.gguf"
  "https://huggingface.co/XelotX/DeepSeek-R1-Distill-Qwen-7B-GGUF/resolve/main/DeepSeek-R1-Distill-Qwen-7B-F16.gguf"
  "https://huggingface.co/microsoft/Phi-3-mini-4k-instruct-gguf/blob/main/Phi-3-mini-4k-instruct-fp16.gguf"
  "https://huggingface.co/ggml-org/Qwen3-4B-GGUF/resolve/main/Qwen3-4B-f16.gguf"
  "https://huggingface.co/ggml-org/Qwen3-8B-GGUF/resolve/main/Qwen3-8B-f16.gguf"
  "https://huggingface.co/XelotX/DeepSeek-R1-Distill-Qwen-7B-GGUF/resolve/main/DeepSeek-R1-Distill-Qwen-7B-F16.gguf"
  "https://huggingface.co/ggml-org/Qwen3-1.7B-GGUF/resolve/main/Qwen3-1.7B-f16.gguf"
  "https://huggingface.co/MaziyarPanahi/Mistral-7B-Instruct-v0.3-GGUF/resolve/main/Mistral-7B-Instruct-v0.3.fp16.gguf"
)

# Create models directory if it doesn't exist
if [ ! -d "$MODEL_DIR" ]; then
    echo "Creating directory: $MODEL_DIR"
    mkdir -p "$MODEL_DIR"
fi

# Loop over each model URL
for URL in "${MODEL_URLS[@]}"; do
    FILENAME=$(basename "$URL")
    echo "Downloading $FILENAME into $MODEL_DIR..."
    wget -c "$URL" -O "$MODEL_DIR/$FILENAME"
done

echo "✅ All downloads complete. Files saved in $MODEL_DIR/"


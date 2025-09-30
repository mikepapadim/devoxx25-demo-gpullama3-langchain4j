package org.example;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.gpullama3.GPULlama3ChatModel;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class _2_ChatMemoryExample {
    // Stateful chat example
    // Tested with Qwen3-1.7B-f16.gguf &&

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // Create model
        Path modelPath = Paths.get("/home/mikepapadim/Storage/gguf_models/Qwen3-1.7B-f16.gguf");

        //@formatter:off
        GPULlama3ChatModel model = GPULlama3ChatModel.builder()
                .modelPath(modelPath)
                .temperature(0.6)
                .topP(1.0)
                .maxTokens(8192)
                .onGPU(Boolean.TRUE) // if false, runs on CPU though a lightweight implementation of llama3.java
                .build();
        //@formatter:on

        String systemPrompt = "You are a helpful assistant. Always respond in a friendly manner and provide helpful information.";
        String userPrompt1 = "Hi, my name is Orion. Who are you?";

        System.out.println("User: " + userPrompt1);

        ChatRequest request = ChatRequest.builder().messages(SystemMessage.from(systemPrompt), UserMessage.from(userPrompt1)).build();

        ChatResponse response = model.chat(request);

        System.out.println("AI thinking: " + response.aiMessage().thinking());
        System.out.println("AI: " + response.aiMessage().text());

        /// Check memory
        String userPrompt2 = "What is the name of the user?";

        System.out.println("User: " + userPrompt2);

        request = ChatRequest.builder().messages(UserMessage.from(userPrompt2)).build();

        response = model.chat(request);

        System.out.println("AI thinking: " + response.aiMessage().thinking());
        System.out.println("AI: " + response.aiMessage().text());
    }
}

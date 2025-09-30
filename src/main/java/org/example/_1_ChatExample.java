package org.example;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.gpullama3.GPULlama3ChatModel;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class _1_ChatExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // Create model
        //Tested witg Qwen3-1.7B-f16.gguf &&
        Path modelPath = Paths.get("/home/mikepapadim/Storage/gguf_models/Qwen3-1.7B-f16.gguf");

        //@formatter:off
        GPULlama3ChatModel model = GPULlama3ChatModel.builder()
                .modelPath(modelPath)
                .temperature(0.6)
                .topP(1.0)
                .maxTokens(2048)
                .onGPU(Boolean.TRUE) // if false, runs on CPU though a lightweight implementation of llama3.java
                .build();

        //@formatter:on

        String systemPrompt = "You are a helpful assistant.";
        String userPrompt1 = "Hi, my name is Orion. Who are you?";

        System.out.println("User: " + userPrompt1);

        //@formatter:off
        ChatRequest request = ChatRequest.builder()
                .messages(
                        SystemMessage.from(systemPrompt),
                        UserMessage.from(userPrompt1)
                )
                .build();
        //@formatter:on

        ChatResponse response = model.chat(request);

        System.out.println("AI: " + response.aiMessage().text());
    }
}

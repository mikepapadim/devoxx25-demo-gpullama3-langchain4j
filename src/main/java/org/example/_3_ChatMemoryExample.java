package org.example;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.gpullama3.GPULlama3StreamingChatModel;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class _3_ChatMemoryExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // Create model
        Path modelPath = Paths.get("/home/devoxx2025-demo/java-ai-demos/models/Qwen3-8B-f16.gguf");

        GPULlama3StreamingChatModel model = GPULlama3StreamingChatModel.builder()
                .modelPath(modelPath)
                .temperature(0.6)
                .topP(0.95)
                .maxTokens(8192)
                .onGPU(Boolean.TRUE) // if false, runs on CPU though a lightweight implementation of llama3.java
                .build();

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        String systemPrompt = "You are a helpful assistant. Always respond in a friendly manner and provide helpful information.";
        chatMemory.add(SystemMessage.from(systemPrompt));

        /// Step 1: Introduction
        UserMessage msg1 = UserMessage.from("Hi! I'm Orion. What is your name?");
        processMessage(model, chatMemory, msg1);

        /// Step 2: Check memory
        UserMessage msg2 = UserMessage.from("What is my name?");
        processMessage(model, chatMemory, msg2);

    }

    private static void processMessage(GPULlama3StreamingChatModel model, ChatMemory chatMemory,
                                       UserMessage userMessage)
            throws ExecutionException, InterruptedException {

        chatMemory.add(userMessage);

        System.out.println("[User]: " + userMessage.singleText());
        System.out.print("[AI]: ");

        AiMessage aiMessage = streamChat(model, chatMemory);
        chatMemory.add(aiMessage);

        System.out.println("\n" + "=".repeat(80) + "\n");
    }

    private static AiMessage streamChat(GPULlama3StreamingChatModel model, ChatMemory chatMemory)
            throws ExecutionException, InterruptedException {

        ChatRequest streamRequest = ChatRequest.builder()
                .messages(chatMemory.messages())
                .build();

        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

        model.chat(streamRequest, new StreamingChatResponseHandler() {

            @Override
            public void onPartialThinking(PartialThinking partialThinking) {
                // Show thinking process
                System.out.print(partialThinking.text());
            }

            @Override
            public void onPartialResponse(String partialResponse) {
                System.out.print(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                //System.out.flush();
                futureResponse.complete(completeResponse);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("\nError: " + error.getMessage());
                futureResponse.completeExceptionally(error);
            }
        });

        return futureResponse.get().aiMessage();
    }
}

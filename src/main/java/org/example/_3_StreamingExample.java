package org.example;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.gpullama3.GPULlama3StreamingChatModel;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class _3_StreamingExample {
    public static void main(String[] args) {

        // Create model
        Path modelPath = Paths.get("/home/orion/LLMModels/Qwen3-8B-f16.gguf");

        GPULlama3StreamingChatModel model = GPULlama3StreamingChatModel.builder()
                .modelPath(modelPath)
                .temperature(0.6)
                .topP(1.0)
                .maxTokens(2048)
                .onGPU(Boolean.TRUE) // if false, runs on CPU though a lightweight implementation of llama3.java
                .build();


        // Create request
        String prompt;

        if (args.length > 0) {
            prompt = args[0];
            System.out.println("User Prompt: " + prompt);
        } else {
            prompt = "What is the capital of France?";
            System.out.println("Example Prompt: " + prompt);
        }

        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

        ChatRequest request = ChatRequest.builder()
                .messages(
                        UserMessage.from(prompt),
                        SystemMessage.from("reply with extensive sarcasm"))
                .build();

        // @formatter:on

        model.chat(request, new StreamingChatResponseHandler() {

            @Override
            public void onPartialThinking(PartialThinking partialThinking) {
                System.out.print(partialThinking.text());
            }

            @Override
            public void onPartialResponse(String partialResponse) {
                System.out.print(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                System.out.println(); // New line after streaming

                futureResponse.complete(completeResponse);
                model.printLastMetrics();
            }

            @Override
            public void onError(Throwable error) {
                futureResponse.completeExceptionally(error);
            }
        });

        futureResponse.join();
    }
}
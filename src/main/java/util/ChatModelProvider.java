package util;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.gpullama3.GPULlama3ChatModel;
//import dev.langchain4j.model.openai.OpenAiChatModel;

import java.nio.file.Path;
import java.nio.file.Paths;

//import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

public class ChatModelProvider {

    static Path modelPath = Paths.get("/home/orion/LLMModels/beehive-llama-3.2-8b-instruct-fp16.gguf"); // works
    //static Path modelPath = Paths.get("/home/orion/LLMModels/Qwen3-8B-f16.gguf"); // works -> add /no_think in prompt

    /**
     * Creates and returns a ChatModel.
     */
    public static ChatModel createChatModel(Boolean onGPU) {
        return GPULlama3ChatModel.builder()
                .modelPath(modelPath)
                .temperature(0.1)   // low randomness
                .topP(0.85)         // restrict sampling
                .maxTokens(5000)    // enough space for CV
                .onGPU(onGPU)       // GPU or CPU
                .build();
    }

    /**
     * Creates and returns a ChatModel configured to use the LLaMA 1B model.
     */
    public static ChatModel createChatModelLlama1B(int maxTokens, Boolean onGPU) {
        return GPULlama3ChatModel.builder()
                .modelPath(Paths.get("/home/orion/LLMModels/beehive-llama-3.2-1b-instruct-fp16.gguf"))
                .temperature(0.1)   // low randomness
                .topP(0.85)         // restrict sampling
                .maxTokens(maxTokens)    // enough space for CV
                .onGPU(onGPU)       // GPU or CPU
                .build();
    }

    /**
     * Creates and returns a ChatModel configured to use the LLaMA 8B model.
     *
     * Works well with: _1b_Basic_Agent_Example_Structured
     */
    public static ChatModel createChatModelLlama8B(int maxTokens, Boolean onGPU) {
        return GPULlama3ChatModel.builder()
                .modelPath(Paths.get("/home/orion/LLMModels/beehive-llama-3.2-8b-instruct-fp16.gguf"))
                .temperature(0.1)   // low randomness
                .topP(0.85)         // restrict sampling
                .maxTokens(maxTokens)    // enough space for CV
                .onGPU(onGPU)       // GPU or CPU
                .build();
    }

    /**
     * Creates and returns a ChatModel configured to use the Qwen3 0.6B model.
     */
    public static ChatModel createChatModelQwen3_06B(Boolean onGPU) {
        return GPULlama3ChatModel.builder()
                .modelPath(Paths.get("/home/orion/LLMModels/Qwen3-0.6B-f16.gguf"))
                .temperature(0.1)   // low randomness
                .topP(0.85)         // restrict sampling
                .maxTokens(5000)    // enough space for CV
                .onGPU(onGPU)       // GPU or CPU
                .build();
    }

    /**
     * Creates and returns a ChatModel configured to use the Qwen3 8B model.
     *
     * Works well with: _1b_Basic_Agent_Example_Structured
     */
    public static ChatModel createChatModelQwen3_8B(Boolean onGPU) {
        return GPULlama3ChatModel.builder()
                .modelPath(Paths.get("/home/orion/LLMModels/Qwen3-8B-f16.gguf"))
                .temperature(0.1)   // low randomness
                .topP(0.85)         // restrict sampling
                .maxTokens(5000)    // enough space for CV
                .onGPU(onGPU)       // GPU or CPU
                .build();
    }

}

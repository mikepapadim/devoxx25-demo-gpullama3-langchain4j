package org.example.agents._1_basic_agent;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.chat.ChatModel;
import util.ChatModelProvider;
import util.StringLoader;
import util.log.CustomLogging;
import util.log.LogLevels;

import java.io.IOException;

public class _1a_Basic_Agent_Example {

    /**
     * This example demonstrates how to implement a basic agent to demonstrate the syntax
     * Note that agents are only useful when combined with other agents, which we will show in the next steps.
     * For just one agent, you can better use an AiService.
     *
     * This basic agent turns a user's life story into a clean and complete CV.
     * Note that running this can take a while because the outputted CV
     * will be quite lengthy and the model needs a while.
     */

    // Set logging level
    static {
        CustomLogging.setLevel(LogLevels.DEBUG, 300);  // control how much you see from the model calls
    }

    public static void main(String[] args) throws IOException {

        // 0. Define GPU or CPU from args
        if (args.length == 0) {
            System.err.println("Usage: java Main <GPU|CPU>");
            System.exit(1);
        }

        boolean useGPU = args[0].equalsIgnoreCase("GPU");

        // 1. Define the model that will power the agent
        final ChatModel CHAT_MODEL = ChatModelProvider.createChatModel(useGPU);

        // 2. Define the agent behavior in agent_interfaces/CvGenerator.java

        // 3. Create the agent using AgenticServices
        CvGenerator cvGenerator = AgenticServices
                .agentBuilder(CvGenerator.class)
                .chatModel(CHAT_MODEL)
                .outputName("masterCv") // we can optionally define the name of the output object
                .build();

        // 4. Load text file from resources/documents/user_life_story.txt
        String lifeStory = StringLoader.loadFromResource("/documents/user_life_story.txt");

        // 5. We call the agent to generate the CV
        long startTime = System.nanoTime();
        String cv = cvGenerator.generateCv(lifeStory);
        long endTime = System.nanoTime();


        // 6. and print the generated CV
        System.out.println("=== CV ===");
        System.out.println(cv);

        // In example 1b we'll build the same agent but with structured output

        long durationMs = (endTime - startTime) / 1_000_000; // convert to ms
        System.out.println("\n=== Performance ===");
        System.out.println("Inference time: " + durationMs + " ms");

    }
}
package org.example.agents.storytelling;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import util.ChatModelProvider;
import util.log.CustomLogging;
import util.log.LogLevels;

import java.util.Map;

public class Storytelling {

    static {
        CustomLogging.setLevel(LogLevels.PRETTY, 300);
    }

    private static final ChatModel LLAMA_CHAT_MODEL_GPU = ChatModelProvider.createChatModel(true);
    private static final ChatModel LLAMA_CHAT_MODEL = ChatModelProvider.createChatModel(false);

    public static void main(String[] args) {

        // Start with initial story
        final StringBuilder storyBuilder = new StringBuilder("Once");

        // 1. Create two storyteller agents - they take "story" and output "story"
        StorytellerA storytellerA = AgenticServices.agentBuilder(StorytellerA.class)
                .chatModel(LLAMA_CHAT_MODEL_GPU)
                .outputName("story") // Output the updated story
                .build();

        StorytellerB storytellerB = AgenticServices.agentBuilder(StorytellerB.class)
                .chatModel(LLAMA_CHAT_MODEL)
                .outputName("story") // Output the updated story
                .build();

        // Track the story building
        final int[] wordCounter = {1}; // Start at 1 since we have "Once"

        // 2. Build the story loop
        UntypedAgent storyGame = AgenticServices
                .loopBuilder()
                .subAgents(storytellerA, storytellerB) // They alternate adding words
                .outputName("story") // The story gets passed forward
                .exitCondition(agenticScope -> {
                    String newWord = (String) agenticScope.readState("story");

                    // Clean the new word
                    newWord = cleanWord(newWord);

                    if (newWord.isEmpty()) {
                        System.out.println("⚠️ Agent returned empty word, using placeholder");
                        newWord = "...";
                    }

                    // Check if it's a repeat of the last word
                    String[] currentWords = storyBuilder.toString().split("\\s+");
                    if (currentWords.length > 0 && newWord.equalsIgnoreCase(currentWords[currentWords.length - 1])) {
                        System.out.println("⚠️ Agent repeated last word '" + newWord + "', replacing with alternative");
                        // Use some alternatives to keep story going
                        String[] alternatives = {"suddenly", "then", "however", "but", "and", "very", "quite", "always"};
                        newWord = alternatives[(int)(Math.random() * alternatives.length)];
                    }

                    // Add the new word to our story
                    storyBuilder.append(" ").append(newWord);
                    wordCounter[0]++;

                    // Update state with full story for next agent
                    agenticScope.writeState("story", storyBuilder.toString());

                    // Determine who just added a word
                    String speaker = (wordCounter[0] % 2 == 0) ? "📖 Storyteller A" : "✍️ Storyteller B";

                    // Print the growing story
                    System.out.println("\n" + "─".repeat(70));
                    System.out.println("Word #" + wordCounter[0] + " by " + speaker + ": \"" + newWord + "\"");
                    System.out.println("─".repeat(70));
                    System.out.println("Story: " + storyBuilder.toString());
                    System.out.println();

                    // Stop after 30 words
                    if (wordCounter[0] >= 30) {
                        System.out.println("\n" + "═".repeat(70));
                        System.out.println("📚 STORY COMPLETE!");
                        System.out.println("═".repeat(70));
                        System.out.println("\n" + storyBuilder.toString());
                        System.out.println("\nTotal words: " + wordCounter[0]);
                        System.out.println("═".repeat(70));
                        return true;
                    }

                    return false;
                })
                .maxIterations(15) // 15 iterations = 30 words total
                .build();

        // 3. Start with the story so far
        Map<String, Object> arguments = Map.of("story", storyBuilder.toString());

        // 4. Start the game!
        System.out.println("📖 ONE WORD STORY BUILDER");
        System.out.println("═".repeat(70));
        System.out.println("Two AI agents will collaboratively build a story.");
        System.out.println("Each agent adds exactly ONE word per turn.");
        System.out.println("═".repeat(70));
        System.out.println("\nStarting word: " + storyBuilder.toString());

        storyGame.invoke(arguments);
    }

    // Clean the word from model output
    private static String cleanWord(String word) {
        if (word == null) return "";

        // Remove extra whitespace
        word = word.trim();

        // Take only the first word if multiple were returned
        String[] words = word.split("\\s+");
        if (words.length > 0) {
            word = words[0];
        }

        // Remove quotes if present
        word = word.replaceAll("^\"|\"$", "");
        word = word.replaceAll("^'|'$", "");

        return word;
    }
}

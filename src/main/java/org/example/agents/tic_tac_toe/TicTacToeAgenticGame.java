package org.example.agents.tic_tac_toe;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import domain.GameState;
import util.ChatModelProvider;
import util.log.CustomLogging;
import util.log.LogLevels;

import java.util.Map;

public class TicTacToeAgenticGame {

    static {
        CustomLogging.setLevel(LogLevels.PRETTY, 300);
    }

    private static final ChatModel LLAMA_CHAT_MODEL_GPU = ChatModelProvider.createChatModelLlama8B(5000,true);
    private static final ChatModel LLAMA_CHAT_MODEL = ChatModelProvider.createChatModelLlama1B(5000,false);

    public static void main(String[] args) {

        // 1. Initialize with empty board (declare early so we can reference it)
        String initialBoard = "---------";

        // 2. Create the two player agents - they return String (the updated board)
        PlayerX playerX = AgenticServices.agentBuilder(PlayerX.class)
                .chatModel(LLAMA_CHAT_MODEL_GPU)
                .outputName("board") // Just the board string
                .build();

        PlayerO playerO = AgenticServices.agentBuilder(PlayerO.class)
                .chatModel(LLAMA_CHAT_MODEL)
                .outputName("board") // Just the board string
                .build();

        // Track moves and previous board state
        final int[] moveCounter = {0};
        final String[] lastPlayer = {""};
        final String[] previousBoard = {initialBoard};

        // 3. Build the game loop with alternating players
        UntypedAgent ticTacToeGame = AgenticServices
                .loopBuilder()
                .subAgents(playerX, playerO) // Players alternate: X, O, X, O, ...
                .outputName("board")
                .exitCondition(agenticScope -> {
                    String board = (String) agenticScope.readState("board");
                    moveCounter[0]++;

                    // Determine which player just moved
                    String currentPlayer = (moveCounter[0] % 2 == 1) ? "X" : "O";
                    lastPlayer[0] = currentPlayer;

                    // Clean up the board string
                    board = cleanBoard(board);

                    // Validate board format
                    if (board == null || board.length() != 9) {
                        System.out.println("⚠️  Invalid board received: " + board);
                        board = extractBoard(board);
                    }

                    // Validate that only one move was made
                    String prev = previousBoard[0];
                    int differences = 0;
                    for (int i = 0; i < 9; i++) {
                        if (board.charAt(i) != prev.charAt(i)) {
                            differences++;
                        }
                    }

                    if (differences > 1) {
                        System.out.println("⚠️  Player " + currentPlayer + " made " + differences + " moves! Reverting to single move.");
                        board = fixMultipleMoves(prev, board, currentPlayer);
                    } else if (differences == 0) {
                        System.out.println("⚠️  Player " + currentPlayer + " made no move! Making a random move.");
                        board = makeRandomMove(prev, currentPlayer);
                    }

                    // Update state and track previous board
                    agenticScope.writeState("board", board);
                    previousBoard[0] = board;

                    // Print current state
                    printBoard(board, "Move #" + moveCounter[0] + " - Player " + currentPlayer);

                    // Check for winner
                    String winner = checkWinner(board);
                    if (winner != null) {
                        System.out.println("\n🎮 GAME OVER! Result: " + winner);
                        return true;
                    }

                    // Check for tie
                    if (!board.contains("-")) {
                        System.out.println("\n🎮 GAME OVER! Result: TIE");
                        return true;
                    }

                    System.out.println("Game continues...\n");
                    return false;
                })
                .maxIterations(5) // Maximum 5 iterations (10 moves), safety limit
                .build();

        // 3. Initialize with empty board
        //String initialBoard = "---------";

        Map<String, Object> arguments = Map.of("board", initialBoard);

        // 4. Start the game!
        System.out.println("🎮 Starting Tic-Tac-Toe Game with Agentic Players!");
        System.out.println("Player X vs Player O");
        System.out.println("=" .repeat(50));
        printBoard(initialBoard, "Initial empty board");

        String finalBoard = (String) ticTacToeGame.invoke(arguments);

        // 5. Print final results
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📊 FINAL GAME STATE");
        System.out.println("=".repeat(50));
        finalBoard = cleanBoard(finalBoard);
        printBoard(finalBoard, "Game Complete");
        String winner = checkWinner(finalBoard);
        System.out.println("\n🏆 Result: " + (winner != null ? winner : "No winner"));
        System.out.println("📝 Total moves: " + moveCounter[0]);
    }

    // Fix when player made multiple moves - keep only the first change
    private static String fixMultipleMoves(String prev, String current, String player) {
        char[] fixed = prev.toCharArray();
        for (int i = 0; i < 9; i++) {
            if (current.charAt(i) != prev.charAt(i) && prev.charAt(i) == '-') {
                fixed[i] = player.charAt(0);
                break; // Only take first valid move
            }
        }
        return new String(fixed);
    }

    // Make a random move when player made no move
    private static String makeRandomMove(String board, String player) {
        char[] move = board.toCharArray();
        for (int i = 0; i < 9; i++) {
            if (move[i] == '-') {
                move[i] = player.charAt(0);
                return new String(move);
            }
        }
        return board; // Board full, can't move
    }

    // Normalize the board: treat 0 and O as the same
    private static String normalizeBoard(String board) {
        if (board == null) return "---------";
        // Replace all zeros with O
        return board.replace('0', 'O');
    }

    // Clean the board string by extracting just the 9 characters
    private static String cleanBoard(String board) {
        if (board == null) return "---------";

        // Normalize 0 to O
        board = normalizeBoard(board);

        // Remove all whitespace and newlines
        board = board.replaceAll("\\s+", "");

        // Find the first occurrence of 9 consecutive valid characters
        for (int i = 0; i <= board.length() - 9; i++) {
            String candidate = board.substring(i, i + 9);
            if (candidate.matches("[XO-]{9}")) {
                return candidate;
            }
        }

        return board.length() >= 9 ? board.substring(0, 9) : board;
    }

    // Extract board from potentially verbose output
    private static String extractBoard(String output) {
        if (output == null) return "---------";

        // Normalize 0 to O
        output = normalizeBoard(output);

        // Try to find 9 consecutive characters that look like a board
        String cleaned = output.replaceAll("\\s+", "");
        for (int i = 0; i <= cleaned.length() - 9; i++) {
            String candidate = cleaned.substring(i, i + 9);
            if (candidate.matches("[XO-]{9}")) {
                return candidate;
            }
        }

        // Fallback: try to extract any sequence of X, O, and -
        StringBuilder board = new StringBuilder();
        for (char c : output.toCharArray()) {
            if (c == 'X' || c == 'O' || c == '-') {
                board.append(c);
                if (board.length() == 9) break;
            }
        }

        // Pad with dashes if too short
        while (board.length() < 9) {
            board.append('-');
        }

        return board.substring(0, 9);
    }

    // Check if there's a winner
    private static String checkWinner(String board) {
        if (board == null || board.length() != 9) return null;

        // All winning combinations
        int[][] wins = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // columns
                {0, 4, 8}, {2, 4, 6}              // diagonals
        };

        for (int[] win : wins) {
            char a = board.charAt(win[0]);
            char b = board.charAt(win[1]);
            char c = board.charAt(win[2]);

            if (a != '-' && a == b && b == c) {
                return String.valueOf(a);
            }
        }

        return null;
    }

    // Print the board in a readable format
    private static void printBoard(String board, String message) {
        System.out.println("\n" + message);
        System.out.println("-".repeat(50));
        if (board != null && board.length() >= 9) {
            System.out.printf(" %s | %s | %s %n",
                    board.charAt(0), board.charAt(1), board.charAt(2));
            System.out.println("-----------");
            System.out.printf(" %s | %s | %s %n",
                    board.charAt(3), board.charAt(4), board.charAt(5));
            System.out.println("-----------");
            System.out.printf(" %s | %s | %s %n",
                    board.charAt(6), board.charAt(7), board.charAt(8));
        } else {
            System.out.println("Invalid board: " + board);
        }
        System.out.println("-".repeat(50));
    }

}

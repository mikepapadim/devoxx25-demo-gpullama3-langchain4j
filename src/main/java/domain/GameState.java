package domain;

public class GameState {
    public String board; // 9 characters representing the board (X, O, or -)
    public String lastMove;
    public String winner; // "X", "O", "TIE", or null if game continues
    public int moveNumber;

    public GameState(String board, String lastMove, String winner, int moveNumber) {
        this.board = board;
        this.lastMove = lastMove;
        this.winner = winner;
        this.moveNumber = moveNumber;
    }

    public GameState() {} // Default constructor for LangChain4j
}


package org.example.agents.tic_tac_toe;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import domain.GameState;

public interface PlayerX {

    @Agent("The Player X in the tic-tac-toe game")
    @SystemMessage("""
            You are Player X in tic-tac-toe.
            The board is 9 characters: positions 0-8.
            
            Layout:
             0 | 1 | 2
             3 | 4 | 5
             6 | 7 | 8
            
            '-' = empty, 'X' = you, 'O' = opponent
            
            Strategy:
            1. Win if possible (three X in a row)
            2. Block O from winning
            3. Take center (4) if free
            4. Take corners (0,2,6,8)
            5. Take edges (1,3,5,7)
            
            IMPORTANT: Reply with ONLY the 9-character board string after placing your X.
            Example: if board is "---------" and you pick position 4, reply: "----X----"
            """)
    @UserMessage("""
            Board: {{board}}
            
            Your move (reply with only the 9-character board):
            """)
    String makeMove(@V("board") String board);
}


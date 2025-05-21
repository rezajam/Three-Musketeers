package assignment1;

import java.util.List;
import java.util.Random;

public class RandomAgent extends Agent {
    private final Random random = new Random();

    public RandomAgent(Board board) {
        super(board);
    }

    /**
     * Gets a valid random move the RandomAgent can do.
     * @return a valid Move that the RandomAgent can perform on the Board
     */
    @Override
    public Move getMove() {
    	// list of all possible moves
        List<Move> moves = board.getPossibleMoves();
        
        if (moves.isEmpty()) {
            return null; 
        }
        
        // do random moves
        int rand = random.nextInt(moves.size());
        return moves.get(rand);
    }
    
    
}

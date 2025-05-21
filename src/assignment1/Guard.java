package assignment1;

public class Guard extends Piece {

    public Guard() {
        super("O", Type.GUARD);
    }

    /**
     * Returns true if the Guard can move onto the given cell.
     * @param cell Cell to check if the Guard can move onto
     * @return True, if Guard can move onto given cell, false otherwise
     */
    @Override
    public boolean canMoveOnto(Cell cell) { // TODO
    	// Guard only moves to EMPTY cells (No Piece there)
    	// == null
    	return !cell.hasPiece(); 
//        return true;
    }
}

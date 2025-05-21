package assignment1;

import java.util.List;

public class BoardEvaluatorImpl implements BoardEvaluator {

    /**
     * Calculates a score for the given Board
     * A higher score means the Musketeer is winning
     * A lower score means the Guard is winning
     * Add heuristics to generate a score given a Board
     * @param board Board to calculate the score of
     * @return int Score of the given Board
     */
    @Override
    public int evaluateBoard(Board board) { // TODO
    	// remember: score is prefers Musketeers
    	// from current board we gather all muketeer and guard cells
        List<Cell> muskCells = board.getMusketeerCells();
        List<Cell> guardCells = board.getGuardCells();

        int score = 0;
        
        // We check if Musketeers are aligned (if so, Guards are winning)
        if (checkAlignment(muskCells)) {
            score -= 10;  // HUGE penalty if Musketeers are ALIGNED
        }
        
        // Then we calculate sum of distances between all Musketeers
        // Musketeers do better when they are spread out.
        int distScore = computeDistanceSumScore(muskCells);
        score += distScore;  

        // Then We calculate number of possible moves with the current board
        // for Musketeers
        score += getPossibleDestScore(muskCells, board);
        // for Guards
        score += getPossibleDestScore(guardCells, board);

        return score;
    }

    /**
     * Sums all the Possible Destinations for the given piece. 
     * The more destinations possible number the the highter the score.
     */
    private int getPossibleDestScore(List<Cell> pieces, Board board) {
        int destNum = 0;
        // for each given piece
        for (Cell piece : pieces) {
        	destNum += board.getPossibleDestinations(piece).size();
        }
        return destNum;
    }

    /**
     * Sums all distances between all Musketeers.
     */
    private int computeDistanceSumScore(List<Cell> muskCells) {
        int totalDistance = 0;
        // we calculate the distance of each muskCell to all muskCells
        for (int i = 0; i < muskCells.size(); i++) {
            for (int j = i + 1; j < muskCells.size(); j++) {
            	// we calculate the absolute distance
                totalDistance += computeAbsDistance(muskCells.get(i).getCoordinate(), muskCells.get(j).getCoordinate());
            }
        }
        return totalDistance;
    }

    /**
     * Computes the absolute distance between two coordinates.
     */
    private int computeAbsDistance(Coordinate c1, Coordinate c2) {
        return Math.abs(c1.row - c2.row) + Math.abs(c1.col - c2.col);
    }

    /**
     * Checks if all Musketeers are in the same row or column.
     * Same implemenation in isGameOver()
     */
    private boolean checkAlignment(List<Cell> MuskCells) {
    	
        int muskFirstRow = MuskCells.get(0).getCoordinate().row;
        int muskFirstCol = MuskCells.get(0).getCoordinate().col;
        
        boolean allMuskSameRow = true;
        boolean allMuskSameCol = true;
	
	        for (Cell MuskCell : MuskCells) {
            Coordinate coord = MuskCell.getCoordinate();
            if (coord.row != muskFirstRow) allMuskSameRow = false;
            if (coord.col != muskFirstCol) allMuskSameCol = false;
            
            if (!allMuskSameRow && !allMuskSameCol) break;  
        }
        // Returns true only if Musketeers are aligned in a row OR column.
        // if (allMuskSameRow || allMuskSameCol) {
        return true; 
    }
}

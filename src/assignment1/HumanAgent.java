package assignment1;

import java.util.Scanner;

public class HumanAgent extends Agent {

    Scanner scanner = new Scanner(System.in);

    public HumanAgent(Board board) {
        super(board);
    }

    /**
     * Asks the human for a move with from and to coordinates and makes sure its valid.
     * Create a Move object with the chosen fromCell and toCell
     * @return the valid human inputted Move
     */
    @Override
    public Move getMove() { // TODO
        while(true) {
            try{
            	// showing the board again
            	System.out.println("\n" + board);
            	// input prompt
            	System.out.printf("[%s] Enter your move: ", board.getTurn().getType());
                // input into UpperCase
                String input = scanner.nextLine().trim().toUpperCase();
                // input split via space
                String[] inputCells = input.split(" ");
                
                // check if input isValid
                if (!isValidInput(inputCells)) {
                    System.out.println("[Error] Invalid input! Please enter 2 positions with correct format\ne.g., moving from A1 to B1 will be [A1 B1].\n[Try again.]");
                    continue;
                }
                
                
                
                // Since it is correct, make string input to Coordinates                
                Coordinate fromCoor = inputToCoordinates(inputCells[0]);
                Coordinate toCoor = inputToCoordinates(inputCells[1]);

                // Get cell from those coordinates
                Cell fromCell = board.getCell(fromCoor);
                Cell toCell = board.getCell(toCoor);

                // Check if the piece at fromCell matches the current player's turn type 
                if (fromCell.getPiece().getType() != board.getTurn()) {
                	System.out.printf("[Error] Invalid move! You must move your own piece.\n[Try again.]\n", board.getTurn().getType());
                    continue;
                }
               
                
                
                // now get the Move from these cells
                Move move = new Move(fromCell, toCell);
                // isValidMove ?
                if (board.isValidMove(move)) {
                    return move;
                }else{
                    System.out.println("[Error] Invalid Move!\n[Try again.]");
                }
            }catch (Exception e){
                System.out.println("[Error] " + e.getMessage());
            }
        }
    }
    
    /**
     * check if the input is correct Format
     */
    private boolean isValidInput(String[] inputCells) {
        if (inputCells.length != 2) {
            return false; // Must have exactly two inputs (e.g., "A1 B2")
        }

        for (String cell : inputCells) {
        	// Each part must be exactly 2 characters long (e.g., "A1")
            if (cell.length() != 2) { 
                return false; 
            }
            
            // if so...extract A1 -> A and 1
            char colChar = cell.charAt(0);
            char rowChar = cell.charAt(1);
            
            // 1st character must be a letter, 2nd must be a digit
            if (!Character.isLetter(colChar) || !Character.isDigit(rowChar)) {
                return false; 
            }
        }
        return true;
    }
    
    
    /**
     * Convert string input to Coordinates like (0,1),...
     */
    private Coordinate inputToCoordinates(String input) {
        // Extract characters
        char colChar = input.charAt(0);
        char rowChar = input.charAt(1);

        // Convert colChar to number
        int col = 0;
        // Supports larger board sizes 
        String columns = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
        for (int i = 0; i < columns.length(); i++) {
            if (columns.charAt(i) == colChar) {
                col = i;
                break;
            }
        }

        // Convert row using getNumericValue()
        // A1 means column 0 row 1 -1=0 and so on ....
        int row = Character.getNumericValue(rowChar) - 1;

        // Final bounds check (depends on board size)
        if ( !(row >= 0 && row < board.size) && (col >= 0 && col < board.size) ) {
    		throw new IllegalArgumentException(
    	            String.format("Coordinates are out of bounds.")
    	    );
        }
        return new Coordinate(row, col);
    }
}

package assignment1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.Map;


public class Board {
    public int size = 5;

    // 2D Array of Cells for representation of the game board
    public final Cell[][] board = new Cell[size][size];

    private Piece.Type turn;
    private Piece.Type winner;
    // [x] Map to store captured pieces associated with each move
    private final Map<Move, Piece> capturedPieces = new HashMap<>();


    /**
     * Create a Board with the current player turn set.
     */
    public Board() {
        this.loadBoard("Boards/Starter.txt");
    }

    /**
     * Create a Board with the current player turn set and a specified board.
     * @param boardFilePath The path to the board file to import (e.g. "Boards/Starter.txt")
     */
    public Board(String boardFilePath) {
        this.loadBoard(boardFilePath);
    }

    /**
     * Creates a Board copy of the given board.
     * @param board Board to copy
     */
    public Board(Board board) {
        this.size = board.size;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.board[row][col] = new Cell(board.board[row][col]);
            }
        }
        this.turn = board.turn;
        this.winner = board.winner;
    }

    /**
     * @return the Piece.Type (Muskeeteer or Guard) of the current turn
     */
    public Piece.Type getTurn() {
        return turn;
    }

    /**
     * Get the cell located on the board at the given coordinate.
     * @param coordinate Coordinate to find the cell
     * @return Cell that is located at the given coordinate
     */
    public Cell getCell(Coordinate coordinate) { // TODO
        int row = coordinate.row;
        int col = coordinate.col;
        if (isCoordinatesWithinBounds(row, col)) {
        	return board[row][col];
        }else {
        	throw new IllegalArgumentException(
        			String.format("Coordinate (%d, %d) is out of bounds or invalid.", row, col)
    	    );
        }
    }

    /**
     * @return the game winner Piece.Type (Muskeeteer or Guard) if there is one otherwise null
     */
    public Piece.Type getWinner() {
        return winner;
    }

    /**
     * Gets all the Musketeer cells on the board.
     * @return List of cells
     */
    public List<Cell> getMusketeerCells() { // TODO
    	return getCellsByType(Piece.Type.MUSKETEER);
    }

    /**
     * Gets all the Guard cells on the board.
     * @return List of cells
     */
    public List<Cell> getGuardCells() { // TODO

    	return getCellsByType(Piece.Type.GUARD);
    }
    
    private List<Cell> getCellsByType(Piece.Type type) {
    	// list of all the Cells of given type only on the board
        List<Cell> cells = new ArrayList<>();
        
        // search for row and cols of 5x5 size 
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
            	// each targeted cell with its row and col
                Cell targetCell = board[row][col];
                //	check if the target cell has something && that something is a GUARD / MUSKETEER
                if (targetCell.hasPiece() && // check if the Cell 
                		targetCell.getPiece().getType().equals(type)) {
                    cells.add(targetCell);
                }
            }
        }
        return cells;
        

    }

    /**
     * Executes the given move on the board and changes turns at the end of the method.
     * @param move a valid move
     */
    public void move(Move move) { // TODO
        // Retrieve actual board cells (avoid deep copy issue)
        Cell fromCell = getCell(move.fromCell.getCoordinate());
        Cell toCell = getCell(move.toCell.getCoordinate());

        // Store the captured piece using a new Move reference copy that matches the board\
        // Create a new Move reference copy
        Move referenceMove = new Move(fromCell, toCell); 
        // Store captured piece (its the toCell-Piece)
        capturedPieces.put(referenceMove, toCell.getPiece()); 

        // ACTUALLY MOVE the piece from `fromCell` -> `toCell`
        Piece fromPiece = fromCell.getPiece();
        toCell.setPiece(fromPiece);
        // Clear out the original cell
        fromCell.setPiece(null); 
        
        // changeTurn
        changeTurn();
    }


    /**
     * Undo the move given.
     * @param move Copy of a move that was done and needs to be undone. The move copy has the correct piece info in the
     *             from and to cell fields. Changes turns at the end of the method.
     */
    public void undoMove(Move move) { // TODO
    	// Remember: here move is what we JUST DID !
        // Retrieve actual board cells (avoid deep copy issue)
        Cell fromCell = getCell(move.fromCell.getCoordinate());
        Cell toCell = getCell(move.toCell.getCoordinate());

        // Search for the corresponding Move object in capturedPieces list 
        // find (fromCell, toCell) -> our capturedPiece
        Move referenceMove = null;
        for (Move key : capturedPieces.keySet()) {
        	// key = move so
        	// move.fromCell.getCoordinate().equals(move.fromCell.getCoordinate())
            if (key.fromCell.getCoordinate().equals(move.fromCell.getCoordinate()) &&
                key.toCell.getCoordinate().equals(move.toCell.getCoordinate())) {
            	// if found we use that key to get the capturedPiece
                referenceMove = key;
                break;
            }
        }
        
        // but if not found --> ERROR
        if (referenceMove == null) {
            System.out.println("[ERROR] Undo failed. Move not found in capturedPieces.");
            return;
        }

        // Get the removed capturedPiece and update the capturedPieces 
        Piece capturedPiece = capturedPieces.remove(referenceMove);

        // Move the piece back to its original position
        Piece toPiece = toCell.getPiece();
        fromCell.setPiece(toPiece);

        // Restore the captured piece 
        // (OR leave empty if nothing was captured)
        toCell.setPiece(capturedPiece);

        // change turn
        changeTurn();
    }


    /**
     * Changes the Turn 
     */
    private void changeTurn() {
        this.turn = (this.turn == Piece.Type.MUSKETEER) ? Piece.Type.GUARD : Piece.Type.MUSKETEER;
    }

    /**
     * Checks if the given move is valid. Things to check:
     * (1) the toCell is next to the fromCell
     * (2) the fromCell piece can move onto the toCell piece.
     * @param move a move
     * @return     True, if the move is valid, false otherwise
     */
    public Boolean isValidMove(Move move) { // TODO
        Cell fromCell = move.fromCell;
        Cell toCell = move.toCell;
        
        // 1. check if fromCell IS ADJACENT to toCell
        // Distance of row coordinates of fromCell to toCell
        int rowDist = Math.abs(fromCell.getCoordinate().row - toCell.getCoordinate().row);
        // Distance of cols coordinates of fromCell to toCell
        int colDist = Math.abs(fromCell.getCoordinate().col - toCell.getCoordinate().col);
        boolean isAdjecent = (rowDist == 1 && colDist == 0) || (rowDist == 0 && colDist == 1);
        
        
        // 2. check if the fromCell-Piece can move to toCell-Piece:
        // == null
        if(!fromCell.hasPiece()) {
        	return false;
        }
        // to prevent NullPointerException
        boolean canMoveOnto = fromCell.getPiece().canMoveOnto(toCell);
        
        
        
//        return piece.canMoveOnto(toCell);
        return isAdjecent && canMoveOnto;



//        return true;
    }

    /**
     * Get all the possible cells that have pieces that can be moved this turn.
     * @return      Cells that can be moved from the given cells
     */
    public List<Cell> getPossibleCells() { // TODO
    	// list of all the Cells that possibly depending on the turn is a valid cell
        List<Cell> possibleCells = new ArrayList<>();
        
        // [x] max debug
//        System.out.println("Current turn: " + turn);
        
        // Loop through all cells on the board
        for (int row=0; row < size; row++) {
            for (int col=0; col < size; col++) {
                Cell possibleCell = board[row][col];

                // Check if possibleCell has something && that something is this current 'turn''s type (guard or Musketeer) 
                // && there are valid destinations for this piece to move to
                if(possibleCell.hasPiece() 
                		&&	possibleCell.getPiece().getType().equals(turn)
                		&&	!getPossibleDestinations(possibleCell).isEmpty()) {
                	
                	// [x] max debug
//                    System.out.println("Adding possible cell at: (" + row + ", " + col + ")");

                	
                	// if all was good --> then add to list of PossibleCells
                    possibleCells.add(possibleCell);
                }
            }
            
        }
        // [x] max debug
//        System.out.println("number of possible cells found "+ possibleCells.size());

        return possibleCells;
    }

    /**
     * Get all the possible cell destinations that is possible to move to from the fromCell.
     * @param fromCell The cell that has the piece that is going to be moved
     * @return List of cells that are possible to get to
     */
    public List<Cell> getPossibleDestinations(Cell fromCell) { // TODO
    	
    	// list of possibleDestinations and list of potentialAdjacentMoves
        List<Cell> possibleDestinations = new ArrayList<>();
        List<Move> potentialMoves = getAdjacentMoves(fromCell);
        

        if (!fromCell.hasPiece()) {
            // [x] max debug
//            System.out.println("No piece in the fromCell: " + fromCell);
            return possibleDestinations; // No piece -> no moves possible.
        }
        
        // [x] max debug
//        System.out.println("Checking possible moves for piece at: " + fromCell.getCoordinate());

        // loop through all the potential adjacent moves in the board
        for (Move move : potentialMoves) {
        	
            // [x] max debug
//            System.out.println("Evaluating move to: " + move.toCell.getCoordinate());
            
            // if each potential move is a valid move for the piece type -> add to possible Destinations
            if (isValidMove(move)) {
            	
                // [x] max debug
//                System.out.println("Valid move to: " + move.toCell.getCoordinate());
                possibleDestinations.add(move.toCell);
            }
        }
        
        
        return possibleDestinations;
    }
    
    /**
     * Gets all the potential adjacent moves given fromCell 
     */
    private List<Move> getAdjacentMoves(Cell fromCell) {
    	// list of all potential adjacent moves 
        List<Move> moves = new ArrayList<>();
        // extract coordinates of the given fromCell as coord
        Coordinate coord = fromCell.getCoordinate();
        
        // array of directions we move to (orthogonal) 
        int[][] directions = {{1, 0},   // to down
        					  {-1, 0},  // to up
        					  {0, 1},   // to right
        					  {0, -1}}; // to left
        					  
        // each fromCell coord + direction to move
        for (int[] dir : directions) {
            int newRow = coord.row + dir[0]; 
            int newCol = coord.col + dir[1];
            
            // then we check if the new moved coordinate now is not out of bounds
            if (isCoordinatesWithinBounds(newRow, newCol)) {
            	// if so, add each move to the moves list
                moves.add(new Move(fromCell, board[newRow][newCol]));
            }
        }

        return moves;
    }
    
    /**
     * Finds if the coordinates are within bounds 
     */
    private boolean isCoordinatesWithinBounds(int row, int col) {
        return row >= 0 && row < size &&  // row = [0, size]
        	   col >= 0 && col < size;    // col = [0, size]
    }
    
    /**
     * Get all the possible moves that can be made this turn.
     * @return List of moves that can be made this turn
     */
    public List<Move> getPossibleMoves() { // TODO
    	
    	// remember: moves are fromCell , toCell so (0,1)->(0,2) and (0,2)->(0,1) are 2 diff
        List<Move> allPossibleMoves = new ArrayList<>();
//        List<Cell> possibleCells = getPossibleCells();
        
        // we use the above getPossibleCells and destinations functions
        for (Cell fromCell : getPossibleCells()) { // from All possible cells
            for (Cell toCell : getPossibleDestinations(fromCell)) { // toCell destination each fromCell can be
                allPossibleMoves.add(new Move(fromCell, toCell));  
            }
        }

        return allPossibleMoves;
    }
    

    /**
     * Checks if the game is over and sets the winner if there is one.
     * @return True, if the game is over, false otherwise.
     */
    public boolean isGameOver() { // TODO
    	
    	// Guards won:     if Musketeers on  same row || same col
    	// Musketeers won: if Musketeers NOT same row || same col && Musketeers have no valid moves
    	List<Cell> MuskCells = getMusketeerCells();
        
        // Get the first Musketeer's row and column
        int muskFirstRow = MuskCells.get(0).getCoordinate().row;
        int muskFirstCol = MuskCells.get(0).getCoordinate().col;
        
        boolean allMuskSameRow = true;
        boolean allMuskSameCol = true;
         
        // Check if all Musketeers Cells are in the same row or column
        for (Cell MuskCell : MuskCells) {
            Coordinate coord = MuskCell.getCoordinate();
            // if MuskCell not same just one of FirstCol or FirstRow --> not all same 
            if (coord.row != muskFirstRow) allMuskSameRow = false;
            if (coord.col != muskFirstCol) allMuskSameCol = false;
            
            // If one MuskCell are just once not in the same row AND not in the same column -> stop checking
            if (!allMuskSameRow && !allMuskSameCol) break;  
            
            // if not both --> there is hope!
        }
        
        // After the loop in all Musk Cells if SameRow OR SameCol...
        if (allMuskSameRow || allMuskSameCol) {
            this.winner = Piece.Type.GUARD; // ...Guards win!
            return true; // and done!
        }
        
        // If not same row... 
        // ...Check if at least one Musketeer has a valid move
        for (Cell MuskCell : MuskCells) {
            if (!getPossibleDestinations(MuskCell).isEmpty()) {
                // Game is not over, Musketeers can still move
                return false; 
            }
        }
        
        // If musketeers are not same row and col...
        // and no valid moves exists for Musketeers... 
        this.winner = Piece.Type.MUSKETEER; // Musketeers win !
        return true; // done

    }

    /**
     * Saves the current board state to the boards directory
     */
    public void saveBoard() {
        String filePath = String.format("boards/%s.txt",
                new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        File file = new File(filePath);

        try {
            file.createNewFile();
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(turn.getType() + "\n");
            for (Cell[] row: board) {
                StringBuilder line = new StringBuilder();
                for (Cell cell: row) {
                    if (cell.getPiece() != null) {
                        line.append(cell.getPiece().getSymbol());
                    } else {
                        line.append("_");
                    }
                    line.append(" ");
                }
                writer.write(line.toString().strip() + "\n");
            }
            writer.close();
            System.out.printf("Saved board to %s.\n", filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Failed to save board to %s.\n", filePath);
        }
    }

    @Override
    public String toString() {
        StringBuilder boardStr = new StringBuilder("  | A B C D E\n");
        boardStr.append("--+----------\n");
        for (int i = 0; i < size; i++) {
            boardStr.append(i + 1).append(" | ");
            for (int j = 0; j < size; j++) {
                Cell cell = board[i][j];
                boardStr.append(cell).append(" ");
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }

    /**
     * Loads a board file from a file path.
     * @param filePath The path to the board file to load (e.g. "Boards/Starter.txt")
     */
    private void loadBoard(String filePath) {

        File file = new File(filePath);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.printf("File at %s not found.", filePath);
            System.exit(1);
        }

        turn = Piece.Type.valueOf(scanner.nextLine().toUpperCase());

        int row = 0, col = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] pieces = line.trim().split(" ");
            for (String piece: pieces) {
                Cell cell = new Cell(new Coordinate(row, col));
                switch (piece) {
                    case "O" -> cell.setPiece(new Guard());
                    case "X" -> cell.setPiece(new Musketeer());
                    default -> cell.setPiece(null);
                }
                this.board[row][col] = cell;
                col += 1;
            }
            col = 0;
            row += 1;
        }
        scanner.close();
        System.out.printf("Loaded board from %s.\n", filePath);
    }
}
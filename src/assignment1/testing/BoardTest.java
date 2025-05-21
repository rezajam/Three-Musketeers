package assignment1.testing;

import assignment1.*;
import org.junit.*;

import java.util.List;

public class BoardTest {
	// remember its (row, COL)
    private Board board;

    @Before
    public void setup() {
        this.board = new Board();
    }
    
    // max
    @Test
    public void testNoValidMovesForMusketeers() {
        // Load the board from the custom configuration file for this specific test
        this.board = new Board("Boards/BlockedMusketeers.txt");

        // Test that there are no possible moves for Musketeers
        List<Cell> possibleCells = board.getPossibleCells();
        Assert.assertTrue("There should be no possible cells for Musketeers to move", possibleCells.isEmpty());
    }
    
    // max
    @Test
    public void testUndoEdgeOfBoardMove() {
        this.board = new Board("Boards/undo_edge_of_board.txt");
        Cell musketeerCell = board.getCell(new Coordinate(0, 0));
        Cell guardCell = board.getCell(new Coordinate(1, 0));
        Move move = new Move(musketeerCell, guardCell);

        board.move(move);
        board.undoMove(move);
        
        // Check to see if piece at its original cell
        Assert.assertTrue("Musketeer should be back in original position", musketeerCell.getPiece().getType() == Piece.Type.MUSKETEER);
        Assert.assertTrue("Guard should be back in original position", guardCell.getPiece().getType() == Piece.Type.GUARD);
    }
    
    // max
    @Test
    public void testUndoMultipleMoves() {
        this.board = new Board("Boards/undo_multiple_moves.txt");
        Cell firstMusketeerCell = board.getCell(new Coordinate(1, 1));
        Cell firstGuardCell = board.getCell(new Coordinate(1, 2));
        Move firstMove = new Move(firstMusketeerCell, firstGuardCell);

        Cell secondMusketeerCell = board.getCell(new Coordinate(3, 3));
        Cell secondGuardCell = board.getCell(new Coordinate(3, 2));
        Move secondMove = new Move(secondMusketeerCell, secondGuardCell);

        board.move(firstMove);
        board.move(secondMove);

        board.undoMove(secondMove);
        board.undoMove(firstMove);

        Assert.assertTrue("First Musketeer should be back in original position", firstMusketeerCell.getPiece().getType() == Piece.Type.MUSKETEER);
        Assert.assertTrue("First Guard should be back in original position", firstGuardCell.getPiece().getType() == Piece.Type.GUARD);
        Assert.assertTrue("Second Musketeer should be back in original position", secondMusketeerCell.getPiece().getType() == Piece.Type.MUSKETEER);
        Assert.assertTrue("Second Guard should be back in original position", secondGuardCell.getPiece().getType() == Piece.Type.GUARD);
    }
    
    // ------- is valid move TESTS
	@Test
	public void isValidMoveGeneralTest() {
	    // Assume the board is set up with a Guard at position (2, 2) and an empty cell at (2, 3)
	    Cell guardCell = board.getCell(new Coordinate(2, 2));
	    Cell emptyCell = board.getCell(new Coordinate(2, 3));
	    
	    // Create a move from the Guard to the adjacent empty cell
	    Move validGuardMove = new Move(guardCell, emptyCell);
	    Assert.assertTrue("Guard should be able to move to an adjacent empty cell", board.isValidMove(validGuardMove));

	    // Attempt to move the Guard to a non-adjacent cell (e.g., (2, 4))
	    Coordinate nonAdjacentCoord = new Coordinate(2, 4);
	    Cell nonAdjacentCell = board.getCell(nonAdjacentCoord);
	    Move invalidGuardMove = new Move(guardCell, nonAdjacentCell);
	    Assert.assertFalse("Guard should not be able to move to a non-adjacent cell", board.isValidMove(invalidGuardMove));
	}	
    
    
    // max
    @Test
    public void testInvalidPieceType() {
        this.board = new Board("Boards/isValidMove_invalid_piece_type.txt");
        Cell emptyCell = board.getCell(new Coordinate(2, 2));
        Cell targetCell = board.getCell(new Coordinate(2, 3));
        Move invalidMove = new Move(emptyCell, targetCell);

        Assert.assertFalse("Move should not be valid if there is no piece to move", board.isValidMove(invalidMove));
    }
    
    // max
    @Test
    public void testComplexBoardStates() {
        this.board = new Board("Boards/isValidMove_complex_board_states.txt");
        Cell musketeerCell = board.getCell(new Coordinate(0, 1));
        Cell guardCell = board.getCell(new Coordinate(0, 2));
        Move validMove = new Move(musketeerCell, guardCell);

        Assert.assertTrue("Musketeer should be able to move to adjacent Guard", board.isValidMove(validMove));

        Cell blockedMusketeerCell = board.getCell(new Coordinate(2, 1));
        Cell anotherMusketeerCell = board.getCell(new Coordinate(1, 1));
        Move blockedMove = new Move(blockedMusketeerCell, anotherMusketeerCell);

        Assert.assertFalse("Musketeer should not be able to move to a cell blocked by another Musketeer", board.isValidMove(blockedMove));
    }
    
    // ------- getPossibleCells TESTS
    // max
    @Test
    public void testBasicValidMoves() {
        this.board = new Board("Boards/getPossibleCells_basic_valid_moves.txt");
        List<Cell> possibleCells = board.getPossibleCells();

        // Expecting the Musketeer at (0, 1) and the Guard at (1, 1) to have possible moves
        Assert.assertEquals("There should be 2 possible cells with moves", 2, possibleCells.size());
        Assert.assertTrue("Guard at (0, 1) should be able to move", 
                          possibleCells.contains(board.getCell(new Coordinate(0, 2))));
        Assert.assertTrue("Guard at (1, 1) should be able to move", 
                          possibleCells.contains(board.getCell(new Coordinate(1, 1))));
    }
    
    // max
    @Test
    public void testMixedPieceTypes() {
        this.board = new Board("Boards/getPossibleCells_mixed_piece_types.txt");
        List<Cell> possibleCells = board.getPossibleCells();

        // Assuming it's the Musketeer's turn
        Assert.assertEquals("There should be 2 possible cells with moves", 2, possibleCells.size());
        Assert.assertTrue("Musketeer at (0, 1) should be able to move", 
                          possibleCells.contains(board.getCell(new Coordinate(0, 1))));
        Assert.assertTrue("Musketeer at (1, 2) should be able to move", 
                          possibleCells.contains(board.getCell(new Coordinate(1, 2))));
    }
    
    @Test
    public void testEdgeOfBoard() {
        this.board = new Board("Boards/getPossibleCells_edge_of_board.txt");
        List<Cell> possibleCells = board.getPossibleCells();

        // Expecting the Guard at (4, 4) to have possible moves
        Assert.assertEquals("There should be 1 possible cell with moves", 1, possibleCells.size());
        Assert.assertTrue("Guard at (4, 4) should be able to move", 
                          possibleCells.contains(board.getCell(new Coordinate(4, 4))));
    }
    
    // max
    @Test
    public void testNoPossibleMoves() {
        this.board = new Board("Boards/getPossibleCells_no_possible_moves.txt");
        List<Cell> possibleCells = board.getPossibleCells();

        // Expecting no possible moves
        Assert.assertTrue("There should be no possible cells with moves", possibleCells.isEmpty());
    }
    
    
    // ------- getPossibleDestinations TESTS -------------------------------------
    // max
    @Test
    public void testBasicMovement() {
        this.board = new Board("Boards/getPossibleDestinations_basic_movement.txt");
        Cell musketeerCell = board.getCell(new Coordinate(0, 1));
        List<Cell> destinations = board.getPossibleDestinations(musketeerCell);

        // Expecting the Musketeer at (0, 1) to move to (0, 2)
        Assert.assertEquals("Musketeer should have 1 possible destination", 1, destinations.size());
        Assert.assertTrue("Musketeer should be able to move to (0, 2)", 
                          destinations.contains(board.getCell(new Coordinate(0, 2))));
    }
    
    // max
    @Test
    public void testBlockedaMovement() {
        this.board = new Board("Boards/getPossibleDestinations_blocked_movement.txt");
        Cell musketeerCell = board.getCell(new Coordinate(1, 2));
        List<Cell> destinations = board.getPossibleDestinations(musketeerCell);

        // Expecting the Musketeer at (1, 2) to have no possible moves
        Assert.assertTrue("Musketeer should have no possible destinations", destinations.isEmpty());
    }
    
    @Test
    public void testDestinationEdgeOfBoard() {
    	// Test for Musketeers
        this.board = new Board("Boards/getPossibleDestinations_edge_of_board.txt");
        Cell musketeerCell = board.getCell(new Coordinate(0, 0));
        List<Cell> destinations = board.getPossibleDestinations(musketeerCell);

        // Expecting the Musketeer at (0, 0) to have no possible moves
        Assert.assertTrue("Musketeer should have no possible destinations", destinations.isEmpty());
    }
    
    @Test
    public void testDestinationEdgeOfBoard2() {
    	 // Test for Guard
    	this.board = new Board("Boards/getPossibleDestinations_edge_of_board2.txt");
        Cell guardCell = board.getCell(new Coordinate(4, 4));
        List<Cell> guardDestinations = board.getPossibleDestinations(guardCell);

        // Expecting the Guard at (4, 4) to have 2 possible moves
        Assert.assertEquals("Guard should have 2 possible destinations", 2, guardDestinations.size());
        Assert.assertTrue("Guard should be able to move to (4, 3)", 
                          guardDestinations.contains(board.getCell(new Coordinate(4, 3))));
        Assert.assertTrue("Guard should be able to move to (3, 4)", 
                          guardDestinations.contains(board.getCell(new Coordinate(3, 4))));
    }
    
    // max
    @Test
    public void testNoMovement() {
    	// Test for Musketeers
        this.board = new Board("Boards/getPossibleDestinations_no_movement.txt");
        Cell musketeerCell = board.getCell(new Coordinate(3, 3));
        List<Cell> destinations = board.getPossibleDestinations(musketeerCell);

        // Expecting the Musketeer at (0, 0) to have no possible moves
        Assert.assertTrue("Musketeer should have no possible destinations", destinations.isEmpty());
    }
    
    // max
    @Test
    public void testNoMovement2() {
    	// Test for Guard
        this.board = new Board("Boards/getPossibleDestinations_no_movement2.txt");
        Cell guardCell = board.getCell(new Coordinate(3, 3));
        List<Cell> destinations = board.getPossibleDestinations(guardCell);

        // Expecting the Guard at (3, 3) to have no possible moves
        Assert.assertTrue("Guard should have no possible destinations", destinations.isEmpty());
    }
    
    // ------- getPossibleMoves TESTS -------------------------------------
    
    // max
    // getPossibleCells_basic_valid_moves.txt
    @Test
    public void testBasicMoves() {
    	// Test for Guards
        this.board = new Board("Boards/getPossibleCells_basic_valid_moves.txt");
        List<Move> possibleMoves = board.getPossibleMoves();


        Assert.assertEquals("There should be 5 possible moves for GUARD", 5, possibleMoves.size());
    }
    
    @Test
    public void testBasicMoves2() {
    	// Test for Musketeers
        this.board = new Board("Boards/getPossibleCells_basic_valid_moves2.txt");
        List<Move> possibleMoves = board.getPossibleMoves();
        
        Assert.assertEquals("There should be 2 possible moves for Musketeers", 2, possibleMoves.size());

    }
    
    // ------- GameOver TESTS -------------------------------------
    // max
    @Test
    public void testIsGameOverRuns() {
        // Set up a simple board configuration
    	this.board = new Board("Boards/GameOver.txt");

        // Call the isGameOver method
        boolean gameOver = board.isGameOver();

        // Check if the method runs and returns a boolean
        Assert.assertNotNull("isGameOver should return a boolean value ", gameOver);

        // Optionally, check if the winner is set if the game is over
        if (gameOver) {
            Piece.Type winner = board.getWinner();
            Assert.assertNotNull("Winner should be set if the game is over", winner);
        }
    }
    
    // ------- general TESTS -------------------------------------

    @Test
    public void testBasicGetCell(){
        Coordinate coordinate = new Coordinate(1,1);
        Cell cell = board.getCell(coordinate);
        Assert.assertNotNull("The cell is null!",cell);
    }

    @Test
    public void testGetMusketeerCells(){
        List<Cell> muskCells = board.getMusketeerCells();
        Assert.assertFalse("No Musketeers on boards", muskCells.isEmpty());
    }

    @Test
    public void testGetGuardCells() {
        List<Cell> guardCells = board.getGuardCells();
        Assert.assertFalse("No Guards on boards", guardCells.isEmpty());
    }


    @Test
    public void testGetPossibleCells() {
        List<Cell> possibleCells = board.getPossibleCells();
        Assert.assertFalse("No cells with any possible moves on board", possibleCells.isEmpty());
    }

    @Test
    public void testGetPossibleDestinations() {
        Cell muskCells = board.getMusketeerCells().get(0);
        List<Cell> destinations = board.getPossibleDestinations(muskCells);
        Assert.assertFalse("No Musketeer has any valid destinations", destinations.isEmpty());
    }

    @Test
    public void testGetPossibleMoves() {
        List<Move> moves = board.getPossibleMoves();
        Assert.assertFalse("No valid moves for any cells on board", moves.isEmpty());
    }


 
}

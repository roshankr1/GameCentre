package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Manage the current pattern board game.
 */
class PatternBoardManager implements Serializable {
    /**
     * The correct board.
     */
    private PatternBoard correctBoard;

    /**
     * The current board.
     */
    private PatternBoard currentBoard;

    /**
     * The moves done so far.
     */
    private Stack<List<Integer>> moves;

    /**
     * The level of difficulty.
     */
    private int level = 1;


    /**
     * Get the score for the current board.
     */
    int getScore() {
        return this.level;
    }

    /**
     * Manage the current pattern game.
     *
     * @param numRows The number of rows.
     * @param numCols The number of columns.
     */
    PatternBoardManager(int numRows, int numCols) {
        this.moves = new Stack<>();
        this.currentBoard = new PatternBoard(numRows, numCols);
        this.correctBoard = new PatternBoard(level, numRows, numCols);
    }

    /**
     * Return the current pattern board.
     *
     * @return the current pattern board
     */
    PatternBoard getPatternBoard() {
        return this.currentBoard;
    }

    /**
     * Return the correct pattern board.
     *
     * @return the correct pattern board
     */
    PatternBoard getCorrectBoard() {
        return this.correctBoard;
    }

    /**
     * Check if the current board is the same as the correct board.
     * Precondition: current board and correct board have the same number of rows and columns
     *
     * @return whether current board is the same as the correct board.
     */
    boolean puzzleSolved() {
        boolean matched = true;
        for (int i = 0; i != currentBoard.getNumRows(); i++) {
            for (int j = 0; j != currentBoard.getNumCols(); j++) {
                PatternTile currentTile = currentBoard.getTile(i, j);
                PatternTile correctTile = correctBoard.getTile(i, j);
                if (currentTile.getColor() != correctTile.getColor()) {
                    matched = false;
                }
            }
        }
        return matched;
    }

    /**
     * Process the tap at position on the board, change color as appropriate.
     *
     * @param position the position of the tap.
     */
    void touchMove(Integer position) {
        int row = position / currentBoard.getNumRows();
        int col = position % currentBoard.getNumCols();

        PatternTile tile = currentBoard.getTile(row, col);
        List<Integer> move = Arrays.asList(row, col, tile.getColor());
        moves.push(move);
        currentBoard.updateColor(row, col);
    }


    /**
     * Get the last move of the user performed.
     *
     * @return last move
     */
    private List<Integer> getLastMove() {
        if (moves.isEmpty()) {
            return null;
        } else {
            return moves.pop();
        }
    }

    /**
     * Undo the last move if possible, otherwise return false.
     *
     * @return whether undo was successful.
     */
    boolean undo() {
        List<Integer> lastMove = getLastMove();
        if (lastMove == null) {
            return false;
        } else {
            Integer row = lastMove.get(0);
            Integer col = lastMove.get(1);
            Integer color = lastMove.get(2);
            PatternTile tile = currentBoard.getTile(row, col);
            tile.setColor(color);
            return true;
        }
    }

    /**
     * Update the level, change the correct board to new board with a higher level of difficulty.
     *
     * @return whether or not level was updated
     */
    boolean updateLevel() {
        int row = this.currentBoard.getNumRows();
        int col = this.currentBoard.getNumCols();
        if (level != row * col) {
            level++;
        } else {
            return false; // return false if there are no more levels
        }
        //reset moves and create new boards with updated level
        this.moves = new Stack<>();
        this.currentBoard = new PatternBoard(row, col);
        this.correctBoard = new PatternBoard(level, row, col);
        return true;
    }
}
package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class SlidingBoardManager implements Serializable {

    /**
     * The board being managed.
     */
    private SlidingBoard board;
    /**
     * The moves done so far on this board.
     */
    private Stack<int[]> moves;
    /**
     * number of moves used to calculate score
     */
    private int numMoves = 0;

    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */
    SlidingBoardManager(SlidingBoard board) {
        this.board = board;
        this.moves = new Stack<>();
    }

    /**
     * Return the current board.
     */
    SlidingBoard getBoard() {
        return board;
    }

    /**
     * Manage a new shuffled board.
     */
    SlidingBoardManager(int numRows, int numCols) {
        List<SlidingTile> tiles = new ArrayList<>();
        final int numTiles = numRows * numCols;
        for (int tileNum = 0; tileNum != numTiles - 1; tileNum++) {
            tiles.add(new SlidingTile(tileNum));
        }
        tiles.add(new SlidingTile(-1));

        Collections.shuffle(tiles);
        this.moves = new Stack<>();
        this.board = new SlidingBoard(tiles, numRows, numCols);
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    boolean puzzleSolved() {
        boolean solved = true;
        int index = 1;
        for (SlidingTile item : this.board) {
            if (index != item.getId() && index != board.getNumRows() * board.getNumCols()) {
                solved = false;
            } else if (index == board.getNumRows() * board.getNumCols() && item.getId() != 0) {
                solved = false;
            }
            index++;
        }
        return solved;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {
        int row = position / board.getNumCols();
        int col = position % board.getNumCols();
        int blankId = 0;
        // Are any of the 4 the blank tile?
        SlidingTile above = row == 0 ? null : board.getTile(row - 1, col);
        SlidingTile below = row == board.getNumRows() - 1 ? null : board.getTile(row + 1, col);
        SlidingTile left = col == 0 ? null : board.getTile(row, col - 1);
        SlidingTile right = col == board.getNumCols() - 1 ? null : board.getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    void touchMove(int position) {
        int row = position / board.getNumRows();
        int col = position % board.getNumCols();
        int blankId = 0;
        SlidingTile above = row == 0 ? null : board.getTile(row - 1, col);
        SlidingTile below = row == board.getNumRows() - 1 ? null : board.getTile(row + 1, col);
        SlidingTile left = col == 0 ? null : board.getTile(row, col - 1);
        SlidingTile right = col == board.getNumCols() - 1 ? null : board.getTile(row, col + 1);
        numMoves++;
        if (below != null && below.getId() == blankId) {
            board.swapTiles(row, col, row + 1, col);
            int[] move = {row, col, row + 1, col};
            moves.push(move);
        } else if (above != null && above.getId() == blankId) {
            board.swapTiles(row, col, row - 1, col);
            int[] move = {row, col, row - 1, col};
            moves.push(move);
        } else if (left != null && left.getId() == blankId) {
            board.swapTiles(row, col, row, col - 1);
            int[] move = {row, col, row, col - 1};
            moves.push(move);
        } else if (right != null && right.getId() == blankId) {
            board.swapTiles(row, col, row, col + 1);
            int[] move = {row, col, row, col + 1};
            moves.push(move);
        }
    }

    /**
     * Return number of moves
     *
     * @return number of moves
     */
    int getMoves() {
        return numMoves;
    }

    /**
     * Return size of board
     *
     * @return size of board
     */
    int getSize() {
        return this.board.getNumCols();
    }

    /**
     * Get the last move the user performed
     *
     * @return the latest move
     */
    int[] getLastMove() {
        if (moves.isEmpty()) {
            return null;
        } else {
            numMoves--;
            return moves.pop();
        }
    }

}

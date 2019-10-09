package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Manager for the minesweeper game.
 */
class MinesweeperManager extends Observable implements Serializable {

    /**
     * The minesweeper board.
     */
    private MinesweeperBoard minesweeperBoard;
    /**
     * The starting time.
     */
    private long startTime = System.nanoTime();
    /**
     * The old elapsed time.
     */
    private long oldElapsedTime = 0;
    /**
     * The current elapsed time.
     */
    private long currentElapsedTime = 0;
    /**
     * Check if score has been written.
     */
    private boolean didWriteScore = false;

    /**
     * The first constructor for this class.
     *
     * @param minesweeperBoard the board
     */
    MinesweeperManager(MinesweeperBoard minesweeperBoard) {
        this.minesweeperBoard = minesweeperBoard;
    }

    /**
     * Getter for didWriteScore
     */
    boolean getDidWriteScore() {
        return didWriteScore;
    }

    /**
     * Setter for didWriteScore
     */
    void setDidWriteScore(boolean didWriteScore) {
        this.didWriteScore = didWriteScore;
    }

    /**
     * The method to get the minesweeperBoard of this class.
     *
     * @return the board
     */
    MinesweeperBoard getMinesweeperBoard() {
        return this.minesweeperBoard;
    }

    /**
     * Manage a new randomly generated minesweeperBoard.
     * Adapted from: https://introcs.cs.princeton.edu/java/14array/Minesweeper.java.html
     */
    MinesweeperManager(int numRows, int numCols) {
        boolean[][] bombs = new boolean[numRows + 2][numCols + 2];
        double prob = 0.1;
        for (int row = 1; row <= numRows; row++)
            for (int col = 1; col <= numCols; col++)
                if (Math.random() < prob)
                    bombs[row][col] = true;

        int[][] sol = new int[numRows + 2][numCols + 2];
        for (int row = 1; row <= numRows; row++)
            for (int col = 1; col <= numCols; col++)
                for (int ii = row - 1; ii <= row + 1; ii++)
                    for (int jj = col - 1; jj <= col + 1; jj++)
                        if (bombs[ii][jj])
                            sol[row][col]++;

        String[][] board = new String[numRows + 2][numCols + 2];
        for (int row = 1; row <= numRows; row++)
            for (int col = 1; col <= numCols; col++)
                if (bombs[row][col])
                    board[row][col] = "*";
                else
                    board[row][col] = String.valueOf(sol[row][col]);

        List<MinesweeperTile> tiles = new ArrayList<>();
        for (int row = 0; row < numRows; row++)
            for (int col = 0; col < numCols; col++)
                tiles.add(new MinesweeperTile(board[row + 1][col + 1]));
        this.minesweeperBoard = new MinesweeperBoard(tiles);
    }

    /**
     * Checks if the game is over by checking if a "*" tile is "flipped."
     *
     * @return true if game is over and false otherwise.
     */
    boolean gameOver() {
        for (MinesweeperTile item : this.minesweeperBoard) {
            if (item.getValue().equals("*")) {
                if (item.getFlipped()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the elapsed time
     *
     * @return the elapsed time
     */
    long getElapsedTime() {
        if (!puzzleSolved() && !gameOver()) {
            currentElapsedTime = ((System.nanoTime() - startTime) / 1000000000) + oldElapsedTime;
        }
        return currentElapsedTime;
    }

    /**
     * Setter for oldElapsedTime to current time
     */
    void setOldElapsedTime() {
        this.oldElapsedTime = currentElapsedTime;
    }

    /**
     * Return whether the minesweeperboard is solved by checking if all non-bomb tiles are "flipped."
     *
     * @return whether all the non-bomb tiles are "flipped."
     */
    boolean puzzleSolved() {
        for (MinesweeperTile item : this.minesweeperBoard) {
            if (!item.getValue().equals("*")) {
                if (!item.getFlipped()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns whether the tap is valid by checking the tile tapped is not yet "flipped."
     *
     * @param position of the tile in the list.
     * @return true if the tile is not flipped, false otherwise
     */
    boolean isValidTap(int position) {
        int row = position / minesweeperBoard.getNumRows();
        int col = position % minesweeperBoard.getNumCols();

        MinesweeperTile mineTile = minesweeperBoard.getTile(row, col);
        return !mineTile.getFlipped();
    }

    /**
     * Makes the move by flipping the tile tapped.
     *
     * @param position the position of the tap
     */
    void touchMove(int position) {
        int row = position / minesweeperBoard.getNumRows();
        int col = position % minesweeperBoard.getNumCols();
        //doesn't check if valid tap
        MinesweeperTile mineTile = minesweeperBoard.getTile(row, col);
        if (mineTile.getValue().equals("*")) {
            mineTile.setFlipped(true);
        } else {
            //flipTiles(row, col);
            minesweeperBoard.flipTiles(row, col);
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Set start time to current time
     */
    void setStartTime() {
        startTime = System.nanoTime();
    }
}

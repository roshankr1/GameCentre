package fall2018.csc2017.slidingtiles;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * The sliding tiles board.
 */
class SlidingBoard extends Observable implements Serializable, Iterable<SlidingTile> {
    /**
     * The number of rows.
     */
    private int numRows;

    /**
     * The number of rows.
     */
    private int numCols;

    /**
     * The tiles on the board in row-major order.
     */
    private SlidingTile[][] tiles;

    /**
     * Get number of rows.
     *
     * @return num rows
     */
    int getNumRows() {
        return numRows;
    }

    /**
     * Get number of cols.
     *
     * @return num cols.
     */
    int getNumCols() {
        return numCols;
    }

    @NonNull
    @Override
    public Iterator<SlidingTile> iterator() {
        return new BoardIterator();
    }

    /**
     * Iterator for SlidingBoard class
     * Overrides hasNext and next methods
     */
    private class BoardIterator implements Iterator<SlidingTile> {
        /**
         * row index of the next item
         */
        int row = 0;
        /**
         * column index of the next item
         */
        int col = 0;

        @Override
        public boolean hasNext() {
            return (row < numRows);
        }

        @Override
        public SlidingTile next() {
            SlidingTile result = getTile(row, col);
            //increase row num if on last col
            if (col == numCols - 1 && row < numRows) {
                row++;
                col = 0;
            } else {
                //increase col num if not at last row or col
                col++;
            }
            return result;
        }
    }

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == numRows * numCols
     *
     * @param tiles the tiles for the board
     */
    SlidingBoard(List<SlidingTile> tiles, int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.tiles = new SlidingTile[numRows][numCols];

        Iterator<SlidingTile> iter = tiles.iterator();

        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    SlidingTile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Swap the tiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    void swapTiles(int row1, int col1, int row2, int col2) {
        SlidingTile temp = getTile(row1, col1);
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {
        return "SlidingBoard{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    //NEW CODE

    /**
     * Check if the board is solvable.
     *
     * @return true if it is solvable and false otherwise.
     */
    boolean isSolvable() {
        List<SlidingTile> tiles = new ArrayList<>();
        for (int i = 0; i != numRows; i++) {
            for (int j = 0; j != numCols; j++) {
                tiles.add(getTile(i, j));
            }
        }
        int finalCount = 0;
        for (int i = 0; i != tiles.size(); i++) {
            int currentCount = 0;
            if (i != tiles.size() - 1) {
                for (int j = i + 1; j != tiles.size(); j++) {
                    if (tiles.get(j).getId() != 0 && tiles.get(j).getId() < tiles.get(i).getId()) {
                        currentCount = currentCount + 1;
                    }
                }
            }
            finalCount = finalCount + currentCount;
        }
        boolean solvable = false;
        if (numRows % 2 == 1) {
            if (finalCount % 2 == 0) {
                solvable = true;
            }
        } else {
            for (int i = 0; i != 4; i++) {
                if (tiles.get(i).getId() == 0) {
                    if (finalCount % 2 == 1) {
                        solvable = true;
                    }
                }
            }
            for (int i = 4; i != 8; i++) {
                if (tiles.get(i).getId() == 0) {
                    if (finalCount % 2 == 0) {
                        solvable = true;
                    }
                }
            }
            for (int i = 8; i != 12; i++) {
                if (tiles.get(i).getId() == 0) {
                    if (finalCount % 2 == 1) {
                        solvable = true;
                    }
                }
            }
            for (int i = 12; i != 16; i++) {
                if (tiles.get(i).getId() == 0) {
                    if (finalCount % 2 == 0) {
                        solvable = true;
                    }
                }
            }
        }
        return solvable;


    }

}

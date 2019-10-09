package fall2018.csc2017.slidingtiles;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * A minesweeper board.
 */
public class MinesweeperBoard extends Observable implements Serializable, Iterable<MinesweeperTile> {
    /**
     * The number of rows.
     */
    private final int numRows = 10;

    /**
     * The number of cols.
     */
    private final int numCols = 10;

    /**
     * The tiles.
     */
    private MinesweeperTile[][] tiles;

    /**
     * Constructor with premade tiles.
     *
     * @param tiles tiles of the board.
     */
    MinesweeperBoard(List<MinesweeperTile> tiles) {
        this.tiles = new MinesweeperTile[numRows][numCols];

        Iterator<MinesweeperTile> iter = tiles.iterator();

        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
    }

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

    /**
     * Return minesweeper tile at [row, col].
     *
     * @param row the row
     * @param col the col
     * @return the minesweeper tile
     */
    MinesweeperTile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Flips all the tiles. Used when game is over.
     */
    void flipAllTiles() {
        for (MinesweeperTile item : this) {
            item.setFlipped(true);
        }
        setChanged();
        notifyObservers();
    }

    void flipTiles(int row, int col) {
        MinesweeperTile curTile = getTile(row, col);
        curTile.setFlipped(true);
        int rows = getNumRows();
        int cols = getNumCols();
        String curTileValue = curTile.getValue();
        if (curTileValue.equals("0")) {
            if (0 <= row - 1) {
                flipNeighboringTiles(row - 1, col);
            }
            if (row + 1 <= rows - 1) {
                flipNeighboringTiles(row + 1, col);
            }
            if (0 <= col - 1) {
                flipNeighboringTiles(row, col - 1);
            }
            if (col + 1 <= cols - 1) {
                flipNeighboringTiles(row, col + 1);
            }
        }
    }

    /**
     * Recursive method to flip neighboring tiles if applicable.
     *
     * @param row row of tile
     * @param col col of tile
     */
    private void flipNeighboringTiles(int row, int col) {
        MinesweeperTile tile = getTile(row, col);
        String rightValue = tile.getValue();
        if (!rightValue.equals("*")) {
            if (!tile.getFlipped() && rightValue.equals("0"))
                flipTiles(row, col);
            if (!rightValue.equals("0"))
                tile.setFlipped(true);
        }
    }

    @NonNull
    @Override
    public Iterator<MinesweeperTile> iterator() {
        return new MinesweeperBoardIterator();
    }

    /**
     * Iterator for minesweeper board.
     */
    private class MinesweeperBoardIterator implements Iterator<MinesweeperTile> {
        /**
         * Row index.
         */
        int row = 0;
        /**
         * Column index.
         */
        int col = 0;

        @Override
        public boolean hasNext() {
            return row < numRows;
        }

        @Override
        public MinesweeperTile next() {
            MinesweeperTile result = getTile(row, col);
            if (col == numCols - 1 && row < numRows) {
                row++;
                col = 0;
            } else {
                col++;
            }
            return result;
        }

    }

}

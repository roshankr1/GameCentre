package fall2018.csc2017.slidingtiles;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Random;

/**
 * The pattern memory game board.
 */
public class PatternBoard extends Observable implements Serializable, Iterable<PatternTile> {
    /**
     * The number of rows
     */
    private int numRows;

    /**
     * Get the number of rows
     *
     * @return num rows
     */
    int getNumRows() {
        return numRows;
    }

    /**
     * The number of columns.
     */
    private int numCols;

    /**
     * Get the number of columns
     *
     * @return num cols
     */
    int getNumCols() {
        return numCols;
    }

    /**
     * The tiles of the pattern game board
     */
    private PatternTile[][] patternTiles;

    /**
     * The very basic color sequence of tile: blue, green, pink, purple, yellow
     */
    private static ArrayList<Integer> colorSequence = new ArrayList<>(Arrays.asList(
            0, 1, 2, 3, 4, 5));


    @NonNull
    @Override
    public Iterator<PatternTile> iterator() {
        return new PatternBoardIterator();
    }

    /**
     * Iterator for PatternBoard class
     * Overrides hasNext and next methods
     */
    private class PatternBoardIterator implements Iterator<PatternTile> {
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
        public PatternTile next() {
            PatternTile result = getTile(row, col);
            if (col == numCols - 1 && row < numRows) {
                row++;
                col = 0;
            } else {
                col++;
            }
            return result;
        }
    }

    /**
     * A new board of empty pattern tiles (white tiles).
     * Precondition: len(tiles) == numRows*numCols
     */
    PatternBoard(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.patternTiles = new PatternTile[numRows][numCols];
        int id = 0;
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                this.patternTiles[row][col] = new PatternTile(id + 1);
                id++;
            }
        }
    }

    /**
     * Make a correct board with a randomized pattern based on game level.
     * Precondition: level <= len(tiles)
     */
    PatternBoard(int level, int numRows, int numCols) {
        Random rand = new Random();
        this.numRows = numRows;
        this.numCols = numCols;
        this.patternTiles = new PatternTile[numRows][numCols];

        List<Integer> emptyTiles = new ArrayList<>();
        List<Integer> colors = colorSequence.subList(1, colorSequence.size());
        int id = 1;
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                this.patternTiles[row][col] = new PatternTile(id);
                emptyTiles.add(id);
                id++;
            }
        }
        for (int i = 0; i != level; i++) {
            int index = rand.nextInt(emptyTiles.size());
            int emptyId = emptyTiles.get(index);
            PatternTile colorTile = getTileFromId(emptyId);
            colorTile.setColor(colors.get(rand.nextInt(colors.size())));
            emptyTiles.remove(index);
        }
    }

    /**
     * Get a tile from patternTiles based on index.
     */
    private PatternTile getTileFromId(int id) {
        int position = id - 1;
        int row = position / this.getNumRows();
        int col = position % this.getNumCols();
        return this.patternTiles[row][col];
    }

    /**
     * Return the pattern tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile col
     * @return the tile at (row, col)
     */
    PatternTile getTile(int row, int col) {
        return patternTiles[row][col];
    }

    /**
     * For tile tapped, based on its current color, increment color to next color in the sequence
     *
     * @param row the row of the tile
     * @param col the col of the tile
     */
    void updateColor(int row, int col) {
        PatternTile tile = getTile(row, col);
        int currentColor = tile.getColor();
        if (currentColor == 5) { // if tile is yellow (last color in sequence)
            currentColor = 0;
            tile.setColor(currentColor);
        } else {
            currentColor++;
            tile.setColor(currentColor);
        }
        setChanged();
        notifyObservers();
    }
}


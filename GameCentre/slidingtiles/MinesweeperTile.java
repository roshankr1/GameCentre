package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

/**
 * A minesweeper tile.
 */
class MinesweeperTile implements Serializable {
    /**
     * Value of the tile.
     * "0" for blank.
     * "*" for mine.
     */
    private String value;

    /**
     * Background image id.
     */
    private int background;

    /**
     * Whether the value of the tile is "flipped' or visible to the user.
     */
    private boolean flipped;

    /**
     * A minesweeper tile with value value.
     *
     * @param value value of tile, 0 if blank, range 1-8
     */
    MinesweeperTile(String value) {
        this.value = value;
        this.flipped = false;
        switch (value) {
            case "0":
                background = R.drawable.minesweeperblank;
                break;
            case "1":
                background = R.drawable.minesweeper1;
                break;
            case "2":
                background = R.drawable.minesweeper2;
                break;
            case "3":
                background = R.drawable.minesweeper3;
                break;
            case "4":
                background = R.drawable.minesweeper4;
                break;
            case "5":
                background = R.drawable.minesweeper5;
                break;
            case "6":
                background = R.drawable.minesweeper6;
                break;
            case "7":
                background = R.drawable.minesweeper7;
                break;
            case "8":
                background = R.drawable.minesweeper8;
                break;
            case "*":
                background = R.drawable.minesweepermine;
                break;
        }
    }

    /**
     * Get the value of the tile.
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the background id of the tile.
     *
     * @return background id
     */
    public int getBackground() {
        if (!flipped) {
            return R.drawable.minesweeperbutton;
        }
        return background;
    }

    /**
     * Get the flipped boolean of the tile.
     */
    boolean getFlipped() {
        return flipped;
    }

    /**
     * Set the flipped boolean of the tile.
     */
    void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

}

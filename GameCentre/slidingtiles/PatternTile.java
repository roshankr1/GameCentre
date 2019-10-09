package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A Tile in a pattern memory game.
 */
class PatternTile implements Serializable {
    /**
     * All possible color images for a pattern tile.
     */
    private static final HashMap<Integer, Integer> COLOR_CODES = new HashMap<Integer, Integer>() {{
        put(0, R.drawable.tile_16);
        put(1, R.drawable.blue);
        put(2, R.drawable.green);
        put(3, R.drawable.pink);
        put(4, R.drawable.purple);
        put(5, R.drawable.yellow);
    }};

    /**
     * The unique id of the tile
     */
    private int id;
    /**
     * The background color id to find color image for the tile
     */
    private int color;
    /**
     * The background id
     */
    private int background;
    /**
     * The very basic color sequence of tile: blue, green, pink, purple, yellow
     */
    private static ArrayList<Integer> colorSequence = new ArrayList<>(Arrays.asList(
            0, 1, 2, 3, 4, 5));


    /**
     * Return background id
     *
     * @return background id
     */
    public int getBackground() {
        return background;
    }

    /**
     * Return the background color id
     *
     * @return the background color id
     */
    public int getColor() {
        return color;
    }

    /**
     * Set the color and background ids of the tile
     *
     * @param color color of tile
     */
    public void setColor(Integer color) {
        this.color = color;
        this.background = COLOR_CODES.get(color);
    }


    /**
     * A pattern game tile with an id and initial color white.
     *
     * @param id the id
     */
    PatternTile(int id) {
        this.id = id;
        this.background = COLOR_CODES.get(0);
        this.color = 0;
    }


}


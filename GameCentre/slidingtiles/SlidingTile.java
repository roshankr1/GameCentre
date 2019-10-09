package fall2018.csc2017.slidingtiles;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * A SlidingTile in a sliding tiles puzzle.
 */
class SlidingTile implements Comparable<SlidingTile>, Serializable {

    /**
     * The background id to find the tile image.
     */
    private int background;

    /**
     * The unique id.
     */
    private int id;

    /**
     * Return the background id.
     *
     * @return the background id
     */
    public int getBackground() {
        return background;
    }

    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    public int getId() {
        return id;
    }

    /**
     * A SlidingTile with id and background. The background may not have a corresponding image.
     *
     * @param id         the id
     * @param background the background
     */
    SlidingTile(int id, int background) {
        this.id = id;
        this.background = background;
    }

    /**
     * A tile with a background id; look up and set the id.
     *
     * @param backgroundId the id of the background
     */
    SlidingTile(int backgroundId) {
        id = backgroundId + 1;
        if (id == 0)
            background = R.drawable.tile_16;
        else if (id == 1)
            background = R.drawable.tile1;
        else if (id == 2)
            background = R.drawable.tile2;
        else if (id == 3)
            background = R.drawable.tile3;
        else if (id == 4)
            background = R.drawable.tile4;
        else if (id == 5)
            background = R.drawable.tile5;
        else if (id == 6)
            background = R.drawable.tile6;
        else if (id == 7)
            background = R.drawable.tile7;
        else if (id == 8)
            background = R.drawable.tile8;
        else if (id == 9)
            background = R.drawable.tile9;
        else if (id == 10)
            background = R.drawable.tile10;
        else if (id == 11)
            background = R.drawable.tile11;
        else if (id == 12)
            background = R.drawable.tile12;
        else if (id == 13)
            background = R.drawable.tile13;
        else if (id == 14)
            background = R.drawable.tile14;
        else if (id == 15)
            background = R.drawable.tile15;
        else if (id == 16)
            background = R.drawable.tile16;
        else if (id == 17)
            background = R.drawable.tile17;
        else if (id == 18)
            background = R.drawable.tile18;
        else if (id == 19)
            background = R.drawable.tile19;
        else if (id == 20)
            background = R.drawable.tile20;
        else if (id == 21)
            background = R.drawable.tile21;
        else if (id == 22)
            background = R.drawable.tile22;
        else if (id == 23)
            background = R.drawable.tile23;
        else if (id == 24)
            background = R.drawable.tile24;
    }

    @Override
    public int compareTo(@NonNull SlidingTile o) {
        return o.id - this.id;
    }
}

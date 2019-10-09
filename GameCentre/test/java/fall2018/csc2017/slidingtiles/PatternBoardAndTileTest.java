package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Local unit test for PatternBoardGameManager
 */
public class PatternBoardAndTileTest {
    private PatternBoardManager patternBoardManager = new PatternBoardManager(7, 7);
    private PatternBoard patternBoard = new PatternBoard(7, 7);

    /**
     * Test whether the correct pattern board is the same as the current board.
     */
    @Test
    public void testPuzzleSolved() {
        // get the correct board
        PatternBoard correctBoard = patternBoardManager.getCorrectBoard();
        // get the colored tile's position and color
        boolean colorTileNotFound = true;
        int position = 0;
        int coloredRow = 0;
        int coloredCol = 0;
        int color = 0;
        while (colorTileNotFound) {
            int row = position / correctBoard.getNumRows();
            int col = position % correctBoard.getNumCols();
            PatternTile tile = correctBoard.getTile(row, col);
            if (tile.getColor() != 0) {
                colorTileNotFound = false;
                coloredRow = row;
                coloredCol = col;
                color = tile.getColor();
            } else {
                position++;
            }
        }

        // change the correct tile to correct color
        PatternBoard currentBoard = patternBoardManager.getPatternBoard();
        PatternTile correctTile = currentBoard.getTile(coloredRow, coloredCol);
        correctTile.setColor(color);
        assertTrue(patternBoardManager.puzzleSolved());
        // change a wrong tile's color
        PatternTile wrongTile;
        // choose a wrong position
        if (coloredRow == 6) {
            wrongTile = currentBoard.getTile(coloredRow - 1, coloredCol);
        } else if (coloredCol == 6) {
            wrongTile = currentBoard.getTile(coloredRow, coloredCol - 1);
        } else {
            wrongTile = currentBoard.getTile(coloredRow, coloredCol + 1);
        }
        wrongTile.setColor(color);
        assertFalse(patternBoardManager.puzzleSolved());

    }

    /**
     * Test whether tap leads to the correct color in the color sequence
     */
    @Test
    public void testTouchMove() {
        // Test 1. Test if the current color (not the last color in the color sequence) change to the next color in the color sequence
        // in this test case the current color is white
        // choose a tile in the current board, this test case chooses tiles at position 8, which on row 1 & col 1
        PatternTile tile = patternBoardManager.getPatternBoard().getTile(1, 1);
        // get the current color of the tile
        int color = tile.getColor();
        patternBoardManager.touchMove(8);
        // get the new color of the tile
        int newColor = tile.getColor();
        assertEquals(newColor, color + 1);

        // Test 2. Tap the last color in the color sequence (yellow) tile, test if the color change to white
        // tap 5 more time should get a white tile
        for (int tapTime = 0; tapTime != 5; tapTime++) {
            patternBoardManager.touchMove(8);
        }
        // get the new color of the tile
        newColor = tile.getColor();
        assertEquals(0, newColor);
    }

    /**
     * Test undo method
     */
    @Test
    public void testUndo() {
        // 1. if there is no last move
        assertFalse(patternBoardManager.undo());

        // 2. if there is a last move
        PatternTile tile1 = patternBoardManager.getPatternBoard().getTile(0, 1);
        int tile1Color = tile1.getColor();

        // 2.1 Undo color at same position
        patternBoardManager.touchMove(1);
        patternBoardManager.touchMove(1);
        patternBoardManager.undo();
        // the previous color of the tile
        int tile1PrevColor = tile1.getColor();
        assertEquals(tile1Color + 1, tile1PrevColor);
        patternBoardManager.undo();
        tile1PrevColor = tile1.getColor();
        assertEquals(tile1Color, tile1PrevColor);

        // 2.2 Undo color at different position
        PatternTile tile2 = patternBoardManager.getPatternBoard().getTile(0, 2);
        int tile2Color = tile2.getColor();
        patternBoardManager.touchMove(1);
        patternBoardManager.touchMove(2);
        patternBoardManager.undo();
        int tile2PrevColor = tile2.getColor();
        assertEquals(tile2Color, tile2PrevColor);
        patternBoardManager.undo();
        assertEquals(tile1Color, 0);
    }

    /**
     * Test whether the level updates when the previous level is completed
     */
    @Test
    public void testUpdateLevel() {
        // the highest possible score is 49
        for (int score = 0; score != 49; score++) {
            patternBoardManager.updateLevel();
        }
        int newScore = patternBoardManager.getScore();
        assertEquals(49, newScore);

        // if there are no more level to update
        assertFalse(patternBoardManager.updateLevel());
    }

    /**
     * Test getBackGround in PatternTile
     */
    @Test
    public void testGetBackGround() {
        PatternTile tile = patternBoardManager.getPatternBoard().getTile(0, 0);
        int background = tile.getBackground();
        assertEquals(R.drawable.tile_16, background);
    }

    /**
     * Test if the pattern board is iterable
     */
    @Test
    public void testPatternBoardIterable() {
        assertTrue(patternBoard instanceof Iterable);
        if (patternBoard instanceof Iterable) {
            Iterable<PatternTile> it = (Iterable<PatternTile>) patternBoard;
            Iterator<PatternTile> i = it.iterator();
            assert (i.hasNext());
        }
    }
}
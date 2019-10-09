package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SlidingBoardAndTileTest {

    /**
     * The solvable 3x3 board manager for testing.
     */
    private SlidingBoardManager boardManager3x3;

    /**
     * The solvable 4x4 board manager for testing.
     */
    private SlidingBoardManager boardManager4x4;

    /**
     * The solvable 5x5 board manager for testing.
     */
    private SlidingBoardManager boardManager5x5;

    /**
     * The unsolvable 3x3 board manager for testing.
     */
    private SlidingBoardManager boardManagerUnsolvable3x3;

    /**
     * The unsolvable 4x4 board manager for testing.
     */
    private SlidingBoardManager boardManagerUnsolvable4x4;

    /**
     * The unsolvable 5x5 board manager for testing.
     */
    private SlidingBoardManager boardManagerUnsolvable5x5;

    /**
     * Make a set of tiles for a board that are in order.
     *
     * @return a set of tiles that are in order
     */
    private List<SlidingTile> makeTiles(int size) {
        List<SlidingTile> tiles = new ArrayList<>();
        final int numTiles;
        if (size == 3) {
            numTiles = 3 * 3;
        } else if (size == 4) {
            numTiles = 4 * 4;
        } else if (size == 5) {
            numTiles = 5 * 5;
        } else {
            numTiles = 0;
        }
        for (int tileNum = 0; tileNum != numTiles - 1; tileNum++) {
            tiles.add(new SlidingTile(tileNum + 1, tileNum));
        }
        tiles.add(new SlidingTile(0, -1));

        return tiles;
    }

    /**
     * Make a solved 3x3 SlidingBoard.
     */
    private void setUpCorrect3x3() {
        List<SlidingTile> tiles = makeTiles(3);
        SlidingBoard board = new SlidingBoard(tiles, 3, 3);
        boardManager3x3 = new SlidingBoardManager(board);
    }

    /**
     * Make an unsolvable 3x3 SlidingBoard.
     */
    private void setUpUnsolvable3x3() {
        List<SlidingTile> tiles = makeTiles(3);
        Collections.swap(tiles, 4, 5);
        SlidingBoard board = new SlidingBoard(tiles, 3, 3);
        boardManagerUnsolvable3x3 = new SlidingBoardManager(board);
    }

    /**
     * Make a solved 4x4 SlidingBoard.
     */
    private void setUpCorrect4x4() {
        List<SlidingTile> tiles = makeTiles(4);
        SlidingBoard board = new SlidingBoard(tiles, 4, 4);
        boardManager4x4 = new SlidingBoardManager(board);
    }

    /**
     * Make an unsolvable 4x4 SlidingBoard.
     */
    private void setUpUnsolvable4x4() {
        List<SlidingTile> tiles = makeTiles(4);
        Collections.swap(tiles, 10, 11);
        SlidingBoard board = new SlidingBoard(tiles, 4, 4);
        boardManagerUnsolvable4x4 = new SlidingBoardManager(board);
    }

    /**
     * Make a solved 5x5 SlidingBoard.
     */
    private void setUpCorrect5x5() {
        List<SlidingTile> tiles = makeTiles(5);
        SlidingBoard board = new SlidingBoard(tiles, 5, 5);
        boardManager5x5 = new SlidingBoardManager(board);
    }

    /**
     * Make an unsolvable 5x5 SlidingBoard.
     */
    private void setUpUnsolvable5x5() {
        List<SlidingTile> tiles = makeTiles(5);
        Collections.swap(tiles, 18, 19);
        SlidingBoard board = new SlidingBoard(tiles, 5, 5);
        boardManagerUnsolvable5x5 = new SlidingBoardManager(board);
    }

    /**
     * Shuffle a few tiles.
     */
    private void swapFirstTwoTiles() {
        boardManager3x3.getBoard().swapTiles(0, 0, 0, 1);
    }

    /**
     * Test whether swapping two tiles makes a solved board unsolved.
     */
    @Test
    public void testIsSolved() {
        setUpCorrect3x3();
        assertTrue(boardManager3x3.puzzleSolved());
        swapFirstTwoTiles();
        assertFalse(boardManager3x3.puzzleSolved());
    }

    /**
     * Test whether swapping the first two tiles works.
     */
    @Test
    public void testSwapFirstTwo() {
        setUpCorrect4x4();
        assertEquals(1, boardManager4x4.getBoard().getTile(0, 0).getId());
        assertEquals(2, boardManager4x4.getBoard().getTile(0, 1).getId());
        boardManager4x4.getBoard().swapTiles(0, 0, 0, 1);
        assertEquals(2, boardManager4x4.getBoard().getTile(0, 0).getId());
        assertEquals(1, boardManager4x4.getBoard().getTile(0, 1).getId());
        SlidingBoardManager boardManagerNew5x5 = new SlidingBoardManager(5, 5);
        int first = boardManagerNew5x5.getBoard().getTile(0, 0).getId();
        int second = boardManagerNew5x5.getBoard().getTile(0, 1).getId();
        boardManagerNew5x5.getBoard().swapTiles(0, 0, 0, 1);
        assertEquals(second, boardManagerNew5x5.getBoard().getTile(0, 0).getId());
        assertEquals(first, boardManagerNew5x5.getBoard().getTile(0, 1).getId());

    }

    /**
     * Test whether touch move swaps tiles correctly
     */
    @Test
    public void testTouchMove() {
        setUpCorrect4x4();
        boardManager4x4.touchMove(14);
        assertEquals(boardManager4x4.getBoard().getTile(3,2).getId(), 0);
        assertEquals(boardManager4x4.getBoard().getTile(3,3).getId(), 15);
    }

    /**
     * Test whether swapping the last two tiles works.
     */
    @Test
    public void testSwapLastTwo() {
        setUpCorrect4x4();
        assertEquals(15, boardManager4x4.getBoard().getTile(3, 2).getId());
        assertEquals(0, boardManager4x4.getBoard().getTile(3, 3).getId());
        boardManager4x4.getBoard().swapTiles(3, 3, 3, 2);
        assertEquals(0, boardManager4x4.getBoard().getTile(3, 2).getId());
        assertEquals(15, boardManager4x4.getBoard().getTile(3, 3).getId());
    }

    /**
     * Test whether isValidHelp works.
     */
    @Test
    public void testIsValidTap() {
        setUpCorrect4x4();
        assertTrue(boardManager4x4.isValidTap(11));
        assertTrue(boardManager4x4.isValidTap(14));
        assertFalse(boardManager4x4.isValidTap(10));
    }

    /**
     * Test whether a 3x3 board is always solvable.
     */
    @Test
    public void testIsSolvable() {
        setUpCorrect3x3();
        setUpUnsolvable3x3();
        assertTrue(boardManager3x3.getBoard().isSolvable());
        assertFalse(boardManagerUnsolvable3x3.getBoard().isSolvable());
        setUpCorrect4x4();
        setUpUnsolvable4x4();
        assertTrue(boardManager4x4.getBoard().isSolvable());
        assertFalse(boardManagerUnsolvable4x4.getBoard().isSolvable());
        setUpCorrect5x5();
        setUpUnsolvable5x5();
        assertTrue(boardManager5x5.getBoard().isSolvable());
        assertFalse(boardManagerUnsolvable5x5.getBoard().isSolvable());
    }

}


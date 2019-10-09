package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing for minesweeper game.
 */
public class MinesweeperBoardAndTileTest {
    /**
     * Board manager for testing.
     */
    private MinesweeperManager boardManager;

    /**
     * Create tiles for testing.
     *
     * @return list of tiles
     */
    private List<MinesweeperTile> makeTiles() {

        MinesweeperTile[] list2 = {
                new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"),
                new MinesweeperTile("2"), new MinesweeperTile("2"), new MinesweeperTile("1"), new MinesweeperTile("0"), new MinesweeperTile("1"), new MinesweeperTile("1"), new MinesweeperTile("2"), new MinesweeperTile("1"), new MinesweeperTile("1"), new MinesweeperTile("0"),
                new MinesweeperTile("*"), new MinesweeperTile("*"), new MinesweeperTile("1"), new MinesweeperTile("0"), new MinesweeperTile("1"), new MinesweeperTile("*"), new MinesweeperTile("4"), new MinesweeperTile("*"), new MinesweeperTile("2"), new MinesweeperTile("0"),
                new MinesweeperTile("*"), new MinesweeperTile("3"), new MinesweeperTile("1"), new MinesweeperTile("0"), new MinesweeperTile("1"), new MinesweeperTile("3"), new MinesweeperTile("*"), new MinesweeperTile("*"), new MinesweeperTile("2"), new MinesweeperTile("0"),
                new MinesweeperTile("1"), new MinesweeperTile("2"), new MinesweeperTile("1"), new MinesweeperTile("1"), new MinesweeperTile("0"), new MinesweeperTile("2"), new MinesweeperTile("*"), new MinesweeperTile("3"), new MinesweeperTile("1"), new MinesweeperTile("0"),
                new MinesweeperTile("0"), new MinesweeperTile("1"), new MinesweeperTile("*"), new MinesweeperTile("1"), new MinesweeperTile("0"), new MinesweeperTile("1"), new MinesweeperTile("1"), new MinesweeperTile("1"), new MinesweeperTile("0"), new MinesweeperTile("0"),
                new MinesweeperTile("0"), new MinesweeperTile("1"), new MinesweeperTile("2"), new MinesweeperTile("2"), new MinesweeperTile("1"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"),
                new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("1"), new MinesweeperTile("*"), new MinesweeperTile("1"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"),
                new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("1"), new MinesweeperTile("1"), new MinesweeperTile("1"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"),
                new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0"), new MinesweeperTile("0")
        };
        return new ArrayList<>(Arrays.asList(list2));
    }

    /**
     * Set up random minesweeper game.
     */
    private void setUpRandom() {
        boardManager = new MinesweeperManager(10, 10);
    }

    /**
     * Set up a minesweeper game.
     */
    private void setUp() {
        List<MinesweeperTile> tiles = makeTiles();
        MinesweeperBoard board = new MinesweeperBoard(tiles);
        boardManager = new MinesweeperManager(board);
    }

    /**
     * Set up a solved minesweeper game.
     */
    private void setUpSolved() {
        setUp();
        for (MinesweeperTile tile : boardManager.getMinesweeperBoard()) {
            if (!tile.getValue().equals("*")) {
                tile.setFlipped(true);
            }
        }
    }

    /**
     * Tests if tile background is correctly flipped or not flipped.
     */
    @Test
    public void testTileBackground() {
        setUp();
        assertEquals(boardManager.getMinesweeperBoard().getTile(0, 0).getBackground(), R.drawable.minesweeperbutton);
        setUpSolved();
        assertEquals(boardManager.getMinesweeperBoard().getTile(0, 0).getBackground(), R.drawable.minesweeperblank);
    }

    /**
     * Test if touch move correctly flips the tiles.
     */
    @Test
    public void testFlipTiles() {
        setUp();
        boardManager.touchMove(3);
        assertTrue(boardManager.getMinesweeperBoard().getTile(0, 3).getFlipped());
        boardManager.touchMove(20);
        assertTrue(boardManager.getMinesweeperBoard().getTile(2, 0).getFlipped());
    }

    /**
     * Test that game overs are properly recognized.
     */
    @Test
    public void testGameOver() {
        setUp();
        assertFalse(boardManager.gameOver());
        boardManager.getMinesweeperBoard().flipAllTiles();
        assertTrue(boardManager.gameOver());
    }

    /**
     * Test that getters and setters for elapsed time work correctly.
     */
    @Test
    public void testElapsedTime() {
        setUp();
        boardManager.setStartTime();
        assertEquals(0, boardManager.getElapsedTime());
        boardManager.setOldElapsedTime();
        assertEquals(0, boardManager.getElapsedTime());
    }

    /**
     * Test that getters and setters for didWriteScore work correctly.
     */
    @Test
    public void testDidWriteScore() {
        setUp();
        assertFalse(boardManager.getDidWriteScore());
        boardManager.setDidWriteScore(true);
        assertTrue(boardManager.getDidWriteScore());
    }

    /**
     * Test that puzzleSolved returns correctly for random new boards and solved boards.
     */
    @Test
    public void testPuzzleSolved() {
        setUpRandom();
        assertFalse(boardManager.puzzleSolved());
        setUpSolved();
        assertTrue(boardManager.puzzleSolved());
    }

    /**
     * Test that a valid tap returns true.
     */
    @Test
    public void testIsValidTap() {
        setUp();
        assertTrue(boardManager.isValidTap(0));
    }

}

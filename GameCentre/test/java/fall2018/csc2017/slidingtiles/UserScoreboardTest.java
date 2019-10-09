package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UserScoreboardTest {
    /**
     * User scoreboard object for testing ascending scores.
     */
    private UserScoreboard userScoreboardAsc = new UserScoreboard("bob");
    /**
     * User scoreboard object for testing descending scores.
     */
    private UserScoreboard userScoreboardDesc = new UserScoreboard("john");

    /**
     * Test add score method for an ascending order game
     */
    @Test
    public void testAddScoreAscending() {
        userScoreboardAsc.addScoreAscending(UserScoreboard.GAME_NAMES[0], 50);
        List<Integer> equalList = new ArrayList<>();
        equalList.add(50);
        equalList.add(0);
        equalList.add(0);
        assertEquals(equalList, userScoreboardAsc.getScores("tiles3x3"));
    }

    /**
     * Test add score method for an descending order game
     */
    @Test
    public void testAddScoreDescending() {
        userScoreboardDesc.addScoreDescending(UserScoreboard.GAME_NAMES[4], 10);
        List<Integer> equalList = new ArrayList<>();
        equalList.add(10);
        equalList.add(0);
        equalList.add(0);
        assertEquals(equalList, userScoreboardDesc.getScores("pattern"));
    }

    /**
     * Test method that returns what to display on scoreboard
     */
    @Test
    public void testScoreToDisplay() {
        assertArrayEquals(new String[]{"", ""}, userScoreboardAsc.scoreToDisplay("tiles3x3", 2));
        userScoreboardDesc.addScoreDescending(UserScoreboard.GAME_NAMES[4], 10);
        assertArrayEquals(new String[]{"john", "10"}, userScoreboardDesc.scoreToDisplay("pattern", 0));
    }
}

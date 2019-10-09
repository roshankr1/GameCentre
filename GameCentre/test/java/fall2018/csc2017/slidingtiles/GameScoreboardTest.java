package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class GameScoreboardTest {
    /**
     * Game scoreboard object for testing ascending scores
     */
    private GameScoreboard gameScoreboardAsc = new GameScoreboard();
    /**
     * Game scoreboard object for testing descending scores
     */
    private GameScoreboard gameScoreboardDesc = new GameScoreboard();

    /**
     * Test add score method for an ascending order game
     */
    @Test
    public void testAddScoreAscending() {
        List<String> userScore1 = new ArrayList<>();
        userScore1.add("bob");
        userScore1.add("70");
        List<String> userScore2 = new ArrayList<>();
        userScore2.add("mark");
        userScore2.add("50");
        List<List<String>> equalList = new ArrayList<>();
        List<String> emptyList = new ArrayList<>();
        emptyList.add("");
        emptyList.add("0");
        equalList.add(userScore2);
        equalList.add(userScore1);
        equalList.add(emptyList);
        gameScoreboardAsc.addScoreAscending(GameScoreboard.GAME_NAMES[0], userScore1);
        gameScoreboardAsc.addScoreAscending(GameScoreboard.GAME_NAMES[0], userScore2);
        assertEquals(equalList, gameScoreboardAsc.getScores("tiles3x3"));
    }

    /**
     * Test add score method for an descending order game
     */
    @Test
    public void testAddScoreDescending() {
        List<String> userScore1 = new ArrayList<>();
        userScore1.add("john");
        userScore1.add("10");
        List<String> userScore2 = new ArrayList<>();
        userScore2.add("john");
        userScore2.add("20");
        List<List<String>> equalList = new ArrayList<>();
        List<String> emptyList = new ArrayList<>();
        emptyList.add("");
        emptyList.add("0");
        equalList.add(userScore2);
        equalList.add(userScore1);
        equalList.add(emptyList);
        gameScoreboardDesc.addScoreDescending(GameScoreboard.GAME_NAMES[4], userScore1);
        gameScoreboardDesc.addScoreDescending(GameScoreboard.GAME_NAMES[4], userScore2);
        assertEquals(equalList, gameScoreboardDesc.getScores("pattern"));
    }

    /**
     * Test method that returns what to display on scoreboard
     */
    @Test
    public void testScoreToDisplay() {
        assertEquals("", gameScoreboardAsc.scoreToDisplay("tiles3x3", 2));
        List<String> userScore = new ArrayList<>();
        userScore.add("john");
        userScore.add("10");
        gameScoreboardDesc.addScoreDescending(GameScoreboard.GAME_NAMES[4], userScore);
        assertEquals("10", gameScoreboardDesc.scoreToDisplay("pattern", 0));
    }
}
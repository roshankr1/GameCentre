package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The scoreboard
 */
class UserScoreboard implements Serializable {
    /**
     * A user scoreboard, maps the game to the high scores.
     */
    private Map<String, List<Integer>> scoreboard = new HashMap<>();
    /**
     * List of game names.
     */
    static final String[] GAME_NAMES = {"tiles3x3", "tiles4x4", "tiles5x5", "minesweeper", "pattern"};
    /**
     * The user of this scoreboard.
     */
    private String userName;

    /**
     * Create the new user scoreboard
     */
    UserScoreboard(String userName) {
        super();
        for (String game : UserScoreboard.GAME_NAMES) {
            List<Integer> scores = new ArrayList<>();
            scores.add(0);
            scores.add(0);
            scores.add(0);
            scoreboard.put(game, scores);
        }
        this.userName = userName;
    }

    /**
     * Add scores to the scoreboard for given game in ascending order
     *
     * @param game  game to add score for
     * @param score the user's score
     */
    void addScoreAscending(String game, Integer score) {
        List<Integer> scores = scoreboard.get(game);
        // only add new score to scoreboard if it is at least smaller than the lowest top score
        if (score < scores.get(scores.size() - 1) || scores.get(scores.size() - 1) == 0) {
            scores.set(scores.size() - 1, score);
        }

        Collections.sort(scores);
        // update the scoreboard map
        scoreboard.put(game, scores);
        reformatScores(game);
    }

    /**
     * Add scores to the scoreboard for given game in ascending order
     *
     * @param game  game to add score for
     * @param score the user's score
     */
    void addScoreDescending(String game, Integer score) {
        List<Integer> scores = scoreboard.get(game);

        // only add new score to scoreboard if it is at least bigger than the lowest top score
        if (score > scores.get(scores.size() - 1)) {
            scores.set(scores.size() - 1, score);
        }
        Collections.sort(scores, Collections.<Integer>reverseOrder());
        // update the scoreboard map
        scoreboard.put(game, scores);
        reformatScores(game);
    }

    /**
     * Reformats scoreboard to move default scores to the end.
     *
     * @param game the game to reformat scores for
     */
    private void reformatScores(String game) {
        List<Integer> scores = scoreboard.get(game);
        List<Integer> formattedScores = new ArrayList<>();
        int defaultCount = 0;
        for (Integer i : scores) {
            if (i != 0) {
                formattedScores.add(i);
            } else {
                defaultCount++;
            }
        }
        for (int i = 0; i != defaultCount; i++) {
            formattedScores.add(0);
        }
        scoreboard.put(game, formattedScores);
    }

    /**
     * Return the top scores of a given game.
     *
     * @param name game to get scores of
     * @return list of top scores
     */
    List<Integer> getScores(String name) {
        return scoreboard.get(name);
    }

    String[] scoreToDisplay(String game, int index) {
        List<Integer> scores = scoreboard.get(game);
        if (scores.get(index) == 0) {
            return new String[]{"", ""};
        } else {
            return new String[]{userName, Integer.toString(scores.get(index))};
        }
    }


}

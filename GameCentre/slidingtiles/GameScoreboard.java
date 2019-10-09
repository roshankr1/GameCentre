package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GameScoreboard implements Serializable {
    /**
     * A game scoreboard, maps the games to top player's and their scores.
     */
    private Map<String, List<List<String>>> scoreboard = new HashMap<>();
    /**
     * List of game names.
     */
    static final String[] GAME_NAMES = {"tiles3x3", "tiles4x4", "tiles5x5", "minesweeper", "pattern"};

    /**
     * Create a game scoreboard.
     */
    GameScoreboard() {
        super();
        for (String game : GameScoreboard.GAME_NAMES) {
            List<List<String>> scores = new ArrayList<>();
            for (int i = 0; i != 3; i++) {
                List<String> userScore = new ArrayList<>();
                userScore.add("");
                userScore.add("0");
                scores.add(userScore);
            }
            scoreboard.put(game, scores);
        }
    }

    /**
     * Comparator comparing scores of users in ascending order.
     */
    class AscendingComparator implements Comparator<List<String>> {
        @Override
        public int compare(List<String> user1, List<String> user2) {
            Integer score1 = Integer.parseInt(user1.get(1));
            Integer score2 = Integer.parseInt(user2.get(1));
            if (score1 == 0) {
                return 1;
            } else if (score2 == 0) {
                return -1;
            }
            return score1.compareTo(score2);
        }
    }

    /**
     * Comparator comparing scores of users in descending order.
     */
    class DescendingComparator implements Comparator<List<String>> {
        @Override
        public int compare(List<String> user1, List<String> user2) {
            Integer score1 = Integer.parseInt(user1.get(1));
            Integer score2 = Integer.parseInt(user2.get(1));
            if (score1 == 0) {
                return 1;
            } else if (score2 == 0) {
                return -1;
            }
            return score2.compareTo(score1);
        }
    }

    /**
     * Add score to scoreboard if its within the top number of scores. Sort by ascending order.
     *
     * @param game      the game to set top scores
     * @param userScore list of scores with usernames
     */
    void addScoreAscending(String game, List<String> userScore) {
        List<List<String>> scores = scoreboard.get(game);

        AscendingComparator comparator = new AscendingComparator();
        if (comparator.compare(userScore, scores.get(scores.size() - 1)) < 0 || scores.get(scores.size() - 1).get(1).equals("0")) {
            scores.set(scores.size() - 1, userScore);
        }
        Collections.sort(scores, new AscendingComparator());
        scoreboard.put(game, scores);
        reformatScores(game);
    }

    /**
     * Add score to scoreboard if its within the top number of scores. Sort by descending order.
     *
     * @param game      the game to set top scores
     * @param userScore list of scores with usernames
     */
    void addScoreDescending(String game, List<String> userScore) {
        List<List<String>> scores = scoreboard.get(game);

        DescendingComparator comparator = new DescendingComparator();
        if (comparator.compare(userScore, scores.get(scores.size() - 1)) < 0) {
            scores.set(scores.size() - 1, userScore);
        }
        Collections.sort(scores, new DescendingComparator());
        scoreboard.put(game, scores);
        reformatScores(game);
    }

    /**
     * Reformats scoreboard to move default scores to the end.
     *
     * @param game the game to reformat scores for
     */
    private void reformatScores(String game) {
        List<List<String>> scores = scoreboard.get(game);
        List<List<String>> formattedScores = new ArrayList<>();
        int defaultCount = 0;
        for (List<String> userScores : scores) {
            if (!userScores.get(1).equals("0")) {
                formattedScores.add(userScores);
            } else {
                defaultCount++;
            }
        }
        for (int i = 0; i != defaultCount; i++) {
            List<String> defaultScore = new ArrayList<>();
            defaultScore.add("");
            defaultScore.add("0");
            formattedScores.add(defaultScore);
        }
        scoreboard.put(game, formattedScores);
    }

    List<List<String>> getScores(String game) {
        return scoreboard.get(game);
    }

    String scoreToDisplay(String game, int index) {
        List<List<String>> scores = scoreboard.get(game);
        if (scores.get(index).get(1).equals("0")) {
            return "";
        } else {
            return scores.get(index).get(1);
        }
    }
}

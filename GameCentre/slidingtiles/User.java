package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A user
 */
class User implements Serializable {
    /**
     * The user's username.
     */
    private String username;

    /**
     * The user's password.
     */
    private String password;

    /**
     * The user's games.
     */
    private Map<String, String[]> games;

    /**
     * The user's file for scores.
     */
    private String scoresFile;

    /**
     * Initialize user.
     *
     * @param username user's username
     * @param password user's password
     * @param allGames user's games
     */
    User(String username, String password, String[] allGames) {
        this.username = username;
        this.password = password;
        this.games = new HashMap<>();
        for (String game : allGames) {
            String fileName = username + game + ".ser";
            String fileNameTemp = username + game + "Temp" + ".ser";
            String[] files = {fileName, fileNameTemp};
            this.games.put(game, files);
        }
        this.scoresFile = username + "scores" + ".ser";
    }

    /**
     * Get game file.
     *
     * @param gameName game
     * @return the game file
     */
    String getGameFile(String gameName) {
        return games.get(gameName)[0];
    }

    /**
     * Get temp game file.
     *
     * @param gameName game
     * @return the temp game file
     */
    String getTempFile(String gameName) {
        return games.get(gameName)[1];
    }

    /**
     * Get scoreboard file.
     *
     * @return the scores file name
     */
    String getScoresFile() {
        return this.scoresFile;
    }


    /**
     * Get username.
     *
     * @return user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set username.
     *
     * @param username new username
     */
    void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get password.
     *
     * @return user's password
     */
    String getPassword() {
        return password;
    }

    /**
     * Set password.
     *
     * @param password new password
     */
    void setPassword(String password) {
        this.password = password;
    }
}

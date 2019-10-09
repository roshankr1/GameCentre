package fall2018.csc2017.slidingtiles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Activity for scoreboard
 */
public class ScoreboardActivity extends AppCompatActivity {
    /**
     * File name for storing leaderboard for games.
     */
    private final static String GAME_FILE = "LEADERBOARD.ser";
    /**
     * Scoreboard for user for a game.
     */
    private UserScoreboard userScoreboard;
    /**
     * Scoreboard for game.
     */
    private GameScoreboard gameScoreboard;
    /**
     * The current user
     */
    private User currentUser;
    /**
     * Name of specified game for current game
     */
    private String currentGame;

    /**
     * Set app to light mode.
     */
    public static void setLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    /**
     * Set app to dark mode.
     */
    public static void setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }


    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        } else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        Bundle b = getIntent().getExtras();
        // get the name of the user and the game to display a scoreboard for
        if (b.get("userId") != null) {
            currentUser = (User) b.get("userId");
        }
        if (b.get("gameName") != null) {
            currentGame = (String) b.get("gameName");
        }
        String scoresFile = currentUser.getScoresFile();
        userScoreboard = new UserScoreboard(currentUser.getUsername());
        gameScoreboard = new GameScoreboard();
        loadScoresFromFile(scoresFile, "user");
        loadScoresFromFile(GAME_FILE, "leader");

        List<List<String>> leaderBoard = gameScoreboard.getScores(currentGame);

        TextView user1 = findViewById(R.id.User1);
        user1.setText(userScoreboard.scoreToDisplay(currentGame, 0)[0]);

        TextView user2 = findViewById(R.id.User2);
        user2.setText(userScoreboard.scoreToDisplay(currentGame, 1)[0]);

        TextView user3 = findViewById(R.id.User3);
        user3.setText(userScoreboard.scoreToDisplay(currentGame, 2)[0]);

        TextView user4 = findViewById(R.id.User4);
        user4.setText(leaderBoard.get(0).get(0));

        TextView user5 = findViewById(R.id.User5);
        user5.setText(leaderBoard.get(1).get(0));

        TextView user6 = findViewById(R.id.User6);
        user6.setText(leaderBoard.get(2).get(0));

        TextView score1 = findViewById(R.id.Score1);
        score1.setText(userScoreboard.scoreToDisplay(currentGame, 0)[1]);

        TextView score2 = findViewById(R.id.Score2);
        score2.setText(userScoreboard.scoreToDisplay(currentGame, 1)[1]);

        TextView score3 = findViewById(R.id.Score3);
        score3.setText(userScoreboard.scoreToDisplay(currentGame, 2)[1]);

        TextView score4 = findViewById(R.id.Score4);
        score4.setText(gameScoreboard.scoreToDisplay(currentGame, 0));

        TextView score5 = findViewById(R.id.Score5);
        score5.setText(gameScoreboard.scoreToDisplay(currentGame, 1));

        TextView score6 = findViewById(R.id.Score6);
        score6.setText(gameScoreboard.scoreToDisplay(currentGame, 2));
    }


    /**
     * Load scoreboard from file
     */
    private void loadScoresFromFile(String fileName, String type) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                if (type.equals("user")) {
                    userScoreboard = (UserScoreboard) input.readObject();
                } else if (type.equals("leader")) {
                    gameScoreboard = (GameScoreboard) input.readObject();
                }
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }
}

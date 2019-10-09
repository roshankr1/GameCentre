package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The initial activity for the sliding puzzle tile game.
 */
public class SlidingStartingActivity extends AppCompatActivity {
    /**
     * The board manager.
     */
    private SlidingBoardManager boardManager;
    /**
     * The name of the sliding tiles game.
     */
    private static final String NAME = "tiles";
    /**
     * The current user.
     */
    private User currentUser;

    /**
     * The specific type of game
     */
    private String currentType;

    /**
     * The current save file.
     */
    private String currentFile;

    /**
     * The current temp save file.
     */
    private String currentTempFile;
    /**
     * The game size.
     */
    private int gameSize;

    /**
     * Set app to light mode.
     */
    static void setLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    /**
     * Set app to dark mode.
     */
    static void setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        } else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        gameSize = ((String) b.get("gameSize")).charAt(0) - '0';
        if (b.get("userId") != null) {
            currentUser = (User) b.get("userId");
        }
        if (b.get("gameSize") != null) {
            currentType = (String) b.get("gameSize");
        }
        currentFile = currentUser.getGameFile(NAME);
        currentTempFile = currentUser.getTempFile(NAME);
        setContentView(R.layout.activity_starting_);
        addStartButtonListener();
        addLoadButtonListener();
        addAutoLoadButtonListener();
        addScoreboardButtonListener();
    }

    /**
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardManager = new SlidingBoardManager(gameSize, gameSize);
                //NEW CODE
                while (!boardManager.getBoard().isSolvable()) {
                    boardManager = new SlidingBoardManager(gameSize, gameSize);
                }
                switchToAutoSaved();
            }
        });
    }

    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromFile(currentFile);
                if (boardManager == null) {
                    ActivityMethods.makeToast(SlidingStartingActivity.this, "No Loaded Game");
                } else {
                    if (boardManager.getSize() != gameSize) {
                        String msg = "Saved game size is " + boardManager.getSize() + " by " + boardManager.getSize();
                        ActivityMethods.makeToast(SlidingStartingActivity.this, msg);
                    } else {
                        saveToFile(currentFile);
                        ActivityMethods.makeToast(SlidingStartingActivity.this, "Loaded game");
                        switchToGame();
                    }
                }
            }
        });
    }

    /**
     * Activate the load from auto save button.
     */
    private void addAutoLoadButtonListener() {
        Button saveButton = findViewById(R.id.AutoLoadButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromFile(currentTempFile);
                if (boardManager == null) {
                    ActivityMethods.makeToast(SlidingStartingActivity.this, "No Loaded Game");
                } else {
                    if (boardManager.getSize() != gameSize) {
                        String msg = "Saved game size is " + boardManager.getSize() + " by " + boardManager.getSize();
                        ActivityMethods.makeToast(SlidingStartingActivity.this, msg);

                    } else {
                        saveToFile(currentTempFile);
                        ActivityMethods.makeToast(SlidingStartingActivity.this, "Loaded game");
                        switchToAutoSaved();
                    }

                }
            }
        });
    }

    /**
     * Add scoreboard button
     */
    private void addScoreboardButtonListener() {
        Button scoreboardButton = findViewById(R.id.ScoreboardButton);
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToScoreboard(currentUser, currentType, NAME);
            }
        });
    }


    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(currentTempFile);
    }

    /**
     * Switch to saved game`
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, SlidingGameActivity.class);
        tmp.putExtra("user game file", currentFile);
        tmp.putExtra("user temp game file", currentTempFile);
        tmp.putExtra("saved", "saved");
        tmp.putExtra("gameSize", gameSize);
        tmp.putExtra("userId", currentUser);
        tmp.putExtra("typeId", currentType);
        saveToFile(currentFile);
        startActivity(tmp);
    }

    /**
     * Switch to auto saved game
     */
    private void switchToAutoSaved() {
        Intent tmp = new Intent(this, SlidingGameActivity.class);
        tmp.putExtra("user game file", currentFile);
        tmp.putExtra("user temp game file", currentTempFile);
        tmp.putExtra("saved", "auto");
        tmp.putExtra("gameSize", gameSize);
        tmp.putExtra("userId", currentUser);
        tmp.putExtra("typeId", currentType);
        saveToFile(currentTempFile);
        startActivity(tmp);
    }

    /**
     * Switch to scoreboard activity
     *
     * @param user current user
     * @param type specific type of game
     * @param game name of game
     */
    private void switchToScoreboard(User user, String type, String game) {
        Intent tmp = new Intent(this, ScoreboardActivity.class);
        tmp.putExtra("userId", user);
        tmp.putExtra("gameName", game + type);
        startActivity(tmp);
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (SlidingBoardManager) input.readObject();
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

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}

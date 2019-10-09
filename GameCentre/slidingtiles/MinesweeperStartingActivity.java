package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The initial activity for the minesweeper game.
 */
public class MinesweeperStartingActivity extends AppCompatActivity {
    /**
     * The number of rows
     */
    private final int NUM_ROWS = 10;
    /**
     * The number of columns
     */
    private final int NUM_COLS = 10;
    /**
     * The minesweeper board manager.
     */
    private MinesweeperManager minesweeperManager;
    /**
     * The name of the minesweeper game.
     */
    private static final String NAME = "minesweeper";
    /**
     * The current user.
     */
    private User currentUser;
    /**
     * The current save file.
     */
    private String currentFile;
    /**
     * The current temp save file.
     */
    private String currentTempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b.get("userId") != null) {
            currentUser = (User) b.get("userId");
        }
        currentFile = currentUser.getGameFile(NAME);
        currentTempFile = currentUser.getTempFile(NAME);
        setContentView(R.layout.activity_minesweeper_starting);
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
                minesweeperManager = new MinesweeperManager(NUM_ROWS, NUM_COLS);
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
                if (minesweeperManager == null) {
                    ActivityMethods.makeToast(MinesweeperStartingActivity.this, "No Loaded Game");
                } else {
                    saveToFile(currentFile);
                    ActivityMethods.makeToast(MinesweeperStartingActivity.this, "Loaded game");
                    minesweeperManager.setOldElapsedTime();
                    switchToGame();
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
                if (minesweeperManager == null) {
                    ActivityMethods.makeToast(MinesweeperStartingActivity.this, "No Loaded Game");
                } else {
                    saveToFile(currentTempFile);
                    ActivityMethods.makeToast(MinesweeperStartingActivity.this, "Loaded game");
                    minesweeperManager.setOldElapsedTime();
                    switchToAutoSaved();
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
                switchToScoreboard(currentUser, NAME);
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
     * Switch to auto saved game
     */
    private void switchToAutoSaved() {
        Intent tmp = new Intent(this, MinesweeperActivity.class);
        tmp.putExtra("user game file", currentFile);
        tmp.putExtra("user temp game file", currentTempFile);
        tmp.putExtra("saved", "auto");
        tmp.putExtra("userId", currentUser);
        saveToFile(currentTempFile);
        startActivity(tmp);
    }

    /**
     * Switch to saved game
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, MinesweeperActivity.class);
        tmp.putExtra("user game file", currentFile);
        tmp.putExtra("user temp game file", currentTempFile);
        tmp.putExtra("saved", "saved");
        tmp.putExtra("userId", currentUser);
        saveToFile(currentFile);
        startActivity(tmp);
    }

    /**
     * Switch to scoreboard activity
     *
     * @param user current user
     * @param game name of game
     */
    private void switchToScoreboard(User user, String game) {
        Intent tmp = new Intent(this, ScoreboardActivity.class);
        tmp.putExtra("userId", user);
        tmp.putExtra("gameName", game);
        startActivity(tmp);
    }

    /**
     * Load the minesweeper manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                minesweeperManager = (MinesweeperManager) input.readObject();
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
     * Save the minesweeper manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(minesweeperManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}

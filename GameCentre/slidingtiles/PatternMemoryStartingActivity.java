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
 * The initial activity for the pattern memory game.
 */
public class PatternMemoryStartingActivity extends AppCompatActivity {
    /**
     * The pattern board manager.
     */
    private PatternBoardManager patternManager;
    /**
     * The name of the pattern memory game.
     */
    private static final String NAME = "pattern";
    /**
     * The current user.
     */
    private User currentUser;
    /**
     * The current temp save file.
     */
    private String currentTempFile;
    /**
     * The game size
     */
    private final int SIZE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b.get("userId") != null) {
            currentUser = (User) b.get("userId");
        }
        currentTempFile = currentUser.getTempFile(NAME);
        setContentView(R.layout.activity_pattern_starting_);
        addStartButtonListener();
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
                patternManager = new PatternBoardManager(SIZE, SIZE);
                switchToAutoSaved();
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
                if (patternManager == null) {
                    ActivityMethods.makeToast(PatternMemoryStartingActivity.this, "No Loaded Game");
                } else {
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
        Intent tmp = new Intent(this, PatternShowCorrectActivity.class);
        tmp.putExtra("user temp game file", currentTempFile);
        tmp.putExtra("saved", "auto");
        tmp.putExtra("gameSize", SIZE);
        tmp.putExtra("userId", currentUser);
        String TYPE = "7X7";
        tmp.putExtra("typeId", TYPE);
        saveToFile(currentTempFile);
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
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                patternManager = (PatternBoardManager) input.readObject();
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
            outputStream.writeObject(patternManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


}

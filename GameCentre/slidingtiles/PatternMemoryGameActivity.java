package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * The pattern memory game activity.
 */
public class PatternMemoryGameActivity extends AppCompatActivity implements Observer {
    /**
     * The pattern board manager.
     */
    private PatternBoardManager patternManager;
    /**
     * Type of saved file
     */
    private String typeSaved;
    /**
     * Current user
     */
    private User currentUser;
    /**
     * Auto saved file
     */
    private String currentTempFile;
    /**
     * File name for scoreboard
     */
    private String scoresFile;
    /**
     * Scoreboard to holds scores
     */
    private UserScoreboard userScoreboard;
    /**
     * Scoreboard file for the game.
     */
    private final static String GAME_FILE = "LEADERBOARD.ser";
    /**
     * Scoreboard for the game.
     */
    private GameScoreboard gameScoreboard;
    /**
     * The game size.
     */
    private int gameSize;

    /**
     * The pattern tile buttons to display.
     */
    private List<Button> patternTileButtons;
    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;

    /**
     * Set up the background image for each button on the master list
     * of positions and then call the adapter to set the view.
     */
    //Display
    public void display() {
        updateTileButtons();
        writeScore();
        gridView.setAdapter(new CustomAdapter(patternTileButtons, columnWidth, columnHeight));
        saveToFile(currentTempFile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b.get("user temp game file") != null) {
            currentTempFile = (String) b.get("user temp game file");
        }
        if (b.get("saved") != null) {
            typeSaved = (String) b.get("saved");
        }
        if (typeSaved.equals("auto")) {
            loadFromFile(currentTempFile);
        }
        if (b.get("userId") != null) {
            currentUser = (User) b.get("userId");
        }
        gameSize = (Integer) b.get("gameSize");
        createTileButtons(this);
        setContentView(R.layout.activity_pattern_main);

        scoresFile = currentUser.getScoresFile();
        userScoreboard = new UserScoreboard(currentUser.getUsername());
        gameScoreboard = new GameScoreboard();
        addUndoButtonListener();
        addQuitButtonListener();
        addCheckButtonListener();

        //Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(gameSize);
        gridView.setPatternManager(patternManager);
        patternManager.getPatternBoard().addObserver(this);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / gameSize;
                        columnHeight = displayHeight / gameSize;

                        display();
                    }
                });
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        patternTileButtons = new ArrayList<>();
        for (PatternTile tile : patternManager.getCorrectBoard()) {
            Button tmp = new Button(context);
            tmp.setBackgroundResource(tile.getBackground());
            this.patternTileButtons.add(tmp);
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        PatternBoard board = patternManager.getPatternBoard();
        int nextPos = 0;
        for (Button b : patternTileButtons) {
            int row = nextPos / board.getNumRows();
            int col = nextPos % board.getNumCols();
            b.setBackgroundResource(board.getTile(row, col).getBackground());
            nextPos++;
        }
    }

    /**
     * Writes current score to game board.
     */
    void writeScore() {
        TextView currentScore = findViewById(R.id.ScoreText);
        //change all games to use getScore(), implementation can be different
        int score = patternManager.getScore();
        currentScore.setText(String.format("%d", score));
    }

    /**
     * Save score to scoreboard.
     */
    void saveScore() {
        int score = patternManager.getScore() - 1;
        loadScoresFromFile(scoresFile, "user");
        userScoreboard.addScoreDescending("pattern", score);
        saveScoresToFile(scoresFile, "user");

        loadScoresFromFile(GAME_FILE, "leader");
        List<String> userScore = new ArrayList<>();
        userScore.add(currentUser.getUsername());
        userScore.add(Integer.toString(score));
        gameScoreboard.addScoreDescending("pattern", userScore);
        saveScoresToFile(GAME_FILE, "leader");
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(currentTempFile);
    }

    /**
     * Load the pattern board manager from fileName.
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
     * Save the pattern board manager to fileName.
     *
     * @param fileName the name of the file
     */
    private void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(patternManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
    }

    /**
     * Activate Undo button.
     */
    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!patternManager.undo()) {
                    ActivityMethods.makeToast(PatternMemoryGameActivity.this, "No moves to undo");

                } else {
                    display();
                    ActivityMethods.makeToast(PatternMemoryGameActivity.this, "Undo Successful");

                }
            }
        });
    }

    /**
     * Activate the quit button, saves current level to scoreboard.
     */
    private void addQuitButtonListener() {
        Button quitButton = findViewById(R.id.QuitButton);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScore();
                ActivityMethods.makeToast(PatternMemoryGameActivity.this, "Score Saved");
                switchToStarting();
            }
        });
    }

    /**
     * Activate the check button. Lets the user check whether current board is correct.
     */
    private void addCheckButtonListener() {
        Button checkButton = findViewById(R.id.CheckButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (patternManager.puzzleSolved()) {
                    if (patternManager.updateLevel()) {
                        String level = Integer.toString(patternManager.getScore());
                        ActivityMethods.makeToast(PatternMemoryGameActivity.this, "Correct! Next Level " + level);
                        switchToNextLevel();

                    } else {
                        ActivityMethods.makeToast(PatternMemoryGameActivity.this, "Correct! Max Level Reached!");
                    }
                } else {
                    ActivityMethods.makeToast(PatternMemoryGameActivity.this, "Incorrect Pattern");
                }
            }
        });
    }

    /**
     * Switch to auto saved game
     */
    private void switchToNextLevel() {
        Intent tmp = new Intent(this, PatternShowCorrectActivity.class);
        tmp.putExtra("user temp game file", currentTempFile);
        tmp.putExtra("saved", "auto");
        tmp.putExtra("gameSize", gameSize);
        tmp.putExtra("userId", currentUser);
        String TYPE = "7X7";
        tmp.putExtra("typeId", TYPE);
        saveToFile(currentTempFile);
        startActivity(tmp);
    }

    /**
     * Switch to main menu.
     */
    private void switchToStarting() {
        Intent tmp = new Intent(this, PatternMemoryStartingActivity.class);
        tmp.putExtra("userId", currentUser);
        tmp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(tmp);
    }

    /**
     * Saves scoreboard to file
     */
    private void saveScoresToFile(String fileName, String type) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            if (type.equals("user")) {
                outputStream.writeObject(userScoreboard);
            } else if (type.equals("leader")) {
                outputStream.writeObject(gameScoreboard);
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
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

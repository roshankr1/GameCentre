package fall2018.csc2017.slidingtiles;

import android.content.Context;
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
 * The game activity for minesweeper.
 */
public class MinesweeperActivity extends AppCompatActivity implements Observer {
    /**
     * The minesweeper manager.
     */
    private MinesweeperManager minesweeperManager;
    /**
     * The type of saved file.
     */
    private String typeSaved;
    /**
     * The current user.
     */
    private User currentUser;
    /**
     * The number of rows
     */
    private final int NUM_ROWS = 10;
    /**
     * The number of columns
     */
    private final int NUM_COLS = 10;
    /**
     * The save file.
     */
    private String currentFile;
    /**
     * The auto saved file.
     */
    private String currentTempFile;
    /**
     * File name for scoreboard.
     */
    private String scoresFile;
    /**
     * Scoreboard to hold scores.
     */
    private UserScoreboard userScoreboard;
    /**
     * File name for storing leaderboard for games.
     */
    private final static String GAME_FILE = "LEADERBOARD.ser";
    /**
     * Scoreboard for the game.
     */
    private GameScoreboard gameScoreboard;
    /**
     * The buttons to display.
     */
    private List<Button> tileButtons;
    /**
     * The grid view.
     */
    private GestureDetectGridView gridView;
    /**
     * The column width and height based on device size.
     */
    private static int columnWidth, columnHeight;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    public void display() {
        updateTileButtons();
        writeScore();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
        saveToFile(currentTempFile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b.get("user temp game file") != null) {
            currentTempFile = (String) b.get("user temp game file");
        }
        if (b.get("user game file") != null && b.get("saved") != null) {
            currentFile = (String) b.get("user game file");
            typeSaved = (String) b.get("saved");
        }
        if (typeSaved.equals("saved")) {
            loadFromFile(currentFile);
        } else if (typeSaved.equals("auto")) {
            loadFromFile(currentTempFile);
        }

        if (b.get("userId") != null) {
            currentUser = (User) b.get("userId");
        }

        createTileButtons(this);
        setContentView(R.layout.activity_minesweeper_game);
        scoresFile = currentUser.getScoresFile();
        userScoreboard = new UserScoreboard(currentUser.getUsername());
        gameScoreboard = new GameScoreboard();
        addSaveButtonListener();

        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(NUM_COLS);
        gridView.setMinesweeperManager(minesweeperManager);
        minesweeperManager.addObserver(this);
        minesweeperManager.getMinesweeperBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / NUM_COLS;
                        columnHeight = displayHeight / NUM_ROWS;

                        display();
                    }
                });
        minesweeperManager.setStartTime();
        // Update score every second taken from https://stackoverflow.com/questions/14814714/update-textview-every-second
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                writeScore();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Log.e("score updater", "Update score was interrupted.");
                }
            }
        };

        thread.start();
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        MinesweeperBoard board = minesweeperManager.getMinesweeperBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != board.getNumRows(); row++) {
            for (int col = 0; col != board.getNumCols(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        MinesweeperBoard board = minesweeperManager.getMinesweeperBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / board.getNumRows();
            int col = nextPos % board.getNumCols();
            b.setBackgroundResource(board.getTile(row, col).getBackground());
            nextPos++;
        }
    }

    /**
     * Writes current score to the screen, saves score when user wins
     */
    void writeScore() {
        TextView currentScore = findViewById(R.id.ScoreText);
        currentScore.setText(String.format("%d", minesweeperManager.getElapsedTime()));
        if (minesweeperManager.puzzleSolved() && !minesweeperManager.gameOver() && !minesweeperManager.getDidWriteScore()) {
            int score = Math.toIntExact(minesweeperManager.getElapsedTime());
            loadScoresFromFile(scoresFile, "user");
            userScoreboard.addScoreAscending("minesweeper", score);
            saveScoresToFile(scoresFile, "user");

            loadScoresFromFile(GAME_FILE, "leader");
            List<String> userScore = new ArrayList<>();
            userScore.add(currentUser.getUsername());
            userScore.add(Integer.toString(score));
            gameScoreboard.addScoreAscending("minesweeper", userScore);
            saveScoresToFile(GAME_FILE, "leader");
            minesweeperManager.setDidWriteScore(true);
        }
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
     * Load the board manager from fileName.
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
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    private void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(minesweeperManager);
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

    /**
     * Activate the save button.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(currentFile);
                ActivityMethods.makeToast(MinesweeperActivity.this, "Game saved");
            }
        });
    }
}

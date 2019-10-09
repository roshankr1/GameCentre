package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for displaying the pattern to memorize for pattern memory game. Delay of 5 secs.
 */
public class PatternShowCorrectActivity extends AppCompatActivity {
    private static int TIME_OUT = 5000;
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
     * Type of game
     */
    private final String TYPE = "7X7";
    /**
     * Auto saved file
     */
    private String currentTempFile;
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
        gridView.setAdapter(new CustomAdapter(patternTileButtons, columnWidth, columnHeight));
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
        setContentView(R.layout.activity_pattern_corrrect);

        //Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(gameSize);
        gridView.setPatternManager(patternManager);
        //patternManager.getPatternBoard().addObserver(this);
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent tmp = new Intent(PatternShowCorrectActivity.this, PatternMemoryGameActivity.class);
                tmp.putExtra("user temp game file", currentTempFile);
                tmp.putExtra("saved", typeSaved);
                tmp.putExtra("gameSize", gameSize);
                tmp.putExtra("userId", currentUser);
                tmp.putExtra("typeId", TYPE);
                saveToFile(currentTempFile);
                tmp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tmp);
                finish();
            }
        }, TIME_OUT);
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

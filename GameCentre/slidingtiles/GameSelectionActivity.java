package fall2018.csc2017.slidingtiles;

/*
login - log in, sign up
selection - choose game
launcher - choose game mode
starting - load, save
game - play game
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity for game selection page.
 */
public class GameSelectionActivity extends AppCompatActivity {
    /**
     * The user.
     */
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b.get("userId") != null) {
            currentUser = (User) b.get("userId");
        }
        setContentView(R.layout.activity_game_selection);
        TextView textview = findViewById(R.id.textView);
        textview.setText(String.format("Hello %s! Choose your game!", currentUser.getUsername()));
        addSlidingTilesButtonListener();
        addMatchingTilesButtonListener();
        addMinesweeperButtonListener();
    }

    /**
     * Adds minesweeper button listener.
     */
    private void addMinesweeperButtonListener() {
        Button minesweeperButton = findViewById(R.id.minesweeper);
        minesweeperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToMinesweeper();
            }
        });
    }

    /**
     * Adds matching tiles game button listener.
     */
    private void addMatchingTilesButtonListener() {
        Button matchingTilesButton = findViewById(R.id.matching_tiles);
        matchingTilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToPattern();
            }
        });
    }

    /**
     * Switch to pattern memory game starting activity.
     */
    private void switchToPattern() {
        Intent tmp = new Intent(this, PatternMemoryStartingActivity.class);
        tmp.putExtra("userId", currentUser);
        startActivity(tmp);
    }

    /**
     * Adds sliding tiles game button listener.
     */
    private void addSlidingTilesButtonListener() {
        Button minesweeperButton = findViewById(R.id.sliding_tiles);
        minesweeperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSlidingTiles();
            }
        });
    }

    /**
     * Switch to sliding tiles.
     */
    private void switchToSlidingTiles() {
        Intent tmp = new Intent(this, GameLauncherActivity.class);
        tmp.putExtra("userId", currentUser);
        startActivity(tmp);
    }

    /**
     * Switch to minesweeper.
     */
    private void switchToMinesweeper() {
        Intent tmp = new Intent(this, MinesweeperStartingActivity.class);
        tmp.putExtra("userId", currentUser);
        startActivity(tmp);
    }
}

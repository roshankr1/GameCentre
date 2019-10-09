package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameLauncherActivity extends AppCompatActivity {
    /**
     * The user.
     */
    private User currentUser;

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
        if (b.get("userId") != null) {
            currentUser = (User) b.get("userId");
        }
        setContentView(R.layout.activity_game_launcher);
        TextView textview = findViewById(R.id.LauncherText);
        textview.setText(String.format("Hello %s! Choose your game", currentUser.getUsername()));
        addSlidingTiles3ButtonListener();
        addSlidingTiles4ButtonListener();
        addSlidingTiles5ButtonListener();
    }

    /**
     * Add button listener for 3x3 sliding tiles game.
     */
    private void addSlidingTiles3ButtonListener() {
        Button slidingtiles3button = findViewById(R.id.SlidingTiles3Button);
        slidingtiles3button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToStarting(currentUser, "3x3");
            }
        });
    }

    /**
     * Add button listener for 4x4 sliding tiles game.
     */
    private void addSlidingTiles4ButtonListener() {
        Button slidingtiles4button = findViewById(R.id.SlidingTiles4Button);
        slidingtiles4button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToStarting(currentUser, "4x4");
            }
        });
    }

    /**
     * Add button listener for 5x5 sliding tiles game.
     */
    private void addSlidingTiles5ButtonListener() {
        Button slidingtiles5button = findViewById(R.id.SlidingTiles5Button);
        slidingtiles5button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToStarting(currentUser, "5x5");
            }
        });
    }

    /**
     * Switch to game starting activity.
     *
     * @param user     a user
     * @param gameSize size of game
     */
    private void switchToStarting(User user, String gameSize) {
        Intent tmp = new Intent(this, SlidingStartingActivity.class);
        tmp.putExtra("userId", user);
        tmp.putExtra("gameSize", gameSize);
        startActivity(tmp);
    }
}

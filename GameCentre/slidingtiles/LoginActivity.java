package fall2018.csc2017.slidingtiles;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Activity for login page.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * File storing all users
     */
    private final String USERS_FILE = "users_file.ser";
    /**
     * Name of games in game centre
     */
    private final String[] ALL_GAMES = {"tiles", "minesweeper", "pattern"};
    /**
     * UserManager for game centre
     */
    private UserManager userManager;

    /**
     * LoginActivity Line 43-45, 94-97; GameLauncherActivity Line 33-35;
     * SlidingStartingActivity Line 77-79; ScoreboardActivity Line 59-61
     * was adapted from here: https://www.youtube.com/watch?v=-qsHE3TpJqw
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        } else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        userManager = new UserManager();
        setContentView(R.layout.activity_login);
        addSignInButtonListener();
        addSignUpButtonListener();
        addLightModeButtonListener();
        addDarkModeButtonListener();
    }

    /**
     * Light mode button
     */
    private void addLightModeButtonListener() {
        Button lightmodebutton = findViewById(R.id.LightModeButton);
        lightmodebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityMethods.makeToast(LoginActivity.this, "Light Mode On");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                GameLauncherActivity.setLightMode();
                SlidingStartingActivity.setLightMode();
                ScoreboardActivity.setLightMode();
                restartApp();
            }
        });
    }

    /**
     * Dark mode button
     */
    private void addDarkModeButtonListener() {
        Button darkmodebutton = findViewById(R.id.DarkModeButton);
        darkmodebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityMethods.makeToast(LoginActivity.this, "Dark Mode On");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                GameLauncherActivity.setDarkMode();
                SlidingStartingActivity.setDarkMode();
                ScoreboardActivity.setDarkMode();
                restartApp();
            }
        });
    }

    /**
     * Restarts the app
     */
    public void restartApp() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * Sign in button
     */
    private void addSignInButtonListener() {
        Button signinbutton = findViewById(R.id.SignInButton);
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromFile(USERS_FILE);
                EditText username = findViewById(R.id.Username);
                EditText password = findViewById(R.id.Password);
                if (!(userManager.userExists(username.getText().toString()))) {
                    ActivityMethods.makeToast(LoginActivity.this, "User Does Not Exist");
                } else {
                    if (userManager.validUser(username.getText().toString(), password.getText().toString())) {
                        User user = userManager.getUser(username.getText().toString());
                        //switchToGameLauncher(user);
                        switchToGameSelection(user);
                    } else {
                        ActivityMethods.makeToast(LoginActivity.this, "Incorrect Username or Password");
                    }
                }
            }
        });
    }

    /**
     * Sign up button
     */
    private void addSignUpButtonListener() {
        Button signUpButton = findViewById(R.id.SignUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromFile(USERS_FILE);
                EditText username = findViewById(R.id.Username);
                EditText password = findViewById(R.id.Password);
                if (!(userManager.userExists(username.getText().toString()))) {
                    if (userManager.addUser(username.getText().toString(), password.getText().toString(), ALL_GAMES)) {
                        ActivityMethods.makeToast(LoginActivity.this, "Created user successfully");
                        saveToFile(USERS_FILE);
                    } else {
                        ActivityMethods.makeToast(LoginActivity.this, "Username or password cannot be blank");
                    }

                } else {
                    ActivityMethods.makeToast(LoginActivity.this, "User already exists");
                }
            }
        });
    }

    /**
     * Switch to game centre interface for each user
     *
     * @param user user to switch to
     */
//    private void switchToGameLauncher(User user) {
//        Intent tmp = new Intent(this, GameLauncherActivity.class);
//        tmp.putExtra("userId", user);
//        startActivity(tmp);
//    }
    private void switchToGameSelection(User user) {
        Intent tmp = new Intent(this, GameSelectionActivity.class);
        tmp.putExtra("userId", user);
        startActivity(tmp);
    }


    /**
     * Load the user manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                userManager = (UserManager) input.readObject();
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
            outputStream.writeObject(userManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}



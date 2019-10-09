#README
#Note

##Setup Instructions 
* Use Android Studio.
1. Clone the repository from MarkUs by copying the URL and choosing to import from version
control -> Git
2. Enter the URL and import from Gradle. Click Next -> In the Gradle project field, append
"\Phase2\GameCentre" to the existing project path or navigate to the GameCentre folder manually. Then click Finish.
3. If an unregistered VCS message pops up, click "Add Root". The app should be able to
be built and run.


## Functionality Descriptions 
### Sign In/ Sign Up 
Users have to sign in or sign up to access the game centre. Users sign up by entering a username 
and a password and clicking the Sign Up button and then they can sign in. Existing users can sign 
in by correctly entering their username and password. 

### Minesweeper 
From logging in, users can choose the minesweeper game. You select tiles on the board and win if all the 
tiles flip over without hitting a bomb tile. Your score is how long you took to beat the game, it only
gets recorded if you win. You can save and auto save this game. 

### Matching Tiles 
From logging in, users can choose the matching tiles game. Colored tiles will show up on a board for 5 seconds, 
after which the player must recreate the pattern on a blank board. Clicking each tiles cycles through a set of 
colors. If the pattern is correct, clicking "Check" will advance player to the next level. If player chooses "Quit" 
the number of levels cleared will be saved to the scoreboard and the app will return to the matching tiles menu. 

### Scoreboard 
View Scoreboard button displays the top three scores for a user for the game they have just played 
and the top 3 scores for the game from all users. 

### Dark Mode 
Selecting Dark Mode from the login screen will change the app's theme colours to a dark theme. 

### Undo 
The undo button on a game allows the user to go back one step in the game. There are unlimited 
undos. Minesweeper has no undo. 

### Autosave and Save and Load
Users can choose when to save by clicking Save Game and the game will auto save after each move. 
The user can choose which version of the saved game to load from the loading screen. Matching tiles only 
has autosave. 

### Game Complexity 
Only for SlidingTiles game, the user can choose between 3x3, 4x4, and 5x5 game boards. Users can only 
load the game with the same complexity as the saved games. If a user saved a 4x4 game they cannot 
load a saved game from the 3x3 and 5x5 menu screens. They must select 4x4 to return to that loaded 
game. 


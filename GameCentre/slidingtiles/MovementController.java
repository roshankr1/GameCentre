package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.widget.Toast;


class MovementController {

    private SlidingBoardManager boardManager = null;
    private MinesweeperManager minesweeperManager = null;
    private PatternBoardManager patternBoardManager = null;

    MovementController() {
    }

    void setBoardManager(SlidingBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    void setMinesweeperManager(MinesweeperManager minesweeperManager) {
        this.minesweeperManager = minesweeperManager;
    }

    void setPatternBoardManager(PatternBoardManager patternManager) {
        this.patternBoardManager = patternManager;
    }

    void processTapMovement(Context context, int position, boolean display) {
        if (boardManager.isValidTap(position)) {
            boardManager.touchMove(position);
            if (boardManager.puzzleSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }

    void processTapMinesweeperMovement(Context context, int position, boolean display) {
        if (minesweeperManager.isValidTap(position) && !minesweeperManager.gameOver()) {
            minesweeperManager.touchMove(position);
            if (minesweeperManager.puzzleSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            } else if (minesweeperManager.gameOver()) {
                Toast.makeText(context, "YOU LOSE!", Toast.LENGTH_SHORT).show();
                minesweeperManager.getMinesweeperBoard().flipAllTiles();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }

    void processTapPatternMovement(Context context, int position, boolean display) {
        patternBoardManager.touchMove(position);
    }
}

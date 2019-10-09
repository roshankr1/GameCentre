package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.widget.Toast;

/**
 * Methods for Activity classes.
 */
class ActivityMethods {
    /**
     * Make a toast message.
     *
     * @param context the context
     * @param msg     message to display
     */
    static void makeToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

package net.pupil.newlife.common;

import android.app.Activity;
import android.widget.Toast;

public class PupilToastUtils {

    public static void toastShort(Activity activity, String content) {
        activity.runOnUiThread(() -> {
            Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
        });
    }
}

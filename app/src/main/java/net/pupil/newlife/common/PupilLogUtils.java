package net.pupil.newlife.common;

import android.util.Log;

public class PupilLogUtils {

    public static int LogLevel = Log.VERBOSE;

    public static void setLogLevel(int logLevel) {
        LogLevel = logLevel;
    }

    public static void hintLog() {
        LogLevel = 1;
    }

    public static void logV(String TAG, String content) {
        if (LogLevel >= Log.VERBOSE) {
            Log.v(TAG, content);
        }
    }

    public static void logD(String TAG, String content) {
        if (LogLevel >= Log.DEBUG) {
            content = "--->" + content;
            Log.d(TAG, content);
        }
    }

    public static void logI(String TAG, String content) {
        if (LogLevel >= Log.INFO) {
            content = "『" + content + "』";
            Log.i(TAG, content);
        }
    }

    public static void logW(String TAG, String content) {
        if (LogLevel >= Log.WARN) {
            content = "【" + content + "】";
            Log.w(TAG, content);
        }
    }

    public static void logE(String TAG, String content) {
        logE(TAG, content, null);
    }

    public static void logE(String TAG, String content, Throwable tr) {
        if (LogLevel >= Log.ERROR) {
            Log.e(TAG, content, tr);
        }
    }
}

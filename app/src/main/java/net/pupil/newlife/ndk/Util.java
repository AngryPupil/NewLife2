package net.pupil.newlife.ndk;

import android.util.Log;

public class Util {
    static {
        try {
            System.load("/data/data/net.pupil.newlife/assets/python/PIL/_imaging.so");
            System.load("/data/data/net.pupil.newlife/assets/python/PIL/_imagingmath.so");
            System.load("/data/data/net.pupil.newlife/assets/python/PIL/_imagingmorph.so");
            System.load("/data/data/net.pupil.newlife/assets/python/PIL/_imagingtk.so");
            System.loadLibrary("jni_test");
//            System.loadLibrary("_imaging");
        } catch (Exception e) {
            Log.e("jni_test_", ""+e);
        }

    }
    public static native int run(String path);
}

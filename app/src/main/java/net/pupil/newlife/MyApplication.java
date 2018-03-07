package net.pupil.newlife;

import android.app.Application;

import net.pupil.newlife.im.Constants;

import info.emm.messenger.IMClient;

/**
 * Created by Angry on 2017/4/20.
 */

public class MyApplication extends Application implements Constants {

    @Override
    public void onCreate() {
        super.onCreate();
//        IMClient.getInstance().init(getApplicationContext(), mediaServerIP, mediaServerport, httpServerAddress);
    }
}

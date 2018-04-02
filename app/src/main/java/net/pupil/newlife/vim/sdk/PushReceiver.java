package net.pupil.newlife.vim.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by fish902 on 2017/5/26.
 */

public class PushReceiver  extends BroadcastReceiver {
    private static final String TAG = "VIMActivity Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "FirstReceiver: " );
        String msg = intent.getStringExtra("msg");
        Log.i(TAG, "FirstReceiver msg: "+msg);
        com.royasoft.im.api.Message message = intent.getParcelableExtra("data");
        if(message != null) {
            Log.i(TAG, "FirstReceiver text: " + message.getText());
            Log.i(TAG, "FirstReceiver ext: " + message.getExt());
        }



    }
}

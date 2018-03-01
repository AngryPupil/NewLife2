package net.pupil.newlife.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import info.emm.messenger.IMClient;
import info.emm.messenger.MQ;
import info.emm.messenger.NotificationCenter;
import info.emm.messenger.VYEventListener;

public class InitReceiver extends BroadcastReceiver implements Constants, VYEventListener {

    private static final String TAG = "InitReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        IMClient.getInstance().init(context, mediaServerIP, mediaServerport, httpServerAddress);
        IMClient.getInstance().registerEventListener(this);
    }

    @Override
    public void onMessageArrival(MQ.VYMessage vyMessage) {
        Log.d(TAG, "onMessageArrival");

//        NotificationCenter.getInstance().postNotificationName(NotificationCenter.MessageArrivalCode, vyMessage);
        String message = ((MQ.textMessageBody) vyMessage.getBody()).getMessage();
        Log.d(TAG, "onMessageArrival From:" + vyMessage.getFrom() + " To:" + vyMessage.getTo() + " Message:" +message);
    }

    @Override
    public void onSendMessageResponse(MQ.VYMessage vyMessage) {
        String message = ((MQ.textMessageBody) vyMessage.getBody()).getMessage();
        Log.d(TAG, "onSendMessageResponse From:" + vyMessage.getFrom() + " To:" + vyMessage.getTo() + " Message:" +message);
    }
}

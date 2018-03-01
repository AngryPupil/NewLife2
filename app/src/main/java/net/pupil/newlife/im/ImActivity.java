package net.pupil.newlife.im;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import net.pupil.newlife.R;

import java.util.Date;
import java.util.List;

import info.emm.messenger.ChatManager;
import info.emm.messenger.IMClient;
import info.emm.messenger.MQ;
import info.emm.messenger.MediaController;
import info.emm.messenger.NotificationCenter;
import info.emm.messenger.VYCallBack;
import info.emm.messenger.VYConversation;

public class ImActivity extends AppCompatActivity implements Constants, VYCallBack, NotificationCenter.NotificationCenterDelegate {

    private final String TAG = "ImActivity";
    private static final int REQUEST_READ_PHONE_STATE = 2222;
    private static final int REQUEST_READ_PHONE_STATE2 = 2223;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im);

        findViewById(R.id.start_recording).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean isCancel = false;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        MediaController.getInstance().startRecording(userBUserId);
                        return true;
                    case MotionEvent.ACTION_MOVE: {
                        if (event.getY() < 0) {
                            MediaController.getInstance().stopRecording(false);
                            isCancel = true;
                        }
                        return true;
                    }
                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        if (event.getY() >= 0) {
                            if (!isCancel) {
                                MediaController.getInstance().stopRecording(true);
                            }
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        findViewById(R.id.load_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatManager.getInstance().loadAllConversations();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        NotificationCenter.getInstance().addObserver(this,NotificationCenter.recordProgressChanged);
        NotificationCenter.getInstance().addObserver(this,NotificationCenter.recordStarted);
        NotificationCenter.getInstance().addObserver(this,NotificationCenter.recordStartError);
        NotificationCenter.getInstance().addObserver(this,NotificationCenter.recordStopped);

        NotificationCenter.getInstance().addObserver(this,NotificationCenter.conversionsLoadedComplete);
        NotificationCenter.getInstance().addObserver(this,NotificationCenter.processGetUnReadCount);
    }

    public void login1(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            imLogin(userAUserId, userAPass);
        }
    }

    public void login2(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE2);
        } else {
            imLogin(userBUserId, userBPass);
        }
    }

    public void sendText(View view) {
        String content = "消息_"+ DateFormat.format("yyyy年MM月dd日 HH时mm分ss秒.SSS", new Date());
        if (content.length() > 0) {
            MQ.VYMessage message = MQ.VYMessage.createSendMessage(MQ.VYMessage.Type.TEXT);
            MQ.VYMessageBody msgbody = new MQ.textMessageBody(content);
            message.addBody(msgbody);
            message.setChatType(MQ.VYMessage.ChatType.CHATTYPE_SINGLE);
            message.setTo(userBUserId);
            IMClient.getInstance().SendMessage(message);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    imLogin(userAUserId, userAPass);
                }
                break;
            case REQUEST_READ_PHONE_STATE2:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    imLogin(userBUserId, userBPass);
                }
                break;

            default:
                break;
        }
    }

    private void imLogin(String userAUserId, String userAPass) {
        Log.d(TAG, "login... "+ IMClient.getInstance().isLoggedIn());
        IMClient.getInstance().login(userAUserId, userAPass, this);
    }

    @Override
    public void onError(int i) {
        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

        VYConversation conver = ChatManager.getInstance().getConversation(userBUserId, MQ.VYMessage.ChatType.CHATTYPE_SINGLE);
        conver.loadMessages(10);
        List<MQ.VYMessage> messageList = conver.getMessages();
        Log.d(TAG, "VYCallBack onSuccess size:" + messageList.size());
    }

    @Override
    public void didReceivedNotification(int i, Object... objects) {
        Log.d(TAG, "didReceivedNotification");

        if (i == NotificationCenter.conversionsLoadedComplete) {
            VYConversation conversation = ChatManager.getInstance().getConversationByIndex(0);
            MQ.textMessageBody textMessageBody = (MQ.textMessageBody) conversation.getMessage().getBody();
            Log.d(TAG, "收到消息内容：" + textMessageBody.getMessage() + " From：" + conversation.getMessage().getFrom() + " To：" + conversation.getMessage().getTo());
        } else if (i == NotificationCenter.processGetUnReadCount) {
            VYConversation conversation = ChatManager.getInstance().getConversationByIndex(0);
            int unReadCount = conversation.getUnReadCount();
            Log.d(TAG, "unread count:" + unReadCount);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.getInstance().removeObserver(this,NotificationCenter.recordProgressChanged);
        NotificationCenter.getInstance().removeObserver(this,NotificationCenter.recordStarted);
        NotificationCenter.getInstance().removeObserver(this,NotificationCenter.recordStartError);
        NotificationCenter.getInstance().removeObserver(this,NotificationCenter.recordStopped);

        NotificationCenter.getInstance().removeObserver(this,NotificationCenter.conversionsLoadedComplete);
        NotificationCenter.getInstance().removeObserver(this,NotificationCenter.processGetUnReadCount);
    }
}

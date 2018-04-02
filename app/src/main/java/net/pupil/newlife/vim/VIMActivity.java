package net.pupil.newlife.vim;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.royasoft.im.api.ResponseFuture;

import net.pupil.newlife.R;
import net.pupil.newlife.vim.sdk.ConnectPushInterface;
import net.pupil.newlife.vim.sdk.LinkagePushUtils;
import net.pupil.newlife.vim.sdk.MessageCallback;
import net.pupil.newlife.vim.sdk.PushConsts;

import static net.pupil.newlife.vim.sdk.PushConsts.APP_KEY;
import static net.pupil.newlife.vim.sdk.PushConsts.SDK_APP_ID;
import static net.pupil.newlife.vim.sdk.PushConsts.SDK_APP_SEC;

public class VIMActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();
    private Context mCtx = this;

    private final String mA = "15832188527";
    private final String mB = "13932143287";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vim);
    }

    public void loginVIMA(View view) {
        login(mA);
    }

    public void loginVIMB(View view) {
        login(mB);
    }

    private void login(final String userId) {
        Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "    userId : " + userId);
        LinkagePushUtils.initPushUser(this, userId, userId, new ConnectPushInterface() {
            @Override
            public void SuccessConnectPush() {
                Log.d(TAG, "SuccessConnectPush");
                LinkagePushUtils.connectPush(mCtx, SDK_APP_ID, SDK_APP_SEC, userId, new ConnectPushInterface() {
                    @Override
                    public void SuccessConnectPush() {
                        Log.d(TAG, "connectPush SuccessConnectPush");
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                com.royasoft.im.api.IMClient.getOfflineMessages().setCallback(new ResponseFuture.Callback() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Log.e(TAG, "onSuccess");
                                    }

                                    @Override
                                    public void onError(String s) {
                                        Log.e(TAG, "onError:" + s);
                                    }
                                });
                            }
                        });
                        thread.start();
                    }

                    @Override
                    public void FailerConnectPush(int ret, String msg) {
                        Log.d(TAG, "connectPush FailerConnectPush " + ret + " " + msg);
                    }
                });
            }

            @Override
            public void FailerConnectPush(int ret, String msg) {
                Log.d(TAG, "FailerConnectPush " + ret + " " + msg);
            }
        });
    }

    public void sendTextA(View view) {
        LinkagePushUtils.sendMessage(PushConsts.ChatType.TEXT, "来自B的测试", mA, null, new MessageCallback() {
            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "sendTextA onSuccess");
            }

            @Override
            public void onError(String s) {
                Log.d(TAG, "sendTextA onError");
            }
        });
    }

    public void sendTextB(View view) {
        LinkagePushUtils.sendMessage(PushConsts.ChatType.TEXT, "来自A的测试", mB, null, new MessageCallback() {
            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "sendTextB onSuccess");
            }

            @Override
            public void onError(String s) {
                Log.d(TAG, "sendTextB onError");
            }
        });
    }

}

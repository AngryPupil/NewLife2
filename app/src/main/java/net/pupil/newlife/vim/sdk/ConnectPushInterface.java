package net.pupil.newlife.vim.sdk;

/**
 * Created by fish902 on 2017/5/31.
 */

public interface ConnectPushInterface {
    void SuccessConnectPush();
    void FailerConnectPush(int ret, String msg);
}

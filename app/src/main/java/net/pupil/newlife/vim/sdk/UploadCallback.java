package net.pupil.newlife.vim.sdk;

import com.royasoft.android.updownload.Upload;
import com.royasoft.im.util.Http.ProgressCallback;
import com.royasoft.im.api.Message;

/**
 * Created by fish902 on 2017/5/31.
 */

public interface UploadCallback extends ProgressCallback{

    public void onComplete(String s) ;

    @Override
    public void onError(int i, String s) ;

    @Override
    public void onProgress(long l, long l1) ;
//    {
//    private Message message;
//    public UploadCallback(Message message) {
//        this.message = message;
//    }
//
//    @Override
//    public void onComplete(String s) {
//      //加入对上传文件完成的处理
//    }
//
//    @Override
//    public void onError(int i, String s) {
//        //加入对上传文件错误的处理
//    }
//
//    @Override
//    public void onProgress(long l, long l1) {
//        //加入对上传文件流程中的处理
//    }
}

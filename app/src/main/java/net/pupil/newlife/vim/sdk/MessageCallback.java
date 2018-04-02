package net.pupil.newlife.vim.sdk;

import com.royasoft.im.api.IMClient;
import com.royasoft.im.api.Message;
import com.royasoft.im.api.ResponseFuture;

import static android.R.attr.duration;
import static android.R.id.message;

/**
 * Created by fish902 on 2017/5/31.
 */

public interface MessageCallback extends ResponseFuture.Callback {



    public void onSuccess(final Object o);


    public void onError(String s);


}

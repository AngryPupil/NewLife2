package net.pupil.newlife.vim.sdk;

import java.util.Objects;

/**
 * Created by fish902 on 2017/5/31.
 */

public interface HisInterface {
    void onSuccess(Object objects);
    void onError(int ret, String msg);
}

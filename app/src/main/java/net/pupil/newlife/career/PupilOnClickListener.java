package net.pupil.newlife.career;

import android.view.View;

public class PupilOnClickListener implements View.OnClickListener {

    public static final long CLICK_INTERVAL = 800;

    public ActualListener mActualListener;

    public PupilOnClickListener (ActualListener listener) {
        mActualListener = listener;
    }

    /**
     * 防止连点
     */
    @Override
    public void onClick(View v) {
        v.setClickable(false);
        v.postDelayed(() -> {
            v.setClickable(true);
        }, CLICK_INTERVAL);
        mActualListener.onClick(v);
    }

    public interface ActualListener {
        void onClick(View v);
    }
}

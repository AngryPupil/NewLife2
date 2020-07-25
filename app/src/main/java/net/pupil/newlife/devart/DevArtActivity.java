package net.pupil.newlife.devart;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import net.pupil.newlife.R;

public class DevArtActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_art);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("outState", "pupil");
        Log.d("TAG", "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("TAG", savedInstanceState.getString("outState"));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("TAG", "newConfig " + newConfig.orientation);
    }

    public void anim_translate(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.dev_art_translate);
        view.startAnimation(animation);
    }

    public void anim_translate_obj(View view) {
        ObjectAnimator.ofFloat(view, "TranslationX", 0, 100).setDuration(100)
                .start();
    }

    public void layoutParamsAnim(View view) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.leftMargin += 100;
        view.requestLayout();
//        view.setLayoutParams(layoutParams);
    }

    public void animSlide(View view) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 1).setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                DevArtView dav = ((DevArtView) findViewById(R.id.DevArtView));
                dav.scrollTo((int) (-200 * fraction), 0);
            }
        });
        animator.start();
    }

    final float FRAME_COUNT = 30f; //帧数
    final int MSG_SCROLL_TO = 1;
    private float mCount;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SCROLL_TO:
                    mCount++;
                    if (mCount < FRAME_COUNT) {
                        float fraction = mCount / FRAME_COUNT;
                        int scrollY = (int) (-200 * fraction);
                        View view = findViewById(R.id.DevArtView);
                        view.scrollTo(0, scrollY);
                        mHandler.sendEmptyMessageDelayed(MSG_SCROLL_TO, 50);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void handlerSlide(View view) {
        mHandler.sendEmptyMessageDelayed(MSG_SCROLL_TO, 50);
    }

//    public void scroller(View view) {
//        Log.d("TAG", "scroller");
//        DevArtView dav = ((DevArtView) findViewById(R.id.DevArtView));
//        int scrollX = dav.getScrollX();
//        int scrollY = dav.getScrollY();
//        dav.getScroller().startScroll(scrollX, scrollY, -100, -100, 1000);
//        dav.invalidate();
//    }
}

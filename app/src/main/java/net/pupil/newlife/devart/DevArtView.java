package net.pupil.newlife.devart;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.nineoldandroids.view.ViewHelper;

import net.pupil.newlife.R;

public class DevArtView extends LinearLayout implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private Context mContext;
    GestureDetector mDetector;
    Scroller mScroller;
    private int mLastX;
    private int mLastY;

    public Scroller getScroller() {
        return mScroller;
    }

    public DevArtView(Context context) {
        super(context);
        mContext = context;
    }

    public DevArtView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mDetector = new GestureDetector(mContext, this);
        mDetector.setOnDoubleTapListener(this);

        mScroller = new Scroller(mContext);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        findViewById(R.id.btn_scroller).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onLayout", "scroller onClick");
                int scrollX = getScrollX();
                int scrollY = getScrollY();
                mScroller.startScroll(scrollX, scrollY, -100, -100, 1000);
                invalidate();
            }
        });
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    public DevArtView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        VelocityTracker tracker = VelocityTracker.obtain();
//        tracker.addMovement(event);
//
//        tracker.computeCurrentVelocity(1000);
//        float xV = tracker.getXVelocity();
//        float yV = tracker.getYVelocity();
//
//        Log.d("TAG", xV + " - " + yV);
//
//        tracker.clear();
//        tracker.recycle();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.d("TAG", "onTouchEvent");
//                int scrollX = getScrollX();
//                int scrollY = getScrollY();
//                mScroller.startScroll(scrollX, scrollY, -100, -100, 1000);
//                invalidate();
//                break;
//        }

        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                Log.d("TAG", deltaX + " " + deltaY + "===" + getTranslationX() + " " + getTranslationY());
                ObjectAnimator.ofFloat(this, "TranslationX", getTranslationX() + deltaX).setDuration(0).start();
                ObjectAnimator.ofFloat(this, "TranslationY", getTranslationY() + deltaY).setDuration(0).start();
//                int tX = (int) (ViewHelper.getTranslationX(this) + deltaX);
//                int tY = (int) (ViewHelper.getTranslationY(this) + deltaY);
//                ViewHelper.setTranslationX(this, tX);
//                ViewHelper.setTranslationY(this, tY);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        mLastX = x;
        mLastY = y;

//        return mDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d("TAG", "onDoubleTap");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d("TAG", "onDoubleTapEvent");
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("TAG", "onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

}

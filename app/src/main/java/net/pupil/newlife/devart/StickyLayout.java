package net.pupil.newlife.devart;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;

import net.pupil.newlife.R;

public class StickyLayout extends LinearLayout {

    private float mLastX;
    private float mLastY;
    private float mLastInterceptX;
    private float mLastInterceptY;
    private Scroller mScroller;
    private ListView mListView;

    public StickyLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = (int) (y - mLastY);
                Log.d("onTouchEvent", "scrollY:" + getScrollY() + " ,-deltaY:" + -deltaY);
                mScroller.startScroll(0, getScrollY(), 0, -deltaY * 3, 100);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mListView = (ListView) findViewById(R.id.list_view);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("onInterceptTouchEvent", "position:" + mListView.getFirstVisiblePosition());
                if (getScrollY() < 80) {
                    intercepted = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }

        mLastY = y;
        mLastInterceptY = y;
        return intercepted;
    }
}

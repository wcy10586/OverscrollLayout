package com.wcy.overscrolllayout;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by wuchangyou on 2016/8/18.
 */
public class MyListView extends ListView implements AbsListView.OnScrollListener {

    private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;
    private static final float SCROLL_RATIO = 0.5f;// 阻尼系数
    private int mMaxYOverscrollDistance;

    ViewConfiguration configuration;
    Scroller mScroller;
    VelocityTracker velocityTracker;


    final int fling = 10;
    final int stop = 11;

    private float curVe;
    private float myVel;
    private long startTime;


    private float mDeceleration;
    private float mPpi;
    GestureDetector d;


    public MyListView(Context context) {
        super(context);
        initBounceListView();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBounceListView();
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBounceListView();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
//        Log.e("name", "===computeScroll====" + mScroller.getCurrVelocity());
    }

    private void initBounceListView() {
        setOnScrollListener(this);
        configuration = ViewConfiguration.get(getContext());
        mScroller = new Scroller(getContext(), null);

        mPpi = getContext().getResources().getDisplayMetrics().density * 160.0f;
        mDeceleration = computeDeceleration(ViewConfiguration.getScrollFriction());
        GestureDetector d = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener());
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        Log.e("name",l+"===========================" + t +"===" + oldl +"===" + oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                velocityTracker.clear();
                velocityTracker.addMovement(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(ev);
                break;

            case MotionEvent.ACTION_UP:
                velocityTracker.addMovement(ev);
                velocityTracker.computeCurrentVelocity(1000, configuration.getScaledMaximumFlingVelocity());

                float a = velocityTracker.getYVelocity();
                int initialY = 0;
                if (a > 0) {

                }

                myVel = a;
                startTime = System.currentTimeMillis();
//                Log.e("name", "=onTouchEvent======" + a);
                break;
        }
        return super.onTouchEvent(ev);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            float a = velocityTracker.getYVelocity();
            Log.e("name", "===onScrollStateChanged====" + getCurVe());
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    private float getCurVe() {
        float d = mDeceleration * timePassed() / 2000.0f;
        curVe = Math.abs(myVel) - d;
        return curVe;
    }

    public int timePassed() {
        return (int) (System.currentTimeMillis() - startTime);
    }


    private float computeDeceleration(float friction) {
        return SensorManager.GRAVITY_EARTH   // g (m/s^2)
                * 39.37f               // inch/meter
                * mPpi                 // pixels per inch
                * friction;
    }
}

package com.wcy.overscroll;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.OvershootInterpolator;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by changyou on 2016/4/7.
 */
public class OverScrollLayout extends RelativeLayout {
    private static final String TAG = "OverScrollLayout";
    private ViewConfiguration configuration;
    private View child;
    private boolean prepareOverScrollVertical;
    private float downY;
    private float oldY;
    private int dealtY;
    private Scroller mScroller;

    private boolean prepareOverScrollHorizontally;
    private float downX;
    private float oldX;
    private int dealtX;

    private boolean canOverScrollHorizontally;
    private boolean canOverScrollVertical;

    private boolean topOverScrollEnable = true;
    private boolean bottomOverScrollEnable = true;
    private boolean leftOverScrollEnable = true;
    private boolean rightOverScrollEnable = true;
    private OnOverScrollListener onOverScrollListener;
    private OverScrollCheckListener checkListener;

    public static int SCROLL_VERTICAL = LinearLayout.VERTICAL;
    public static int SCROLL_HORIZONTAL = LinearLayout.HORIZONTAL;

    public OverScrollLayout(Context context) {
        super(context);
        init();
    }

    public OverScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("NewApi")
    public OverScrollLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        configuration = ViewConfiguration.get(getContext());
        mScroller = new Scroller(getContext(), new OvershootInterpolator(0.75f));
    }

    @Override
    protected void onFinishInflate() {
        int childCount = getChildCount();
        if (childCount > 1) {
            throw new IllegalStateException("OverScrollLayout only can host 1 elements");
        } else if (childCount == 1) {
            child = getChildAt(0);
            child.setOverScrollMode(OVER_SCROLL_NEVER);
        }
        super.onFinishInflate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int scrollerY = mScroller.getCurrY();
            scrollTo(mScroller.getCurrX(), scrollerY);
            postInvalidate();
        } else {

        }
    }

    protected void mSmoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        mSmoothScrollBy(dx, dy);
    }


    protected void mSmoothScrollBy(int dx, int dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        invalidate();
    }

    public boolean isTopOverScrollEnable() {
        return topOverScrollEnable;
    }

    /**
     * @param topOverScrollEnable
     */
    public void setTopOverScrollEnable(boolean topOverScrollEnable) {
        this.topOverScrollEnable = topOverScrollEnable;
    }

    public boolean isBottomOverScrollEnable() {
        return bottomOverScrollEnable;
    }

    /**
     * if set OverScrollCheckListener this will not check;
     */
    public void setBottomOverScrollEnable(boolean bottomOverScrollEnable) {
        this.bottomOverScrollEnable = bottomOverScrollEnable;
    }

    public boolean isLeftOverScrollEnable() {
        return leftOverScrollEnable;
    }

    /**
     * if set OverScrollCheckListener this will not check;
     */
    public void setLeftOverScrollEnable(boolean leftOverScrollEnable) {
        this.leftOverScrollEnable = leftOverScrollEnable;
    }

    public boolean isRightOverScrollEnable() {
        return rightOverScrollEnable;
    }

    /**
     * if set OverScrollCheckListener this will not check;
     */
    public void setRightOverScrollEnable(boolean rightOverScrollEnable) {
        this.rightOverScrollEnable = rightOverScrollEnable;
    }

    public OnOverScrollListener getOnOverScrollListener() {
        return onOverScrollListener;
    }

    /**
     * if set OverScrollCheckListener this will not check;
     */
    public void setOnOverScrollListener(OnOverScrollListener onOverScrollListener) {
        this.onOverScrollListener = onOverScrollListener;
    }

    public OverScrollCheckListener getOverScrollCheckListener() {
        return checkListener;
    }

    public void setOverScrollCheckListener(OverScrollCheckListener checkListener) {
        this.checkListener = checkListener;
    }

    private void checkCanOverScrollDirection() {
        if (checkListener != null) {
            int mOrientation = checkListener.getContentViewScrollDirection();
            canOverScrollHorizontally = RecyclerView.HORIZONTAL == mOrientation;
            canOverScrollVertical = RecyclerView.VERTICAL == mOrientation;
        } else if (child instanceof AbsListView || child instanceof ScrollView || child instanceof WebView) {
            canOverScrollHorizontally = false;
            canOverScrollVertical = true;
        } else if (child instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) child;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int mOrientation = -1;
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                mOrientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            } else if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                mOrientation = manager.getOrientation();
            }
            canOverScrollHorizontally = RecyclerView.HORIZONTAL == mOrientation;
            canOverScrollVertical = RecyclerView.VERTICAL == mOrientation;
        } else if (child instanceof HorizontalScrollView) {
            canOverScrollHorizontally = true;
            canOverScrollVertical = false;
        } else if (child instanceof ViewPager) {
            //forbid ViewPager  over scroll
            canOverScrollHorizontally = false;
            canOverScrollVertical = false;
        } else {
            canOverScrollHorizontally = false;
            canOverScrollVertical = true;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
                oldY = 0;
                oldX = 0;
                break;
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                oldY = 0;
                prepareOverScrollVertical = false;
                dealtY = 0;

                downX = ev.getX();
                oldX = 0;
                prepareOverScrollHorizontally = false;
                dealtX = 0;
                checkCanOverScrollDirection();
                break;

            case MotionEvent.ACTION_MOVE:
                if (prepareOverScrollVertical) {
                    if (Math.abs(dealtY) >= configuration.getScaledTouchSlop()) {
                        if (dealtY < 0 && !canChildScrollUp() || dealtY > 0 && !canChildScrollDown()) {
                            if (onOverScrollListener != null) {
                                if (dealtY < 0) {
                                    onOverScrollListener.onTopOverScroll();
                                } else if (dealtY > 0) {
                                    onOverScrollListener.onBottomOverScroll();
                                }
                            }
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                            super.dispatchTouchEvent(ev);
                            if (oldY == 0) {
                                oldY = ev.getY();
                                return true;
                            }
                            dealtY += oldY - ev.getY();
                            oldY = ev.getY();
                            overScroll(dealtX, dealtY);
                            return true;
                        } else {
                            dealtY = 0;
                            oldY = 0;
                            prepareOverScrollVertical = false;
                            return super.dispatchTouchEvent(resetVertical(ev));
                        }
                    } else {
                        if (oldY == 0) {
                            oldY = ev.getY();
                            return true;
                        }
                        dealtY += oldY - ev.getY();
                        oldY = ev.getY();
                    }
                } else {
                    if (canOverScroll()) {
                        boolean checkPrepareMove = checkPrepareMoveVertical(ev.getX(), ev.getY());
                        if (prepareOverScrollVertical && !checkPrepareMove) {
                            ev = resetVertical(ev);
                        }
                        prepareOverScrollVertical = checkPrepareMove;
                    }
                }
                if (prepareOverScrollHorizontally) {
                    if (Math.abs(dealtX) >= configuration.getScaledTouchSlop()) {
                        if (dealtX < 0 && !canChildScrollLeft() || dealtX > 0 && !canChildScrollRight()) {
                            if (onOverScrollListener != null) {
                                if (dealtX < 0) {
                                    onOverScrollListener.onLeftOverScroll();
                                } else if (dealtX > 0) {
                                    onOverScrollListener.onRightOverScroll();
                                }
                            }
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                            super.dispatchTouchEvent(ev);
                            if (oldX == 0) {
                                oldX = ev.getX();
                                return true;
                            }
                            dealtX += oldX - ev.getX();
                            oldX = ev.getX();
                            overScroll(dealtX, dealtY);
                            return true;
                        } else {
                            dealtX = 0;
                            oldX = 0;
                            prepareOverScrollVertical = false;
                            return super.dispatchTouchEvent(resetHorizontally(ev));
                        }
                    } else {
                        if (oldX == 0) {
                            oldX = ev.getX();
                            return true;
                        }
                        dealtX += oldX - ev.getX();
                        oldX = ev.getX();
                    }
                } else {
                    if (canOverScroll()) {
                        boolean checkPrepareMove = checkPrepareMoveHorizontally(ev.getX(), ev.getY());
                        if (prepareOverScrollHorizontally && !checkPrepareMove) {
                            ev = resetHorizontally(ev);
                        }
                        prepareOverScrollHorizontally = checkPrepareMove;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                oldY = 0;
                oldX = 0;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mSmoothScrollTo(0, 0);
                prepareOverScrollVertical = false;
                prepareOverScrollHorizontally = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private MotionEvent resetVertical(MotionEvent event) {
        oldY = 0;
        dealtY = 0;
        prepareOverScrollVertical = false;
        overScroll(dealtX, dealtY);
        event.setAction(MotionEvent.ACTION_DOWN);
        super.dispatchTouchEvent(event);
        event.setAction(MotionEvent.ACTION_MOVE);
        return event;
    }

    private MotionEvent resetHorizontally(MotionEvent event) {
        oldX = 0;
        dealtX = 0;
        prepareOverScrollHorizontally = false;
        overScroll(dealtX, dealtY);
        event.setAction(MotionEvent.ACTION_DOWN);
        super.dispatchTouchEvent(event);
        event.setAction(MotionEvent.ACTION_MOVE);
        return event;
    }

    private boolean canOverScroll() {
        return child != null;
    }


    private void overScroll(int dealtX, int dealtY) {
        mSmoothScrollTo(dealtX / 2, dealtY / 2);
    }


    private boolean checkPrepareMoveVertical(float currentX, float currentY) {
        if (!canOverScrollVertical) {
            return false;
        }
        float dealtX = downX - currentX;
        float dealtY = downY - currentY;
        if (checkListener != null) {
            return checkListener.canOverScroll(dealtX, dealtY, child);
        } else {
            if (dealtY < 0 && topOverScrollEnable) {
                return !canChildScrollUp();
            } else if (dealtY > 0 && bottomOverScrollEnable) {
                return !canChildScrollDown();
            }
            return false;
        }
    }

    private boolean checkPrepareMoveHorizontally(float currentX, float currentY) {
        if (!canOverScrollHorizontally) {
            return false;
        }
        float dealtX = downX - currentX;
        float dealtY = downY - currentY;
        if (checkListener != null) {
            return checkListener.canOverScroll(dealtX, dealtY, child);
        } else {
            if (dealtX < 0 && leftOverScrollEnable) {
                return !canChildScrollLeft();
            } else if (dealtX > 0 && rightOverScrollEnable) {
                return !canChildScrollRight();
            }
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 是否能下拉
     *
     * @return
     */
    private boolean canChildScrollUp() {
        if (checkListener != null) {
            return checkListener.canScrollUp();
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (child instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) child;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            }
        }
        return ViewCompat.canScrollVertically(child, -1);

    }


    /**
     * 是否能上拉
     *
     * @return
     */
    private boolean canChildScrollDown() {
        if (checkListener != null) {
            return checkListener.canScrollDown();
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (child instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) child;
                return absListView.getChildCount() > 0
                        && (absListView.getLastVisiblePosition() < absListView.getChildCount() - 1
                        || absListView.getChildAt(absListView.getChildCount() - 1).getBottom() > absListView.getHeight() - absListView.getPaddingBottom());
            }
        }
        return ViewCompat.canScrollVertically(child, 1);

    }

    private boolean canChildScrollLeft() {
        if (checkListener != null) {
            return checkListener.canScrollLeft();
        }
        return ViewCompat.canScrollHorizontally(child, -1);
    }

    private boolean canChildScrollRight() {
        if (checkListener != null) {
            return checkListener.canScrollRight();
        }
        return ViewCompat.canScrollHorizontally(child, 1);
    }


}

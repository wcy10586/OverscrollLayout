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
    private float downY;
    private float oldY;
    private int dealtY;
    private Scroller mScroller;

    private float downX;
    private float oldX;
    private int dealtX;
    private boolean isVerticalMove;
    private boolean isHorizontallyMove;

    private boolean isOverScrollTop;
    private boolean isOverScrollBottom;
    private boolean isOverScrollLeft;
    private boolean isOverScrollRight;

    private boolean checkScrollDirectionFinish;
    private boolean canOverScrollHorizontally;
    private boolean canOverScrollVertical;
    private float baseOverScrollLength;

    private boolean topOverScrollEnable = true;
    private boolean bottomOverScrollEnable = true;
    private boolean leftOverScrollEnable = true;
    private boolean rightOverScrollEnable = true;
    private OnOverScrollListener onOverScrollListener;
    private OverScrollCheckListener checkListener;

    public static int SCROLL_VERTICAL = LinearLayout.VERTICAL;
    public static int SCROLL_HORIZONTAL = LinearLayout.HORIZONTAL;

    private float fraction = 0.5f;
    private boolean finishOverScroll;
    private boolean abortScroller;
    private boolean shouldSetScrollerStart;

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
            throw new IllegalStateException("OverScrollLayout only can host 1 element");
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
            if (abortScroller) {
                abortScroller = false;
                return;
            }
            if (finishOverScroll) {
                isOverScrollTop = false;
                isOverScrollBottom = false;
                isOverScrollLeft = false;
                isOverScrollRight = false;
                finishOverScroll = false;
            }
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
     * @param topOverScrollEnable true can over scroll top false otherwise
     */
    public void setTopOverScrollEnable(boolean topOverScrollEnable) {
        this.topOverScrollEnable = topOverScrollEnable;
    }

    public boolean isBottomOverScrollEnable() {
        return bottomOverScrollEnable;
    }

    /**
     * @param bottomOverScrollEnable true can over scroll bottom false otherwise
     */
    public void setBottomOverScrollEnable(boolean bottomOverScrollEnable) {
        this.bottomOverScrollEnable = bottomOverScrollEnable;
    }

    public boolean isLeftOverScrollEnable() {
        return leftOverScrollEnable;
    }

    /**
     * @param leftOverScrollEnable true can over scroll left false otherwise
     */
    public void setLeftOverScrollEnable(boolean leftOverScrollEnable) {
        this.leftOverScrollEnable = leftOverScrollEnable;
    }

    public boolean isRightOverScrollEnable() {
        return rightOverScrollEnable;
    }

    /**
     * @param rightOverScrollEnable true can over scroll right false otherwise
     */
    public void setRightOverScrollEnable(boolean rightOverScrollEnable) {
        this.rightOverScrollEnable = rightOverScrollEnable;
    }

    public OnOverScrollListener getOnOverScrollListener() {
        return onOverScrollListener;
    }

    /**
     * @param onOverScrollListener
     */
    public void setOnOverScrollListener(OnOverScrollListener onOverScrollListener) {
        this.onOverScrollListener = onOverScrollListener;
    }

    public OverScrollCheckListener getOverScrollCheckListener() {
        return checkListener;
    }

    /**
     * @param checkListener for custom view check over scroll
     */
    public void setOverScrollCheckListener(OverScrollCheckListener checkListener) {
        this.checkListener = checkListener;
    }

    public float getFraction() {
        return fraction;
    }

    /**
     * @param fraction the fraction for over scroll.it is num[0f,1f],
     */
    public void setFraction(float fraction) {
        if (fraction < 0 || fraction > 1) {
            return;
        }
        this.fraction = fraction;
    }

    private void checkCanOverScrollDirection() {
        if (checkScrollDirectionFinish) {
            return;
        }
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
        checkScrollDirectionFinish = true;
        if (canOverScrollVertical) {
            baseOverScrollLength = getHeight();
        } else {
            baseOverScrollLength = getWidth();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
                oldY = 0;
                oldX = 0;
                break;
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                oldY = 0;
                dealtY = mScroller.getCurrY();
                if (dealtY == 0) {
                    isVerticalMove = false;
                } else {
                    shouldSetScrollerStart = true;
                    abortScroller = true;
                    mScroller.abortAnimation();
                }

                downX = ev.getX();
                oldX = 0;
                dealtX = mScroller.getCurrX();
                if (dealtX == 0) {
                    isHorizontallyMove = false;
                } else {
                    shouldSetScrollerStart = true;
                    abortScroller = true;
                    mScroller.abortAnimation();
                }
                if (isOverScrollTop || isOverScrollBottom || isOverScrollLeft || isOverScrollRight) {
                    return true;
                }
                checkCanOverScrollDirection();
                break;

            case MotionEvent.ACTION_MOVE:
                if (!canOverScroll()) {
                    return super.dispatchTouchEvent(ev);
                }

                if (canOverScrollVertical) {
                    if (isOverScrollTop || isOverScrollBottom) {
                        if (onOverScrollListener != null) {
                            if (isOverScrollTop) {
                                onOverScrollListener.onTopOverScroll();
                            }
                            if (isOverScrollBottom) {
                                onOverScrollListener.onBottomOverScroll();
                            }
                        }
                        if (shouldSetScrollerStart) {
                            shouldSetScrollerStart = false;
                            mScroller.startScroll(dealtX, dealtY, 0, 0);
                        }
                        if (oldY == 0) {
                            oldY = ev.getY();
                            return true;
                        }
                        dealtY += getDealt(oldY - ev.getY(), dealtY);
                        oldY = ev.getY();
                        if (isOverScrollTop && dealtY > 0) {
                            dealtY = 0;
                        }
                        if (isOverScrollBottom && dealtY < 0) {
                            dealtY = 0;
                        }
                        overScroll(dealtX, dealtY);
                        if ((isOverScrollTop && dealtY == 0 && !isOverScrollBottom) ||
                                (isOverScrollBottom && dealtY == 0 && !isOverScrollTop)) {
                            oldY = 0;
                            isOverScrollTop = false;
                            isOverScrollBottom = false;
                            if (!isChildCanScrollVertical()) {
                                return true;
                            }
                            return super.dispatchTouchEvent(resetVertical(ev));
                        }
                        return true;
                    } else {
                        checkMoveDirection(ev.getX(), ev.getY());
                        if (oldY == 0) {
                            oldY = ev.getY();
                            return true;
                        }
                        boolean tempOverScrollTop = isTopOverScroll(ev.getY());
                        if (!isOverScrollTop && tempOverScrollTop) {
                            oldY = ev.getY();
                            isOverScrollTop = tempOverScrollTop;
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                            super.dispatchTouchEvent(ev);
                            return true;
                        }
                        isOverScrollTop = tempOverScrollTop;
                        boolean tempOverScrollBottom = isBottomOverScroll(ev.getY());
                        if (!isOverScrollBottom && tempOverScrollBottom) {
                            oldY = ev.getY();
                            isOverScrollBottom = tempOverScrollBottom;
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                            super.dispatchTouchEvent(ev);
                            return true;
                        }
                        isOverScrollBottom = tempOverScrollBottom;
                        oldY = ev.getY();
                    }
                } else if (canOverScrollHorizontally) {
                    if (isOverScrollLeft || isOverScrollRight) {
                        if (onOverScrollListener != null) {
                            if (isOverScrollLeft) {
                                onOverScrollListener.onLeftOverScroll();
                            }
                            if (isOverScrollRight) {
                                onOverScrollListener.onRightOverScroll();
                            }
                        }
                        if (shouldSetScrollerStart) {
                            shouldSetScrollerStart = false;
                            mScroller.startScroll(dealtX, dealtY, 0, 0);
                        }
                        if (oldX == 0) {
                            oldX = ev.getX();
                            return true;
                        }
                        dealtX += getDealt(oldX - ev.getX(), dealtX);
                        oldX = ev.getX();
                        if (isOverScrollLeft && dealtX > 0) {
                            dealtX = 0;
                        }
                        if (isOverScrollRight && dealtX < 0) {
                            dealtX = 0;
                        }
                        overScroll(dealtX, dealtY);
                        if ((isOverScrollLeft && dealtX == 0 && !isOverScrollRight) ||
                                (isOverScrollRight && dealtX == 0 && !isOverScrollLeft)) {
                            oldX = 0;
                            isOverScrollRight = false;
                            isOverScrollLeft = false;
                            if (!isChildCanScrollHorizontally()) {
                                return true;
                            }
                            return super.dispatchTouchEvent(resetHorizontally(ev));
                        }
                        return true;
                    } else {
                        checkMoveDirection(ev.getX(), ev.getY());
                        if (oldX == 0) {
                            oldX = ev.getX();
                            return true;
                        }
                        boolean tempOverScrollLeft = isLeftOverScroll(ev.getX());
                        if (!isOverScrollLeft && tempOverScrollLeft) {
                            oldX = ev.getX();
                            isOverScrollLeft = tempOverScrollLeft;
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                            super.dispatchTouchEvent(ev);
                            return true;
                        }
                        isOverScrollLeft = tempOverScrollLeft;
                        boolean tempOverScrollRight = isRightOverScroll(ev.getX());
                        if (!isOverScrollRight && tempOverScrollRight) {
                            oldX = ev.getX();
                            isOverScrollRight = tempOverScrollRight;
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                            super.dispatchTouchEvent(ev);
                            return true;
                        }
                        isOverScrollRight = tempOverScrollRight;
                        oldX = ev.getX();
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                oldY = 0;
                oldX = 0;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                finishOverScroll = true;
                mSmoothScrollTo(0, 0);
                break;
        }
        return super.dispatchTouchEvent(ev);

    }

    private float getDealt(float dealt, float distance) {
        float temp = dealt * (1.5f - fraction - Math.abs(distance) / baseOverScrollLength) / 3;
        return temp;
    }

    private MotionEvent resetVertical(MotionEvent event) {
        oldY = 0;
        dealtY = 0;
        event.setAction(MotionEvent.ACTION_DOWN);
        super.dispatchTouchEvent(event);
        event.setAction(MotionEvent.ACTION_MOVE);
        return event;
    }

    private MotionEvent resetHorizontally(MotionEvent event) {
        oldX = 0;
        dealtX = 0;
        event.setAction(MotionEvent.ACTION_DOWN);
        super.dispatchTouchEvent(event);
        event.setAction(MotionEvent.ACTION_MOVE);
        return event;
    }

    private boolean canOverScroll() {
        return child != null;
    }


    private void overScroll(int dealtX, int dealtY) {
        mSmoothScrollTo(dealtX, dealtY);
    }

    private boolean isTopOverScroll(float currentY) {
        if (isOverScrollTop) {
            return true;
        }
        if (!topOverScrollEnable || !isVerticalMove) {
            return false;
        }
        float dealtY = oldY - currentY;
        return dealtY < 0 && !canChildScrollUp();
    }

    private boolean isBottomOverScroll(float currentY) {
        if (isOverScrollBottom) {
            return true;
        }
        if (!bottomOverScrollEnable || !isVerticalMove) {
            return false;
        }
        float dealtY = oldY - currentY;
        return dealtY > 0 && !canChildScrollDown();
    }

    private boolean isLeftOverScroll(float currentX) {
        if (isOverScrollLeft) {
            return true;
        }
        if (!leftOverScrollEnable || !isHorizontallyMove) {
            return false;
        }
        float dealtX = oldX - currentX;
        return dealtX < 0 && !canChildScrollLeft();
    }

    private boolean isRightOverScroll(float currentX) {
        if (!rightOverScrollEnable || !isHorizontallyMove) {
            return false;
        }
        float dealtX = oldX - currentX;
        return dealtX > 0 && !canChildScrollRight();
    }

    private boolean isChildCanScrollVertical() {
        return canChildScrollDown() || canChildScrollUp();
    }

    private boolean isChildCanScrollHorizontally() {
        return canChildScrollLeft() || canChildScrollRight();
    }

    private void checkMoveDirection(float currentX, float currentY) {
        if (isVerticalMove || isHorizontallyMove) {
            return;
        }
        if (canOverScrollVertical) {
            float dealtY = currentY - downY;
            isVerticalMove = Math.abs(dealtY) >= configuration.getScaledTouchSlop();
        } else if (canOverScrollHorizontally) {
            float dealtX = currentX - downX;
            isHorizontallyMove = Math.abs(dealtX) >= configuration.getScaledTouchSlop();
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

    /**
     * 是否能左拉
     *
     * @return
     */
    private boolean canChildScrollLeft() {
        if (checkListener != null) {
            return checkListener.canScrollLeft();
        }
        return ViewCompat.canScrollHorizontally(child, -1);
    }

    /**
     * 是否能右拉
     *
     * @return
     */
    private boolean canChildScrollRight() {
        if (checkListener != null) {
            return checkListener.canScrollRight();
        }
        return ViewCompat.canScrollHorizontally(child, 1);
    }
}

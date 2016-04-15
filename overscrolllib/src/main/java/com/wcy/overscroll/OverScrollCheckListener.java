package com.wcy.overscroll;

import android.view.View;

/**
 * Created by changyou on 2016/4/14.
 */
public interface OverScrollCheckListener {
    /**
     * return int ,if the direction is {@link OverScrollLayout} OverScrollLayout.SCROLL_VERTICAL means the contentView can scroll vertical.
     * if  the direction is {@link OverScrollLayout} OverScrollLayout.SCROLL_HORIZONTAL means the contentView can scroll horizontal.
     * if other value overScrollLayout will never can over scroll.
     */
    int getContentViewScrollDirection();

    /**
     * @param dealtX      the down point to a current point horizontal change,if dealtX is positive drag to left else negative right.
     * @param dealtY      the down point to a current point vertical change,if dealtY is positive drag to top else negative bottom.
     * @param contentView the direct child of OverScrollLayout
     * @return boolean, if true can over scroll else can not;
     */
    boolean canOverScroll(float dealtX, float dealtY, View contentView);

    boolean canScrollUp();

    boolean canScrollDown();

    boolean canScrollLeft();

    boolean canScrollRight();
}

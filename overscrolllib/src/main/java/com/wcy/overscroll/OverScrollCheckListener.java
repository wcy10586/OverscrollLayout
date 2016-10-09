package com.wcy.overscroll;

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

    boolean canScrollUp();

    boolean canScrollDown();

    boolean canScrollLeft();

    boolean canScrollRight();
}

package com.wcy.overscrolllayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.HorizontalScrollView;

/**
 * Created by wuchangyou on 2016/8/18.
 */
public class MyhScrollView  extends HorizontalScrollView{
    public MyhScrollView(Context context) {
        super(context);
    }

    public MyhScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyhScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e("name",l+"===========================" + t +"===" + oldl +"===" + oldt +"===="+ getChildAt(0).getLeft() +"====" + getChildAt(0).getRight());
    }
}

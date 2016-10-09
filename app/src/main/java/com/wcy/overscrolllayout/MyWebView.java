package com.wcy.overscrolllayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by wuchangyou on 2016/8/18.
 */
public class MyWebView  extends WebView{

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        View parent = (View) getParent();
        Log.e("name",l+"===========================" + t +"===" + oldl +"===" + oldt +"===-------" + getContentHeight()  +"---" +parent.getRight());
    }
}

package com.wcy.overscrolllayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.origamilabs.library.views.StaggeredGridView;
import com.wcy.overscroll.OverScrollCheckListener;
import com.wcy.overscroll.OverScrollLayout;

/**
 * Created by changyou on 2016/4/14.
 */
public class CustomViewActivity extends AppCompatActivity {
    private OverScrollLayout overScrollLayout;
    private StaggeredGridView staggeredGridView;
    private String str = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_view);
        overScrollLayout = (OverScrollLayout) findViewById(R.id.overscroll);
        staggeredGridView = (StaggeredGridView) findViewById(R.id.stagger);
        setOverScroll();
        setStaggeredGridView();
    }

    private void setOverScroll() {
        overScrollLayout.setOverScrollCheckListener(new OverScrollCheckListener() {
            @Override
            public int getContentViewScrollDirection() {
                return OverScrollLayout.SCROLL_VERTICAL;
            }

            @Override
            public boolean canOverScroll(float dealtX, float dealtY, View contentView) {
                if (dealtY < 0 && !canScrollUp()) {
                    return true;
                }

                if (dealtY > 0 && !canScrollDown()) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean canScrollUp() {
                int fp = staggeredGridView.getFirstPosition();
                if (fp == 0) {
                    View child = staggeredGridView.getChildAt(0);
                    if (child.getTop() >= staggeredGridView.getPaddingTop()) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean canScrollDown() {
                int fp = staggeredGridView.getFirstPosition();
                int lp = staggeredGridView.getChildCount() + fp - 1;
                if (lp == staggeredGridView.getAdapter().getCount() - 1) {
                    int numClos = staggeredGridView.getColumnCount();
                    int childCount = staggeredGridView.getChildCount();
                    for (int i = 1; i <= numClos; i++) {
                        View view = staggeredGridView.getChildAt(childCount - i);
                        if (view.getBottom() <= staggeredGridView.getHeight() + staggeredGridView.getPaddingBottom()) {
                            return false;
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean canScrollLeft() {
                return false;
            }

            @Override
            public boolean canScrollRight() {
                return false;
            }
        });
    }


    private void setStaggeredGridView() {
        staggeredGridView.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 30;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView != null) {
                tv = (TextView) convertView;
            } else {
                tv = new TextView(CustomViewActivity.this);
                tv.setWidth(1080);
                tv.setMinHeight(180);
                tv.setTextSize(20);
                tv.setTextColor(Color.BLACK);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                convertView = tv;
            }
            if (position % 3 == 0) {
                tv.setText(str);
            } else {
                tv.setText("position " + position);
            }
            return convertView;
        }
    }

}

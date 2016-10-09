package com.wcy.overscrolllayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wcy.overscroll.OnOverScrollListener;
import com.wcy.overscroll.OverScrollLayout;

/**
 * Created by changyou on 2016/4/14.
 */
public class ListViewActivity extends AppCompatActivity {
    private String TAG = "overscroll";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        setListView();
        setOverScroll();
    }

    private void setOverScroll(){
        OverScrollLayout layout = (OverScrollLayout) findViewById(R.id.overscroll);
        layout.setFraction(0.75f);
        layout.setOnOverScrollListener(new OnOverScrollListener() {
            @Override
            public void onTopOverScroll() {
                Log.i(TAG, "====onTopOverScroll=======");
            }

            @Override
            public void onBottomOverScroll() {
                Log.i(TAG,"====onBottomOverScroll=======");
            }

            @Override
            public void onLeftOverScroll() {
                Log.i(TAG,"====onLeftOverScroll=======");
            }

            @Override
            public void onRightOverScroll() {
                Log.i(TAG,"====onRightOverScroll=======");
            }
        });
    }

    private void setListView() {
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 20;
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
                tv = new TextView(ListViewActivity.this);
                tv.setWidth(1080);
                tv.setHeight(180);
                tv.setTextSize(20);
                tv.setTextColor(Color.BLACK);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                convertView = tv;
            }
            tv.setText("position " + position);
            return convertView;
        }
    }
}

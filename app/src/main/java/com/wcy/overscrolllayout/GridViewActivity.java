package com.wcy.overscrolllayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by changyou on 2016/4/14.
 */
public class GridViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view);
        setGridVew();
    }

    private void setGridVew() {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setNumColumns(3);
        gridView.setAdapter(new MyAdapter());
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
                tv = new TextView(GridViewActivity.this);
                tv.setWidth(240);
                tv.setHeight(240);
                tv.setTextSize(20);
                tv.setTextColor(Color.BLACK);
                tv.setGravity(Gravity.CENTER);
                convertView = tv;
            }
            tv.setText("position " + position);
            return convertView;
        }
    }
}

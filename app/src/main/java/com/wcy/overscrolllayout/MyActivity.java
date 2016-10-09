package com.wcy.overscrolllayout;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by wuchangyou on 2016/8/18.
 */
public class MyActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new MyAdapter());
//        WebView webView = (WebView)findViewById(R.id.webView);
//        webView.loadUrl("http://www.2cto.com/kf/201508/435091.html");
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 100;
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
                tv = new TextView(MyActivity.this);
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

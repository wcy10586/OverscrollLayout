package com.wcy.overscrolllayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.listView).setOnClickListener(this);
        findViewById(R.id.gridView).setOnClickListener(this);
        findViewById(R.id.scrollView).setOnClickListener(this);
        findViewById(R.id.horizontalScrollView).setOnClickListener(this);
        findViewById(R.id.webView).setOnClickListener(this);
        findViewById(R.id.recyclerView).setOnClickListener(this);
        findViewById(R.id.viewPager).setOnClickListener(this);
        findViewById(R.id.customView).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        int id = v.getId();
        switch (id) {
            case R.id.listView:
                intent.setClass(this, ListViewActivity.class);
                break;
            case R.id.gridView:
                intent.setClass(this, GridViewActivity.class);
                break;
            case R.id.scrollView:
                intent.setClass(this, ScrollViewActivity.class);
                break;
            case R.id.horizontalScrollView:
                intent.setClass(this, HorizontalScrollViewActivity.class);
                break;
            case R.id.webView:
                intent.setClass(this, WebViewActivity.class);
                break;
            case R.id.recyclerView:
                intent.setClass(this, RecyclerViewActivity.class);
                break;
            case R.id.viewPager:
                intent.setClass(this, PagerViewActivity.class);
                break;
            case R.id.customView:
                intent.setClass(this, CustomViewActivity.class);
                break;

        }
        startActivity(intent);
    }

    private void setRecyclerView() {
//        recyclerView = (RecyclerView) findViewById(R.id.recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
//        recyclerView.setAdapter(new MyAdapter());
    }

    class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(MainActivity.this);
            textView.setWidth(1080);
            textView.setHeight(180);
            textView.setBackgroundColor(Color.CYAN);
            return new MyHolder(textView);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.textView.setText("  ====   " + position);
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}

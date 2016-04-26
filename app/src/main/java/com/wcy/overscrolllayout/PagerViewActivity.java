package com.wcy.overscrolllayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcy.overscroll.OverScrollLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by changyou on 2016/4/14.
 */
public class PagerViewActivity extends AppCompatActivity {
    private List<View> views = new ArrayList<>();
    String str = "在的低剂量辐射导致罹患癌症、智力不足、神经系统疾病和遗传突变的人口逐年增加。\n" +
            "——白俄罗斯百科全书\n" +
            "一九八六年四月二十九日，波兰、德国、奥地利和罗马尼亚都检测到高剂量辐射。\n" +
            "——《切尔诺贝利灾变的影响》，明斯克，萨哈罗夫国际辐射生态学学院\n" +
            "目前用石棺封住的四号反应炉炉心，仍有大约二十吨核燃料，没有人知道里面的情况究竟如何...\n" +
            "我们是空气，我们不是土地……\n" +
            "——马马达舒维利\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_view);
        setPager();
    }

    private void setPager() {
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        for (int i = 0; i < 2; i++) {
            if (i == 2) {
                View view = getLayoutInflater().inflate(R.layout.page_item2, null);
                TextView textView = (TextView) view.findViewById(R.id.text);
                textView.setText(str);
                views.add(view);
                OverScrollLayout layout = (OverScrollLayout) view.findViewById(R.id.overscroll);
                layout.setLeftOverScrollEnable(false);
            } else {
                View view = getLayoutInflater().inflate(R.layout.page_item1, null);
                TextView textView = (TextView) view.findViewById(R.id.text);
                textView.setText("position " + i);
                view.setBackgroundColor(Color.RED + i * 50);
                views.add(view);
            }
        }

        pager.setAdapter(new MyPagerAdapter());

    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = Integer.MAX_VALUE % views.size();
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            position = Integer.MAX_VALUE % views.size();
            View view = views.get(position);
            container.removeView(view);
        }
    }
}

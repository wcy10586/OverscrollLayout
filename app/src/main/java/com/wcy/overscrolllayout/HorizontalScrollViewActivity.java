package com.wcy.overscrolllayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by changyou on 2016/4/14.
 */
public class HorizontalScrollViewActivity extends AppCompatActivity {
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
        setContentView(R.layout.horizontal_scroll_view);
        TextView tv = (TextView) findViewById(R.id.text);
        tv.setText(str);
    }
}

# OverscrollLayout
一个可以对任意view实现超屏幕滑动的layout，继承自RelatvieLayout,只能包含一个子view.
默认对AbsListView,RecyclerView,ScrollView,HorizontalScrollView,ViewPager 
以及一般的View做了处理。ViewPager本身禁止超屏幕滑动。一般的View支持纵向滑动。AbsListView,RecyclerView,ScrollView,
HorizontalScrollView支持在他们的滚动方向上做超屏幕滑动。有自定义的View想要支持这个功能，可以对OverScrollLayout设置OverScrollCheckListener.也可以通过设置OnOverScrollListener来监听是否是正在做超屏幕滑动。
![image](https://github.com/wcy10586/OverscrollLayout/blob/master/app/aaa.gif)

一般的view 可以这么用

        <com.wcy.overscroll.OverScrollLayout xmlns:android="http://schemas.android.com/apk/res/android"
            android:id="@+id/overscroll"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@android:color/holo_blue_bright"
            android:orientation="vertical">
            <ListView
                android:id="@+id/list"
                android:layout_width="match_parent"
                android:layout_height="match_parent"></ListView>
        </com.wcy.overscroll.OverScrollLayout>


在Activity里面如果需要监听overScroll可以这样写

            overScrollLayout.setOnOverScrollListener(new OnOverScrollListener() {
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

    
    如果不需要监听，则和使用一个RelativeLayout一样简单。
    
    
    如果是对自定义的View做OverScroll的话需要设置OvserScrollCheckListener.
     overScrollLayout.setOverScrollCheckListener(OverScrollCheckListener l)
     在OverScrollCheckListener中
     public int getContentViewScrollDirection() ；用于指定overscroll的方向，
     包括横向和纵向：OverScrollLayout.SCROLL_VERTICAL，OverScrollLayout.SCROLL_HORIZONTAL。
      public boolean canOverScroll(float dealtX, float dealtY, View contentView)；用于判断是否可以OverScroll.
      public boolean canScrollUp();用于判断是否到达顶部，用于检查垂直Overscroll;
      public boolean canScrollDown();用于判断是否到达底部，用于检查垂直OverScroll；
      public boolean canScrollLeft()；用于判断是否到达左边界,用于检查水平OverScroll；
      public boolean canScrollRight() ；用于判断是否已到达右边界,用于检查水平OverScroll。
      
      以为StaggeredGridView为例子，要实现它的OverScroll  那么OverScrollCheckListener 可以这么实现：
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
    

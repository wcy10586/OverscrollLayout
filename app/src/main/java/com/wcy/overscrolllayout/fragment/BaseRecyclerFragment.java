package com.wcy.overscrolllayout.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcy.overscrolllayout.R;

/**
 * Created by changyou on 2016/4/15.
 */
public class BaseRecyclerFragment extends Fragment {
    private String str = "aaaaaaaaa\naaaaaaaa\naaaaaaaaa\naaaaaaaaaaaaaa\naaaaaaaaaaaaa\naaaaaaaa";

    private RecyclerView recyclerView;

    private LayoutInflater inflater;

    private int itemRes;
    private int itemCount;
    private int layoutManagerType;
    public static int TYPE_LINEAR_V = 1;
    public static int TYPE_GRID_V = 2;
    public static int TYPE_STAGGER_V = 3;
    public static int TYPE_LINEAR_H = 4;
    public static int TYPE_GRID_H = 5;
    public static int TYPE_STAGGER_H = 6;

    public static Fragment getFragment(int itemResId, int itemCount, int layoutManagerType) {
        BaseRecyclerFragment fragment = new BaseRecyclerFragment();
        fragment.setItem(itemResId, itemCount, layoutManagerType);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.recycler_view_fragment, null);
        setRecyclerView(view);
        return view;
    }

    private void setItem(int itemResId, int itemCount, int layoutManagerType) {
        this.itemRes = itemResId;
        this.itemCount = itemCount;
        this.layoutManagerType = layoutManagerType;
    }

    private void setRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setAdapter(new MyAdapter());
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        if (layoutManagerType == TYPE_LINEAR_V) {
            return new LinearLayoutManager(getActivity());
        } else if (layoutManagerType == TYPE_LINEAR_H) {
            return new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        } else if (layoutManagerType == TYPE_GRID_V) {
            return new GridLayoutManager(getActivity(), 2);
        } else if (layoutManagerType == TYPE_GRID_H) {
            return new GridLayoutManager(getActivity(), 2, RecyclerView.HORIZONTAL, false);
        } else if (layoutManagerType == TYPE_STAGGER_V) {
            return new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        } else {
            return new StaggeredGridLayoutManager(2, RecyclerView.HORIZONTAL);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(itemRes, null);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            if (position % 3 == 0) {
                holder.textView.setText(str);
            } else {
                holder.textView.setText("position " + position);
            }
        }

        @Override
        public int getItemCount() {
            return itemCount;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }


}

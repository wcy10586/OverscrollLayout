package com.wcy.overscrolllayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcy.overscrolllayout.fragment.BaseRecyclerFragment;

import javax.crypto.spec.RC2ParameterSpec;

/**
 * Created by changyou on 2016/4/14.
 */
public class RecyclerViewActivity extends AppCompatActivity {
    private Fragment listV = BaseRecyclerFragment.getFragment(R.layout.recycler_item_vertical, 20, BaseRecyclerFragment.TYPE_LINEAR_V);
    private Fragment gridV = BaseRecyclerFragment.getFragment(R.layout.recycler_item_vertical, 20, BaseRecyclerFragment.TYPE_GRID_V);
    private Fragment staggerV = BaseRecyclerFragment.getFragment(R.layout.recycler_item_vertical, 20, BaseRecyclerFragment.TYPE_STAGGER_V);

    private Fragment listH = BaseRecyclerFragment.getFragment(R.layout.recycler_item_horizontal, 10, BaseRecyclerFragment.TYPE_LINEAR_H);
    private Fragment gridH = BaseRecyclerFragment.getFragment(R.layout.recycler_item_horizontal, 10, BaseRecyclerFragment.TYPE_GRID_H);
    private Fragment staggerH = BaseRecyclerFragment.getFragment(R.layout.recycler_item_horizontal, 10, BaseRecyclerFragment.TYPE_STAGGER_H);

    private Fragment currentFragment = listV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        setToolBar();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_root, currentFragment).commit();
    }


    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();
        if (id == R.id.linear) {
            if (currentFragment != listV) {
                currentFragment = listV;
                transaction.replace(R.id.fragment_root, listV).commit();
            }
        } else if (id == R.id.grid) {
            if (currentFragment != gridV) {
                transaction.replace(R.id.fragment_root, gridV).commit();
                currentFragment = gridV;
            }
        } else if (id == R.id.stagger) {
            if (currentFragment != staggerV) {
                transaction.replace(R.id.fragment_root, staggerV).commit();
                currentFragment = staggerV;
            }
        } else if (id == R.id.linear1) {
            if (currentFragment != listH) {
                transaction.replace(R.id.fragment_root, listH).commit();
                currentFragment = listH;
            }
        } else if (id == R.id.grid1) {
            if (currentFragment != gridH) {
                transaction.replace(R.id.fragment_root, gridH).commit();
                currentFragment = gridH;
            }
        } else if (id == R.id.stagger1) {
            if (currentFragment != staggerH) {
                transaction.replace(R.id.fragment_root, staggerH).commit();
                currentFragment = staggerH;
            }
        }
        return true;
    }

}

package com.kk.lp.welcome;

import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;

import com.kk.lp.BaseActivity;
import com.kk.lp.R;

public class WelcomeActivity extends BaseActivity {

    ViewPager view_pager;
    SparseArrayCompat<Welcome> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        initData();
        view_pager.setAdapter(new WelcomePagerAdapter(getSupportFragmentManager(), array));
        WelcomePageTransformer welcomePageTransformer =  new WelcomePageTransformer();
        view_pager.setPageTransformer(false, welcomePageTransformer);
    }

    private SparseArrayCompat initData() {
        array = new SparseArrayCompat<>();
        array.put(0, new Welcome("one", "one page show", R.drawable.compute, R.color.colorPrimaryDark));
        array.put(1, new Welcome("two", "two page show", R.drawable.present, R.color.colorAccent));
        array.put(2, new Welcome("three", "three page show", R.drawable.plane, R.color.green));
        return array;
    }
}


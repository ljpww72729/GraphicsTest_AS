package com.kk.lp.welcome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;

/**
 * Created by lipeng on 2016 5-16.
 */
public class WelcomePagerAdapter extends FragmentPagerAdapter {

    SparseArrayCompat<Welcome> arrayCompat;
    public WelcomePagerAdapter(FragmentManager fm, SparseArrayCompat<Welcome> arrayCompat) {
        super(fm);
        this.arrayCompat = arrayCompat;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = WelcomeFragment.newInstance(arrayCompat.get(position), position);
        return fragment;
    }

    @Override
    public int getCount() {
        return arrayCompat.size();
    }
}

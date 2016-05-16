package com.kk.lp.welcome;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by lipeng on 2016 5-16.
 */
public class WelcomePageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        if (page.getTag() instanceof PageTransformerDelegate){
            ((PageTransformerDelegate)page.getTag()).transformPage(position);
        }
    }
}

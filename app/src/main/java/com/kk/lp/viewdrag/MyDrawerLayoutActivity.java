package com.kk.lp.viewdrag;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.kk.lp.R;

public class MyDrawerLayoutActivity extends AppCompatActivity {

    private MyDrawerLayout mDrawerLayout;
    private FrameLayout mEndDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_drawer_layout);
        mDrawerLayout = (MyDrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setBgTransparent(true);
        mEndDrawer = (FrameLayout) findViewById(R.id.my_end_view);
        // Register a pre-draw listener to get the initial width of the DrawerLayout so
        // that we can determine the width of the drawer based on the Material spec at
        // https://www.google.com/design/spec/patterns/navigation-drawer.html#navigation-drawer-specs
        mDrawerLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        // What is the width of the entire DrawerLayout?
                        final int drawerLayoutWidth = mDrawerLayout.getWidth();

                        // What is the action bar size?
                        final Resources.Theme theme = mDrawerLayout.getContext().getTheme();
                        final TypedArray a = theme.obtainStyledAttributes(
                                new int[] { android.support.v7.appcompat.R.attr.actionBarSize });
                        final int actionBarSize = a.getDimensionPixelSize(0, 0);
                        if (a != null) {
                            a.recycle();
                        }

                        // Compute the width of the drawer and set it on the layout params.
                        final int idealDrawerWidth = 5 * actionBarSize;
                        final int maxDrawerWidth = Math.max(0, drawerLayoutWidth - actionBarSize);
                        final int drawerWidth = Math.min(idealDrawerWidth, maxDrawerWidth);

//                        final DrawerLayout.LayoutParams startDrawerLp =
//                                (DrawerLayout.LayoutParams) mStartDrawer.getLayoutParams();
//                        startDrawerLp.width = drawerWidth;
//                        mStartDrawer.setLayoutParams(startDrawerLp);

                        final MyDrawerLayout.LayoutParams endDrawerLp =
                                (MyDrawerLayout.LayoutParams) mEndDrawer.getLayoutParams();
                        endDrawerLp.width = drawerWidth;
                        mEndDrawer.setLayoutParams(endDrawerLp);

                        // Remove ourselves as the pre-draw listener since this is a one-time
                        // configuration.
                        mDrawerLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
    }
}

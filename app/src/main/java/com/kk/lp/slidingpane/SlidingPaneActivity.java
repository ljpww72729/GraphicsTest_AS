package com.kk.lp.slidingpane;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kk.lp.BaseActivity;
import com.kk.lp.R;

public class SlidingPaneActivity extends BaseActivity {

    private MySlidingPaneLayout mSlidingLayout;
    private ListView mList;
    private TextView mContent;
    private TextView content_text;

//    private ActionBarHelper mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Window window = getWindow();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_pane);

        mSlidingLayout = (MySlidingPaneLayout) findViewById(R.id.sliding_pane_layout);
        mList = (ListView) findViewById(R.id.left_pane);
        mContent = (TextView) findViewById(R.id.content_text);
        content_text = (TextView) findViewById(R.id.content_text);
        content_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingLayout.openPane();
            }
        });

        mSlidingLayout.setPanelSlideListener(new SliderListener());
//        mSlidingLayout.setParallaxDistance(100);
//        mSlidingLayout.setSliderFadeColor(0);
        mSlidingLayout.setShadowDrawableLeft(getResources().getDrawable(R.drawable.shadow_left));

        mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                Shakespeare.TITLES));
        mList.setOnItemClickListener(new ListItemClickListener());


//        mActionBar = createActionBarHelper();
//        mActionBar.init();

        mSlidingLayout.getViewTreeObserver().addOnGlobalLayoutListener(new FirstLayoutListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * The action bar up action should open the slider if it is currently closed,
         * as the left pane contains content one level up in the navigation hierarchy.
         */
        if (item.getItemId() == android.R.id.home && !mSlidingLayout.isOpen()) {
            mSlidingLayout.openPane();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This list item click listener implements very simple view switching by changing
     * the primary content text. The slider is closed when a selection is made to fully
     * reveal the content.
     */
    private class ListItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mContent.setText(Shakespeare.DIALOGUE[position]);
//            mActionBar.setTitle(Shakespeare.TITLES[position]);
            mSlidingLayout.closePane();
        }
    }

    /**
     * This panel slide listener updates the action bar accordingly for each panel state.
     */
    private class SliderListener extends MySlidingPaneLayout.MySimplePanelSlideListener {
        @Override
        public void onPanelOpened(View panel) {
//            mActionBar.onPanelOpened();
        }

        @Override
        public void onPanelClosed(View panel) {
//            mActionBar.onPanelClosed();
        }
    }

    /**
     * This global layout listener is used to fire an event after first layout occurs
     * and then it is removed. This gives us a chance to configure parts of the UI
     * that adapt based on available space after they have had the opportunity to measure
     * and layout.
     */
    private class FirstLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
//            mActionBar.onFirstLayout();
            mSlidingLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    /**
     * Create a compatible helper that will manipulate the action bar if available.
     */
//    private ActionBarHelper createActionBarHelper() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            return new ActionBarHelperICS();
//        } else {
//            return new ActionBarHelper();
//        }
//    }

    /**
     * Stub action bar helper; this does nothing.
     */
    private class ActionBarHelper {
        public void init() {}
        public void onPanelClosed() {}
        public void onPanelOpened() {}
        public void onFirstLayout() {}
        public void setTitle(CharSequence title) {}
    }

//    /**
//     * Action bar helper for use on ICS and newer devices.
//     */
//    private class ActionBarHelperICS extends ActionBarHelper {
//        private final ActionBar mActionBar;
//        private CharSequence mDrawerTitle;
//        private CharSequence mTitle;
//
//        ActionBarHelperICS() {
//            mActionBar = getActionBar();
//        }
//
//        @Override
//        public void init() {
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//            mActionBar.setHomeButtonEnabled(true);
//            mTitle = mDrawerTitle = getTitle();
//        }
//
//        @Override
//        public void onPanelClosed() {
//            super.onPanelClosed();
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//            mActionBar.setHomeButtonEnabled(true);
//            mActionBar.setTitle(mTitle);
//        }
//
//        @Override
//        public void onPanelOpened() {
//            super.onPanelOpened();
//            mActionBar.setHomeButtonEnabled(false);
//            mActionBar.setDisplayHomeAsUpEnabled(false);
//            mActionBar.setTitle(mDrawerTitle);
//        }
//
//        @Override
//        public void onFirstLayout() {
//            if (mSlidingLayout.canSlide() && !mSlidingLayout.isOpen()) {
//                onPanelClosed();
//            } else {
//                onPanelOpened();
//            }
//        }
//
//        @Override
//        public void setTitle(CharSequence title) {
//            mTitle = title;
//        }
//    }

}

package com.kk.lp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by lipeng on 2016 6-3.
 */
public class CustomTextView extends View {
    private static final String TAG = "CustomTextView";

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void requestLayout() {
        Log.d(TAG, "requestLayout() called with: " + "");
        super.requestLayout();

    }
}

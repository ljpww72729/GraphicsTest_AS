package com.kk.lp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lipeng on 2016 6-3.
 */
public class CustomLinearLayout extends ViewGroup {
    private static final String TAG = "CustomLinearLayout";

    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure() called with: " + "widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int layoutHeight = 0;
        int maxLayoutHeight = -1;
        layoutHeight = maxLayoutHeight = heightSize - getPaddingTop() - getPaddingBottom();
        int count = getChildCount();
        int remainHeight = maxLayoutHeight;
        for (int i = 0; i < count; i++){
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childView.getMeasuredWidth(), MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(100, MeasureSpec.EXACTLY);
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            remainHeight -= childView.getMeasuredHeight();
        }
        setMeasuredDimension(widthSize, heightSize);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout() called with: " + "changed = [" + changed + "], l = [" + l + "], t = [" + t + "], r = [" + r + "], b = [" + b + "]");
        int count = getChildCount();
        int left = l;
        int top = t;
        int right = r;
        int bottom = b;

        for (int i = 0; i < count; i++){
            View childView = getChildAt(i);
            childView.layout(left, top, left + getWidth(), top + getHeight());
            top += childView.getHeight();
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        Log.d(TAG, "drawChild() called with: " + "canvas = [" + canvas.isOpaque() + "], child = [" + child + "], drawingTime = [" + drawingTime + "]");
//        canvas.clipRect(0,5,50,50);
//        child.draw(canvas);
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw() called with: " + "canvas = [" + canvas + "]");
        super.onDraw(canvas);
    }

    @Override
    public void requestLayout() {
        Log.d(TAG, "requestLayout() called with: " + "");
        super.requestLayout();
    }
}

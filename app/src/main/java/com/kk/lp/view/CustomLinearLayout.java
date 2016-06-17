package com.kk.lp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
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
        if (heightMode == MeasureSpec.EXACTLY) {
            layoutHeight = maxLayoutHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            layoutHeight = maxLayoutHeight = heightSize - getPaddingTop() - getPaddingBottom();
        }
        int count = getChildCount();
        int childTotalHeight = 0;
        for (int i = 0; i < count; i++) {
            final View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            int childWidthMeasure = MeasureSpec.makeMeasureSpec(childView.getMeasuredWidth(), MeasureSpec.EXACTLY);
            int childHeightMeasure = MeasureSpec.makeMeasureSpec(childView.getMeasuredHeight() * 2, MeasureSpec.EXACTLY);
            childView.measure(childWidthMeasure, childHeightMeasure);
            childTotalHeight += childView.getMeasuredHeight();
        }
        heightSize = Math.max(childTotalHeight + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight());
        setMeasuredDimension(widthSize, heightSize);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout: ");
//        Log.d(TAG, "onLayout() called with: " + "changed = [" + changed + "], l = [" + l + "], t = [" + t + "], r = [" + r + "], b = [" + b + "]");
        int count = getChildCount();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = r;
        int bottom = b;

        // 如果是绘制子孩子，那么left，top不是参数l,t，因为l,t指的是当前布局相对于父布局的位置
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            childView.layout(left, top, left + childView.getMeasuredWidth(), top + childView.getMeasuredHeight());
            top += childView.getMeasuredHeight();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        Log.d(TAG, "drawChild: ");
//        Log.d(TAG, "drawChild() called with: " + "canvas = [" + canvas.isOpaque() + "], child = [" + child + "], drawingTime = [" + drawingTime + "]");
        boolean result = false;
        Rect childeRect = new Rect();
        canvas.save();
        canvas.getClipBounds(childeRect);
//        canvas.clipRect(childeRect.left,childeRect.top,childeRect.right / 4,childeRect.bottom);
        canvas.clipRect(child.getLeft(),child.getTop(),child.getRight() / 2,child.getBottom());
        result = super.drawChild(canvas, child, drawingTime);
        canvas.restore();
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: ");
//        Log.d(TAG, "onDraw() called with: " + "canvas = [" + canvas + "]");
        super.onDraw(canvas);
    }

    @Override
    public void requestLayout() {
        Log.d(TAG, "requestLayout: ");
//        Log.d(TAG, "requestLayout() called with: " + "");
        super.requestLayout();
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }
}

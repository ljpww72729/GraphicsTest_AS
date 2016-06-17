package com.kk.lp.view;

import android.content.Context;
import android.graphics.Canvas;
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure() called with: " + "widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heigthSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY){
        }else if(heightMode == MeasureSpec.AT_MOST){
            final float scale = getResources().getDisplayMetrics().density;
            heigthSize = (int) (10 * scale + 0.5f);
        }
        setMeasuredDimension(widthSize, heigthSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout() called with: " + "changed = [" + changed + "], left = [" + left + "], top = [" + top + "], right = [" + right + "], bottom = [" + bottom + "]");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw() called with: " + "canvas = [" + canvas + "]");
        super.onDraw(canvas);
    }

    public void setBg(){
        requestLayout();
    }

    public void setInvalidate(){
        setMeasuredDimension(100,100);
        requestLayout();
//        invalidate();
    }


}

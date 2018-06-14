package com.kk.lp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static android.graphics.Bitmap.Config.ARGB_4444;

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
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heigthSize = MeasureSpec.getSize(heightMeasureSpec);
//        if (heightMode == MeasureSpec.EXACTLY){
//        }else if(heightMode == MeasureSpec.AT_MOST){
//            final float scale = getResources().getDisplayMetrics().density;
//            heigthSize = (int) (10 * scale + 0.5f);
//        }
//        setMeasuredDimension(widthSize, heigthSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout() called with: " + "changed = [" + changed + "], left = [" + left + "], top = [" + top + "], right = [" + right + "], bottom = [" + bottom + "]");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw() called with: " + "canvas = [" + canvas + "]");
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(drawText("你好"), 0, 0, paint);
        super.onDraw(canvas);
    }

    public void setBg() {
        requestLayout();
    }

    public void setInvalidate() {
        setMeasuredDimension(100, 100);
        requestLayout();
//        invalidate();
    }

    private Bitmap drawText(String text) {
        int WIDTH = 128;
        int HEIGHT = 64;
        Rect targetRect = new Rect(0, 0, 128, 64);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setStrokeWidth(3);
//        paint.setTextSize(40f);
//        paint.setColor(Color.WHITE);

        paint.setTextSize(20);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        Bitmap textAsBitmap = Bitmap.createBitmap(WIDTH, HEIGHT, ARGB_4444);
        Canvas canvas = new Canvas(textAsBitmap);
        canvas.drawText(text, 0, baseline, paint);
        return textAsBitmap;
//        BitmapHelper.setBmpData(mScreen, 0, 0, textAsBitmap, true);
    }

}

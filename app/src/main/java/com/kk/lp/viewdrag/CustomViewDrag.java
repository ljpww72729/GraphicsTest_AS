package com.kk.lp.viewdrag;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kk.lp.R;

/**
 * Created by lipeng on 1-8.
 */
public class CustomViewDrag extends ViewGroup {

    static ViewDragHelper viewDragHelper;
    Paint paint;

    public CustomViewDrag(Context context) {
        this(context, null);
    }

    public CustomViewDrag(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomViewDrag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置viewgroup初始化时调用ondraw方法，默认是不调用的，true为不绘制即不调用ondraw方法，false为绘制ondraw方法
        //此处设置为默认的不绘制ondraw()方法，具体可参考http://blog.csdn.net/leehong2005/article/details/7299471
        setWillNotDraw(false);
        viewDragHelper = ViewDragHelper.create(this, new CustomDragCallback(this));
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(getResources().getColor(R.color.black));
        RelativeLayout rl = new RelativeLayout(context);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(600, 600);
        rl.setLayoutParams(rlp);
        rl.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        addView(rl);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i ++){
            getChildAt(i).layout(20,20,getChildAt(i).getMeasuredWidth(),getChildAt(i).getMeasuredHeight());
        }
    }

    static class CustomDragCallback extends ViewDragHelper.Callback {

        View mParentView = null;

        /**
         * 将父布局通过构造函数传进来
         * @param parentView 父布局
         */
        public CustomDragCallback(@NonNull ViewGroup parentView){
            this.mParentView = parentView;
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //防止子布局向左划出屏幕，防止子布局向右划出屏幕
            int leftBound = Math.min(Math.max(left, mParentView.getPaddingLeft()), mParentView.getWidth() - child.getWidth());
            return leftBound;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int topBound = Math.min(Math.max(top, mParentView.getPaddingTop()),mParentView.getHeight() - child.getHeight());
            return topBound;
        }

//        @Override
//        public int getViewHorizontalDragRange(View child) {
//            return child.getWidth()/2;
//        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            viewDragHelper.settleCapturedViewAt(0,0);
            mParentView.invalidate();
        }


    }


    @Override

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
//        canvas.clipRect(40,40,100,100);
//        canvas.drawRect(10, 10, 50, 50, paint);
        boolean dc = super.drawChild(canvas, child, drawingTime);
        Rect rect = new Rect();
        child.getHitRect(rect);
        //需要在onmeasure()中给childview设定大小后才能获取到childview的大小
        canvas.drawCircle(rect.centerX(),rect.centerY(),child.getMeasuredHeight()/4,paint);
        canvas.clipRect(20,20,100,100);
        canvas.drawColor(getResources().getColor(R.color.textColorSecondary));
        return dc;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        for (int i =0;i < getChildCount();i ++){
            final View child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(width,MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(getResources().getColor(R.color.colorAccent));
        super.onDraw(canvas);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if(action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP){
            viewDragHelper.cancel();
            return  false;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }
}

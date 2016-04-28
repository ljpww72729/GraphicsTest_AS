package com.kk.lp.material_design;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by lipeng on 2016 4-28.
 */
public class LPBottomNavigationBehavior extends CoordinatorLayout.Behavior {

    private static final String TAG = "BNBehavior";
    
    private int mMinOffset;

    private int mMaxOffset;
    /**
     * 展现或者隐藏BottomNavigation需要向上或者向下滑动的距离
     */
    private int mScrollDistance;
    /**
     * 已经滑动的距离
     */
    private int mTotalScrollDistance;
    private int mMaximumVelocity;


    public LPBottomNavigationBehavior() {
    }

    public LPBottomNavigationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        Log.d(TAG, "onLayoutChild: ");
        parent.onLayoutChild(child, layoutDirection);
        mMinOffset = parent.getHeight() - child.getHeight();
        mMaxOffset = parent.getHeight();
        mScrollDistance = child.getHeight();
        if (child.isShown()){
            ViewCompat.offsetTopAndBottom(child, mMinOffset);
        }
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.d(TAG, "onStartNestedScroll: ");
        mTotalScrollDistance = 0;
        //只监测垂直滚动的内嵌视图
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        mTotalScrollDistance += dy;
        Log.d(TAG, "onNestedPreScroll: mTotalScrollDistance===" + String.valueOf(mTotalScrollDistance));
        if (mTotalScrollDistance > mScrollDistance) {//内嵌视图向上滚动
            hideView(child);
        } else if (mTotalScrollDistance < -mScrollDistance){
            showView(child);
        }
    }

    /**
     * 隐藏视图
     *
     * @param child
     */
    private void hideView(View child) {
        if (child.getVisibility() != View.GONE){
            child.setVisibility(View.GONE);
        }
    }

    /**
     * 显示视图
     *
     * @param child
     */
    private void showView(View child) {
        if (child.getVisibility() != View.VISIBLE){
            child.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        Log.d(TAG, "onStopNestedScroll: ");
        mTotalScrollDistance = 0;
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        Log.d(TAG, "onNestedPreFling() called with: mMaximumVelocity" + mMaximumVelocity + " velocityX = [" + velocityX + "], velocityY = [" + velocityY + "]");
        if (velocityY > 0 && velocityY > mMaximumVelocity * 0.1) {//内嵌视图向上滚动
            hideView(child);
        } else if (velocityY < 0 && velocityY < -mMaximumVelocity * 0.1){
            showView(child);
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }
}

package com.kk.lp.material_design;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

/**
 * Created by lipeng on 2016 4-28.
 */
public class LPBottomNavigationBehavior extends CoordinatorLayout.Behavior {

    private static final String TAG = "BNBehavior";

    static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();

    static final int ANIMATION_DURATION = 250;

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
    private boolean isAnimationing = false;


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
//        if (isAnimationing){
//            return false;
//        }
        // TODO: 4-29 这段代码有问题：我并没有隐藏child
        if (child.isShown()) {
            ViewCompat.offsetTopAndBottom(child, mMinOffset);
        }else {
            ViewCompat.offsetTopAndBottom(child, mMaxOffset);
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
        if ((mTotalScrollDistance > 0 && dy < 0) || (mTotalScrollDistance < 0 && dy > 0)){
            mTotalScrollDistance = 0;
        }
        mTotalScrollDistance += dy;
        Log.d(TAG, "onNestedPreScroll: mTotalScrollDistance===" + String.valueOf(mTotalScrollDistance) + "mScrollDistance==" + mScrollDistance);
        if (mTotalScrollDistance > mScrollDistance) {//内嵌视图向上滚动
            hideView(child);
        } else if (mTotalScrollDistance < -mScrollDistance) {
            showView(child);
        }
    }

    /**
     * 隐藏视图
     *
     * @param child
     */
    private void hideView(View child) {
        Log.d(TAG, "hideView: child.getTop() == " + child.getTop());
        if (!isAnimationing && child.getTop() == mMinOffset) {
            isAnimationing = true;
            animateViewOut(child);
        }
    }

    private void animateViewOut(final View child) {
//多次调用动画会导致动画不会立即开始，只会执行最后一次动画，给人感觉有延迟
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewCompat.animate(child)
                    .translationY(child.getHeight())
                    .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(View view) {
                            isAnimationing = false;
                            Log.d(TAG, "animateViewOut: child.getTop() == " + child.getTop());
//                            child.setVisibility(View.INVISIBLE);
                        }

                    }).start();
        } else {
            Animation animation = AnimationUtils.loadAnimation(child.getContext(), android.support.design.R.anim.design_snackbar_out);
            animation.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
            animation.setDuration(ANIMATION_DURATION);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnimationing = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            child.startAnimation(animation);
        }
    }

    /**
     * 显示视图
     *
     * @param child
     */
    private void showView(View child) {
        Log.d(TAG, "showView: child.getTop() == " + child.getTop());
        if (!isAnimationing && child.getTop() == mMinOffset) {
            isAnimationing = true;
            animateViewIn(child);
        }
    }

    private void animateViewIn(final View mView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            ViewCompat.setTranslationY(mView, mView.getHeight());
            ViewCompat.animate(mView)
                    .translationY(0f)
                    .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(View view) {
//                            mView.animateChildrenIn(ANIMATION_DURATION - ANIMATION_FADE_DURATION,
//                                    ANIMATION_FADE_DURATION);
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            isAnimationing = false;
                            Log.d(TAG, "onAnimationEnd: child.getTop() == " + view.getTop());
//                            mView.setVisibility(View.VISIBLE);
                        }
                    }).start();
        } else {
            Animation anim = AnimationUtils.loadAnimation(mView.getContext(),
                    android.support.design.R.anim.design_snackbar_in);
            anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(ANIMATION_DURATION);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnimationing = false;

                }

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mView.startAnimation(anim);
        }
    }

    //    void animateChildrenIn(int delay, int duration) {
//        ViewCompat.setAlpha(mMessageView, 0f);
//        ViewCompat.animate(mMessageView).alpha(1f).setDuration(duration)
//                .setStartDelay(delay).start();
//
//        if (mActionView.getVisibility() == VISIBLE) {
//            ViewCompat.setAlpha(mActionView, 0f);
//            ViewCompat.animate(mActionView).alpha(1f).setDuration(duration)
//                    .setStartDelay(delay).start();
//        }
//    }
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
        } else if (velocityY < 0 && velocityY < -mMaximumVelocity * 0.1) {
            showView(child);
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }
}

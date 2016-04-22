package com.kk.lp.slidingpane;

/**
 * Created by lipeng on 2016 4-1.
 */
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * MySlidingPaneLayout provides a horizontal, multi-pane layout for use at the top level
 * of a UI. A left (or first) pane is treated as a content list or browser, subordinate to a
 * primary detail view for displaying content.
 *
 * <p>Child views may overlap if their combined width exceeds the available width
 * in the MySlidingPaneLayout. When this occurs the user may slide the topmost view out of the way
 * by dragging it, or by navigating in the direction of the overlapped view using a keyboard.
 * If the content of the dragged child view is itself horizontally scrollable, the user may
 * grab it by the very edge.</p>
 *
 * <p>Thanks to this sliding behavior, MySlidingPaneLayout may be suitable for creating layouts
 * that can smoothly adapt across many different screen sizes, expanding out fully on larger
 * screens and collapsing on smaller screens.</p>
 *
 * <p>MySlidingPaneLayout is distinct from a navigation drawer as described in the design
 * guide and should not be used in the same scenarios. MySlidingPaneLayout should be thought
 * of only as a way to allow a two-pane layout normally used on larger screens to adapt to smaller
 * screens in a natural way. The interaction patterns expressed by MySlidingPaneLayout imply
 * a physicality and direct information hierarchy between panes that does not necessarily exist
 * in a scenario where a navigation drawer should be used instead.</p>
 *
 * <p>Appropriate uses of MySlidingPaneLayout include pairings of panes such as a contact list and
 * subordinate interactions with those contacts, or an email thread list with the content pane
 * displaying the contents of the selected thread. Inappropriate uses of MySlidingPaneLayout include
 * switching between disparate functions of your app, such as jumping from a social stream view
 * to a view of your personal profile - cases such as this should use the navigation drawer
 * pattern instead. ({@link DrawerLayout DrawerLayout} implements this pattern.)</p>
 *
 * <p>Like {@link android.widget.LinearLayout LinearLayout}, MySlidingPaneLayout supports
 * the use of the layout parameter <code>layout_weight</code> on child views to determine
 * how to divide leftover space after measurement is complete. It is only relevant for width.
 * When views do not overlap weight behaves as it does in a LinearLayout.</p>
 *
 * <p>When views do overlap, weight on a slideable pane indicates that the pane should be
 * sized to fill all available space in the closed state. Weight on a pane that becomes covered
 * indicates that the pane should be sized to fill all available space except a small minimum strip
 * that the user may use to grab the slideable view and pull it back over into a closed state.</p>
 */
public class MySlidingPaneLayout extends ViewGroup {
    private static final String TAG = "MySlidingPaneLayout";

    /**
     * Default size of the overhang for a pane in the open state.
     * At least this much of a sliding pane will remain visible.
     * This indicates that there is more content available and provides
     * a "physical" edge to grab to pull it closed.
     */
    private static final int DEFAULT_OVERHANG_SIZE = 36; // dp;

    /**
     * If no fade color is given by default it will fade to 80% gray.
     */
    private static final int DEFAULT_FADE_COLOR = 0xcccccccc;

    /**
     * The fade color used for the sliding panel. 0 = no fading.
     */
    private int mSliderFadeColor = DEFAULT_FADE_COLOR;

    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    /**
     * The fade color used for the panel covered by the slider. 0 = no fading.
     */
    private int mCoveredFadeColor;

    /**
     * Drawable used to draw the shadow between panes by default.
     */
    private Drawable mShadowDrawableLeft;

    /**
     * Drawable used to draw the shadow between panes to support RTL (right to left language).
     */
    private Drawable mShadowDrawableRight;

    /**
     * The size of the overhang in pixels.
     * This is the minimum section of the sliding panel that will
     * be visible in the open state to allow for a closing drag.
     */
    private final int mOverhangSize;

    /**
     * True if a panel can slide with the current measurements
     */
    private boolean mCanSlide;

    /**
     * The child view that can slide, if any.
     */
    private View mSlideableView;

    /**
     * How far the panel is offset from its closed position.
     * range [0, 1] where 0 = closed, 1 = open.
     */
    private float mSlideOffset;

    /**
     * How far the non-sliding panel is parallaxed from its usual position when open.
     * range [0, 1]
     */
    private float mParallaxOffset;

    /**
     * How far in pixels the slideable panel may move.
     */
    private int mSlideRange;

    /**
     * A panel view is locked into internal scrolling or another condition that
     * is preventing a drag.
     */
    private boolean mIsUnableToDrag;

    /**
     * Distance in pixels to parallax the fixed pane by when fully closed
     */
    private int mParallaxBy;

    private float mInitialMotionX;
    private float mInitialMotionY;

    private MyPanelSlideListener mPanelSlideListener;

    private final ViewDragHelper mDragHelper;

    /**
     * Stores whether or not the pane was open the last time it was slideable.
     * If open/close operations are invoked this state is modified. Used by
     * instance state save/restore.
     */
    private boolean mPreservedOpenState;
    private boolean mFirstLayout = true;

    private final Rect mTmpRect = new Rect();

    private final ArrayList<DisableLayerRunnable> mPostedRunnables =
            new ArrayList<DisableLayerRunnable>();

    static final SlidingPanelLayoutImpl IMPL;

    static {
        final int deviceVersion = Build.VERSION.SDK_INT;
        if (deviceVersion >= 17) {
            IMPL = new SlidingPanelLayoutImplJBMR1();
        } else if (deviceVersion >= 16) {
            IMPL = new SlidingPanelLayoutImplJB();
        } else {
            IMPL = new SlidingPanelLayoutImplBase();
        }
    }

    /**
     * Listener for monitoring events about sliding panes.
     */
    public interface MyPanelSlideListener {
        /**
         * Called when a sliding pane's position changes.
         * @param panel The child view that was moved
         * @param slideOffset The new offset of this sliding pane within its range, from 0-1
         */
        public void onPanelSlide(View panel, float slideOffset);
        /**
         * Called when a sliding pane becomes slid completely open. The pane may or may not
         * be interactive at this point depending on how much of the pane is visible.
         * @param panel The child view that was slid to an open position, revealing other panes
         */
        public void onPanelOpened(View panel);

        /**
         * Called when a sliding pane becomes slid completely closed. The pane is now guaranteed
         * to be interactive. It may now obscure other views in the layout.
         * @param panel The child view that was slid to a closed position
         */
        public void onPanelClosed(View panel);
    }

    /**
     * No-op stubs for {@link MyPanelSlideListener}. If you only want to implement a subset
     * of the listener methods you can extend this instead of implement the full interface.
     */
    public static class MySimplePanelSlideListener implements MyPanelSlideListener {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
        }
        @Override
        public void onPanelOpened(View panel) {
        }
        @Override
        public void onPanelClosed(View panel) {
        }
    }

    public MySlidingPaneLayout(Context context) {
        this(context, null);
    }

    public MySlidingPaneLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //设置滑动视图的悬挂默认大小，dp单位
        final float density = context.getResources().getDisplayMetrics().density;
        mOverhangSize = (int) (DEFAULT_OVERHANG_SIZE * density + 0.5f);

        final ViewConfiguration viewConfig = ViewConfiguration.get(context);

        //因为重写了draw()方法，所以需要进行以下设置
        setWillNotDraw(false);

        mDragHelper = ViewDragHelper.create(this, 0.5f, new DragHelperCallback());
        mDragHelper.setMinVelocity(MIN_FLING_VELOCITY * density);
    }

    /**
     * Set a distance to parallax the lower pane by when the upper pane is in its
     * fully closed state. The lower pane will scroll between this position and
     * its fully open state.
     *
     * @param parallaxBy Distance to parallax by in pixels
     */
    public void setParallaxDistance(int parallaxBy) {
        mParallaxBy = parallaxBy;
        requestLayout();
    }

    /**
     * @return The distance the lower pane will parallax by when the upper pane is fully closed.
     *
     * @see #setParallaxDistance(int)
     */
    public int getParallaxDistance() {
        return mParallaxBy;
    }

    /**
     * Set the color used to fade the sliding pane out when it is slid most of the way offscreen.
     *
     * @param color An ARGB-packed color value
     */
    public void setSliderFadeColor(@ColorInt int color) {
        mSliderFadeColor = color;
    }

    /**
     * @return The ARGB-packed color value used to fade the sliding pane
     */
    @ColorInt
    public int getSliderFadeColor() {
        return mSliderFadeColor;
    }

    /**
     * Set the color used to fade the pane covered by the sliding pane out when the pane
     * will become fully covered in the closed state.
     *
     * @param color An ARGB-packed color value
     */
    public void setCoveredFadeColor(@ColorInt int color) {
        mCoveredFadeColor = color;
    }

    /**
     * @return The ARGB-packed color value used to fade the fixed pane
     */
    @ColorInt
    public int getCoveredFadeColor() {
        return mCoveredFadeColor;
    }

    public void setPanelSlideListener(MyPanelSlideListener listener) {
        mPanelSlideListener = listener;
    }

    void dispatchOnPanelSlide(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelSlide(panel, mSlideOffset);
        }
    }

    void dispatchOnPanelOpened(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelOpened(panel);
        }
    }

    void dispatchOnPanelClosed(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelClosed(panel);
        }
    }

    /**
     * 更新被遮盖视图的可见性（第一个节点）
     * 如果第一个节点处于第二个节点之下，完全不可见，则把它隐藏，但是该情况我并没有复现
     * @param panel
     */
    void updateObscuredViewsVisibility(View panel) {
        final boolean isLayoutRtl = isLayoutRtlSupport();
        //获取该视图给予子布局的左上角坐标点
        final int startBound = isLayoutRtl ? (getWidth() - getPaddingRight()) :
                getPaddingLeft();
        //获取该视图给予子布局的右上角坐标点，因为是getWidth() - getPaddingRight()，
        // 这就要求该布局必须为布局视图的顶层节点
        final int endBound = isLayoutRtl ? getPaddingLeft() :
                (getWidth() - getPaddingRight());
        final int topBound = getPaddingTop();
        final int bottomBound = getHeight() - getPaddingBottom();
        //可滑动视图的布局坐标
        final int left;
        final int right;
        final int top;
        final int bottom;
        if (panel != null && viewIsOpaque(panel)) {
            left = panel.getLeft();
            right = panel.getRight();
            top = panel.getTop();
            bottom = panel.getBottom();
        } else {
            left = right = top = bottom = 0;
        }

        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            final View child = getChildAt(i);

            if (child == panel) {
                // There are still more children above the panel but they won't be affected.
                break;
            }

            final int clampedChildLeft = Math.max((isLayoutRtl ? endBound :
                    startBound), child.getLeft());
            final int clampedChildTop = Math.max(topBound, child.getTop());
            final int clampedChildRight = Math.min((isLayoutRtl ? startBound :
                    endBound), child.getRight());
            final int clampedChildBottom = Math.min(bottomBound, child.getBottom());
            final int vis;
            if (clampedChildLeft >= left && clampedChildTop >= top &&
                    clampedChildRight <= right && clampedChildBottom <= bottom) {
                vis = INVISIBLE;
            } else {
                vis = VISIBLE;
            }
            child.setVisibility(vis);
        }
    }

    void setAllChildrenVisible() {
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == INVISIBLE) {
                child.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * 判断视图是否为不透明
     * @param v
     * @return true 完全不透明
     */
    private static boolean viewIsOpaque(View v) {
        if (ViewCompat.isOpaque(v)) return true;

        //View#isOpaque方法在API 18之前并不会把所有有效的不透明的滚动条模式考虑进去
        //我猜应该是滚动条为不透明，isOpaque才会返回true吧，只是猜测
        //在新的设备上，仅仅依赖上面的isOpaque方法，并且返回false
        // View#isOpaque didn't take all valid opaque scrollbar modes into account
        // before API 18 (JB-MR2). On newer devices rely solely on isOpaque above and return false
        // here. On older devices, check the view's background drawable directly as a fallback.
        if (Build.VERSION.SDK_INT >= 18) return false;

        final Drawable bg = v.getBackground();
        if (bg != null) {
            return bg.getOpacity() == PixelFormat.OPAQUE;
        }
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFirstLayout = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFirstLayout = true;

        for (int i = 0, count = mPostedRunnables.size(); i < count; i++) {
            final DisableLayerRunnable dlr = mPostedRunnables.get(i);
            dlr.run();
        }
        mPostedRunnables.clear();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: ");
        //widthMeasureSpec，heightMeasureSpec为该MySlidingPaneLayout能够给予子布局的Spec
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            if (isInEditMode()) {
                // Don't crash the layout editor. Consume all of the space if specified
                // or pick a magic number from thin air otherwise.
                // TODO Better communication with tools of this bogus state.
                // It will crash on a real device.
                if (widthMode == MeasureSpec.AT_MOST) {
                    widthMode = MeasureSpec.EXACTLY;
                } else if (widthMode == MeasureSpec.UNSPECIFIED) {
                    widthMode = MeasureSpec.EXACTLY;
                    widthSize = 300;
                }
            } else {
                throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
            }
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            if (isInEditMode()) {
                // Don't crash the layout editor. Pick a magic number from thin air instead.
                // TODO Better communication with tools of this bogus state.
                // It will crash on a real device.
                if (heightMode == MeasureSpec.UNSPECIFIED) {
                    heightMode = MeasureSpec.AT_MOST;
                    heightSize = 300;
                }
            } else {
                throw new IllegalStateException("Height must not be UNSPECIFIED");
            }
        }

        //根据子布局确定最终的布局高度
        int layoutHeight = 0;
        //-1代表的是match_parent，因为match_parent默认值为-1
        int maxLayoutHeight = -1;
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                //如果高度是固定值，如match_parent，200dp；则执行此处
                //此时子布局获得的高度=最大高度=父布局给予的高度-getPaddingTop()-getPaddingBottom()
                layoutHeight = maxLayoutHeight = heightSize - getPaddingTop() - getPaddingBottom();
                break;
            case MeasureSpec.AT_MOST:
                //如果高度是不是固定值，如wrap_parent；则执行此处
                //此时子布局获得的高度无法确定，需要根据子布局进行最终大小的确定
                //最大高度=父布局给予的高度-getPaddingTop()-getPaddingBottom()
                maxLayoutHeight = heightSize - getPaddingTop() - getPaddingBottom();
                break;
        }

        //此处考虑的是layout_weight的情况，暂不做分析
        // TODO: 4-5
        float weightSum = 0;
        //是否有可滑动的布局
        boolean canSlide = false;
        //子布局可获得的宽度，该宽度是固定不变的
        final int widthAvailable = widthSize - getPaddingLeft() - getPaddingRight();
        //子布局布局后的剩余宽度
        int widthRemaining = widthAvailable;
        final int childCount = getChildCount();

        if (childCount > 2) {
            Log.e(TAG, "onMeasure: More than two child views are not supported.");
        }

        // We'll find the current one below.
        mSlideableView = null;

        // First pass. Measure based on child LayoutParams width/height.
        //第一种遍：测量基于布局的width/height。
        // Weight will incur a second pass.
        //weight将会触发第二种遍进行measure
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            //此处的LayoutParams是该自定义ViewGroup中自定义的LayoutParams
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() == GONE) {
                lp.dimWhenOffset = false;
                continue;
            }

            //暂时不考虑weight的情况
            if (lp.weight > 0) {
                weightSum += lp.weight;

                // If we have no width, weight is the only contributor to the final size.
                // Measure this view on the weight pass only.
                //如果没有宽度，则只能通过weight来进行最终大小的确定，因为跳过了所以会在下面进行测量
                if (lp.width == 0) continue;
            }

            //如果是第一个直接子节点，则widthAvailable为该父布局可提供的宽度
            //如果是第二个子节点，widthAvailable仍然为该父布局可提供的宽度，与第一个子节点的widthAvailable是相同的！！！
            int childWidthSpec;
            final int horizontalMargin = lp.leftMargin + lp.rightMargin;
            if (lp.width == LayoutParams.WRAP_CONTENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(widthAvailable - horizontalMargin,
                        MeasureSpec.AT_MOST);
            } else if (lp.width == LayoutParams.MATCH_PARENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(widthAvailable - horizontalMargin,
                        MeasureSpec.EXACTLY);
            } else {
                childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
            }

            //子布局可以获得的高度在前面已有计算
            int childHeightSpec;
            if (lp.height == LayoutParams.WRAP_CONTENT) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight, MeasureSpec.AT_MOST);
            } else if (lp.height == LayoutParams.MATCH_PARENT) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight, MeasureSpec.EXACTLY);
            } else {
                childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
            }

            //通过测量获取该子节点的实际大小
            child.measure(childWidthSpec, childHeightSpec);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            //如果子节点的实际大小大于父布局能过给予的大小并且子布局可以尽可能多的占用空间，
            //但是还是限制不能超过最大高度
            if (heightMode == MeasureSpec.AT_MOST && childHeight > layoutHeight) {
                layoutHeight = Math.min(childHeight, maxLayoutHeight);
            }

            widthRemaining -= childWidth;
            //剩余空间如果小于0，则认为是可滑动的
            //如果第一个子节点是有宽度的，若第二个子节点为match_parent,则测量后的widthRemaining肯定小于0
            //如果第一个子节点宽度为100，第二个子节点宽度为100，widthAvailable为300，则第二个节点布局是不可滑动
            //只有lp.slideable为true,canSlide才为true
            canSlide |= lp.slideable = widthRemaining < 0;
            if (lp.slideable) {
                mSlideableView = child;
            }
        }

        // Resolve weight and make sure non-sliding panels are smaller than the full screen.
        //假设第一布局不可滑动，第二个布局是可以滑动的，则有可滑动的布局，所以canSlide为true,同时假设weightSum为0，不考虑weight的情况
        if (canSlide || weightSum > 0) {
            //可展示的区域
            final int fixedPanelWidthLimit = widthAvailable - mOverhangSize;

            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);

                if (child.getVisibility() == GONE) {
                    continue;
                }

                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                if (child.getVisibility() == GONE) {
                    continue;
                }

                //此处显示了设置weight的时候，设置宽度为0dp,设置weight为大于0的数值的缘由
                final boolean skippedFirstPass = lp.width == 0 && lp.weight > 0;
                final int measuredWidth = skippedFirstPass ? 0 : child.getMeasuredWidth();
                //此处执行的是不可滑动的布局，即第一个布局
                if (canSlide && child != mSlideableView) {
//                    lp.width<0代表的是match_parent,match_parent=-1
//                    以下是限制第一个布局不能占用悬挂空间
                    if (lp.width < 0 && (measuredWidth > fixedPanelWidthLimit || lp.weight > 0)) {
                        // Fixed panels in a sliding configuration should
                        // be clamped to the fixed panel limit.
                        final int childHeightSpec;
                        if (skippedFirstPass) {
                            // Do initial height measurement if we skipped measuring this view
                            // the first time around.
                            if (lp.height == LayoutParams.WRAP_CONTENT) {
                                childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight,
                                        MeasureSpec.AT_MOST);
                            } else if (lp.height == LayoutParams.FILL_PARENT) {
                                childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight,
                                        MeasureSpec.EXACTLY);
                            } else {
                                childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height,
                                        MeasureSpec.EXACTLY);
                            }
                        } else {
                            childHeightSpec = MeasureSpec.makeMeasureSpec(
                                    child.getMeasuredHeight(), MeasureSpec.EXACTLY);
                        }
                        //宽度是不能超过可显示的最大宽度
                        final int childWidthSpec = MeasureSpec.makeMeasureSpec(
                                fixedPanelWidthLimit, MeasureSpec.EXACTLY);
                        child.measure(childWidthSpec, childHeightSpec);
                    }
                } else if (lp.weight > 0) {
                    //没有可滑动的布局或者是可滑动的布局，并且weight>0
                    int childHeightSpec;
                    if (lp.width == 0) {
                        // This was skipped the first time; figure out a real height spec.
                        if (lp.height == LayoutParams.WRAP_CONTENT) {
                            childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight,
                                    MeasureSpec.AT_MOST);
                        } else if (lp.height == LayoutParams.FILL_PARENT) {
                            childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight,
                                    MeasureSpec.EXACTLY);
                        } else {
                            childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height,
                                    MeasureSpec.EXACTLY);
                        }
                    } else {
                        childHeightSpec = MeasureSpec.makeMeasureSpec(
                                child.getMeasuredHeight(), MeasureSpec.EXACTLY);
                    }

                    if (canSlide) {
                        // Consume available space
                        final int horizontalMargin = lp.leftMargin + lp.rightMargin;
                        final int newWidth = widthAvailable - horizontalMargin;
                        final int childWidthSpec = MeasureSpec.makeMeasureSpec(
                                newWidth, MeasureSpec.EXACTLY);
                        if (measuredWidth != newWidth) {
                            child.measure(childWidthSpec, childHeightSpec);
                        }
                    } else {
                        // Distribute the extra width proportionally similar to LinearLayout
                        final int widthToDistribute = Math.max(0, widthRemaining);
                        final int addedWidth = (int) (lp.weight * widthToDistribute / weightSum);
                        final int childWidthSpec = MeasureSpec.makeMeasureSpec(
                                measuredWidth + addedWidth, MeasureSpec.EXACTLY);
                        child.measure(childWidthSpec, childHeightSpec);
                    }
                }
            }
        }

        //
        final int measuredWidth = widthSize;
        final int measuredHeight = layoutHeight + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(measuredWidth, measuredHeight);
        mCanSlide = canSlide;

        if (mDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE && !canSlide) {
            // Cancel scrolling in progress, it's no longer relevant.
            mDragHelper.abort();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout: ");
        final boolean isLayoutRtl = isLayoutRtlSupport();
        if (isLayoutRtl) {
            mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
        } else {
            mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        }
        //获取测量后该布局的宽度
        final int width = r - l;
        final int paddingStart = isLayoutRtl ? getPaddingRight() : getPaddingLeft();
        final int paddingEnd = isLayoutRtl ? getPaddingLeft() : getPaddingRight();
        final int paddingTop = getPaddingTop();

        final int childCount = getChildCount();
        int xStart = paddingStart;
        int nextXStart = xStart;

        if (mFirstLayout) {
            //可滑动且是打开状态，则mSlideOffset为1.f，否则为0.f
            mSlideOffset = mCanSlide && mPreservedOpenState ? 1.f : 0.f;
        }

        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            final int childWidth = child.getMeasuredWidth();
            int offset = 0;

            if (lp.slideable) {
                //是可滑动的布局，第二个子节点
                final int margin = lp.leftMargin + lp.rightMargin;
                //滑动的区间范围
                final int range = Math.min(nextXStart,
                        width - paddingEnd - mOverhangSize) - xStart - margin;
                mSlideRange = range;
                final int lpMargin = isLayoutRtl ? lp.rightMargin : lp.leftMargin;
                //以下是为了设置是否模糊布局
                lp.dimWhenOffset = xStart + lpMargin + range + childWidth / 2 >
                        width - paddingEnd;
                final int pos = (int) (range * mSlideOffset);
                xStart += pos + lpMargin;
                mSlideOffset = (float) pos / mSlideRange;
            } else if (mCanSlide && mParallaxBy != 0) {
                offset = (int) ((1 - mSlideOffset) * mParallaxBy);
                xStart = nextXStart;
            } else {
                //mParallaxBy==0并且是第一个子节点布局
                xStart = nextXStart;
            }

            final int childRight;
            final int childLeft;
            if (isLayoutRtl) {
                childRight = width - xStart + offset;
                childLeft = childRight - childWidth;
            } else {
                childLeft = xStart - offset;
                childRight = childLeft + childWidth;
            }

            final int childTop = paddingTop;
            final int childBottom = childTop + child.getMeasuredHeight();
            child.layout(childLeft, paddingTop, childRight, childBottom);

            nextXStart += child.getWidth();
        }

        if (mFirstLayout) {
            if (mCanSlide) {
                if (mParallaxBy != 0) {
                    parallaxOtherViews(mSlideOffset);
                }
                if (((LayoutParams) mSlideableView.getLayoutParams()).dimWhenOffset) {
                    dimChildView(mSlideableView, mSlideOffset, mSliderFadeColor);
                }
            } else {
                // Reset the dim level of all children; it's irrelevant when nothing moves.
                for (int i = 0; i < childCount; i++) {
                    dimChildView(getChildAt(i), 0, mSliderFadeColor);
                }
            }
            updateObscuredViewsVisibility(mSlideableView);
        }

        mFirstLayout = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged: ");
        super.onSizeChanged(w, h, oldw, oldh);
        // Recalculate sliding panes and their details
        if (w != oldw) {
            mFirstLayout = true;
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        if (!isInTouchMode() && !mCanSlide) {
            mPreservedOpenState = child == mSlideableView;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent: ");

        final int action = MotionEventCompat.getActionMasked(ev);

        // Preserve the open state based on the last view that was touched.
        //当处于不可滑动的状态（指的是第二个节点视图是否可滑动打开或者关闭，一般情况是处于可滑动的状态的），并且是ACTION_DOWN事件，并且子视图的数量大于1
        //则进行判断点击的点是否是点击在第二个视图上，如果是，则改变状态，打开->关闭或者是关闭->打开
        if (!mCanSlide && action == MotionEvent.ACTION_DOWN && getChildCount() > 1) {
            // After the first things will be slideable.
            final View secondChild = getChildAt(1);
            if (secondChild != null) {
                //存在这么一种情况，竖屏的时候是可滑动的，但是横屏的时候是不可滑动的，看参考官方V4包的slidingpane，
                //切换打开或者关闭的状态，此处改变状态的目的是为了当横竖屏切换的时候，保存打开或者关闭的状态。可尝试官方demo查看
                mPreservedOpenState = !mDragHelper.isViewUnder(secondChild,
                        (int) ev.getX(), (int) ev.getY());
            }
        }

        if (!mCanSlide || (mIsUnableToDrag && action != MotionEvent.ACTION_DOWN)) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }

        boolean interceptTap = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mIsUnableToDrag = false;
                final float x = ev.getX();
                final float y = ev.getY();
                mInitialMotionX = x;
                mInitialMotionY = y;

                if (mDragHelper.isViewUnder(mSlideableView, (int) x, (int) y) &&
                        isDimmed(mSlideableView)) {
                    interceptTap = true;
                }
                break;
            }

            //主要用于判断是否意图垂直滑动
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                final float adx = Math.abs(x - mInitialMotionX);
                final float ady = Math.abs(y - mInitialMotionY);
                final int slop = mDragHelper.getTouchSlop();
                //如果adx < slop，则DragHelper不会处理滑动事件，但是如果是adx > slop，则必须判断ady > adx吗？
                //如果大于，则是垂直滑动，取消水平滑动
                if (adx > slop && ady > adx) {
                    mDragHelper.cancel();
                    mIsUnableToDrag = true;
                    return false;
                }
            }
        }


        final boolean interceptForDrag = mDragHelper.shouldInterceptTouchEvent(ev);

        return interceptForDrag || interceptTap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onTouchEvent: ");
        if (!mCanSlide) {
            return super.onTouchEvent(ev);
        }

        mDragHelper.processTouchEvent(ev);

        final int action = ev.getAction();
        boolean wantTouchEvents = true;

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                if (isDimmed(mSlideableView)) {
                    final float x = ev.getX();
                    final float y = ev.getY();
                    final float dx = x - mInitialMotionX;
                    final float dy = y - mInitialMotionY;
                    final int slop = mDragHelper.getTouchSlop();
                    if (dx * dx + dy * dy < slop * slop &&
                            mDragHelper.isViewUnder(mSlideableView, (int) x, (int) y)) {
                        // Taps close a dimmed open pane.
                        closePane(mSlideableView, 0);
                        break;
                    }
                }
                break;
            }
        }

        return wantTouchEvents;
    }

    private boolean closePane(View pane, int initialVelocity) {
        if (mFirstLayout || smoothSlideTo(0.f, initialVelocity)) {
            mPreservedOpenState = false;
            return true;
        }
        return false;
    }

    private boolean openPane(View pane, int initialVelocity) {
        if (mFirstLayout || smoothSlideTo(1.f, initialVelocity)) {
            mPreservedOpenState = true;
            return true;
        }
        return false;
    }

    /**
     * @deprecated Renamed to {@link #openPane()} - this method is going away soon!
     */
    @Deprecated
    public void smoothSlideOpen() {
        openPane();
    }

    /**
     * Open the sliding pane if it is currently slideable. If first layout
     * has already completed this will animate.
     *
     * @return true if the pane was slideable and is now open/in the process of opening
     */
    public boolean openPane() {
        return openPane(mSlideableView, 0);
    }

    /**
     * @deprecated Renamed to {@link #closePane()} - this method is going away soon!
     */
    @Deprecated
    public void smoothSlideClosed() {
        closePane();
    }

    /**
     * Close the sliding pane if it is currently slideable. If first layout
     * has already completed this will animate.
     *
     * @return true if the pane was slideable and is now closed/in the process of closing
     */
    public boolean closePane() {
        return closePane(mSlideableView, 0);
    }

    /**
     * Check if the layout is completely open. It can be open either because the slider
     * itself is open revealing the left pane, or if all content fits without sliding.
     *
     * @return true if sliding panels are completely open
     */
    public boolean isOpen() {
        return !mCanSlide || mSlideOffset == 1;
    }

    /**
     * @return true if content in this layout can be slid open and closed
     * @deprecated Renamed to {@link #isSlideable()} - this method is going away soon!
     */
    @Deprecated
    public boolean canSlide() {
        return mCanSlide;
    }

    /**
     * Check if the content in this layout cannot fully fit side by side and therefore
     * the content pane can be slid back and forth.
     *
     * @return true if content in this layout can be slid open and closed
     */
    public boolean isSlideable() {
        return mCanSlide;
    }

    /**
     * 视图正在被拖拽时调用，目的是为了展现一些特殊效果
     * @param newLeft
     */
    private void onPanelDragged(int newLeft) {
        if (mSlideableView == null) {
            // This can happen if we're aborting motion during layout because everything now fits.
            mSlideOffset = 0;
            return;
        }
        final boolean isLayoutRtl = isLayoutRtlSupport();
        final LayoutParams lp = (LayoutParams) mSlideableView.getLayoutParams();

        int childWidth = mSlideableView.getWidth();
        final int newStart = isLayoutRtl ? getWidth() - newLeft - childWidth : newLeft;

        //此处是该viewgroup的paddingleft
        final int paddingStart = isLayoutRtl ? getPaddingRight() : getPaddingLeft();
        //此处是可滑动视图的leftmargin
        final int lpMargin = isLayoutRtl ? lp.rightMargin : lp.leftMargin;
        final int startBound = paddingStart + lpMargin;

        mSlideOffset = (float) (newStart - startBound) / mSlideRange;

        if (mParallaxBy != 0) {
            parallaxOtherViews(mSlideOffset);
        }

        if (lp.dimWhenOffset) {
            dimChildView(mSlideableView, mSlideOffset, mSliderFadeColor);
        }
        dispatchOnPanelSlide(mSlideableView);
    }

    /**
     * 该方法主要是使视图变暗，模糊
     * @param v
     * @param mag
     * @param fadeColor
     */
    private void dimChildView(View v, float mag, int fadeColor) {
        final LayoutParams lp = (LayoutParams) v.getLayoutParams();

        if (mag > 0 && fadeColor != 0) {
            final int baseAlpha = (fadeColor & 0xff000000) >>> 24;
            int imag = (int) (baseAlpha * mag);
            int color = imag << 24 | (fadeColor & 0xffffff);
            if (lp.dimPaint == null) {
                lp.dimPaint = new Paint();
            }
            lp.dimPaint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_OVER));
            if (ViewCompat.getLayerType(v) != ViewCompat.LAYER_TYPE_HARDWARE) {
                ViewCompat.setLayerType(v, ViewCompat.LAYER_TYPE_HARDWARE, lp.dimPaint);
            }
            invalidateChildRegion(v);
        } else if (ViewCompat.getLayerType(v) != ViewCompat.LAYER_TYPE_NONE) {
            if (lp.dimPaint != null) {
                lp.dimPaint.setColorFilter(null);
            }
            final DisableLayerRunnable dlr = new DisableLayerRunnable(v);
            mPostedRunnables.add(dlr);
            ViewCompat.postOnAnimation(this, dlr);
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        Log.d(TAG, "drawChild: ");
        //重写该方法的目的主要是为了性能上的优化，不绘制不可见的区域
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        boolean result;
        final int save = canvas.save(Canvas.CLIP_SAVE_FLAG);

        if (mCanSlide && !lp.slideable && mSlideableView != null) {
            //第一个子节点
            // Clip against the slider; no sense drawing what will immediately be covered.
            canvas.getClipBounds(mTmpRect);
            if (isLayoutRtlSupport()) {
                mTmpRect.left = Math.max(mTmpRect.left, mSlideableView.getRight());
            } else {
                mTmpRect.right = Math.min(mTmpRect.right, mSlideableView.getLeft());
            }
            canvas.clipRect(mTmpRect);
        }

        if (Build.VERSION.SDK_INT >= 11) { // HC
            result = super.drawChild(canvas, child, drawingTime);
        } else {
            if (lp.dimWhenOffset && mSlideOffset > 0) {
                //绘制模糊图层
                if (!child.isDrawingCacheEnabled()) {
                    child.setDrawingCacheEnabled(true);
                }
                final Bitmap cache = child.getDrawingCache();
                if (cache != null) {
                    canvas.drawBitmap(cache, child.getLeft(), child.getTop(), lp.dimPaint);
                    result = false;
                } else {
                    Log.e(TAG, "drawChild: child view " + child + " returned null drawing cache");
                    result = super.drawChild(canvas, child, drawingTime);
                }
            } else {
                //不需要绘制模糊图层
                if (child.isDrawingCacheEnabled()) {
                    child.setDrawingCacheEnabled(false);
                }
                result = super.drawChild(canvas, child, drawingTime);
            }
        }

        canvas.restoreToCount(save);

        return result;
    }

    private void invalidateChildRegion(View v) {
        IMPL.invalidateChildRegion(this, v);
    }

    /**
     * Smoothly animate mDraggingPane to the target X position within its range.
     *
     * @param slideOffset position to animate to
     * @param velocity initial velocity in case of fling, or 0.
     */
    boolean smoothSlideTo(float slideOffset, int velocity) {
        if (!mCanSlide) {
            // Nothing to do.
            return false;
        }

        final boolean isLayoutRtl = isLayoutRtlSupport();
        final LayoutParams lp = (LayoutParams) mSlideableView.getLayoutParams();

        int x;
        if (isLayoutRtl) {
            int startBound = getPaddingRight() + lp.rightMargin;
            int childWidth = mSlideableView.getWidth();
            x = (int) (getWidth() - (startBound + slideOffset * mSlideRange + childWidth));
        } else {
            int startBound = getPaddingLeft() + lp.leftMargin;
            x = (int) (startBound + slideOffset * mSlideRange);
        }

        if (mDragHelper.smoothSlideViewTo(mSlideableView, x, mSlideableView.getTop())) {
            setAllChildrenVisible();
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            if (!mCanSlide) {
                mDragHelper.abort();
                return;
            }

            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * @deprecated Renamed to {@link #setShadowDrawableLeft(Drawable d)} to support LTR (left to
     * right language) and {@link #setShadowDrawableRight(Drawable d)} to support RTL (right to left
     * language) during opening/closing.
     *
     * @param d drawable to use as a shadow
     */
    @Deprecated
    public void setShadowDrawable(Drawable d) {
        setShadowDrawableLeft(d);
    }

    /**
     * Set a drawable to use as a shadow cast by the right pane onto the left pane
     * during opening/closing.
     *
     * @param d drawable to use as a shadow
     */
    public void setShadowDrawableLeft(Drawable d) {
        mShadowDrawableLeft = d;
    }

    /**
     * Set a drawable to use as a shadow cast by the left pane onto the right pane
     * during opening/closing to support right to left language.
     *
     * @param d drawable to use as a shadow
     */
    public void setShadowDrawableRight(Drawable d) {
        mShadowDrawableRight = d;
    }

    /**
     * Set a drawable to use as a shadow cast by the right pane onto the left pane
     * during opening/closing.
     *
     * @param resId Resource ID of a drawable to use
     */
    @Deprecated
    public void setShadowResource(@DrawableRes int resId) {
        setShadowDrawable(getResources().getDrawable(resId));
    }

    /**
     * Set a drawable to use as a shadow cast by the right pane onto the left pane
     * during opening/closing.
     *
     * @param resId Resource ID of a drawable to use
     */
    public void setShadowResourceLeft(int resId) {
        setShadowDrawableLeft(getResources().getDrawable(resId));
    }

    /**
     * Set a drawable to use as a shadow cast by the left pane onto the right pane
     * during opening/closing to support right to left language.
     *
     * @param resId Resource ID of a drawable to use
     */
    public void setShadowResourceRight(int resId) {
        setShadowDrawableRight(getResources().getDrawable(resId));
    }

    /**
     * 布局完成之后才会调用该方法
     * @param c
     */
    @Override
    public void draw(Canvas c) {
        Log.d(TAG, "draw: ");
        //必须首先调用父方法
        super.draw(c);
        final boolean isLayoutRtl = isLayoutRtlSupport();
        Drawable shadowDrawable;
        if (isLayoutRtl) {
            shadowDrawable = mShadowDrawableRight;
        } else {
            shadowDrawable = mShadowDrawableLeft;
        }

        //获取第二个子节点视图，目的是获取坐标点绘制shadow
        final View shadowView = getChildCount() > 1 ? getChildAt(1) : null;
        if (shadowView == null || shadowDrawable == null) {
            // No need to draw a shadow if we don't have one.
            return;
        }

        final int top = shadowView.getTop();
        final int bottom = shadowView.getBottom();

        //获取shadowDrawable真实的宽度
        final int shadowWidth = shadowDrawable.getIntrinsicWidth();
        final int left;
        final int right;
        if (isLayoutRtlSupport()) {
            left = shadowView.getRight();
            right = left + shadowWidth;
        } else {
            //此处定位的是shadowDrawable应该绘制的坐标
            right = shadowView.getLeft();
            left = right - shadowWidth;
        }

        shadowDrawable.setBounds(left, top, right, bottom);
        shadowDrawable.draw(c);
    }

    private void parallaxOtherViews(float slideOffset) {
        final boolean isLayoutRtl = isLayoutRtlSupport();
        final LayoutParams slideLp = (LayoutParams) mSlideableView.getLayoutParams();
        final boolean dimViews = slideLp.dimWhenOffset &&
                (isLayoutRtl ? slideLp.rightMargin : slideLp.leftMargin) <= 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View v = getChildAt(i);
            if (v == mSlideableView) continue;

            final int oldOffset = (int) ((1 - mParallaxOffset) * mParallaxBy);
            mParallaxOffset = slideOffset;
            final int newOffset = (int) ((1 - slideOffset) * mParallaxBy);
            final int dx = oldOffset - newOffset;

            v.offsetLeftAndRight(isLayoutRtl ? -dx : dx);

            if (dimViews) {
                dimChildView(v, isLayoutRtl ? mParallaxOffset - 1 :
                        1 - mParallaxOffset, mCoveredFadeColor);
            }
        }
    }

    /**
     * Tests scrollability within child views of v given a delta of dx.
     *
     * @param v View to test for horizontal scrollability
     * @param checkV Whether the view v passed should itself be checked for scrollability (true),
     *               or just its children (false).
     * @param dx Delta scrolled in pixels
     * @param x X coordinate of the active touch point
     * @param y Y coordinate of the active touch point
     * @return true if child views of v can be scrolled by delta of dx.
     */
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance first.
            for (int i = count - 1; i >= 0; i--) {
                // TODO: Add versioned support here for transformed views.
                // This will not work for transformed views in Honeycomb+
                final View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() &&
                        y + scrollY >= child.getTop() && y + scrollY < child.getBottom() &&
                        canScroll(child, true, dx, x + scrollX - child.getLeft(),
                                y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }

        return checkV && ViewCompat.canScrollHorizontally(v, (isLayoutRtlSupport() ? dx : -dx));
    }

    boolean isDimmed(View child) {
        if (child == null) {
            return false;
        }
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        return mCanSlide && lp.dimWhenOffset && mSlideOffset > 0;
    }

    //生成默认的LayoutParams，所有直接子节点都会采用该种LayoutParam（自定义）
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof MarginLayoutParams
                ? new LayoutParams((MarginLayoutParams) p)
                : new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams && super.checkLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        ss.isOpen = isSlideable() ? isOpen() : mPreservedOpenState;

        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        if (ss.isOpen) {
            openPane();
        } else {
            closePane();
        }
        mPreservedOpenState = ss.isOpen;
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (mIsUnableToDrag) {
                return false;
            }
//如果是可拖动的视图则返回true
            return ((LayoutParams) child.getLayoutParams()).slideable;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
                if (mSlideOffset == 0) {
                    updateObscuredViewsVisibility(mSlideableView);
                    dispatchOnPanelClosed(mSlideableView);
                    mPreservedOpenState = false;
                } else {
                    dispatchOnPanelOpened(mSlideableView);
                    mPreservedOpenState = true;
                }
            }
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            // Make all child views visible in preparation for sliding things around
            setAllChildrenVisible();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            onPanelDragged(left);
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final LayoutParams lp = (LayoutParams) releasedChild.getLayoutParams();

            int left;
            if (isLayoutRtlSupport()) {
                int startToRight =  getPaddingRight() + lp.rightMargin;
                if (xvel < 0 || (xvel == 0 && mSlideOffset > 0.5f)) {
                    startToRight += mSlideRange;
                }
                int childWidth = mSlideableView.getWidth();
                left = getWidth() - startToRight - childWidth;
            } else {
                left = getPaddingLeft() + lp.leftMargin;
                if (xvel > 0 || (xvel == 0 && mSlideOffset > 0.5f)) {
                    left += mSlideRange;
                }
            }
            mDragHelper.settleCapturedViewAt(left, releasedChild.getTop());
            invalidate();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mSlideRange;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            final LayoutParams lp = (LayoutParams) mSlideableView.getLayoutParams();

            final int newLeft;
            if (isLayoutRtlSupport()) {
                int startBound = getWidth() -
                        (getPaddingRight() + lp.rightMargin + mSlideableView.getWidth());
                int endBound =  startBound - mSlideRange;
                newLeft = Math.max(Math.min(left, startBound), endBound);
            } else {
                int startBound = getPaddingLeft() + lp.leftMargin;
                int endBound = startBound + mSlideRange;
                newLeft = Math.min(Math.max(left, startBound), endBound);
            }
            return newLeft;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            // Make sure we never move views vertically.
            // This could happen if the child has less height than its parent.
            return child.getTop();
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mDragHelper.captureChildView(mSlideableView, pointerId);
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        private static final int[] ATTRS = new int[] {
                android.R.attr.layout_weight
        };

        /**
         * The weighted proportion of how much of the leftover space
         * this child should consume after measurement.
         */
        public float weight = 0;

        /**
         * True if this pane is the slideable pane in the layout.
         */
        boolean slideable;

        /**
         * True if this view should be drawn dimmed
         * when it's been offset from its default position.
         */
        boolean dimWhenOffset;

        Paint dimPaint;

        public LayoutParams() {
            super(FILL_PARENT, FILL_PARENT);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.weight = source.weight;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            final TypedArray a = c.obtainStyledAttributes(attrs, ATTRS);
            this.weight = a.getFloat(0, 0);
            a.recycle();
        }

    }

    static class SavedState extends BaseSavedState {
        boolean isOpen;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            isOpen = in.readInt() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isOpen ? 1 : 0);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    interface SlidingPanelLayoutImpl {
        void invalidateChildRegion(MySlidingPaneLayout parent, View child);
    }

    static class SlidingPanelLayoutImplBase implements SlidingPanelLayoutImpl {
        public void invalidateChildRegion(MySlidingPaneLayout parent, View child) {
            ViewCompat.postInvalidateOnAnimation(parent, child.getLeft(), child.getTop(),
                    child.getRight(), child.getBottom());
        }
    }

    static class SlidingPanelLayoutImplJB extends SlidingPanelLayoutImplBase {
        /*
         * Private API hacks! Nasty! Bad!
         *
         * In Jellybean, some optimizations in the hardware UI renderer
         * prevent a changed Paint on a View using a hardware layer from having
         * the intended effect. This twiddles some internal bits on the view to force
         * it to recreate the display list.
         */
        private Method mGetDisplayList;
        private Field mRecreateDisplayList;

        SlidingPanelLayoutImplJB() {
            try {
                mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[]) null);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "Couldn't fetch getDisplayList method; dimming won't work right.", e);
            }
            try {
                mRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList");
                mRecreateDisplayList.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", e);
            }
        }

        @Override
        public void invalidateChildRegion(MySlidingPaneLayout parent, View child) {
            if (mGetDisplayList != null && mRecreateDisplayList != null) {
                try {
                    mRecreateDisplayList.setBoolean(child, true);
                    mGetDisplayList.invoke(child, (Object[]) null);
                } catch (Exception e) {
                    Log.e(TAG, "Error refreshing display list state", e);
                }
            } else {
                // Slow path. REALLY slow path. Let's hope we don't get here.
                child.invalidate();
                return;
            }
            super.invalidateChildRegion(parent, child);
        }
    }

    static class SlidingPanelLayoutImplJBMR1 extends SlidingPanelLayoutImplBase {
        @Override
        public void invalidateChildRegion(MySlidingPaneLayout parent, View child) {
            ViewCompat.setLayerPaint(child, ((LayoutParams) child.getLayoutParams()).dimPaint);
        }
    }


    private class DisableLayerRunnable implements Runnable {
        final View mChildView;

        DisableLayerRunnable(View childView) {
            mChildView = childView;
        }

        @Override
        public void run() {
            if (mChildView.getParent() == MySlidingPaneLayout.this) {
                ViewCompat.setLayerType(mChildView, ViewCompat.LAYER_TYPE_NONE, null);
                invalidateChildRegion(mChildView);
            }
            mPostedRunnables.remove(this);
        }
    }

    private boolean isLayoutRtlSupport() {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }
}


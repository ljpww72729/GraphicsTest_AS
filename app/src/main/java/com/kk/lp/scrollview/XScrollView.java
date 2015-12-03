package com.kk.lp.scrollview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.kk.lp.R;
import com.kk.lp.utils.StringUtils;
import com.kk.lp.utils.UtilsLog;

import java.util.Date;

/**
 * Created by lipeng on 12-1.
 */
public class XScrollView extends ScrollView {

    private TextView mHeaderTimeView;
    private Context mContext;
    private float mLastY;
    //ScrollView下的子View
    private View mContentView;
    //headerView
    private XListViewHeader mHeaderView;
    private final static float OFFSET_RADIO = 3.0f; // support iOS like pull
    //是否允许下拉刷新
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    private int mHeaderViewHeight; // header view's height
    private Scroller mScroller;
    // the interface to trigger refresh and load more.
    private IXScrollViewListener mScrollViewListener;
    //页面加载完成后存储scrollview子节点的屏幕坐标
    int[] mContentViewOriginLocation = new int[2];
    private Date date = new Date();
    //松手后滑动的时间
    private final static int SCROLL_DURATION = 400; // scroll back duration

    public XScrollView(Context context) {
        this(context, null);
    }

    public XScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mScroller = new Scroller(mContext, new DecelerateInterpolator());
        mHeaderView = new XListViewHeader(mContext);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
        mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.xlistview_header_time);
        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mHeaderViewHeight = mHeaderViewContent.getHeight();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

    }

    /**
     * 设置是否允许下拉刷新
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //判断是否已经初始化页面加载完成后scrollview子节点的位置
        if (mContentViewOriginLocation[1] == 0) {
            mContentView.getLocationOnScreen(mContentViewOriginLocation);
            UtilsLog.v("log", "mContentViewOriginLocation====" + mContentViewOriginLocation[0] + "-----" + mContentViewOriginLocation[1]);
        }
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setRefreshTime(false);
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                int[] location = new int[2];
                mContentView.getLocationOnScreen(location);
                UtilsLog.v("log", "Originlocation=====" + mContentViewOriginLocation[1] + "+++++location====" + location[1]);
                //只有在scrollview子节点向下拉取的时候才更新头的高度
                if (location[1] - mContentViewOriginLocation[1] >= 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                }
                break;
            default:
                mLastY = -1; // reset
                // invoke refresh
                if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                    mPullRefreshing = true;
                    mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
                    if (mScrollViewListener != null) {
                        mScrollViewListener.onRefresh();
                    }
                }
                resetHeaderHeight();
                break;
        }
        if (mHeaderView.getVisiableHeight() > 0) {
            //如果header可见则屏蔽scrollview内部的滚动事件
            return true;
        }
        return super.onTouchEvent(ev);
    }


    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            UtilsLog.v("log", "mHeaderView.getVisiableHeight()====" + mHeaderView.getVisiableHeight() + "mHeaderViewHeight====" + mHeaderViewHeight);
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                mHeaderView.setState(XListViewHeader.STATE_READY);
//                UtilsLog.v("xlistview", "下拉高度超过头部本身高度时，松开刷新");
            } else {
                mHeaderView.setState(XListViewHeader.STATE_NORMAL);
            }
        }
    }

    /**
     * 重新布局，添加头部布局
     */
    private void show() {
        if (mContentView == null) {
            if (getChildCount() > 0) {
                mContentView = getChildAt(0);
            }
            LinearLayout ll = new LinearLayout(mContext);
            ll.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            ll.setOrientation(LinearLayout.VERTICAL);
            this.removeAllViews();
            ll.addView(mHeaderView);
            ll.addView(mContentView);
            addView(ll);
            invalidate();
        }
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        UtilsLog.v("log", "finalHeight - height====" + (finalHeight - height));
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    /**
     * 页面布局加载完后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        show();
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh() {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mHeaderView.setVisiableHeight(mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }


    /**
     * implements this interface to get refresh event.
     */
    public interface IXScrollViewListener {

        void onRefresh();

    }

    public void setXScrollViewListener(IXScrollViewListener l) {
        mScrollViewListener = l;
    }

    @Override
    public void setFadingEdgeLength(int length) {
        // TODO Auto-generated method stub
        super.setFadingEdgeLength(0);
    }

    @Override
    public void setOverScrollMode(int mode) {
        // TODO Auto-generated method stub
        super.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    /**
     * set last refresh time 设置显示的刷新时间
     *
     * @param isNowTime true刷新新的时间，flase刷新当前时间
     */
    public void setRefreshTime(boolean isNowTime) {
        if (isNowTime) {
            date = new Date();// 最近更新时间
        }
        String dateStr = StringUtils.getDateString(date);// 显示默认的刷新时间提示字符串
        if (!isNowTime) {
            mHeaderTimeView.setText(dateStr);
        }
    }

}

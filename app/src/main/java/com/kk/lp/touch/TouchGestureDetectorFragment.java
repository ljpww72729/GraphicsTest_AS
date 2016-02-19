package com.kk.lp.touch;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;

/**
 * 单击调用的顺序为：onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed
 *
 * 双击调用的顺序为：onDown->onSingleTapUp->onDoubleTap->onDoubleTapEvent->onDown->onDoubleTapEvent
 * 虽然是双击，但是id是相同的，坐标点不同
 *
 * Filing调用顺序为：onDown->onScroll(循环调用)->onFiling
 *
 * scroll调用顺序为：onDown->onShowPress->onScroll(循环调用)
 *
 * 长按的调用顺序为：onDown->onShowPress->onLongPress
 *
 * Created by lipeng on 1-13.
 */
public class TouchGestureDetectorFragment extends BaseFragment implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private static final String DEBUG_TAG = "Gesture";
    private GestureDetectorCompat mDetector;

    public static TouchGestureDetectorFragment newInstance() {
        return new TouchGestureDetectorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetector = new GestureDetectorCompat(getActivity(),this);
        mDetector.setOnDoubleTapListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_touch, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });
        return view;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG,"onDown: " + event.toString());
        //此处一定要返回true，不让随后的事件都不会被传递进来并处理
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        //这个事件只有确定为只有一个手指点击的并且在onSingleTapUp之后调用，具体可参考该方法的说明
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }
}

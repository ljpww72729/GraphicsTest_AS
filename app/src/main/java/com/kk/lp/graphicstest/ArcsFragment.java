package com.kk.lp.graphicstest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kk.lp.BaseFragment;

/**
 *
 * @Description: 
 * @author lipeng
 * @version 2015-5-26
 * 
 */

public class ArcsFragment extends BaseFragment{
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = new SampleView(getActivity());
		return view;
	}
	private static class SampleView extends View {
        private Paint[] mPaints;
        private Paint mFramePaint;
        private boolean[] mUseCenters;
        private RectF[] mOvals;
        private RectF[] mOvalsIn;
        private RectF mBigOval;
        private float mStart;
        private float mSweep;
        private int mBigIndex;
		private RectF mBigOvalIn;
		private Paint paintIn;

        private static final float SWEEP_INC = 1;
        private static final float START_INC = 15;

        public SampleView(Context context) {
            super(context);

            mPaints = new Paint[4];
            mUseCenters = new boolean[4];
            mOvals = new RectF[4];
            mOvalsIn = new RectF[4];

            mPaints[0] = new Paint();
            mPaints[0].setAntiAlias(true);
            mPaints[0].setStyle(Paint.Style.FILL);
            mPaints[0].setColor(0x88FF0000);
            mUseCenters[0] = false;

            paintIn = new Paint(mPaints[0]);
            paintIn.setColor(0xff000000);
            
            mPaints[1] = new Paint(mPaints[0]);
            mPaints[1].setColor(0x8800FF00);
            mUseCenters[1] = true;

            mPaints[2] = new Paint(mPaints[0]);
            mPaints[2].setStyle(Paint.Style.STROKE);
            mPaints[2].setStrokeWidth(4);
            mPaints[2].setColor(0x880000FF);
            mUseCenters[2] = false;

            mPaints[3] = new Paint(mPaints[2]);
            mPaints[3].setColor(0x88888888);
            mUseCenters[3] = true;

            mBigOval = new RectF(40, 10, 280, 250);
            mBigOvalIn = new RectF(50, 20, 270, 240);

            mOvals[0] = new RectF( 10, 270,  70, 330);
            mOvals[1] = new RectF( 90, 270, 150, 330);
            mOvals[2] = new RectF(170, 270, 230, 330);
            mOvals[3] = new RectF(250, 270, 310, 330);
            
            mOvalsIn[0] = new RectF( 20, 280,  60, 320);
            mOvalsIn[1] = new RectF( 100, 280, 140, 320);
            mOvalsIn[2] = new RectF(180, 280, 220, 320);
            mOvalsIn[3] = new RectF(260, 280, 300, 320);
            
            
            mFramePaint = new Paint();
            mFramePaint.setAntiAlias(true);
            mFramePaint.setStyle(Paint.Style.STROKE);
            mFramePaint.setStrokeWidth(0);
        }

        private void drawArcs(Canvas canvas, RectF ovalIn,RectF oval, boolean useCenter,
                              Paint paint) {
            canvas.drawRect(oval, mFramePaint);
            canvas.drawArc(oval, mStart, mSweep, useCenter, paint);
            canvas.drawArc(ovalIn, 0, 360, true, paintIn);
        }

        @Override protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            drawArcs(canvas, mBigOvalIn, mBigOval, mUseCenters[mBigIndex],
                     mPaints[mBigIndex]);

            for (int i = 0; i < 4; i++) {
                drawArcs(canvas,mOvalsIn[i], mOvals[i], mUseCenters[i], mPaints[i]);
            }

            mSweep += SWEEP_INC;
            if(mSweep <= 360){
            	invalidate();
            }
//            if (mSweep > 360) {
//                mSweep -= 360;
//                mStart += START_INC;
//                if (mStart >= 360) {
//                    mStart -= 360;
//                }
//                mBigIndex = (mBigIndex + 1) % mOvals.length;
//            }
//            if(mSweep<=360){
//            	invalidate();
//            }
        }
    }
}

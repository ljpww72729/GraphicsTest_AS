package com.kk.lp.graphicstest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import com.kk.lp.R;

/**
 *
 * @Description: 
 * @author lipeng
 * @version 2015-6-16
 * 
 */

public class RadiusButtonViewFragment extends Fragment {
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 60);
		View view = new RadiusButtonView(getActivity());
		view.setLayoutParams(lp);
		return view;
	}
	
	public class RadiusButtonView extends View {

		private Paint paint;
		private RectF rectCenter;
		//按钮的颜色
		private int btnColor;
		//半径
		int radius; 
		
		public RadiusButtonView(Context context) {
			this(context, null);
		}

		public RadiusButtonView(Context context, AttributeSet attrs) {
			this(context, attrs,0);
		}
		
		public RadiusButtonView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			TypedArray tv = context.getTheme().obtainStyledAttributes(attrs, R.styleable.mStyleable, defStyle, 0);
			btnColor = tv.getColor(R.styleable.mStyleable_raidus_color, Color.RED);
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(btnColor);
		}
		
		
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			initRect();
			canvas.drawRoundRect(rectCenter, radius, radius, paint);
			super.onDraw(canvas);
		}
		
		
		/** 
		* @Description:初始化矩形框
		* @author lipeng 
		* @return void 
		*/
		private void initRect(){
			int height = getMeasuredHeight();
			int width = getMeasuredWidth();
			//半径
			radius = height / 2; 
			//中间矩形
			rectCenter = new RectF(0, 0, width, height);
		}
	}

}

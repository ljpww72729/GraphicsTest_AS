package com.kk.lp.graphicstest;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.kk.lp.R;

/**
 *
 * @Description: 
 * @author lipeng
 * @version 2015-6-16
 * 
 */

public class ProgressViewFragment extends Fragment {
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		ImageView view = new ProgressView(getActivity());
		ImageView view = new ImageView(getActivity());
		view.setImageDrawable(getResources().getDrawable(R.drawable.loadi_03));
		view.setScaleType(ScaleType.CENTER);
		return view;
	}
	
	public class ProgressView extends ImageView {

		public ProgressView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init(context);
		}

		public ProgressView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init(context);
		}

		public ProgressView(Context context) {
			super(context);
			init(context);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			// TODO Auto-generated method stub
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//			setMeasuredDimension(80, 80);
		}
		
		
		public void init(Context context){
			setImageDrawable(context.getResources().getDrawable(R.drawable.loadi_03));
			setScaleType(ScaleType.CENTER);
			Animation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setDuration(1600);
			animation.setRepeatCount(Animation.INFINITE);
			animation.setInterpolator(new LinearInterpolator());
			this.startAnimation(animation);
		}
		
	}

}

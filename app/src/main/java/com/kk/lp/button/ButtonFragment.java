package com.kk.lp.button;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;

/**
 *
 * @Description 
 * @author lipeng
 * @version 2015-7-10
 * 
 */

public class ButtonFragment extends BaseFragment {

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ImageButton view = new CustomButton(this.getActivity());
		view.setLayoutParams(lp);
		view.setBackgroundResource(R.drawable.ic_launcher);
		return view;
	}
	
	public class CustomButton extends ImageButton{
		
		public CustomButton(Context context) {
			super(context);
		}

		public CustomButton(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
		
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				CustomButton.this.setColorFilter(getResources().getColor(0X50000000), null);
				break;

			default:
				getBackground().clearColorFilter();
				break;
			}
			return super.onTouchEvent(event);
		}
		
		
	}
	
}

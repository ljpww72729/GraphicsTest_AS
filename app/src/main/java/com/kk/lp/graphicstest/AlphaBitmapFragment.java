package com.kk.lp.graphicstest;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
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

public class AlphaBitmapFragment extends BaseFragment {

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	return super.onCreateView(inflater, container, savedInstanceState);
}
	private static class simpleView extends View{

		private Bitmap mBitmap1;
		private Bitmap mBitmap2;
		private Bitmap mBitmap3;
		
		public simpleView(Context context) {
			this(context, null);
			// TODO Auto-generated constructor stub
		}

		public simpleView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
			
		}
		
	}
}

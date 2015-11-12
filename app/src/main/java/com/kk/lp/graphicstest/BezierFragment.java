package com.kk.lp.graphicstest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @Description:
 * @author lipeng
 * @version 2015-6-16
 * 
 */

public class BezierFragment extends Fragment {
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = new SampleView(getActivity());
		return view;
	}

	private static class SampleView extends View {

		Paint paintB;
		private float startX, startY, controlX, controlY, endX, endY;
		private float startXX, startYY, controlXX, controlYY, endXX, endYY;
		Path path;

		public SampleView(Context context) {
			super(context);
			paintB = new Paint();
			path = new Path();
			paintB.setStyle(Style.FILL_AND_STROKE);
			paintB.setStrokeWidth(4);
			paintB.setAntiAlias(true);
			startX = 0;
			startY = 10;
//			controlX = 20;
//			controlY = 10;
			endX = 130;
			endY = 150;
			startXX = 150;
			startYY = 130;
			endXX = 10;
			endYY = 0;
			float centerX = Math.abs(endXX + startX)/2;
			float centerY = Math.abs(endYY + startY)/2;
			float centerXX = Math.abs(startXX + endX)/2;
			float centerYY = Math.abs(startYY + endY)/2;
			controlX = Math.abs(centerXX + centerX)/2 + 5;
			controlY = Math.abs(centerYY + centerY)/2 - 5;
			controlXX = Math.abs(centerXX + centerX)/2 - 5;
			controlYY = Math.abs(centerYY + centerY)/2 + 5;
			System.out.println("controlX==="+ controlX +"controlY ==" + controlY);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawColor(Color.WHITE);
			path.reset();
			path.moveTo(startX, startY);
			path.quadTo(controlX, controlY, endX, endY);
			path.lineTo(startXX, startYY);
			path.quadTo(controlXX, controlYY, endXX, endYY);
			path.close(); 
			canvas.drawPath(path, paintB);
			invalidate();
		}
	}
}

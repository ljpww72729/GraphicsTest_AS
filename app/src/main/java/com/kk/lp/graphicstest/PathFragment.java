package com.kk.lp.graphicstest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
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

public class PathFragment extends BaseFragment{
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = new SampleView(getActivity());
		return view;
	}
	
	private static class SampleView extends View {

		Paint paint;
		Path path;
		int [] colors;
		PathEffect[] effect = new PathEffect[7];
		float phase;
		
        public SampleView(Context context) {
            super(context);
            paint = new Paint();
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(4);
            path = new Path();
            path.moveTo(0, 0);
            for(int i = 0; i < 15;i ++){
            	path.lineTo(i * 20, (float) Math.random() * 60);
            }
            colors = new int[]{Color.BLACK, Color.RED, Color.BLUE, Color.GRAY, Color.MAGENTA, Color.YELLOW, Color.DKGRAY};
        }

        @Override protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            effect[0] = null;
            effect[1] = new CornerPathEffect(10);
            effect[2] = new DiscretePathEffect(10, 10);
            effect[3] = new DashPathEffect(new float[]{30,1,15,5}, phase);
            Path p = new Path();
            p.addRect(0,0,8,8,Path.Direction.CCW);
            effect[4] = new PathDashPathEffect(p, 24, -phase, android.graphics.PathDashPathEffect.Style.MORPH);
            effect[5] = new ComposePathEffect(effect[4],effect[3]);
            effect[6] = new SumPathEffect( effect[3], effect[4]);
            canvas.translate(8, 8);
            for(int i = 0;i < effect.length; i ++){
            	 paint.setPathEffect(effect[i]);
            	 paint.setColor(colors[i]);
            	 canvas.drawPath(path, paint);
            	 canvas.translate(0, 60);
            }
           phase +=1;
           invalidate();
        }
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
        	// TODO Auto-generated method stub
        	System.out.println("keydown--------" + keyCode);
        	if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getRepeatCount() == 0){
        		return true;
        	}else{
        		return false;
        	}
        }
        @Override
        public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        	// TODO Auto-generated method stub
        	System.out.println("key long press is active!");
        	return super.onKeyLongPress(keyCode, event);
        }
    }
	
}

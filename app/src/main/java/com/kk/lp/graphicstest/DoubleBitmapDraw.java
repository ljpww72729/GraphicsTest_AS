package com.kk.lp.graphicstest;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.kk.lp.BaseFragment;

/**
 * 
 * @Description:
 * @author lipeng
 * @version 2015-6-1
 * 
 */

public class DoubleBitmapDraw extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LinearLayout ll = new LinearLayout(getActivity());
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER_HORIZONTAL);
		LinearLayout.LayoutParams btnlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		Button btn = new Button(getActivity());
		btn.setText("保存绘画");
		final SimpleView simpleView = new SimpleView(getActivity());
		ll.addView(btn, btnlp);
		ll.addView(simpleView);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bitmap bitmap = simpleView.getCacheBitmap();
				File file = new File(Environment.getExternalStorageDirectory(), "abc.png");
				if(file != null){
					if(file.exists())
						file.delete();
				}
				
				FileOutputStream fos;
				try {
					file.createNewFile();
					fos = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//					fos.flush();
					fos.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				MediaScannerConnection.scanFile(getActivity(), new String[]{file.toString()}, null, new OnScanCompletedListener() {
					
					@Override
					public void onScanCompleted(String path, Uri uri) {
						// TODO Auto-generated method stub
					}
				});
				Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
			}
		});
		return ll;
	}

	private static class SimpleView extends View {

		float preX,preY;
		Bitmap cacheBitmap = null;
		Canvas cacheCanvas = null;
		Paint paint = null;
		Path path = null;
		
		public SimpleView(Context context) {
			this(context, null);
			// TODO Auto-generated constructor stub
		}

		public SimpleView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
			cacheBitmap = Bitmap.createBitmap(320, 480, Config.ARGB_8888);
			cacheCanvas = new Canvas();
//			cacheCanvas.drawColor(Color.TRANSPARENT);
//			cacheBitmap.eraseColor(Color.argb(0,0,0,0));
			cacheCanvas.setBitmap(cacheBitmap);
			paint = new Paint(Paint.DITHER_FLAG);
			paint.setColor(Color.RED);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(2);
			paint.setAntiAlias(true);
			paint.setDither(true);
			path = new Path();
		}
		
		public Bitmap getCacheBitmap() {
			return cacheBitmap;
		}

		public void setCacheBitmap(Bitmap cacheBitmap) {
			this.cacheBitmap = cacheBitmap;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			float x = event.getX();
			float y = event.getY();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				path.moveTo(x, y);
				preX = x;
				preY = y;
				break;
			case MotionEvent.ACTION_MOVE:
				path.quadTo(preX, preY, x, y);
				preX = x;
				preY = y;
				break;
			case MotionEvent.ACTION_UP:
				cacheCanvas.drawPath(path, paint);
				path.reset();
				break;
			default:
				break;
			}
			invalidate();
			return true;
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			canvas.saveLayer(0, 0, 320, 480, paint, Canvas.ALL_SAVE_FLAG);
			Paint mPaint = new Paint();
			canvas.drawBitmap(cacheBitmap, 0, 0, mPaint);
			canvas.drawPath(path, paint);
			canvas.restore();
			super.onDraw(canvas);
			
		}
	}
}

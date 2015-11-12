package com.kk.lp.graphicstest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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

public class LayerFragment extends BaseFragment implements Parcelable{

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	View view = new SampleView(getActivity());
	return view;
}
private static class SampleView extends View {
    private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG |
                                        Canvas.CLIP_SAVE_FLAG |
                                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                                        Canvas.CLIP_TO_LAYER_SAVE_FLAG;

    private Paint mPaint;

    public SampleView(Context context) {
        super(context);
        setFocusable(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
//        mPaint.setColor(Color.RED);
//        canvas.drawCircle(75, 75, 75, mPaint);	
        //移动就是移动了，它不会自动复位，除了save跟restore
//        mPaint.setColor(Color.BLUE);
//        canvas.drawCircle(75, 75, 75, mPaint);
//        canvas.saveLayerAlpha(0, 0, 200, 200, 0x22, LAYER_FLAGS);
//        //类似于将此部分画布截取下来用来绘画，在restore之前的绘画均会绘制在此截取的画布上
//
//        mPaint.setColor(Color.RED);
//        canvas.drawCircle(20, 20, 75, mPaint);
//        canvas.drawCircle(75, 75, 75, mPaint);
//        mPaint.setColor(Color.BLUE);
//        canvas.drawCircle(125, 125, 75, mPaint);
//        canvas.drawCircle(200, 200, 75, mPaint);
//        canvas.restore();//只会将save与restore之间的画板恢复为原先的状态（translate复位）
        //里面的绘画也会跟着画板改变
//        canvas.translate(-10, -10);
//        canvas.save();
//        mPaint.setColor(Color.BLACK);
//        canvas.drawCircle(80, 80, 75, mPaint);
//        canvas.restore();
//        Shader shader = new LinearGradient(0, 0, 75, 75, new int[]{Color.RED, Color.BLUE, Color.YELLOW}, null, TileMode.MIRROR);
//       mPaint.setShader(shader);
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(Color.YELLOW);
       mPaint.setShadowLayer(180, 100, 100, Color.BLACK);
       canvas.drawCircle(75, 75, 75, mPaint);
        
    }
}
@Override
public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
}
@Override
public void writeToParcel(Parcel dest, int flags) {
	// TODO Auto-generated method stub
	
}
}

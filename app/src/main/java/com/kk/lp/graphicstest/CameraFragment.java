package com.kk.lp.graphicstest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.kk.lp.BaseFragment;

/**
 * 
 * @Description:
 * @author lipeng
 * @version 2015-6-24
 * 
 */

public class CameraFragment extends BaseFragment {
	private static final int REQUEST_IMAGE_CAPTURE = 0;
	String imageFilePath = null;
	ImageView img = null;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LinearLayout ll = new LinearLayout(getActivity());
		ll.setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp_btn = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		Button btn = new Button(getActivity());
		btn.setText("拍照");
		btn.setLayoutParams(lp_btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				takePicture();
			}
		});
		LayoutParams lp_img = new LayoutParams(600, 800);
		img = new ImageView(getActivity());
		img.setScaleType(ScaleType.CENTER_INSIDE);
		img.setLayoutParams(lp_img);
		ll.addView(btn);
		ll.addView(img);
		return ll;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_IMAGE_CAPTURE:
			if (resultCode == Activity.RESULT_OK) {
				handleSetImage();
			}
			break;

		default:
			break;
		}
	}

	private void galleryAddPic(String imagePath) {
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(imagePath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.getActivity().sendBroadcast(mediaScanIntent);
	}

	// 设置图片
	private void handleSetImage() {
		// TODO Auto-generated method stub
		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = img.getWidth();
		int targetH = img.getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imageFilePath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		targetW = photoW / 2;
		targetH = photoH / 2;
		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, bmOptions);
		String imageFilePathSmall = imageFilePath.replace(".", "_smal.");
		File file = new File(imageFilePathSmall);
//		Bitmap srcBitmap = BitmapFactory.decodeFile(imageFilePath);
		bitmap.getWidth();
		bitmap.getHeight();
		//将图片压缩到指定的宽度与高度
		Bitmap compressBitmap = zoomImage(bitmap, targetW, targetH);

		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			//压缩图片的质量，并不会改变图片的像素
			compressBitmap.compress(CompressFormat.JPEG, 60, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		galleryAddPic(imageFilePathSmall);
		/* Associate the Bitmap to the ImageView */
		img.setImageBitmap(bitmap);
	}
	
	/***
     * 图片的缩放方法
     * 
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                    double newHeight) {
            // 获取这个图片的宽和高
            float width = bgimage.getWidth();
            float height = bgimage.getHeight();
            // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
            // 计算宽高缩放率
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 缩放图片动作
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                            (int) height, matrix, true);
            return bitmap;
    }

	/**
	 * @Description 拍摄一张照片
	 * @author lipeng
	 * @return void
	 */
	private void takePicture() {
		File file = null;
		try {
			file = setUpPhotoFile();
			imageFilePath = file.getAbsolutePath();
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
			Math.random();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			file = null;
			imageFilePath = null;
		}
	}

	/**
	 * @Description 获取图片存储路径
	 * @author lipeng
	 * @return void
	 * @throws IOException
	 */
	private File setUpPhotoFile() throws IOException {
		// TODO Auto-generated method stub
		File file = createImageFile();
		imageFilePath = file.getAbsolutePath();
		return file;
	}

	/**
	 * @Description 创建图片存储路径
	 * @author lipeng
	 * @return void
	 * @throws IOException
	 */
	private File createImageFile() throws IOException {
		File photoFile = null;
		String fileName = "20150727002.jpg";
		File photoDir = getPhotoDir();
		if (photoDir != null) {
			String filePath = getPhotoDir().getAbsolutePath() + File.separator + fileName;
			photoFile = new File(filePath);
			photoFile.createNewFile();
		}
		return photoFile;
	}

	private File getPhotoDir() {
		File photoDir = null;
		String photoCacheDir = "dzjz_cache";
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			photoDir = new File(Environment.getExternalStorageDirectory() + File.separator + photoCacheDir);
			if (photoDir != null) {
				if (!photoDir.mkdirs()) {
					if (!photoDir.exists()) {
						// 创建失败
					}
				}
			}
		} else {
			// 未挂载SD卡
		}
		return photoDir;
	}

}

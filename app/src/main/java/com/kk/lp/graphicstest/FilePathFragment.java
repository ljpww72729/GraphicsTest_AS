package com.kk.lp.graphicstest;

import java.io.File;
import java.io.RandomAccessFile;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kk.lp.BaseFragment;

/**
 *
 * @Description 
 * @author lipeng
 * @version 2015-8-19
 * 
 */

public class FilePathFragment extends BaseFragment {
@Override
@Nullable
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	TextView tv = new TextView(getActivity());
	tv.setText(Environment.getExternalStorageDirectory().getAbsolutePath());
	WriteTxtFile("你好，我是李朋", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "file_test_lp" + File.separator +"abc.txt");
	return tv;
}
public synchronized static void WriteTxtFile(String strcontent, String strFilePath) {
	// 每次写入时，都换行写
	String strContent = strcontent + "\n";
	try {
		String Dir = strFilePath.substring(0, strFilePath.lastIndexOf(File.separator));
		File fileDir = new File(Dir);
		if(!fileDir.exists()){
			fileDir.mkdirs();
		}
		File file = new File(strFilePath);
		if (!file.exists()) {
			Log.d("TestFile", "Create the file:" + strFilePath);
			file.createNewFile();
		}
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		raf.seek(file.length());
		raf.write(strContent.getBytes());
		raf.close();
	} catch (Exception e) {
		Log.e("TestFile", "Error on write File.");
	}
}
}

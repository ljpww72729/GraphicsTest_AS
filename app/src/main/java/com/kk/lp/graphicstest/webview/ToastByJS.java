package com.kk.lp.graphicstest.webview;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * 
 * @Description:
 * @author lipeng
 * @version 2015-6-25
 * 
 */

public class ToastByJS {
	private AtomicInteger ato = new AtomicInteger(1);
	Context context;

	public ToastByJS(Context context) {
		this.context = context;
	}

	// If you've set your targetSdkVersion to 17 or higher, you must add the
	// @JavascriptInterface annotation to any method that you want available to
	// your JavaScript (the method must also be public). If you do not provide
	// the annotation, the method is not accessible by your web page when
	// running on Android 4.2 or higher
	@JavascriptInterface
	public void showToast(String toast) {
		Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
	}

}

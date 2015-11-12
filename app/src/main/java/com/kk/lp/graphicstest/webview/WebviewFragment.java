package com.kk.lp.graphicstest.webview;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.kk.lp.R;

/**
 * 
 * @Description:
 * @author lipeng
 * @version 2015-6-25
 * 
 */

public class WebviewFragment extends Fragment {

	private WebView wv;
	private Button btn;
	private int scrollX = 0;
	private int scrollY = 0;
	private String data = "";
	int index = 0;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_webview, null);
		btn = (Button) view.findViewById(R.id.refresh);
		wv = (WebView) view.findViewById(R.id.webview);
		WebSettings webSettings = wv.getSettings();
		//允许javascript
		webSettings.setJavaScriptEnabled(true);
		initView();
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wv.loadUrl("javascript:showAndroidJS('hello')");
				scrollX = wv.getScrollX();
				scrollY = wv.getScrollY();
				if(index % 3 == 0){
					data = data.replace("key1", "http://img1.imgtn.bdimg.com/it/u=3176667768,1161431363&fm=21&gp=0.jpg");
				}
				if(index % 3 == 1){
					data = data.replace("key2", "http://img.xiaba.cvimage.cn/4cbc56c1a57e26873c140000.jpg");
				}
				if(index % 3 == 2){
					data = data.replace("key3", "http://pica.nipic.com/2007-11-09/2007119124413448_2.jpg");
				}
				index ++;
//				wv.loadData(data, "text/html", "GBK");
			}
		});
		try {
			InputStream is = getActivity().getAssets().open("test.html");
			BufferedInputStream bs = new BufferedInputStream(is);
			// 把字节流转换为字符流
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();
			String buffer = null;
			while ((buffer = br.readLine()) != null) {
				sb.append(buffer);
			}
			data = sb.toString();
//			wv.loadData(data, "text/html", "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wv.addJavascriptInterface(new ToastByJS(getActivity()), "Android");
		wv.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
		wv.loadUrl("file:///android_asset/test.html");
		wv.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				// wv.setScrollY(scrollY);
				super.onPageFinished(view, url);
				if (scrollY > 0 || scrollX > 0) {
					wv.postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							wv.scrollTo(scrollX, scrollY);
						}
					}, 100);
				}
				//获取页面中元素的相关信息
//				 view.loadUrl("javascript:window.local_obj.showSource(document.getElementById('valuetest').innerHTML);");
				//跟直接调用本地方法没有什么区别
//				 view.loadUrl("javascript:Android.showToast('hello');");
				 //注入js代码并立即执行
				 view.loadUrl("javascript:(function() { " +  
			                "document.body.style.background = 'red'; " +  
			                "})()");
				 //注入js代码并立即执行
				 view.loadUrl("javascript:(function() { " +  
						 	" var value = document.getElementById('valuetest').innerHTML;" +
						 	"alert(value);" + 
			                "Android.showToast(value);" +  
			                "})()");
			}
			
		});

		wv.setWebChromeClient(new WebChromeClient() {

		});
	}
	 final class InJavaScriptLocalObj {
		 @JavascriptInterface
	        public void showSource(String html) {
	            System.out.println("HTML=====" + html);
	            Toast.makeText(WebviewFragment.this.getActivity(), html, Toast.LENGTH_LONG).show();
	        }
	    }
}

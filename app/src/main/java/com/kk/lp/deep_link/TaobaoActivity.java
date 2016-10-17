package com.kk.lp.deep_link;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kk.lp.R;
import com.kk.lp.facebookchathead.ServiceChatHead;

public class TaobaoActivity extends AppCompatActivity {

    private static final String TAG = "TaobaoActivity";
    private static final int MY_PERMISSIONS_REQUEST_SYSTEM_ALTER_WINDOW = 1222;
    private WebView webview_taobao;
    private String loadUrl = "http://e22a.com/h.ZBmy0P?cv=AAIqFuXx&sm=000ee5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taobao);
        webview_taobao = (WebView) findViewById(R.id.webview_taobao);
        webview_taobao.getSettings().setJavaScriptEnabled(true);
        webview_taobao.loadUrl(loadUrl);
        webview_taobao.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading() called with: " + "view = [" + view + "], url = [" + url + "]");
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, "onPageStarted() called with: " + "view = [" + view + "], url = [" + url + "], favicon = [" + favicon + "]");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished() called with: " + "view = [" + view + "], url = [" + url + "]");
//                super.onPageFinished(view, url);
                Intent intent = new Intent("android.intent.action.VIEW",
                        android.net.Uri.parse(url));
//                intent.setPackage("com.microquation.linkedmel");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                showOverlayWindowViewToast();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.d(TAG, "onReceivedError() called with: " + "view = [" + view + "], request = [" + request + "], error = [" + error + "]");
                super.onReceivedError(view, request, error);
            }
        });
    }

    /**
     * 不需要请求权限
     */
    public void showOverlayWindowViewToast() {
        startService(new Intent(this, ServiceChatHead.class));
    }

    /**
     * 需要请求权限
     */
    public void showOverlayWindowView() {
        //判断是否有绘制悬浮窗口的权限：SYSTEM_ALERT_WINDOW，针对版本23之后
        //参考：http://developer.android.com/reference/android/Manifest.permission.html#SYSTEM_ALERT_WINDOW
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                //如果有该权限，则开启悬浮窗口
                startService(new Intent(this, ServiceChatHead.class));
            } else {
                //如果没有，则需要开启权限授权activity，让用户手动打开绘制悬浮窗口的权限
                //参考：http://developer.android.com/reference/android/provider/Settings.html#ACTION_MANAGE_OVERLAY_PERMISSION
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        } else {
            //有一些系统会使用内置软件禁止悬浮窗口的展示，需要手动打开
            //版本<23
            //判断是否有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.SYSTEM_ALERT_WINDOW)
                    != PackageManager.PERMISSION_GRANTED) {
                //没有权限

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.SYSTEM_ALERT_WINDOW)) {


                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Snackbar.make(webview_taobao, "请授予绘制悬浮窗口的权限",
                            Snackbar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(TaobaoActivity.this,
                                    new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
                                    MY_PERMISSIONS_REQUEST_SYSTEM_ALTER_WINDOW);
                        }
                    }).show();
                } else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
                            MY_PERMISSIONS_REQUEST_SYSTEM_ALTER_WINDOW);
                }
            } else {
                startService(new Intent(this, ServiceChatHead.class));
            }
        }
    }
}

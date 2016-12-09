package com.kk.lp.deep_link;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.kk.lp.BaseActivity;
import com.kk.lp.R;

import java.net.URISyntaxException;

public class StartOtherAppActivity extends BaseActivity {

    private Button start_other_app, start_other_app_intent, check_other_app, start_taobao, start_deeplinks;
    private WebView start_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent resolveIntent = StartOtherAppActivity.this.getPackageManager().getLaunchIntentForPackage("com.microquation.linkedme");// 这里的packname就是从上面得到的目标apk的包名
//        // 启动目标应用
//        if (resolveIntent != null){
//            StartOtherAppActivity.this.startActivity(resolveIntent);
//        }
//        //防止创建多个实例
//        finish();
        setContentView(R.layout.activity_start_other_app);
        start_other_app = (Button) findViewById(R.id.start_other_app);
        start_other_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resolveIntent = StartOtherAppActivity.this.getPackageManager().getLaunchIntentForPackage("cc.lkme.uexLinkedME");// 这里的packname就是从上面得到的目标apk的包名
                // 启动目标应用
                if (resolveIntent != null) {
                    StartOtherAppActivity.this.startActivity(resolveIntent);
                }
            }
        });
        check_other_app = (Button) findViewById(R.id.check_other_app);
        check_other_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    Intent intent = new Intent("android.intent.action.VIEW",
//                            android.net.Uri.parse("linkedmedemoddddd://linkedme?click_id=G4LCXAjn7"));
//                    startActivity(intent);
//                } catch (ActivityNotFoundException e) {
//                    Toast.makeText(StartOtherAppActivity.this, "没有安装应用", Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
                isPkgInstalled("com.microquation.linkedme");
                Toast.makeText(StartOtherAppActivity.this, isPkgInstalled("com.microquation.linkedme") + "", Toast.LENGTH_LONG).show();

            }
        });
        start_other_app_intent = (Button) findViewById(R.id.start_other_app_intent);
        start_other_app_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW",
                        android.net.Uri.parse("lkmedemo://?click_id=G4LCXAjn7"));
//                intent.setPackage("com.microquation.linkedmel");
                startActivity(intent);
            }
        });
        start_taobao = (Button) findViewById(R.id.start_taobao);
        start_taobao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartOtherAppActivity.this, TaobaoActivity.class);
                startActivity(intent);
            }
        });
        start_deeplinks = (Button) findViewById(R.id.start_deeplinks);
        start_deeplinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://lkme.cc/AfC/CeG9o5VH8";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        start_webview = (WebView) findViewById(R.id.start_webview);
        WebSettings webSettings = start_webview.getSettings();
        //允许javascript
        webSettings.setJavaScriptEnabled(true);
//        start_webview.loadUrl("https://lkme.cc/IfC/yGs2hfPK8");
        start_webview.loadUrl("http://linkedme.cc:9099/browser/test.jsp");
//        start_webview.loadUrl("http://www.whatsmyua.com");
//        start_webview.loadUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.ctoutiao&android_scheme=ctoutiao://?click_id=yGs2hfPK8");
        start_webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //去掉回车、换行、tab
                String stray_spacing = "[\n\r\t\\p{Zl}\\p{Zp}\u0085]+";
                url = url.trim();
                url = url.replaceAll(stray_spacing, "");
                System.out.println("url ===== " + url);
                String rfc2396regex = "^(([a-zA-Z][a-zA-Z0-9\\+\\-\\.]*)://)(([^/?#]*)?([^?#]*)(\\?([^#]*))?)?(#(.*))?";
                String http_scheme_slashes = "^(https?://)/+(.*)";
                //(?i)后面的匹配不区分大小写
                String all_schemes_pattern = "(?i)^(http|https|ftp|mms|rtsp|wais)://.*";
                if (url.matches(all_schemes_pattern)){
                    view.loadUrl(url);
                    return false;
                }
                if (url.matches(rfc2396regex)){
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                        if (intent != null) {
                          //  view.stopLoading();

                            PackageManager packageManager = getPackageManager();
                            ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            if (info != null) {
                                //如果有可接受该intent的APP则直接唤起APP
                                startActivity(intent);
                            } else {
                                //否则加载回调页面
                                String fallbackUrl = intent.getStringExtra("browser_fallback_url");

                                if (!TextUtils.isEmpty(fallbackUrl)) {
                                    // 调用外置浏览器加载回调页面,建议外置浏览器加载回调页面
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
                                    startActivity(browserIntent);

                                    //或者:在WebView中加载
//                                  view.loadUrl(fallbackUrl);
                                }else{
                                   // view.loadUrl(url);
                                    return super.shouldOverrideUrlLoading(view, url);
                                }
                            }

                            return true;
                        }
                    } catch (URISyntaxException e) {
                        //对uri语法异常的处理
                        e.printStackTrace();
                    }
                }
                view.loadUrl(url);
                return false;

//                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
            }

        });
    }



    private boolean isPkgInstalled(String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = StartOtherAppActivity.this.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}

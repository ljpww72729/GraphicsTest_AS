package com.kk.lp.deep_link;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kk.lp.BaseActivity;
import com.kk.lp.R;

public class StartOtherAppActivity extends BaseActivity {

    private Button start_other_app, start_other_app_intent, check_other_app;

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

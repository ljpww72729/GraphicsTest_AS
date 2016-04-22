package com.kk.lp;

import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

/**
 * Created by ljpww72729 on 16/3/6.
 */
public class BaseApplication extends Application {

    private final String TAG = BaseApplication.this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        initStrictMode();
        //获取系统设置下的nightMode，获取的该值是系统默认设置的，无法在自己应用中修改该系统默认值，
        // 但是可以通过设置改变自己应用下该UI的显示模式
        //测试时uiModeManager.getNightMode()，获取的该值是固定的；
        // 无论你如何调用AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);去设置
        //都无法更改uiModeManager.getNightMode()该值
        Log.d(TAG, "onCreate: uiModeManager.getNightMode()====" + uiModeManager.getNightMode());
//        if (uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
//            Log.d(TAG, "onCreate: UiModeManager.MODE_NIGHT_YES");
//        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }else{
//            Log.d(TAG, "onCreate: UiModeManager.MODE_NIGHT_NO");
//            uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        }
        super.onCreate();
    }

    public void initStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
    }

}

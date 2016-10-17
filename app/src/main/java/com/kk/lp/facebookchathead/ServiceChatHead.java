package com.kk.lp.facebookchathead;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.kk.lp.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ljpww72729 on 16/2/20.
 */
public class ServiceChatHead extends Service
{

    private static final String TAG = "ServiceChatHead";
    private WindowManager windowManager;
    private ImageView chatImage;
    private WindowManager.LayoutParams params;
    private boolean monitorExist;
    private Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        chatImage = new ImageView(this);
        chatImage.setImageResource(R.drawable.loadi_03);
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                //TYPE_PHONE需要请求权限
//                WindowManager.LayoutParams.TYPE_PHONE,
                //TYPE_TOAST不需要请求权限
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        chatImage.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(ServiceChatHead.this,"触摸了浮动按钮", Toast.LENGTH_SHORT).show();
                        backToApp();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x =  initialX + (int)(event.getRawX() - initialTouchX);
                        params.y = initialY + (int)(event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(chatImage, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ActivityManager mActivityManager =(ActivityManager)ServiceChatHead.this.getSystemService(Context.ACTIVITY_SERVICE);
                String mPackageName = null;
                Boolean isForeground;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    List<ActivityManager.RunningAppProcessInfo> tasks =  mActivityManager.getRunningAppProcesses();
                    if (null != tasks && tasks.size() > 0) {
                        mPackageName = tasks.get(0).processName;
                    }
                    isForeground = "com.taobao.taobao".equals(mPackageName);
                } else{
                    isForeground = getRunningTask(ServiceChatHead.this, "com.taobao.taobao");
                }

                Log.i(TAG, "run: isForeground==" + isForeground);
                if (isForeground){
                    if (!monitorExist){

                    if(chatImage != null) {
                        Log.i(TAG, "run: chatImage != null");
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "run: add chatImage");
                                windowManager.addView(chatImage, params);
                                monitorExist = true;
                            }
                        });
                    }
                    }
                }else {
                    if (monitorExist){
                    if(chatImage != null) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "run: remove chatImage");
                              ServiceChatHead.this.stopSelf();
                            }
                        });
                    }
                    }
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
        return START_STICKY_COMPATIBILITY;
    }
    public static boolean getRunningTask(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return !TextUtils.isEmpty(packageName) && packageName.equals(cn.getPackageName());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService();
    }

    public void backToApp(){
        Intent resolveIntent = getPackageManager().getLaunchIntentForPackage("com.kk.lp");// 这里的packname就是从上面得到的目标apk的包名
        // 启动目标应用
        if (resolveIntent != null) {
            startActivity(resolveIntent);
        }
        //销毁自己
        stopSelf();
    }

    private void stopService(){
        if (null != timer){
            timer.cancel();
        }
        if(chatImage != null){
            windowManager.removeView(chatImage);
        }
    }

    private String getForegroundApp(Context context) {
        boolean isInit = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usageStatsManager =
                    (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long ts = System.currentTimeMillis();
            List<UsageStats> queryUsageStats =
                    usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
            UsageEvents usageEvents = usageStatsManager.queryEvents(isInit ? 0 : ts - 5000, ts);
            if (usageEvents == null) {
                return null;
            }


            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents.Event lastEvent = null;
            while (usageEvents.getNextEvent(event)) {
                // if from notification bar, class name will be null
                if (event.getPackageName() == null || event.getClassName() == null) {
                    continue;
                }

                if (lastEvent == null || lastEvent.getTimeStamp() < event.getTimeStamp()) {
                    lastEvent = event;
                }
            }

            if (lastEvent == null) {
                return null;
            }
            return lastEvent.getPackageName();
        }
        return null;
    }
}

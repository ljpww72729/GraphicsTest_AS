package com.kk.lp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by lipeng on 2016 4-29.
 */
public class SplashActivity extends BaseActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;  //2 Seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BaseApplication.getInstance().launched) {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            new Handler().postDelayed(new Runnable() {
                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    BaseApplication.getInstance().launched = true;
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
}
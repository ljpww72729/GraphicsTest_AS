package com.kk.lp.support23_2;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.kk.lp.BaseActivity;
import com.kk.lp.R;

public class VectorDrawableActivity extends BaseActivity {

    private static final String CURRENTNIGHTMODE = "currentNightMode";
    private int currentNightMode = AppCompatDelegate.MODE_NIGHT_YES;
    ImageButton change_night_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            switch (savedInstanceState.getInt(CURRENTNIGHTMODE)){
                case -1:
                    currentNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                    break;
                case 1:
                    currentNightMode = AppCompatDelegate.MODE_NIGHT_NO;
                    break;
                case 2:
                    currentNightMode = AppCompatDelegate.MODE_NIGHT_YES;
                    break;
                case 0:
                default:
                    currentNightMode = AppCompatDelegate.MODE_NIGHT_AUTO;
                    break;
            }
        }
        //设置为night模式
        getDelegate().setLocalNightMode(currentNightMode);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_drawable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("矢量图");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initControl();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENTNIGHTMODE, currentNightMode);
    }

    private void initView() {
        change_night_mode = (ImageButton) findViewById(R.id.change_night_mode);
    }

    private void initControl() {
        change_night_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES){
                    currentNightMode = AppCompatDelegate.MODE_NIGHT_NO;
                }else{
                    currentNightMode = AppCompatDelegate.MODE_NIGHT_YES;
                }
                getDelegate().setLocalNightMode(currentNightMode);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    recreate();
                }
            }
        });
    }
}

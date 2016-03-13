package com.kk.lp.support_lib_23_2;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kk.lp.BaseActivity;
import com.kk.lp.R;

public class VectorDrawableLibActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_drawable);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

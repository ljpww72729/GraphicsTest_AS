package com.kk.lp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;

/**
 * Created by lipeng on 2016 6-3.
 */
public class CustomViewFragment extends BaseFragment{

    public static CustomViewFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CustomViewFragment fragment = new CustomViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_linearlayout,container,false);
        return view;
    }
}

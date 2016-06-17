package com.kk.lp.view;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;

/**
 * Created by lipeng on 2016 6-3.
 */
public class CustomViewFragment extends BaseFragment{

    private static final String TAG = "CustomViewFragment";


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
        final CustomTextView ctv = (CustomTextView) view.findViewById(R.id.ct_one);
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        ctv.setBackground(colorDrawable);
        Button set_bg = (Button) view.findViewById(R.id.set_bg);
        set_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ctv.getBackground() instanceof ColorDrawable){
//                    Log.d(TAG, "background is instance of ColorDrawable ");
//                }else{
//                    Log.d(TAG, "background is not instance of ColorDrawable ");
//                }
//                ctv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
//                ctv.setBg();
                ctv.setInvalidate();
            }
        });
        return view;
    }
}

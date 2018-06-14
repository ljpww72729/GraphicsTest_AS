package com.kk.lp.glide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;

/**
 * Created by lipeng on 2016 3-8.
 */
public class ImgWithGlideFragment extends BaseFragment {

    public static ImgWithGlideFragment newInstance() {

        Bundle args = new Bundle();

        ImgWithGlideFragment fragment = new ImgWithGlideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_img_with_glide, null, false);
        return view;
    }
}

package com.kk.lp.viewdrag;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kk.lp.BaseFragment;

import butterknife.ButterKnife;

public class ViewDragFragment extends BaseFragment {


    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static ViewDragFragment newInstance() {
        ViewDragFragment fragment = new ViewDragFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CustomViewDrag cvd = new CustomViewDrag(getActivity());
        ButterKnife.bind(this, cvd);
        return cvd;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}

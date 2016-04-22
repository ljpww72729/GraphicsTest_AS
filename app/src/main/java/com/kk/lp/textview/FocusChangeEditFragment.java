package com.kk.lp.textview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;

/**
 * Created by lipeng on 2016 3-23.
 */
public class FocusChangeEditFragment extends BaseFragment{
    EditText editText2;
    EditText editText1;
    public static FocusChangeEditFragment newInstance() {

        Bundle args = new Bundle();

        FocusChangeEditFragment fragment = new FocusChangeEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus_chage, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        editText1 = (EditText) view.findViewById(R.id.editText1);
        editText2 = (EditText) view.findViewById(R.id.editText2);
        editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
//                    editText2.clearFocus();
                    editText1.requestFocus();
                    //不显示光标
//                    editText2.setCursorVisible(false);
                }
            }
        });
    }
}

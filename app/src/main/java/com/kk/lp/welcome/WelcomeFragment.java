package com.kk.lp.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.lp.BaseFragment;
import com.kk.lp.MainActivity;
import com.kk.lp.R;

public class WelcomeFragment extends BaseFragment implements PageTransformerDelegate{

    public static final String ARG_PARAM = "param";
    public static final String ARG_INDICATOR = "indicator";
    private static final String TAG = "BaseFragment";
    private Welcome welcome;
    private int indicator;
    private TextView wel_title;
    private ImageView wel_img;
    private Button wel_start;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WelcomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WelcomeFragment newInstance(Welcome welcome, int indicator) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM, welcome);
        bundle.putInt(ARG_INDICATOR, indicator);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            welcome = (Welcome) getArguments().getSerializable(ARG_PARAM);
            indicator = getArguments().getInt(ARG_INDICATOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        FrameLayout wel_bg = (FrameLayout) view.findViewById(R.id.wel_bg);
        wel_title = (TextView) view.findViewById(R.id.wel_title);
        TextView wel_des = (TextView) view.findViewById(R.id.wel_des);
        wel_img = (ImageView) view.findViewById(R.id.wel_img);
        wel_start = (Button) view.findViewById(R.id.wel_start);
        wel_bg.setBackgroundResource(welcome.getBackgroundColor());
        wel_title.setText(welcome.getTitle());
        wel_des.setText(welcome.getDescription());
        wel_img.setImageResource(welcome.getImg());
        if (indicator == 2){
            wel_start.setVisibility(View.VISIBLE);
            wel_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
            });
        }
        view.setTag(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void transformPage(float position) {
        if (indicator == 0){
            ViewCompat.setTranslationX(wel_img, getView().getWidth() * position);
        }else if (indicator == 1){
            ViewCompat.setAlpha(wel_img, 1 - Math.abs(position));
            ViewCompat.setTranslationX(wel_img, getView().getWidth() * position);
        }else{
            ViewCompat.setScaleX(wel_img, 1-Math.abs(position));
            ViewCompat.setScaleY(wel_img, 1-Math.abs(position));
            ViewCompat.setAlpha(wel_start, 1 - Math.abs(position));
        }
    }
}

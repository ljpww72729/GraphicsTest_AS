package com.kk.lp.welcome;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;

public class WelcomeFragment extends BaseFragment implements PageTransformerDelegate{

    public static final String ARG_PARAM = "param";
    public static final String ARG_POSITION = "position";
    private static final String TAG = "BaseFragment";
    private Welcome welcome;
    private int position;
    private TextView wel_title;
    private ImageView wel_img;

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
    public static WelcomeFragment newInstance(Welcome welcome, int position) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM, welcome);
        bundle.putInt(ARG_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            welcome = (Welcome) getArguments().getSerializable(ARG_PARAM);
            position = getArguments().getInt(ARG_POSITION);
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
        wel_bg.setBackgroundResource(welcome.getBackgroundColor());
        wel_title.setText(welcome.getTitle());
        wel_des.setText(welcome.getDescription());
        wel_img.setImageResource(welcome.getImg());
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
        ViewCompat.setTranslationX(wel_img, -position);
    }
}

package com.kk.lp.animation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;

import java.util.Random;

/**
 * Created by lipeng on 2016 6-17.
 */
public class AnimationFragment extends BaseFragment{

    public static AnimationFragment newInstance() {

        Bundle args = new Bundle();

        AnimationFragment fragment = new AnimationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public boolean isAnimation = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animation, container, false);
        final LinearLayout lay = (LinearLayout) view.findViewById(R.id.lay);
        Button button = (Button) view.findViewById(R.id.button);
        Button start_animation = (Button) view.findViewById(R.id.start_animation);
        Button start_animator = (Button) view.findViewById(R.id.start_animator);
        final TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(String.valueOf(lay.getTop()));
        start_animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(AnimationFragment.this.getActivity(), R.anim.design_bottom_sheet_in);
                animation.setFillAfter(true);
                animation.setDuration(1000);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        isAnimation = true;
                        System.out.println("animation_start===" + lay.getTop());
                        text.setText(String.valueOf(lay.getTop()));
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isAnimation = false;
                        System.out.println("animation_end===" + lay.getTop());
                        text.setText(String.valueOf(lay.getTop()));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                });
                lay.startAnimation(animation);
                System.out.println("animation_while=start==" + lay.getTop());
                while (true){
                    if (!isAnimation){
                        break;
                    }
                    System.out.println("animation_while===" + lay.getTop());
                    text.setText(String.valueOf(lay.getTop()));
                }
            }
        });
        start_animator.setOnClickListener(new View.OnClickListener() {
            boolean flag = false;
            @Override
            public void onClick(View v) {
                int distance;
                if (flag){
                    flag = false;
                    distance = -100;
                }else {
                    flag = true;
                    distance = 1000;
                }

                ViewCompat.animate(lay).translationY(distance).setDuration(3000).setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        System.out.println("animator_start===" + lay.getTop()+ "===height===" +lay.getHeight());
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        System.out.println("animator_end===" + lay.getTop()+ "===height===" +lay.getHeight());
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        System.out.println("animator_cancel===" + lay.getTop());
                    }
                }).start();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(String.valueOf(new Random().nextInt()));
            }
        });


        return view;
    }
}

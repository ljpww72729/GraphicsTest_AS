package com.kk.lp.deep_link;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpenWithBrowserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpenWithBrowserFragment extends BaseFragment {

    public OpenWithBrowserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment OpenWithBrowserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OpenWithBrowserFragment newInstance() {
        OpenWithBrowserFragment fragment = new OpenWithBrowserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_open_with_browser, container, false);
        final EditText deeplinks = (EditText) view.findViewById(R.id.deeplinks);
        deeplinks.setText("https://lkme.cc/IfC/yGs2hfPK8");
        TextView default_browser = (TextView) view.findViewById(R.id.default_browser);
        default_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(deeplinks.getText().toString()));
                intent.setPackage("com.android.browser");
                startActivity(intent);
            }
        });
        TextView chrome = (TextView) view.findViewById(R.id.chrome);
        chrome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(deeplinks.getText().toString()));
                intent.setPackage("com.android.chrome");
                startActivity(intent);
            }
        });
        TextView uc_browser = (TextView) view.findViewById(R.id.uc_browser);
        uc_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(deeplinks.getText().toString()));
                intent.setPackage("com.UCMobile");
                startActivity(intent);
            }
        });
        TextView qq_browser = (TextView) view.findViewById(R.id.qq_browser);
        qq_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(deeplinks.getText().toString()));
                intent.setPackage("com.tencent.mtt");
                startActivity(intent);
            }
        });
        TextView liebao = (TextView) view.findViewById(R.id.liebao);
        liebao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(deeplinks.getText().toString()));
                intent.setPackage("com.ijinshan.browser_fast");
                startActivity(intent);
            }
        });

        return view;
    }

}

package com.kk.lp.volley;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kk.lp.BaseFragment;
import com.kk.lp.R;

import java.nio.charset.Charset;

/**
 * Created by lipeng on 2016 5-9.
 */
public class VolleyFragment extends BaseFragment {

    TextView response_content;
    Button get_data;
    RequestQueue requestQueue;
    String url = "https://www.baidu.com";
    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Snackbar.make(getView(), "success", Snackbar.LENGTH_SHORT).show();
            String s = new String(response.getBytes(Charset.forName("UTF-8")));
            response_content.setText(s);
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Snackbar.make(getView(), "error", Snackbar.LENGTH_SHORT).show();
            response_content.setText(error.getMessage());
        }
    });

    public static VolleyFragment newInstance() {

        Bundle args = new Bundle();

        VolleyFragment fragment = new VolleyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volley, null, false);
        get_data = (Button) view.findViewById(R.id.get_data);
        response_content = (TextView) view.findViewById(R.id.response_content);
        get_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue.add(request);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        requestQueue = Volley.newRequestQueue(this.getActivity());
    }
}

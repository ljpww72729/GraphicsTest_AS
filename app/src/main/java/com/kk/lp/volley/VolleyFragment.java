package com.kk.lp.volley;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.kk.lp.BaseFragment;
import com.kk.lp.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lipeng on 2016 5-9.
 */
public class VolleyFragment extends BaseFragment {

    public static final String TAG = "VolleyFragment";

    TextView response_content;
    EditText host,ip, suffix;
    Button get_data;
    RequestQueue requestQueue;
    String url = "https://www.baidu.com";
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
        host = (EditText) view.findViewById(R.id.host);
        ip = (EditText) view.findViewById(R.id.ip);
        suffix = (EditText) view.findViewById(R.id.suffix);
        response_content = (TextView) view.findViewById(R.id.response_content);
        get_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                requestQueue.cancelAll(TAG);
//                url = host.getText().toString() + ip.getText().toString() + suffix.getText().toString();
                url = "http://www.kuaidi100.com/query";
                Map<String, String> paramLogin = new HashMap<>();
                paramLogin.put("type", "d");
                paramLogin.put("postid", "dd");

//                StringRequest request = new StringRequest(Request.Method.POST, url, paramLogin, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Snackbar.make(getView(), "success", Snackbar.LENGTH_SHORT).show();
//                        String s = new String(response.getBytes(Charset.forName("UTF-8")));
//                        response_content.setText(s);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Snackbar.make(getView(), "error", Snackbar.LENGTH_SHORT).show();
//                        response_content.setText(error.getMessage());
//                    }
//                });

                    GsonRequest<Student> request = new GsonRequest(Request.Method.POST, url, paramLogin,null, new Response.Listener<Student>() {

                        @Override
                        public void onResponse(Student response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                request.setTag(TAG);
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

    @Override
    public void onStop() {
        super.onStop();
        requestQueue.cancelAll(TAG);
    }
}

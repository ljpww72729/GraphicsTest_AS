package com.kk.lp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by LinkedME06 on 31/07/2017.
 */

public class GetUserInfoFragment extends Fragment {

    public GetUserInfoFragment() {
        // Required empty public constructor
    }

    public static GetUserInfoFragment newInstance() {
        GetUserInfoFragment fragment = new GetUserInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_get_user_info, container, false);
        Button get_contacts = (Button) view.findViewById(R.id.get_contacts);
        get_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    private void obtionContacts() {

    }

}

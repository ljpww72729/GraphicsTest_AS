package com.kk.lp.scrollview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kk.lp.R;

public class XScrollViewFragment extends Fragment {
    private XScrollView xScrollView;
    public XScrollViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment XScrollViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static XScrollViewFragment newInstance() {
        XScrollViewFragment fragment = new XScrollViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xscroll_view, container, false);
        xScrollView = (XScrollView) view.findViewById(R.id.xScrollView);
        xScrollView.setXScrollViewListener(mIXScrollViewListener);// 设置监听方式是本类
        return view;
    }
    /** 第三方的XListView监听初始化 */
    private XScrollView.IXScrollViewListener mIXScrollViewListener = new XScrollView.IXScrollViewListener() {

        @Override
        public void onRefresh() {
            Toast.makeText(XScrollViewFragment.this.getActivity(), "refresh", Toast.LENGTH_LONG).show();
            xScrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onLoad();
                }
            }, 1000);
        }

    };
    /*
	 * 停止刷新或加载操作
	 */
    private void onLoad() {
        xScrollView.stopRefresh();
        xScrollView.setRefreshTime(true);

    }
}

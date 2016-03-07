package com.kk.lp.loader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;

/**
 * Created by lipeng on 2016 3-8.
 * 参考：https://medium.com/google-developers/making-loading-data-on-android-lifecycle-aware-897e12760832#.cukr3ib63
 * 一定要注意它的使用场合，主要用于configuration changes情景，
 */
public class AsyncTaskLoaderFragment extends BaseFragment {

    private static final String TAG = "AsyncTaskLoaderFragment";
    public static final int getWeatherData = 0;
    TextView loader_show_data;

    public static AsyncTaskLoaderFragment newInstance() {
        Log.d(TAG, "newInstance: is on");
        Bundle args = new Bundle();

        AsyncTaskLoaderFragment fragment = new AsyncTaskLoaderFragment();
        fragment.setArguments(args);
        fragment.setRetainInstance(false);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: is on");
        View view = inflater.inflate(R.layout.fragment_async_task_loader, null, false);
        loader_show_data = (TextView) view.findViewById(R.id.loader_show_data);
        Button loader_get_data = (Button) view.findViewById(R.id.loader_get_data);
        loader_get_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoaderManager().restartLoader(getWeatherData, null, loaderCallbacks);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: is on");
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(getWeatherData, savedInstanceState, loaderCallbacks);
    }

    private LoaderManager.LoaderCallbacks<WeatherData> loaderCallbacks = new LoaderManager.LoaderCallbacks<WeatherData>() {
        @Override
        public Loader<WeatherData> onCreateLoader(int id, Bundle args) {
            return new JsonAsyncTaskLoader(AsyncTaskLoaderFragment.this.getActivity());
        }

        @Override
        public void onLoadFinished(Loader<WeatherData> loader, WeatherData data) {
            if (data == null) {
                Toast.makeText(AsyncTaskLoaderFragment.this.getActivity(), "数据为空", Toast.LENGTH_SHORT).show();
                return;
            }
            loader_show_data.setText(data.getName());
        }

        @Override
        public void onLoaderReset(Loader<WeatherData> loader) {
            loader_show_data.setText("");
        }
    };

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewStateRestored: is on");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: is on");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: is on");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: is on");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: is on");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: is on");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: is on");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: is on");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: is on");
    }
}


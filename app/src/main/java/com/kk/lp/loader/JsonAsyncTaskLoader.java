package com.kk.lp.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lipeng on 2016 3-7.
 */
public class JsonAsyncTaskLoader extends AsyncTaskLoader<WeatherData> {

    private static final String TAG = "JsonAsyncTaskLoader";
    private WeatherData weatherData;

    public JsonAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (weatherData != null) {
            Log.d(TAG, "onStartLoading: use cached data");
            // Use cached data
            deliverResult(weatherData);
        } else {
            // We have no data, so kick off loading it
            Log.d(TAG, "onStartLoading: forceLoad");
            forceLoad();
        }

    }

    @Override
    public WeatherData loadInBackground() {
        Log.d(TAG, "loadInBackground: is on");
        // This is on a background thread
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.openweathermap.org/data/2.5/weather?q=Beijing&appid=44db6a862fba0b067b1930da0d769e98&lang=zh_cn&units=metric")
                .build();
        // Parse the JSON using the library of your choice
        // Check isLoadInBackgroundCanceled() to cancel out early
        try {
            Response response = client.newCall(request).execute();
            return JSON.parseObject(response.body().string(), WeatherData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        isLoadInBackgroundCanceled();
        return null;
    }

    @Override
    public void deliverResult(WeatherData data) {
        Log.d(TAG, "deliverResult: is on");
        // We’ll save the data for later retrieval
        weatherData = data;
        // We can do any pre-processing we want here
        // Just remember this is on the UI thread so nothing lengthy!
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d(TAG, "onStopLoading: is on");
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset: is on");
        super.onReset();
    }
}

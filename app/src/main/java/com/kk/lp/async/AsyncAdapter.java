package com.kk.lp.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kk.lp.R;

/**
 * 
 * @Description
 * @author lipeng
 * @version 2015-7-6
 * 
 */

public class AsyncAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mFactory;
	private int mTaskCount;
	List<SimpleAsyncTask> mTaskList;
	private static ExecutorService SINGLE_TASK_EXECUTOR;
	private static ExecutorService LIMITED_TASK_EXECUTOR;
	private static ExecutorService FULL_TASK_EXECUTOR;

	static {
		SINGLE_TASK_EXECUTOR = (ExecutorService) Executors.newSingleThreadExecutor();
		LIMITED_TASK_EXECUTOR = (ExecutorService) Executors.newFixedThreadPool(2);
		FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();
	};

	public AsyncAdapter(Context context, int taskCount) {
		mContext = context;
		mFactory = LayoutInflater.from(mContext);
		mTaskCount = taskCount;
		mTaskList = new ArrayList<SimpleAsyncTask>(taskCount);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mTaskCount;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mTaskList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mFactory.inflate(R.layout.fragment_async_item, null);
			SimpleAsyncTask task = new SimpleAsyncTask((TaskItem) convertView);
			/*
			 * It only supports five tasks at most. More tasks will be scheduled
			 * only after first five finish. In all, the pool size of AsyncTask
			 * is 5, at any time it only has 5 threads running.
			 */
//			 task.execute();
			// use AsyncTask#SERIAL_EXECUTOR is the same to #execute();
			// task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
			// use AsyncTask#THREAD_POOL_EXECUTOR is the same to older version
			// #execute() (less than API 11)
			// but different from newer version of #execute()
			// task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			// one by one, same to newer version of #execute()
			// task.executeOnExecutor(SINGLE_TASK_EXECUTOR);
			// execute tasks at some limit which can be customized
			 task.executeOnExecutor(LIMITED_TASK_EXECUTOR);
			// no limit to thread pool size, all tasks run simultaneously
//			task.executeOnExecutor(FULL_TASK_EXECUTOR);

			mTaskList.add(task);
		}
		return convertView;
	}
	class SimpleAsyncTask extends AsyncTask<Void, Integer, Void> {

		private TaskItem mTaskItem;
		private String mName;
		private AtomicInteger ato = new AtomicInteger(1);

		public SimpleAsyncTask(TaskItem item) {
			mTaskItem = item;
			mName = "Task #" + String.valueOf(ato.getAndIncrement());
		}

		@Override
		protected Void doInBackground(Void... params) {
			int prog = 1;
			while (prog < 101) {
				SystemClock.sleep(100);
				publishProgress(prog);
				prog++;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}

		@Override
		protected void onPreExecute() {
			mTaskItem.setTitle(mName);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			mTaskItem.setProgress(values[0]);
		}
	}

}

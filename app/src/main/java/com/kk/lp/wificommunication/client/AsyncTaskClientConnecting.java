package com.kk.lp.wificommunication.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.AsyncTask;
import android.os.SystemClock;
import de.greenrobot.event.EventBus;

/**
 * 
 * @Description 自动连接socket服务器
 * @author lipeng
 * @version 2015-7-13
 * 
 */

public class AsyncTaskClientConnecting {

	public static final int executorCount = 50;
	//允许一次执行的个数
	public static final int enableCount =  executorCount - 10;
	// 线程个数，允许连接的客户端数量为50个
	private static ExecutorService LIMITED_TASK_EXECUTOR = Executors.newFixedThreadPool(executorCount);
	public static ArrayList<ClientConnectAsyncTask> task = new ArrayList<ClientConnectAsyncTask>();
	//判断是否连接成功
	public static volatile boolean success = false;
	public void clientConnect(ServerInfo serverInfo) {
		ClientConnectAsyncTask ccat = new ClientConnectAsyncTask(serverInfo);
		task.add(ccat);
//		ccat.execute();
		ccat.executeOnExecutor(LIMITED_TASK_EXECUTOR);
	}

	public void cancelAllTask(){
		for (int i = 0; i < task.size(); i++) {
			if(task.get(i) != null){
				task.get(i).cancel(true);
			}
		}
	}
	public static synchronized void removeTask(ClientConnectAsyncTask clientConnectAsyncTask){
		task.remove(clientConnectAsyncTask);
	}
	
	class ClientConnectAsyncTask extends AsyncTask<Void, Void, ServerInfo> {
		private ServerInfo serverInfo;

		public ClientConnectAsyncTask(ServerInfo serverInfo) {
			this.serverInfo = serverInfo;
		}

		@Override
		protected ServerInfo doInBackground(Void... params) {
			ServerInfo resultInfo = null;
			Socket socket = null;
			try {
				SystemClock.sleep(5);
				socket = new Socket();
				System.out.println("正在连接的是" + serverInfo.serverIP);
				SocketAddress socketAddress = new InetSocketAddress(serverInfo.serverIP, serverInfo.port);
				socket.connect(socketAddress, 3000);
				cancelAllTask();
				success = true;
				//连接成功，返回服务器IP地址
				System.out.println("连接成功了。。。。。");
				resultInfo = serverInfo;
				EventBus.getDefault().post(resultInfo);
			} catch (UnknownHostException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			} finally {
				removeTask(this);
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(task.size() == 0 && !success){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("serverInfo", serverInfo);
				EventBus.getDefault().post(map);
				}
			}
			return resultInfo;
		}
		
	}

}

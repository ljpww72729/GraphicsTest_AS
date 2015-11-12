package com.kk.lp.wificommunication.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.AsyncTask;
import android.util.Log;
import de.greenrobot.event.EventBus;

/**
 * 
 * @Description 已连接的socket客户端线程
 * @author lipeng
 * @version 2015-7-13
 * 
 */

public class AsyncTaskClientConnected {
	
	private static SocketServerInfo ssInfo;

	public AsyncTaskClientConnected() {
		ssInfo = new SocketServerInfo();
	}

	// 线程个数，允许连接的客户端数量为10个
	private static ExecutorService LIMITED_TASK_EXECUTOR = Executors.newFixedThreadPool(10);
	public static ArrayList<ClientConnectAsyncTask> task = new ArrayList<ClientConnectAsyncTask>();

	public void clientConnect(Socket socket) {
		ClientConnectAsyncTask ccaTask = new ClientConnectAsyncTask(socket);
		task.add(ccaTask);
		ccaTask.executeOnExecutor(LIMITED_TASK_EXECUTOR);
		noticeClientCount();
	}

	public void cancelAllTask() {
		for (int i = 0; i < task.size(); i++) {
			if (task.get(i) != null) {
				task.get(i).setConnecting(false);
				task.get(i).cancel(true);
			}
		}
		noticeClientCount();
	}

	public static void noticeClientCount(){
		ssInfo.status = SocketServerInfo.SOCKET_CLIENT_CONNECT;
		ssInfo.clientCount = task.size();
		EventBus.getDefault().post(ssInfo);
	}
	
	public synchronized static void removeTask(ClientConnectAsyncTask ccaTask) {
		task.remove(ccaTask);
		noticeClientCount();
	}

	class ClientConnectAsyncTask extends AsyncTask<Void, Void, Void> {
		private Socket socket;
		private boolean connecting = false;

		public void setConnecting(boolean connecting) {
			this.connecting = connecting;
		}

		public ClientConnectAsyncTask(Socket socket) {
			this.socket = socket;
			connecting = true;
			ClientInfo clientInfo = new ClientInfo();
			clientInfo.ip = socket.getInetAddress().getHostAddress();
			clientInfo.msg = "客户端连接，客户端IP地址为：" + clientInfo.ip;
			EventBus.getDefault().post(clientInfo);
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				// 设置连接超时时间
				socket.setSoTimeout(30 * 1000);
				String str;
				while (connecting && SocketUtil.isConnected(socket)) {
					str = SocketUtil.readStrFromStream(socket.getInputStream());
					if (str != null && str.trim().equals("") == false) {
						ClientInfo clientInfo = new ClientInfo();
						clientInfo.msgType = ClientInfo.MSG_DATA;
						clientInfo.data = str;
						EventBus.getDefault().post(clientInfo);
					}
					Log.d("socket", "服务器端获取的数据为------------" + str);
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				ClientInfo clientInfo = new ClientInfo();
				clientInfo.ip = socket.getInetAddress().getHostAddress();
				clientInfo.msg = "客户端连接断开，客户端IP地址为：" + clientInfo.ip;
				EventBus.getDefault().post(clientInfo);
				removeTask(this);
				if (socket != null) {
					try {
						socket.close();
						socket = null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return null;
		}

	}

}

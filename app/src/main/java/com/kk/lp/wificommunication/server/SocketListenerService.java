package com.kk.lp.wificommunication.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import de.greenrobot.event.EventBus;

/**
 *
 * @Description
 * @author lipeng
 * @version 2015-7-10
 *
 */

public class SocketListenerService extends Service {

	public static final String SERVICE_AUTHORITY = "com.kk.lp.socketlistenerservice";

	private String clientInfoStr;
	private ServerBinder serverBinder = new ServerBinder();
	public static ServerSocket serverSocket = null;// 服务Socket
	public static Socket mSocketServer = null;// 用于和客户端连接的socket
	public final static String SOCKET_PORT = "server_socket_port";// 监听端口

	public static ClientInfo clientInfo;

	public class ServerBinder extends Binder {
		// 获取服务器端发送的数据
		public String getClientInfo() {
			return clientInfoStr;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return serverBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		clientInfo = new ClientInfo();
	}

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 监听客户端
				// 获取监听端口
				int port = intent.getIntExtra(SOCKET_PORT, 8000);
				try {
					serverSocket = new ServerSocket(port);// 绑定serverSocket
					// 判断serverSocket是否已经绑定，绑定返回true否则为false
					if (!serverSocket.isBound()) {
						/* 如果绑定不成功，则提示更换端口重新绑定 */
						SocketServerInfo ssInfo = new SocketServerInfo();
						ssInfo.status = SocketServerInfo.SOCKET_CREATE_FAILED;
						EventBus.getDefault().post(ssInfo);
						serverSocket = null;
						return;
					}
					SocketServerInfo ssInfo = new SocketServerInfo();
					ssInfo.status = SocketServerInfo.SOCKET_CREATED;
					EventBus.getDefault().post(ssInfo);
					while (ServerFragment.serverRunning) {
						//监听客户端
						mSocketServer = serverSocket.accept();// 接受请求
						Log.d("socket", "Client Conect!");
						clientInfo.msgType = ClientInfo.MSG_NORMAL;
						clientInfo.socketClient = mSocketServer;
						EventBus.getDefault().post(clientInfo);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// 关闭socket链接
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}

}
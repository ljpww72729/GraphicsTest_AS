package com.kk.lp.wificommunication.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.kk.lp.wificommunication.server.SocketUtil;

import de.greenrobot.event.EventBus;

/**
 * 
 * @Description
 * @author lipeng
 * @version 2015-8-6
 * 
 */

public class ClientSocketService extends Service {

	public Socket socket;
	public static final String SOCKET_SERVER = "client_socket_server";
	public static final String SOCKET_PORT = "client_socket_port";
	public static final String SERVICE_AUTHORITY = "com.kk.lp.clientsocketservice";
	//默认为关闭状态
	public static int clientConnectStatus = SocketClientInfo.SOCKET_NOTCONNECT; 

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String server = intent.getStringExtra(SOCKET_SERVER);
				int port = intent.getIntExtra(SOCKET_PORT, 8000);
				//连接socket
				try {
					socket = new Socket(server, port);
					socket.setKeepAlive(true);//开启保持活动状态的套接字
					clientConnectStatus = SocketClientInfo.SOCKET_CONNECTED;
					SocketUtil.writeStr2Stream("client" + server,
							socket.getOutputStream());
				} catch (UnknownHostException e) {
					clientConnectStatus = SocketClientInfo.SOCKET_CONNECT_FAILED;
					e.printStackTrace();
				} catch (IOException e) {
					clientConnectStatus = SocketClientInfo.SOCKET_CONNECT_FAILED;
					e.printStackTrace();
				}finally{
					SocketClientInfo scStatus = new SocketClientInfo();
					scStatus.status = clientConnectStatus;
					scStatus.socket = socket;
					EventBus.getDefault().post(scStatus);
				}
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		if(socket != null){
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.onDestroy();
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

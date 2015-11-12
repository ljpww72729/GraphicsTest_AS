package com.kk.lp.wificommunication.client;

import java.net.Socket;

/**
 *
 * @Description 
 * @author lipeng
 * @version 2015-8-7
 * 
 */

public class SocketClientInfo {
	
	//未连接，默认状态
	public static final int SOCKET_NOTCONNECT = 0x0000;
	//正在连接
	public static final int SOCKET_CONNECTING = 0x0001;
	//已连接
	public static final int SOCKET_CONNECTED = 0x0002;
	//连接关闭，正常关闭连接
	public static final int SOCKET_CLOSED = 0x0003;
	//尝试连接服务器时未能成功连接
	public static final int SOCKET_CONNECT_FAILED = 0x0004;
	//服务器关闭导致连接关闭
	public static final int SOCKET_SERVERCLOSED = 0x0005;
	
	
	public int status = SOCKET_NOTCONNECT;
	public Socket socket;

}

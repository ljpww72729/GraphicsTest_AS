package com.kk.lp.wificommunication.server;

/**
 * 
 * @Description
 * @author lipeng
 * @version 2015-8-5
 * 
 */

public class SocketServerInfo {

	// 未创建，默认状态
	public static final int SOCKET_NOTCREATE = 0x0000;
	// 正在开启
	public static final int SOCKET_CREATING = 0x0001;
	// 已开启，开始监听
	public static final int SOCKET_CREATED = 0x0002;
	// 连接关闭，正常关闭连接
	public static final int SOCKET_CLOSED = 0x0003;
	// 创建失败
	public static final int SOCKET_CREATE_FAILED = 0x0004;
	// 未有客户端连接
	public static final int SOCKET_NOCLIENT = 0x0005;
	// 未有客户端连接
	public static final int SOCKET_CLIENT_CONNECT = 0x0006;

	// socket的状态
	public int status = SOCKET_NOTCREATE;
	// 连接的客户端的数量
	public int clientCount = 0;

}

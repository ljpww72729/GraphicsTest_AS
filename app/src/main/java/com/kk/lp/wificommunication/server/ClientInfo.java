package com.kk.lp.wificommunication.server;

/**
 * 
 * @Description 客户端状态
 * @author lipeng
 * @version 2015-8-11
 * 
 */

public class ClientInfo {

	// 数据消息类型
	// 非数据消息
	public static final int MSG_NORMAL = 0x0000;
	// 数据消息
	public static final int MSG_DATA = 0x0001;
	// 非数据消息及数据消息
	public static final int MSG_NORMAL_DATA = 0x0002;

	// 客户端IP地址
	public String ip;
	// 数据消息类型，默认普通非数据消息
	public int msgType = MSG_NORMAL;
	// 客户端非数据消息
	public String msg;
	// 客户端发送的数据
	public String data;

}

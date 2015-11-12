package com.kk.lp.wificommunication.client;

/**
 *
 * @Description 服务器连接信息
 * @author lipeng
 * @version 2015-8-6
 * 
 */

public class ServerInfo {
	
	public String serverIP;
	public int port;
	public int start;
	public int end;
	public int taskSize;
	
	public ServerInfo(String serverIP, int port, int start, int end, int taskSize){
		this.serverIP = serverIP;
		this.port = port;
		this.start = start;
		this.end = end;
		this.taskSize = taskSize;
	}
	public ServerInfo(String serverIP, int port, int start, int end){
		this( serverIP,  port,  start,  end, 0);
	}
	public ServerInfo(ServerInfo serverInfo){
		this.serverIP = serverInfo.serverIP;
		this.port = serverInfo.port;
		this.start = serverInfo.start;
		this.end = serverInfo.end;
		this.taskSize = serverInfo.taskSize;
	}

	
}

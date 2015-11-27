package com.kk.lp.wificommunication.client;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;
import com.kk.lp.utils.SPUtils;
import com.kk.lp.wificommunication.server.SocketUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 
 * @Description 开启服务器端
 * @author lipeng
 * @version 2015-7-10
 * 
 */

public class ClientFragment extends BaseFragment {
	public static boolean clientConnected = false;// 服务状态标志位
	private Intent intentClient;
	private Button fc_start, fc_stop, fc_select_client;
	private TextView fc_local_ip;
	public AsyncTaskClientConnecting asyncTaskClientConnecting;
	// 选中的id
	private int selectWhich;
	private ProgressDialog progressDialog;
	private Socket socket;
	private Intent intentReadService;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_client, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		selectWhich = (int) SPUtils.get(getActivity(), ClientSocketService.SOCKET_PORT, 0);
		fc_local_ip = (TextView) view.findViewById(R.id.fc_local_ip);
		fc_local_ip.setText("当前连接的wifi为：" + WifiUtils.getWifiName(getActivity()) + "\n客户端本机地址的是：" + WifiUtils.getServerIP(getActivity()) + "\n默认开启的端口为：" + getPort());
		fc_start = (Button) view.findViewById(R.id.fc_start);
		fc_start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fc_local_ip.setText("当前连接的wifi为：" + WifiUtils.getWifiName(getActivity()) + "\n客户端本机地址的是：" + WifiUtils.getServerIP(getActivity()) + "\n默认开启的端口为：" + getPort());
				showConnectDlg();
				String serverIP = getServerIP();
				// 开启的服务器端口，默认的是第一个服务器端口
				int port = getPort();
				Toast.makeText(getActivity(), "连接的是 " + port + " 端口", Toast.LENGTH_SHORT).show();
				if (serverIP.equals("")) {
					connectRandomHost(port);
				} else {
					// 连接过服务器，则采用存储的IP进行连接尝试
					startSocketClientServer(serverIP, port);
				}
				fc_start.setEnabled(false);
			}

		});
		fc_stop = (Button) view.findViewById(R.id.fc_stop);
		fc_stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopServer();
			}
		});
		fc_select_client = (Button) view.findViewById(R.id.fc_select_client);
		fc_select_client.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showServerDialog();
			}
		});
	}

	/**
	 * @Description 根据端口连接服务
	 * @author lipeng
	 * @param port
	 * @return void
	 */
	void connectRandomHost(int port) {
		try{
		String preServerAddress = getAddressPre();
		// 尝试碰撞连接服务器
		AsyncTaskClientConnecting.success = false;
		getServerIP(preServerAddress, port, 1, AsyncTaskClientConnecting.enableCount);
		}catch(RuntimeException e){
			Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 获取连接的地址前缀
	 */
	private String getAddressPre() {
		String preServerAddress = null;
		String serverAddress = WifiUtils.getServerIP(getActivity());
		if(serverAddress == null || serverAddress.length() <= 4){
			throw new RuntimeException("wifi未连接");
		}
		preServerAddress = serverAddress.substring(0, serverAddress.lastIndexOf(".") + 1);
		return preServerAddress;
	}

	private void getServerIP(String preServerAddress, int serverPort, int start, int end) {
		ServerInfo serverInfo = null;
		String connectServerIP = null;
		for (int i = start; i < end; i++) {
			connectServerIP = preServerAddress + i;
			serverInfo = new ServerInfo(connectServerIP, serverPort, start, end);
			asyncTaskClientConnecting.clientConnect(serverInfo);
		}
	}

	/**
	 * @Description 服务器配置选项
	 * @author lipeng
	 * @return void
	 */
	public void showServerDialog() {

		Builder builder = new AlertDialog.Builder(ClientFragment.this.getActivity());
		// 设置对话框的标题
		builder.setTitle("请设置默认连接的服务器");
		// 0: 默认第一个单选按钮被选中
		builder.setSingleChoiceItems(R.array.server, (int) SPUtils.get(getActivity(), ClientSocketService.SOCKET_PORT, 0), new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				selectWhich = which;
			}
		});

		// 添加一个确定按钮
		builder.setPositiveButton("确 定 ", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 存储默认开启的服务器
				SPUtils.put(ClientFragment.this.getActivity(), ClientSocketService.SOCKET_PORT, selectWhich);
				fc_local_ip.setText("当前连接的wifi为：" + WifiUtils.getWifiName(getActivity()) + "\n客户端本机地址的是：" + WifiUtils.getServerIP(getActivity()) + "\n默认开启的端口为：" + getPort());
			}
		});
		// 创建一个单选按钮对话框
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onDestroy() {
		stopServer();
		super.onDestroy();
	}

	// 停止socket服务
	public void stopServer() {
		socket = null;
		clientConnected = false;
		fc_start.setEnabled(true);
		if (intentClient != null) {
			ClientFragment.this.getActivity().stopService(intentClient);
		}
		if (intentReadService != null) {
			ClientFragment.this.getActivity().stopService(intentReadService);
		}
		if (asyncTaskClientConnecting != null) {
			asyncTaskClientConnecting.cancelAllTask();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
		asyncTaskClientConnecting = new AsyncTaskClientConnecting();
	}

	@Override
	public void onStop() {
		EventBus.getDefault().unregister(this);
		super.onStop();
	}

	/**
	 * @Description 连接服务器成功调用该方法重新进行连接
	 * @author lipeng
	 * @param event
	 * @return void
	 */
	public void onEventMainThread(ServerInfo event) {
		startSocketClientServer(event.serverIP, event.port);
	}

	/**
	 * @Description 连接服务器成功或者失败调用该方法
	 * @author lipeng
	 * @param event
	 * @return void
	 */
	public void onEventMainThread(SocketClientInfo event) {
		if (event.status == SocketClientInfo.SOCKET_CONNECTED) {
			Toast.makeText(getActivity(), "开始读取数据", Toast.LENGTH_SHORT).show();
			intentReadService = new Intent(getActivity(), ReadDataService.class);
			getActivity().startService(intentReadService);
			socket = event.socket;
		} else {
			stopServer();
			if(event.status == SocketClientInfo.SOCKET_CONNECT_FAILED){
				// 如果本地无存储的IP，则尝试碰撞连接连接服务器
				if (getServerIP().equals("")) {
					Toast.makeText(getActivity(), "原有服务器地址失效，重新寻找", Toast.LENGTH_SHORT).show();	
					showConnectDlg();
					connectRandomHost(getPort());
				}
				Toast.makeText(getActivity(), "服务器连接失败，请重试", Toast.LENGTH_SHORT).show();	
			}else if(event.status == SocketClientInfo.SOCKET_SERVERCLOSED){
				Toast.makeText(getActivity(), "服务器关闭，请重新连接服务器", Toast.LENGTH_SHORT).show();	
			}else if(event.status == SocketClientInfo.SOCKET_NOTCONNECT){
				Toast.makeText(getActivity(), "请点击连接按钮，连接服务器", Toast.LENGTH_SHORT).show();	
			}
		}
	}

	public void startSocketClientServer(String serverIP, int serverPort) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

		SPUtils.put(getActivity(), ClientSocketService.SOCKET_SERVER, serverIP);
		intentClient = new Intent(getActivity(), ClientSocketService.class);
		intentClient.putExtra(ClientSocketService.SOCKET_PORT, serverPort);
		intentClient.putExtra(ClientSocketService.SOCKET_SERVER, serverIP);
		ClientFragment.this.getActivity().startService(intentClient);
		Toast.makeText(getActivity(), "正在开启服务" + serverIP, Toast.LENGTH_SHORT).show();
		clientConnected = true;
	}

	/**
	 * @Description 未连接成功，继续连接
	 * @author lipeng
	 * @param map
	 * @return void
	 */
	public void onEventMainThread(Map<String, Object> map) {
		ServerInfo serverInfoTemp = (ServerInfo) map.get("serverInfo");
		if (serverInfoTemp.end < 255) {
			int start = serverInfoTemp.end;
			int end = start + AsyncTaskClientConnecting.enableCount;
			if (end > 255) {
				end = 255;
			}
			getServerIP(serverInfoTemp.serverIP.substring(0, serverInfoTemp.serverIP.lastIndexOf(".") + 1), serverInfoTemp.port, start, end);
		} else {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			stopServer();
			Toast.makeText(getActivity(), "连接失败，请重试", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * @Description 获取存储在本地的IP
	 * @author lipeng
	 * @return
	 * @return String
	 */
	String getServerIP() {
		return (String) SPUtils.get(getActivity(), ClientSocketService.SOCKET_SERVER, "");
	}

	/**
	 * @Description 获取存储在本地的端口号
	 * @author lipeng
	 * @return
	 * @return int
	 */
	int getPort() {
		String portStr = getResources().getStringArray(R.array.server)[(int) SPUtils.get(getActivity(), ClientSocketService.SOCKET_PORT, 0)];
		int port = Integer.valueOf(portStr.substring(portStr.indexOf("(") + 1, portStr.lastIndexOf("端口")));
		return port;
	}

	public void showConnectDlg() {
		progressDialog = ProgressDialog.show(getActivity(), null, "正在搜索服务器，请稍后~", true, true, new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				stopServer();
			}
		});
	}

	/** 
	* @Description 发送数据到服务器
	* @author lipeng
	* @param event 
	* @return void 
	*/
	public void onEventAsync(MessageEvent event) {
		try {
			if (socket != null && SocketUtil.isConnected(socket)) {
				SocketUtil.writeStr2Stream("data is " + event.data + ", time is " + event.time, socket.getOutputStream());
			} else {
				SocketClientInfo scs = new SocketClientInfo();
				scs.status = SocketClientInfo.SOCKET_SERVERCLOSED;
				EventBus.getDefault().post(scs);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

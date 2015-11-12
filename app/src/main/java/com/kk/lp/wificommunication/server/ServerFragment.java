package com.kk.lp.wificommunication.server;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.kk.lp.wificommunication.client.ClientSocketService;
import com.kk.lp.wificommunication.client.WifiUtils;

import de.greenrobot.event.EventBus;

/**
 * 
 * @Description 开启服务器端
 * @author lipeng
 * @version 2015-7-10
 * 
 */

public class ServerFragment extends BaseFragment {
	public static boolean serverRunning = false;// 服务状态标志位
	private Intent intentServer;
	Handler mHandler = new Handler();
	private Button fs_start, fs_stop, fs_select_server;
	private TextView fs_local_ip;
	// 选中的id
	private int selectWhich;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_server, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		selectWhich = (int) SPUtils.get(getActivity(), SocketListenerService.SOCKET_PORT, 0);
		fs_local_ip = (TextView) view.findViewById(R.id.fs_local_ip);
		fs_local_ip.setText("当前连接的wifi为：" + WifiUtils.getWifiName(getActivity()) + "\n服务器端本机地址的是：" + WifiUtils.getServerIP(getActivity()) + "\n默认开启的端口为：" + getPort());
		fs_start = (Button) view.findViewById(R.id.fs_start);
		fs_start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fs_local_ip.setText("当前连接的wifi为：" + WifiUtils.getWifiName(getActivity()) + "\n服务器端本机地址的是：" + WifiUtils.getServerIP(getActivity()) + "\n默认开启的端口为：" + getPort());
				serverRunning = true;
				intentServer = new Intent(getActivity(), SocketListenerService.class);
				int port = getPort();
				intentServer.putExtra(SocketListenerService.SOCKET_PORT, port);
				ServerFragment.this.getActivity().startService(intentServer);
				fs_start.setEnabled(false);
			}
		});
		fs_stop = (Button) view.findViewById(R.id.fs_stop);
		fs_stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopServer();
			}
		});
		fs_select_server = (Button) view.findViewById(R.id.fs_select_server);
		fs_select_server.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showServerDialog();
			}
		});
	}

	/**
	 * @Description 服务器配置选项
	 * @author lipeng
	 * @return void
	 */
	public void showServerDialog() {

		Builder builder = new AlertDialog.Builder(ServerFragment.this.getActivity());
		// 设置对话框的标题
		builder.setTitle("请设置默认开启的服务器");
		// 0: 默认第一个单选按钮被选中
		builder.setSingleChoiceItems(R.array.server, (int) SPUtils.get(getActivity(), SocketListenerService.SOCKET_PORT, 0), new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				selectWhich = which;
			}
		});

		// 添加一个确定按钮
		builder.setPositiveButton("确 定 ", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 存储默认开启的服务器
				SPUtils.put(ServerFragment.this.getActivity(), SocketListenerService.SOCKET_PORT, selectWhich);
				fs_local_ip.setText("当前连接的wifi为：" + WifiUtils.getWifiName(getActivity()) + "\n服务器端本机地址的是：" + WifiUtils.getServerIP(getActivity()) + "\n默认开启的端口为：" + getPort());
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
		serverRunning = false;
		fs_start.setEnabled(true);
		if (intentServer != null) {
			ServerFragment.this.getActivity().stopService(intentServer);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	public void onStop() {
		EventBus.getDefault().unregister(this);
		super.onStop();
	}

	/** 
	* @Description 服务器端当前socket状态
	* @author lipeng
	* @param event 
	* @return void 
	*/
	public void onEventMainThread(SocketServerInfo event) {
		if(event.status == SocketServerInfo.SOCKET_CREATE_FAILED){
			Toast.makeText(getActivity(), "端口绑定失败，请更换端口重新绑定！", Toast.LENGTH_SHORT).show();
		}else if(event.status == SocketServerInfo.SOCKET_CREATED){
			Toast.makeText(getActivity(), "端口绑定成功，开始监听客户端连接", Toast.LENGTH_SHORT).show();
		}else if(event.status == SocketServerInfo.SOCKET_CLIENT_CONNECT){
			Toast.makeText(getActivity(), "当前连接的客户端数为：" + event.clientCount, Toast.LENGTH_SHORT).show();
		}
	}

	
	/** 
	* @Description 客户端发送的消息
	* @author lipeng
	* @param event 
	* @return void 
	*/
	public void onEventMainThread(ClientInfo event) {
		if (event.msgType == ClientInfo.MSG_NORMAL) {
			Toast.makeText(getActivity(), event.msg, Toast.LENGTH_SHORT).show();
		} else if (event.msgType == ClientInfo.MSG_DATA) {
			Toast.makeText(getActivity(), event.data, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(), "消息：" + event.msg + "\n数据：" + event.data, Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * @Description 获取要监听的端口
	 * @author lipeng
	 * @return
	 * @return int
	 */
	public int getPort() {
		// 开启的服务器端口，默认的是第一个服务器端口
		String portStr = getResources().getStringArray(R.array.server)[(int) SPUtils.get(getActivity(), SocketListenerService.SOCKET_PORT, 0)];
		int port = Integer.valueOf(portStr.substring(portStr.indexOf("(") + 1, portStr.lastIndexOf("端口")));
		return port;
	}

}

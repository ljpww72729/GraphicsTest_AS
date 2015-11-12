package com.kk.lp.wificommunication.client;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.kk.lp.wificommunication.server.SocketUtil;

import de.greenrobot.event.EventBus;

/**
 *
 * @Description 
 * @author lipeng
 * @version 2015-8-10
 * 
 */

public class ReadDataService extends Service {
	
	private Timer timer;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
				timer = new Timer();
				timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						EventBus.getDefault().post(new MessageEvent(new Random().nextInt(1000) + "", SocketUtil.getNowTime()));
					}
				}, 1000, 3000);
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		timer.cancel();
		super.onDestroy();
	}

}

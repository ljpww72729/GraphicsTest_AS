package com.kk.lp.view;

import android.app.Activity;
import android.view.ViewConfiguration;

/**
 *
 * @Description 
 * @author lipeng
 * @version 2015-8-12
 * 
 */

public class KeyActivity extends Activity{

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		super.onUserInteraction();
		ViewConfiguration.getTapTimeout();
		ViewConfiguration.getLongPressTimeout();
	}
	
}

package com.kk.lp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.kk.lp.annotation.AnnotationFragment_;
import com.kk.lp.async.AsyncFragment;
import com.kk.lp.button.ButtonFragment;
import com.kk.lp.graphicstest.ArcsFragment;
import com.kk.lp.graphicstest.BezierFragment;
import com.kk.lp.graphicstest.CameraFragment;
import com.kk.lp.graphicstest.DoubleBitmapDraw;
import com.kk.lp.graphicstest.FilePathFragment;
import com.kk.lp.graphicstest.LayerFragment;
import com.kk.lp.graphicstest.PathFragment;
import com.kk.lp.graphicstest.ProgressViewFragment;
import com.kk.lp.graphicstest.RadiusButtonViewFragment;
import com.kk.lp.graphicstest.webview.WebviewFragment;
import com.kk.lp.textview.TextViewLongClick;
import com.kk.lp.wificommunication.client.ClientFragment;
import com.kk.lp.wificommunication.server.ServerFragment;

public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new ArcsFragment();
			break;
		case 1:
			fragment = new LayerFragment();
			break;
		case 2:
			fragment = new PathFragment();
			break;
		case 3:
			fragment = new DoubleBitmapDraw();
			break;
		case 4:
			fragment = new BezierFragment();
			break;
		case 5:
			fragment = new ProgressViewFragment();
			break;
		case 6:
			fragment = new RadiusButtonViewFragment();
			break;
		case 7:
			fragment = new CameraFragment();
			break;
		case 8:
			fragment = new WebviewFragment();
			break;
		case 9:
			fragment = new AsyncFragment();
			break;
		case 10:
			fragment = new ButtonFragment();
			break;
		case 11:
			fragment = new ServerFragment();
			break;
		case 12:
			fragment = new AnnotationFragment_();
			break;
		case 13:
			fragment = new ServerFragment();
			break;
		case 14:
			fragment = new ClientFragment();
			break;
		case 15:
			fragment = new FilePathFragment();
			break;
		case 16:
			fragment = new TextViewLongClick();
			break;
			
		default:
			break;
		}
		fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	
}

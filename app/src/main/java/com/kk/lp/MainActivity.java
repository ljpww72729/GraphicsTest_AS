package com.kk.lp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kk.lp.animation.AnimationFragment;
import com.kk.lp.annotation.AnnotationFragment_;
import com.kk.lp.async.AsyncFragment;
import com.kk.lp.button.ButtonFragment;
import com.kk.lp.deep_link.OpenWithBrowserFragment;
import com.kk.lp.deep_link.StartOtherAppActivity;
import com.kk.lp.deviceInfo.DeviceInfoActivity;
import com.kk.lp.deviceInfo.InstalledAppListFragment;
import com.kk.lp.facebookchathead.ServiceChatHead;
import com.kk.lp.glide.ImgWithGlideFragment;
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
import com.kk.lp.loader.AsyncTaskLoaderFragment;
import com.kk.lp.material_design.MaterialDetailActivity;
import com.kk.lp.mvp.MVPActivity;
import com.kk.lp.percentlayout.PercentLayoutFragment;
import com.kk.lp.popupmenuandwindow.PopupMenuFragment;
import com.kk.lp.scrollview.XScrollViewFragment;
import com.kk.lp.slidingpane.SlidingPaneActivity;
import com.kk.lp.support23_2.VectorDrawableActivity;
import com.kk.lp.support_lib_23_2.VectorDrawableLibActivity;
import com.kk.lp.textview.FocusChangeEditFragment;
import com.kk.lp.textview.TextViewLongClick;
import com.kk.lp.touch.TouchGestureDetectorFragment;
import com.kk.lp.view.CustomViewFragment;
import com.kk.lp.viewdrag.MyDrawerLayoutActivity;
import com.kk.lp.viewdrag.ViewDragActivity;
import com.kk.lp.volley.VolleyFragment;
import com.kk.lp.wificommunication.client.ClientFragment;
import com.kk.lp.wificommunication.server.ServerFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int MY_PERMISSIONS_REQUEST_SYSTEM_ALTER_WINDOW = 0;
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        Log.i(TAG, "onCreate: " + Build.MODEL);
    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;

        if (id == R.id.ArcsFragment) {
            fragment = new ArcsFragment();
            // Handle the camera action
        } else if (id == R.id.LayerFragment) {
            fragment = new LayerFragment();
        } else if (id == R.id.PathFragment) {
            fragment = new PathFragment();
        } else if (id == R.id.DoubleBitmapDraw) {
            fragment = new DoubleBitmapDraw();
        } else if (id == R.id.BezierFragment) {
            fragment = new BezierFragment();
        } else if (id == R.id.ProgressViewFragment) {
            fragment = new ProgressViewFragment();
        } else if (id == R.id.RadiusButtonViewFragment) {
            fragment = new RadiusButtonViewFragment();
        } else if (id == R.id.CameraFragment) {
            fragment = new CameraFragment();
        } else if (id == R.id.WebviewFragment) {
            fragment = new WebviewFragment();
        } else if (id == R.id.AsyncFragment) {
            fragment = new AsyncFragment();
        } else if (id == R.id.ButtonFragment) {
            fragment = new ButtonFragment();
        } else if (id == R.id.AnnotationFragment_) {
            fragment = new AnnotationFragment_();
        } else if (id == R.id.ServerFragment) {
            fragment = new ServerFragment();
        } else if (id == R.id.ClientFragment) {
            fragment = new ClientFragment();
        } else if (id == R.id.FilePathFragment) {
            fragment = new FilePathFragment();
        } else if (id == R.id.TextViewLongClick) {
            fragment = new TextViewLongClick();
        } else if (id == R.id.PercentLayoutFragment) {
            fragment = PercentLayoutFragment.newInstance();
        } else if (id == R.id.PopupMenuFragment) {
            fragment = PopupMenuFragment.newInstance();
        } else if (id == R.id.XScrollViewFragment) {
            fragment = XScrollViewFragment.newInstance();
        } else if (id == R.id.ViewDragFragment) {
//            fragment = ViewDragFragment.newInstance();
            Intent intent = new Intent(this, ViewDragActivity.class);
            startActivity(intent);
        } else if (id == R.id.TouchGestureDetectorFragment) {
            fragment = TouchGestureDetectorFragment.newInstance();
        } else if (id == R.id.FABFragment) {
            Intent intent = new Intent(this, MaterialDetailActivity.class);
            startActivity(intent);
        } else if (id == R.id.vectorDrawable) {
            Intent intent = new Intent(this, VectorDrawableActivity.class);
            startActivity(intent);
        } else if (id == R.id.chatHead) {
            showOverlayWindowView();
        } else if (id == R.id.vectorDrawableActivity) {
            Intent intent = new Intent(this, VectorDrawableLibActivity.class);
            startActivity(intent);
        } else if (id == R.id.glide_img) {
            fragment = ImgWithGlideFragment.newInstance();
        } else if (id == R.id.async_task_loader) {
            fragment = AsyncTaskLoaderFragment.newInstance();
        } else if (id == R.id.mvp) {
            Intent intent = new Intent(this, MVPActivity.class);
            startActivity(intent);
        } else if (id == R.id.focusChange) {
            fragment = FocusChangeEditFragment.newInstance();
        } else if (id == R.id.myDrawerLayoutActivity) {
            Intent intent = new Intent(this, MyDrawerLayoutActivity.class);
            startActivity(intent);
        } else if (id == R.id.slidingPaneActivity) {
            Intent intent = new Intent(this, SlidingPaneActivity.class);
            startActivity(intent);
        } else if (id == R.id.volley) {
            fragment = VolleyFragment.newInstance();
        } else if (id == R.id.custom_linearlayout) {
            fragment = CustomViewFragment.newInstance();
        } else if (id == R.id.AnimationFragment) {
            fragment = AnimationFragment.newInstance();
        } else if (id == R.id.start_other_app) {
            Intent intent = new Intent(this, StartOtherAppActivity.class);
            startActivity(intent);
        } else if (id == R.id.device_info) {
            Intent intent = new Intent(this, DeviceInfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.installed_app_list) {
            fragment = InstalledAppListFragment.newInstance();
        } else if (id == R.id.openwithbrowserfragment) {
            fragment = OpenWithBrowserFragment.newInstance();
        }

        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService(new Intent(this, ServiceChatHead.class));
    }

    public void showOverlayWindowView() {
        //判断是否有绘制悬浮窗口的权限：SYSTEM_ALERT_WINDOW，针对版本23之后
        //参考：http://developer.android.com/reference/android/Manifest.permission.html#SYSTEM_ALERT_WINDOW
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                //如果有该权限，则开启悬浮窗口
                startService(new Intent(this, ServiceChatHead.class));
            } else {
                //如果没有，则需要开启权限授权activity，让用户手动打开绘制悬浮窗口的权限
                //参考：http://developer.android.com/reference/android/provider/Settings.html#ACTION_MANAGE_OVERLAY_PERMISSION
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        } else {
            //有一些系统会使用内置软件禁止悬浮窗口的展示，需要手动打开
            //版本<23
            //判断是否有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.SYSTEM_ALERT_WINDOW)
                    != PackageManager.PERMISSION_GRANTED) {
                //没有权限

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.SYSTEM_ALERT_WINDOW)) {


                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Snackbar.make(mDrawerLayout, "请授予绘制悬浮窗口的权限",
                            Snackbar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
                                    MY_PERMISSIONS_REQUEST_SYSTEM_ALTER_WINDOW);
                        }
                    })
                            .show();
                } else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
                            MY_PERMISSIONS_REQUEST_SYSTEM_ALTER_WINDOW);
                }
            } else {
                startService(new Intent(this, ServiceChatHead.class));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SYSTEM_ALTER_WINDOW: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "已授予绘制悬浮窗口的权限", Toast.LENGTH_LONG).show();
                    startService(new Intent(this, ServiceChatHead.class));
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "未授予绘制悬浮窗口的权限", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Notice that your app receives the onTrimMemory() callback with TRIM_MEMORY_UI_HIDDEN only
     * when all the UI components of your app process become hidden from the user.
     * 与onstop()不同之处在于，onstop()在跳转到其他页面时也会被调用，而onTrimMemory()只会在应用进程所有组件 都被的情况下才会被触发调用
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}

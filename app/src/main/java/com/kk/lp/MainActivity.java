package com.kk.lp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
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
import com.kk.lp.material_design.MaterialDetailActivity;
import com.kk.lp.percentlayout.PercentLayoutFragment;
import com.kk.lp.popupmenuandwindow.PopupMenuFragment;
import com.kk.lp.scrollview.XScrollViewFragment;
import com.kk.lp.support_lib_23_2.VectorDrawableActivity;
import com.kk.lp.textview.TextViewLongClick;
import com.kk.lp.touch.TouchGestureDetectorFragment;
import com.kk.lp.viewdrag.ViewDragFragment;
import com.kk.lp.wificommunication.client.ClientFragment;
import com.kk.lp.wificommunication.server.ServerFragment;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //该处声明Android-Iconics，只有在layout布局文件对应的activity中声明，图片才能加载出来
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
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
            super.onBackPressed();
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
        }else if (id == R.id.RadiusButtonViewFragment) {
            fragment = new RadiusButtonViewFragment();
        }else if (id == R.id.CameraFragment) {
            fragment = new CameraFragment();
        }else if (id == R.id.WebviewFragment) {
            fragment = new WebviewFragment();
        }else if (id == R.id.AsyncFragment) {
            fragment = new AsyncFragment();
        }else if (id == R.id.ButtonFragment) {
            fragment = new ButtonFragment();
        }else if (id == R.id.AnnotationFragment_) {
            fragment = new AnnotationFragment_();
        }else if (id == R.id.ServerFragment) {
            fragment = new ServerFragment();
        }else if (id == R.id.ClientFragment) {
            fragment = new ClientFragment();
        }else if (id == R.id.FilePathFragment) {
            fragment = new FilePathFragment();
        }else if (id == R.id.TextViewLongClick) {
            fragment = new TextViewLongClick();
        }else if (id == R.id.PercentLayoutFragment) {
            fragment = PercentLayoutFragment.newInstance();
        }else if (id == R.id.PopupMenuFragment) {
            fragment = PopupMenuFragment.newInstance();
        }else if (id == R.id.XScrollViewFragment) {
            fragment = XScrollViewFragment.newInstance();
        }else if (id == R.id.ViewDragFragment) {
            fragment = ViewDragFragment.newInstance();
        }else if (id == R.id.TouchGestureDetectorFragment) {
            fragment = TouchGestureDetectorFragment.newInstance();
        }else if (id == R.id.FABFragment) {
            Intent intent = new Intent(this, MaterialDetailActivity.class);
            startActivity(intent);
        }else if (id == R.id.vectorDrawable) {
            Intent intent = new Intent(this, VectorDrawableActivity.class);
            startActivity(intent);
        }
if (fragment != null){
    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

package com.kk.lp.popupmenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.internal.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;

import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PopupMenuFragment extends BaseFragment {


    @Bind(R.id.show)
    Button show;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static PopupMenuFragment newInstance() {
        PopupMenuFragment fragment = new PopupMenuFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_popup_menu_fragment, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.show)
    public void show(Button show){
        PopupMenu popupMenu = new PopupMenu(this.getActivity(), show);
        popupMenu.inflate(R.menu.options_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.dictLookup:
                        Toast.makeText(PopupMenuFragment.this.getActivity(), "dictLookup", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.readFromHere:
                        Toast.makeText(PopupMenuFragment.this.getActivity(), "readFromHere", Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }
        });
        show.setOnTouchListener(popupMenu.getDragToOpenListener());
        //使用反射，强制显示菜单图标
        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popupMenu);
            mHelper.setForceShowIcon(true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        popupMenu.show();
    }
}

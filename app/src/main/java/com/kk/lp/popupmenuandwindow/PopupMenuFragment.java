package com.kk.lp.popupmenuandwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PopupMenuFragment extends BaseFragment {


    @Bind(R.id.show)
    Button show;
    @Bind(R.id.show_popupwindow)
    Button show_popupwindow;
    private Context mContext;
    private PopupWindow popupWindow = null;
    private int width = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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
        initPopupWindow();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.show)
    public void show(Button show) {
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

    @OnClick(R.id.show_popupwindow)
    public void showPopupWindow(Button showPopupWindow) {
        changePopupWindowStatus(true);
    }

    public void initPopupWindow() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popupwindow, null);
        Button btn_pw = (Button) view.findViewById(R.id.btn_pw);
        btn_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "show", Toast.LENGTH_SHORT).show();
                changePopupWindowStatus(false);
            }
        });
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });
        //必须加上此方法才能点击外面区域使popupwindow消失
        //请参考：http://www.cnblogs.com/mengdd/p/3569127.html
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //21版本级以上才能调用下面的方法
            popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent, null));
        } else {
            popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        }
        //如果想获取PopupWindow对象实际布局大小的值，则可以采用如下代码：
        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int viewWidth = view.getMeasuredWidth();
        int viewHeight = view.getMeasuredHeight();
        popupWindow.setWidth(viewWidth);
        popupWindow.setHeight(viewHeight);
    }

    /**
     * 改变popupwindow的显示与隐藏
     *
     * @param show
     */
    public void changePopupWindowStatus(boolean show) {
        if (popupWindow != null) {
            if (show && !popupWindow.isShowing()) {
                int anchorWidth = show_popupwindow.getWidth();
                width = popupWindow.getWidth();
                //使显示的pupupWindow右边margin为20px
                popupWindow.showAsDropDown(show_popupwindow, -width + anchorWidth - 20, -10);
                //获取空间在屏幕中的位置坐标
//                int[] location = new int[2];
//                show_popupwindow.getLocationOnScreen(location);
//                popupWindow.showAtLocation(show_popupwindow, Gravity.NO_GRAVITY,location[0] - 10,location[1] + show_popupwindow.getHeight());
            } else if (!show && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    }
    @OnClick(R.id.listpopupwindow)
    public void listpopupwindowClick(Button btn){
        ListPopupWindow lpw = new ListPopupWindow(mContext);
        lpw.setAnchorView(btn);
        LinkedList<HashMap<String,String>> data = new LinkedList<HashMap<String, String>>();
        HashMap<String,String> one = new HashMap<>();
        one.put("key","保存订单");
        data.add(one);
        HashMap<String,String> two = new HashMap<>();
        two.put("key","提交");
        data.add(two);
        String[] from = new String[]{"key"};
        int[] to = new int[]{android.R.id.text1};
        ListAdapter listAdapter = new SimpleAdapter(mContext,data,android.R.layout.simple_list_item_1,from,to);
        lpw.setAdapter(listAdapter);
        lpw.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        lpw.setVerticalOffset(20);
        lpw.setPromptView(LayoutInflater.from(mContext).inflate(R.layout.xlistview_header, null));
        lpw.show();
    }

}

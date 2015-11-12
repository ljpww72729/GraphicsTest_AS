package com.kk.lp.textview;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;

/**
 *
 * @Description 
 * @author lipeng
 * @version 2015-10-10
 * 
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TextViewLongClick extends BaseFragment {

    //private EditText ed;
    private TextView ed;

    //actionmode callback.
    private ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallback;

    @SuppressLint("NewApi")
	@Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.activity_hello_txt_view, null);
        ed = (TextView) view.findViewById(R.id.txtview);
        initActionModeCallbacks();
        ed.setTextIsSelectable(true);
        ed.setCustomSelectionActionModeCallback(mActionModeCallback);

        ed.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Log.v(this.toString(), "Long click.");
                ed.setCursorVisible(true);
                mActionMode = getActivity().startActionMode(mActionModeCallback);  
                v.setSelected(true);
                return false;
            }
        });
    	return view;
    }
    
    @SuppressLint("NewApi")
	public void initActionModeCallbacks() {

        /*
         * This function initializes the callbacks.
         */

        mActionModeCallback = new ActionMode.Callback() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                //nothing to do here.
                Log.v(this.toString(), "Preparing action mode.");
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                Log.v(this.toString(), "Destroy action mode.");
                //mActionModeCallback = null;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                Log.v(this.toString(), "Creating new action mode menu.");
                //inflate a new menu.
                menu.clear();
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.options_menu, menu);
                Log.v(this.toString(), "Done inflating menu.");
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                Log.v(this.toString(), "An item was clicked.");
                switch(item.getItemId()) {
                case R.id.dictLookup:
                    Log.v(this.toString(), "Look up dictionary.");
                    break;

                case R.id.readFromHere:
                    Log.v(this.toString(), "Start reading from here:" + ed.getSelectionStart());
                }
                return false;
            }
        };
    }
}

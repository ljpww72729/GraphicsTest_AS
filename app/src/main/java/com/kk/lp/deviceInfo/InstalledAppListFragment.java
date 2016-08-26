package com.kk.lp.deviceInfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kk.lp.BaseFragment;
import com.kk.lp.R;
import com.kk.lp.databinding.FragmentInstalledAppListBinding;
import com.kk.lp.databinding.ItemInstalledAppInfoBinding;

import java.util.List;

public class InstalledAppListFragment extends BaseFragment {

    private List<ResolveInfo> mApps;
    private FragmentInstalledAppListBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void loadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = this.getActivity().getPackageManager().queryIntentActivities(mainIntent, 0);
        binding.installedAppRecycler.setAdapter(new MyAdapter(mApps));
    }

    public static InstalledAppListFragment newInstance() {
        Bundle args = new Bundle();
        InstalledAppListFragment fragment = new InstalledAppListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_installed_app_list, null, false);
        binding.installedAppRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        binding.installedAppRecycler.setLayoutManager(linearLayoutManager);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadApps();
    }
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ResolveInfo> appInfo;

    public MyAdapter(List<ResolveInfo> appInfo) {
        this.appInfo = appInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemInstalledAppInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_installed_app_info, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.viewDataBinding.appName.setText(appInfo.get(position).activityInfo.applicationInfo.loadLabel(holder.viewDataBinding.getRoot().getContext().getPackageManager()));
        holder.viewDataBinding.appName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "";
                ActivityInfo activityInfo = appInfo.get(position).activityInfo;
                info += activityInfo.packageName + "\n";
                AlertDialog alertDialog = new AlertDialog.Builder(holder.viewDataBinding.getRoot().getContext())
                        .setIcon(activityInfo.loadIcon(v.getContext().getPackageManager()))
                        .setTitle(activityInfo.applicationInfo.loadLabel(v.getContext().getPackageManager()))
                        .setMessage(info).setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appInfo.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemInstalledAppInfoBinding viewDataBinding;

        public ViewHolder(ItemInstalledAppInfoBinding binding) {
            super(binding.getRoot());
            viewDataBinding = binding;
        }
    }
}
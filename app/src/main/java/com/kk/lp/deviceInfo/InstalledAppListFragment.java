package com.kk.lp.deviceInfo;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.kk.lp.BaseFragment;
import com.kk.lp.R;
import com.kk.lp.databinding.FragmentInstalledAppListBinding;
import com.kk.lp.databinding.ItemInstalledAppInfoBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import static com.android.volley.VolleyLog.TAG;

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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray = getListOfApps();
                Log.i(TAG, "run: " + jsonArray);
                List<AppEntity> list = getRecentAppList();
                Log.i(TAG, "run: list=" + list.size());
                List<AppEntity> list2 = queryAllRunningAppInfo();
                Log.i(TAG, "run: list2=" + list2.size());
                List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();
                Log.i(TAG, "run: androidprocesses=" + processes.size());

            }
        });
        thread.start();
    }

    public JSONArray getListOfApps() {
        JSONArray arr = new JSONArray();
        PackageManager pm = getActivity().getPackageManager();

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        if (packages != null) {
            for (ApplicationInfo appInfo : packages) {
                //过滤掉系统应用
                if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
                    JSONObject packObj = new JSONObject();
                    try {
                        //获得应用名称
                        CharSequence labelCs = appInfo.loadLabel(pm);
                        String label = labelCs == null ? null : labelCs.toString();
                        if (label != null)
                            packObj.put("name", label);
                        //获得应用的包名
                        String packName = appInfo.packageName;
                        if (packName != null) {
                            packObj.put("packageName", packName);
                            String uriScheme = getURIScheme(packName);
                            if (!uriScheme.equals(""))
                                packObj.put("URI Scheme", uriScheme);
                        }
                        //获取sourceDir中的公开部分的完整路径（包括资源和manifest）
                        String pSourceDir = appInfo.publicSourceDir;
                        if (pSourceDir != null)
                            packObj.put("public_source_dir", pSourceDir);
                        String sourceDir = appInfo.sourceDir;
                        if (sourceDir != null)
                            packObj.put("source_dir", sourceDir);

                        PackageInfo packInfo = pm.getPackageInfo(appInfo.packageName, PackageManager.GET_PERMISSIONS);
                        if (packInfo != null) {
                            if (packInfo.versionCode >= 9) {
                                //获取应用安装时间
                                packObj.put("install_date", packInfo.firstInstallTime);
                                //获取应用上次更新时间，若未更新过，则与安装时间相同
                                packObj.put("last_update_date", packInfo.lastUpdateTime);
                            }
                            //编译版本号
                            packObj.put("version_code", packInfo.versionCode);
                            //用户版本号
                            if (packInfo.versionName != null)
                                packObj.put("version_name", packInfo.versionName);
                        }

                        arr.put(packObj);
                    } catch (JSONException | PackageManager.NameNotFoundException ignore) {
                    }
                }
            }
        }
        return arr;
    }

    public String getURIScheme(String packageName) {
        String scheme = "";
        if (!isLowOnMemory()) {
            PackageManager pm = getActivity().getPackageManager();
            try {
                ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
                String sourceApk = ai.publicSourceDir;
                JarFile jf = null;
                InputStream is = null;
                byte[] xml;
                try {
                    jf = new JarFile(sourceApk);
                    is = jf.getInputStream(jf.getEntry("AndroidManifest.xml"));
                    xml = new byte[is.available()];
                    //noinspection ResultOfMethodCallIgnored
                    is.read(xml);
                    scheme = new ApkParser().decompressXML(xml);
                } catch (Exception ignored) {
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                            // noinspection unused
                            is = null;
                        }
                        if (jf != null) {
                            jf.close();
                        }
                    } catch (IOException ignored) {
                    }
                }
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }
        return scheme;
    }

    /**
     * <p>Checks the current device's {@link ActivityManager} system service and returns the value
     * of the lowMemory flag.</p>
     *
     * @return <p>A {@link Boolean} value representing the low memory flag of the current device.</p>
     * <p>
     * <ul>
     * <li><i>true</i> - the free memory on the current device is below the system-defined threshold
     * that triggers the low memory flag.</li>
     * <li><i>false</i> - the device has plenty of free memory.</li>
     * </ul>
     */
    private boolean isLowOnMemory() {
        ActivityManager activityManager = (ActivityManager)getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(mi);
        return mi.lowMemory;
    }

    /**
     * ?????????
     * @return
     */
    public List<AppEntity> getRecentAppList() {
        List<AppEntity> list = new ArrayList<>();
        ActivityManager mActivityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RecentTaskInfo> recentTasks = mActivityManager.getRecentTasks(10, 0);
        for (ActivityManager.RecentTaskInfo taskInfo : recentTasks) {

            Intent intent = taskInfo.baseIntent;
            ResolveInfo resolveInfo = getActivity().getPackageManager().resolveActivity(intent, 0);
            if (resolveInfo == null)continue;

            if (isSystemApp(resolveInfo)) continue;

            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if(activityInfo==null)continue;

            AppEntity entity = new AppEntity();
            entity.setAppIcon(drawableToBitmap(resolveInfo.loadIcon(getActivity().getPackageManager())));
            entity.setAppName(resolveInfo.loadLabel(getActivity().getPackageManager()).toString());
            entity.setPackageName(activityInfo.packageName);

            ApplicationInfo applicationInfo = activityInfo.applicationInfo;
            if (applicationInfo == null)continue;

            if(applicationInfo.publicSourceDir!= null){
                entity.setSrcPath(applicationInfo.publicSourceDir);
            }
            list.add(entity);
        }
        return list;
    }


    public boolean isSystemApp(ResolveInfo resolveInfo) {
        if (resolveInfo == null) return false;
        try {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo == null) return false;

            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(activityInfo.packageName, PackageManager.GET_ACTIVITIES);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if(applicationInfo == null)return false;

            return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
        }
        return false;
    }
    /**
     * 将Drawable转化为Bitmap
     * @param drawable
     * @return
     */
    private static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    //////////////////////// Android L ///////////////////////////////////
    //需要特殊权限
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public List<UsageStats> getUsageStatsList(){
        UsageStatsManager usm = getUsageStatsManager();
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, -1);
        long startTime = calendar.getTimeInMillis();
        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
        return usageStatsList;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public List<AppEntity> getRecentAppInfo(){
        List<UsageStats> usageStatsList = getUsageStatsList();
        List<AppEntity> list = new ArrayList<>();
        for (UsageStats u : usageStatsList){
            String packageName = u.getPackageName();
            ApplicationInfo applicationInfo = getAppInfo(packageName);
            //系统引用不加入最近列表
            if(!isUserApp(applicationInfo))continue;
            AppEntity entity = warpAppEntity(applicationInfo);
            if (entity == null)continue;
            list.add (entity);
        }
        return list;
    }

    @SuppressWarnings("ResourceType")
    private UsageStatsManager getUsageStatsManager(){
        UsageStatsManager usm = (UsageStatsManager) this.getActivity().getSystemService("usagestats");
        return usm;
    }

    //根据包名获取对应的ApplicationInfo 信息
    private ApplicationInfo getAppInfo(String packageName){
        ApplicationInfo appInfo = null;
        try {
            appInfo = getActivity().getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return appInfo;
    }

    boolean isUserApp(ApplicationInfo ai) {
        if(ai == null)return false;
        int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
        return (ai.flags & mask) == 0;
    }
    // 将ApplicationInfo转化为AppEntity
    private AppEntity warpAppEntity(ApplicationInfo appInfo){
        if (appInfo == null)return  null;
        AppEntity entity = new AppEntity();
        entity.setAppName(appInfo.loadLabel(getActivity().getPackageManager()).toString());
        entity.setPackageName(appInfo.packageName);
        entity.setAppIcon(drawableToBitmap(appInfo.loadIcon(getActivity().getPackageManager())));
        entity.setSrcPath(appInfo.sourceDir);
        return entity;
    }

    // 查询所有正在运行的应用程序信息： 包括他们所在的进程id和进程名
    // 这儿我直接获取了系统里安装的所有应用程序，然后根据报名pkgname过滤获取所有真正运行的应用程序
    private List<AppEntity> queryAllRunningAppInfo() {
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> listAppcations = getActivity().getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(listAppcations,new ApplicationInfo.DisplayNameComparator(getActivity().getPackageManager()));// 排序

        // 保存所有正在运行的包名 以及它所在的进程信息
        Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<String, ActivityManager.RunningAppProcessInfo>();

        ActivityManager mActivityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
                .getRunningAppProcesses();
        Log.i(TAG, "queryAllRunningAppInfo: " + appProcessList.size());

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
            int pid = appProcess.pid; // pid
            String processName = appProcess.processName; // 进程名
            Log.i(TAG, "processName: " + processName + "  pid: " + pid);

            String[] pkgNameList = appProcess.pkgList; // 获得运行在该进程里的所有应用程序包

            // 输出所有应用程序的包名
            for (int i = 0; i < pkgNameList.length; i++) {
                String pkgName = pkgNameList[i];
                Log.i(TAG, "packageName " + pkgName + " at index " + i+ " in process " + pid);
                // 加入至map对象里
                pgkProcessAppMap.put(pkgName, appProcess);
            }
        }
        // 保存所有正在运行的应用程序信息
        List<AppEntity> runningAppInfos = new ArrayList<AppEntity>(); // 保存过滤查到的AppInfo

        for (ApplicationInfo app : listAppcations) {
            // 如果该包名存在 则构造一个RunningAppInfo对象
            if (pgkProcessAppMap.containsKey(app.packageName)) {
                // 获得该packageName的 pid 和 processName
                int pid = pgkProcessAppMap.get(app.packageName).pid;
                String processName = pgkProcessAppMap.get(app.packageName).processName;
                runningAppInfos.add(getAppInfo(app, pid, processName));
            }
        }

        return runningAppInfos;

    }
    // 某一特定经常里所有正在运行的应用程序
    private List<AppEntity> querySpecailPIDRunningAppInfo(Intent intent , int pid) {


        String[] pkgNameList = intent.getStringArrayExtra("EXTRA_PKGNAMELIST");
        String processName = intent.getStringExtra("EXTRA_PROCESS_NAME");


        // 保存所有正在运行的应用程序信息
        List<AppEntity> runningAppInfos = new ArrayList<AppEntity>(); // 保存过滤查到的AppInfo

        for(int i = 0 ; i<pkgNameList.length ;i++){
            //根据包名查询特定的ApplicationInfo对象
            ApplicationInfo appInfo;
            try {
                appInfo = getActivity().getPackageManager().getApplicationInfo(pkgNameList[i], 0);
                runningAppInfos.add(getAppInfo(appInfo, pid, processName));
            }
            catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  // 0代表没有任何标记;
        }
        return runningAppInfos ;
    }


    // 构造一个RunningAppInfo对象 ，并赋值
    private AppEntity getAppInfo(ApplicationInfo app, int pid, String processName) {
        AppEntity appInfo = new AppEntity();
        appInfo.setAppName((String) app.loadLabel(getActivity().getPackageManager()));
        appInfo.setAppIcon(drawableToBitmap(app.loadIcon(getActivity().getPackageManager())));
        appInfo.setPackageName(app.packageName);

//        appInfo.setPid(pid);
//        appInfo.setProcessName(processName);

        return appInfo;
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
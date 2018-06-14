package com.kk.lp.deep_link;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.util.TypedValue;

/**
 * Android系统相关工具类
 *
 * Created by LinkedME06 on 13/01/2017.
 */

public class AndroidUtils {

    /**
     * dp转px
     *
     * @param context Context
     * @param dp      dp
     * @return px
     */
    public static int convertDpToPixels(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context Context
     * @param sp      sp
     * @return px
     */
    public static int convertSpToPixels(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * 是否存在某个service
     *
     * @param mContext    Context
     * @param serviceName Service Name
     * @return true：存在 false：不存在
     */
    public static boolean hasService(Context mContext, String serviceName) {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_SERVICES);
            ServiceInfo[] serviceInfos = info.services;
            if (serviceInfos != null) {
                for (ServiceInfo serviceInfo : serviceInfos) {

                    if (serviceInfo.name.equals(serviceName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 判断是否可以处理该intent
     *
     * @param mContext Context
     * @param intent   Intent
     * @return true:存在 false:不存在
     */
    public static boolean resolveIntent(Context mContext, Intent intent) {
        ResolveInfo resolveInfo = mContext.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo != null;
    }


}

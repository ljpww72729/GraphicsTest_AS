package com.kk.lp.utils;

import android.util.Log;

public class UtilsLog {

	private static final boolean LOG = true;
	public static final boolean LOGTOAST = false;
	private static final boolean LOGFF = false;
	private static final String NULL_STR = "msg is null!";

	public static void v(String tag, String msg) {
		if (LOG)
			Log.v(tag, msg != null ? msg : NULL_STR);

	}

	public static void v(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.v(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void d(String tag, String msg) {
		if (LOG)
			Log.d(tag, msg != null ? msg : NULL_STR);
	}
    public static void d(String tag, int msg) {
        if (LOG)
            Log.d(tag,String.valueOf(msg));
    }

	public static void d(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.d(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void i(String tag, String msg) {
		if (LOG)
			Log.i(tag, msg != null ? msg : NULL_STR);
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.i(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void w(String tag, String msg) {
		if (LOG)
			Log.w(tag, msg != null ? msg : NULL_STR);
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.w(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void e(String tag, String msg) {
		if (LOG)
			Log.e(tag, msg != null ? msg : NULL_STR);
	}

	public static void e(String tag, String msg, Throwable tr) {
		try {
			if (LOG)
				Log.e(tag, msg != null ? msg : NULL_STR, tr);
			if (LOGFF) {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

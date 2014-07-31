package com.bing.support.debug;

import android.util.Log;

import com.bing.friendplace.BuildConfig;

public class AppLog {

	public static void i(String tag,String msg){
		if (BuildConfig.DEBUG) {
			Log.i(tag, msg);
		}
	}
	
	public static void d(String tag,String msg){
		if (BuildConfig.DEBUG) {
			Log.d(tag, msg);
		}
	}
	
	public static void w(String tag,String msg){
		if (BuildConfig.DEBUG) {
			Log.w(tag, msg);
		}
	}
	
	public static void e(String tag,String msg){
		if (BuildConfig.DEBUG) {
			Log.e(tag, msg);
		}
	}
	
}

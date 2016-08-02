package com.m.h5hall;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.BitmapUtils;
import com.m.h5hall.comm.data.UserData;
import com.m.h5hall.main.activity.LoginActivity;
import com.m.ms.api.pay.GPay;
import com.umeng.analytics.AnalyticsConfig;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

public class MyApplication extends Application {

	private static MyApplication sInstance;

	private MyDbHelper mMyDbHelper;
	private MyPreferences mMyPreferences;
	private BackStackManager mBackStackManager;
	private UserData mUserData;
	
	private boolean finished = false;
	
	public static MyApplication getInstance() {
		return sInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		GPay.getAPI().init(this, "MFWL", null);
		sInstance = this;
		
		// 异常处理，不需要处理时注释掉这两句即可！
//	    CrashHandler.getInstance().init(getApplicationContext());
	    
	    getBackStackManager();
	    getMyPrefs();
	    getMyDbHelper();
	    
	}


	public synchronized MyDbHelper getMyDbHelper() {
		if (mMyDbHelper == null) {
			mMyDbHelper = new MyDbHelper(this);
			
		}
		return mMyDbHelper;
	}
		
	public MyPreferences getMyPrefs() {
		if (mMyPreferences == null) {
			mMyPreferences = new MyPreferences(this);
		}
		return mMyPreferences;
	}

	public BackStackManager getBackStackManager() {
		if (mBackStackManager == null) {
			mBackStackManager = new BackStackManager(this);
		}
		return mBackStackManager;
	}
	
	public UserData getUserData() {
		if (mUserData == null) {
			mUserData = new UserData(this);
		}
		return mUserData;
	}

	public String getVersionName() {
		String versionName = "";
		try {
			PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionName = pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName; 
	}
	
	public int getVersionNum() {
		int versionCode = 0;
		try {
			PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionCode = pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode; 
	}

	public void finish() {
		finished = true;
	}
	
	public boolean isFinished() {
		return finished;
	}
}

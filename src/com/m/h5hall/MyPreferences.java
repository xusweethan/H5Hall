package com.m.h5hall;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

public class MyPreferences {

	private Context mContext;
	private SharedPreferences mPrefs;
	private Gson mGson;

	public MyPreferences(Context context) {
		mContext = context;
		mPrefs = context.getSharedPreferences(MyPreferences.class.getName(), 0);
		mGson = new Gson();
	}

	public SharedPreferences getPreferences() {
		return mPrefs;
	}

	public String getLoginName() {
		return mPrefs.getString("loginName", "");
	}
	
	public void setLoginName(String loginName) {
		mPrefs.edit().putString("loginName", loginName).commit();
	}

	public String getLoginPwd() {
		return mPrefs.getString("loginPwd", "");
	}
	
	public void setLoginPwd(String loginPwd) {
		mPrefs.edit().putString("loginPwd", loginPwd).commit();
	}
	
	public String getRecentGames() {
		return mPrefs.getString("recent", "");
	}

	public void setRecentGames(String games) {
		mPrefs.edit().putString("recent", games).commit();
	}

//	public String getUserSk() {
//		return mPrefs.getString("sk", "");
//	}
//
//	public void setUserSk(String sk) {
//		mPrefs.edit().putString("sk", sk).commit();
//	}
//
//	public String getHeadPic() {
//		return mPrefs.getString("headPic", "");
//	}
//
//	public void setHeadPic(String headPic) {
//		mPrefs.edit().putString("headPic", headPic).commit();
//	}
//
//	public String getUserName() {
//		return mPrefs.getString("name", "");
//	}
//
//	public void setUserName(String name) {
//		mPrefs.edit().putString("name", name).commit();
//	}
//
//	public String getMobile() {
//		return mPrefs.getString("mobile", "");
//	}
//
//	public void setMobile(String mobile) {
//		mPrefs.edit().putString("mobile", mobile).commit();
//	}
//
//	public String getUserCode() {
//		return mPrefs.getString("code", "");
//	}
//
//	public void setUserCode(String code) {
//		mPrefs.edit().putString("code", code).commit();
//	}
//
//	public String getEmail() {
//		return mPrefs.getString("email", "");
//	}
//
//	public void setEmail(String email) {
//		mPrefs.edit().putString("email", email).commit();
//	}
	
	public boolean getFileEncryptStatus(String file) {
		return mPrefs.getBoolean(file, false);
	}
	
	public void setFileEncryptStatus(String file, boolean status) {//设置文件加密状态
		mPrefs.edit().putBoolean(file, status).commit();
	}
	
}

package com.m.h5hall.comm.data;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.m.h5hall.MyApplication;
import com.m.h5hall.MyPreferences;
import com.m.h5hall.comm.dto.BaseDto;
import com.m.h5hall.comm.entity.GlobalGame;
import com.m.h5hall.main.activity.LoginActivity;
import com.m.h5hall.main.activity.MainActivity;
import com.m.h5hall.main.activity.SplashActivity;
import com.m.h5hall.main.dto.LoginData;
import com.m.h5hall.utils.ConstUrl;
import com.m.h5hall.utils.Constant;
import com.m.h5hall.utils.MD5Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class GamesData {
	private Context mContext;
	
	public LoginData mLoginInfo;
	public List<GlobalGame> mGlobalGames = new ArrayList<GlobalGame>();
	
	private MyPreferences mPrefs;
	private HttpUtils mHttpUtils;
	private Handler mHandler = new Handler();

	public GamesData(Context context) {
		mContext = context;
		mPrefs = MyApplication.getInstance().getMyPrefs();
		mLoginInfo = null;
		
		mHttpUtils = new HttpUtils();
	}
	
	public LoginData getLoginInfo() {
		return mLoginInfo;
	}

	public void setLoginInfo(LoginData loginInfo) {
		mLoginInfo = loginInfo;
	}

	/**
	 * 用户登陆
	 * 
	 * @param activity
	 * @param user
	 *            姓名
	 * @param pwd
	 *            密码
	 * @param loginFlag
	 *            登陆方式，0:正常登陆，1:启动时自动登录，2:被顶下线后重新
	 */
	public void login(final Activity activity, final String user,
			final String pwd, final int loginFlag) {

		String ts = (System.currentTimeMillis() / 1000) + "";
		RequestParams params = new RequestParams();
		params.addBodyParameter("username", user);
		params.addBodyParameter("password", MD5Utils.md5(pwd));
		params.addQueryStringParameter("ts", ts);
		params.addQueryStringParameter("ver", "1");
		params.addQueryStringParameter("hid", "1");
		params.addQueryStringParameter("net", "1");
		params.addQueryStringParameter("apt", "0");

		mHttpUtils.send(HttpRequest.HttpMethod.POST, ConstUrl.LOGIN, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						
						if (activity.getClass() == LoginActivity.class) {
							((LoginActivity)activity).mLoginBtn.setClickable(false);
						}
					}
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						
						Log.i("loginActivity", responseInfo.result);
						final BaseDto<LoginData> dto = new Gson().fromJson(
								responseInfo.result,
								new TypeToken<BaseDto<LoginData>>() {
								}.getType());
						if ("success".equals(dto.message)) {
							saveInfo(dto.result, user, pwd);
							
							Log.i("login", "success");
							
						

						} else {
							Log.i("login", "failure + " + dto.message);
							
						}
//						if (loginFlag == 1) {
//							mHandler.postDelayed(new Runnable() {
//								@Override
//								public void run() {
//									activity.startActivity(new Intent(activity, MainActivity.class));
//									Log.i("UserData", "onSuccess----1");
//									activity.finish();
//								}
//							}, 2000);
//						} else 
						if (loginFlag == 2) {
							activity.startActivity(new Intent(activity, MainActivity.class));
							Log.i("UserData", "onSuccess----2");
							activity.finish();
						}
						if (activity.getClass() == LoginActivity.class) {
							((LoginActivity)activity).mLoginBtn.setClickable(true);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("login", "failure + " + msg);
						if (loginFlag == 1) {
							mHandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									activity.startActivity(new Intent(activity, MainActivity.class));
									Log.i("UserData", "onFailure----1");
									activity.finish();
								}
							}, 2000);
						} else if (loginFlag == 2) {
							activity.startActivity(new Intent(activity, MainActivity.class));
							Log.i("UserData", "onFailure----2");
							activity.finish();
						}
						if (activity.getClass() == LoginActivity.class) {
							((LoginActivity)activity).mLoginBtn.setClickable(true);
						}
					}
				});

	}
	

	private void saveInfo(LoginData data, String user, String pwd) {
		System.out.println("ak = " + data.getAk());
		System.out.println("sk = " + data.getSk());
		System.out.println("headPic = " + data.getHeadPic());
		System.out.println("name = " + data.getName());
		System.out.println("mobile = " + data.getMobile());
		System.out.println("code = " + data.getCode());
		System.out.println("email = " + data.getEmail());
		MyApplication.getInstance().getUserData().setLoginInfo(data);

		mPrefs.setLoginName(user);
		mPrefs.setLoginPwd(pwd);
	}

}

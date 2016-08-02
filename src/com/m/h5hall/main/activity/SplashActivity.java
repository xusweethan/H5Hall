package com.m.h5hall.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.m.h5hall.R;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

@ContentView(R.layout.activity_splash)
public class SplashActivity extends Activity {

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				startActivity(new Intent(getApplicationContext(),
						MainActivity.class));
				Log.i("SplashActivity", "handleMessage----1");
				finish();
				break;
			default:
				break;
			}
		}

	};

	@ViewInject(R.id.splash_image)
	private ImageView mImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		MobclickAgent.setDebugMode( true );
		AnalyticsConfig.setAppkey(this, "56ceae4667e58e44bf0004ba");
		AnalyticsConfig.setChannel("MFWL");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);

		mImage.setImageResource(R.drawable.p_start);

		// MyPreferences myPreferences =
		// MyApplication.getInstance().getMyPrefs();
		// String loginName = myPreferences.getLoginName();
		// String loginPwd = myPreferences.getLoginPwd();
		// if (!TextUtils.isEmpty(loginName) && !TextUtils.isEmpty(loginPwd)) {
		// // System.out.println("自动登录");
		// MyApplication
		// .getInstance()
		// .getUserData()
		// .login(SplashActivity.this, loginName, loginPwd, 1);
		// } else {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(getApplicationContext(),
						MainActivity.class));
				Log.i("SplashActivity", "postDelayed");
				finish();
			}
		}, 1000);
		// }
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}

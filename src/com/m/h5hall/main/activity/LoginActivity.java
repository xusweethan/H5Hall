package com.m.h5hall.main.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.m.h5hall.MyApplication;
import com.m.h5hall.MyPreferences;
import com.m.h5hall.R;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends Activity {

	public static final String EXTRA_RELOGIN = "LoginActivity.EXTRA_RELOGIN";

	private EditText mPhoneEdt;
	private EditText mPwdEdt;
	private TextView mForgetpwdTxt;
	public Button mLoginBtn;

	private MyPreferences mPrefs;
	private boolean flagRelogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		mPrefs = MyApplication.getInstance().getMyPrefs();
		String loginName = mPrefs.getLoginName();
		String loginPwd = mPrefs.getLoginPwd();

		String strFlag = getIntent().getStringExtra(EXTRA_RELOGIN);
		if ("relogin".equals(strFlag)) {
			flagRelogin = true;
		} else {
			flagRelogin = false;
		}

		mPhoneEdt = (EditText) findViewById(R.id.login_phone);
		mPwdEdt = (EditText) findViewById(R.id.login_password);
		mForgetpwdTxt = (TextView) findViewById(R.id.login_forget);

		if (loginName != "" && loginPwd != "") {
			mPhoneEdt.setText(loginName);
			mPwdEdt.setText(loginPwd);
		}

		mLoginBtn = (Button) findViewById(R.id.login_btn);
		mLoginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!TextUtils.isEmpty(mPhoneEdt.getText())
						&& !TextUtils.isEmpty(mPwdEdt.getText())) {
					// if (flagRelogin) {
					// MyApplication.getInstance().getUserData().login(LoginActivity.this,
					// mPhoneEdt
					// .getText().toString(), mPwdEdt.getText().toString(), 2);
					// } else {
					// MyApplication.getInstance().getUserData().login(LoginActivity.this,
					// mPhoneEdt
					// .getText().toString(), mPwdEdt.getText().toString(), 0);
					// }
				}
			}
		});

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
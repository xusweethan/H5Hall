/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.m.h5hall.main.activity;

import org.apache.cordova.CordovaActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.m.h5hall.utils.FileStorageUtils;
import com.umeng.analytics.MobclickAgent;

public class GameActivity extends CordovaActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set by <content src="index.html" /> in config.xml
		Intent it = getIntent();
		String key = it.getStringExtra("key");
		MobclickAgent.onEvent(GameActivity.this,"Play",key);
		loadUrl("file://" + FileStorageUtils.mBaseDir + "/" + key
				+ "/index.html");
		// loadUrl("file:///sdcard/civillizations_wars/www/index.html");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("GameActivity", "onDestroy");
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

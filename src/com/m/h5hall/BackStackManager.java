package com.m.h5hall;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class BackStackManager {

	private static final String TAG = BackStackManager.class.getName();

	private Context mContext;
	private List<Activity> mBackStack; 

	public BackStackManager(Context context) {
		mContext = context;
		mBackStack = new ArrayList<Activity>();
	}

	public void add(Activity activity) {
		mBackStack.add(0, activity);
	}

	public void remove(Activity activity) {
		mBackStack.remove(activity);
		activity.finish();
	}
	
	public void remove(Class<?> activityClass) {
		for (int i = 0; i < mBackStack.size(); i++) {
			if (mBackStack.get(i).getClass() == activityClass) {
				remove(mBackStack.get(i));
			}
		}
	}
	
	public void removeAll() {
		for (int i = 0; i < mBackStack.size(); i++) {
			remove(mBackStack.get(i));
		}
	}

	public void backToRemain(int remain) {
		if (mBackStack.size() > remain) {
			while (mBackStack.size() > remain) {
				mBackStack.remove(0).finish();
			}
		}
	}

	public boolean backTo(Class<?> activityClass) {
		for (int i = 0; i < mBackStack.size(); i++) {
			if (mBackStack.get(i).getClass() == activityClass) {
				backToRemain(mBackStack.size() - i);
				return true;
			}
		}
		return false;
	}

	public void print() {
		for (int i = 0; i < mBackStack.size(); i++) {
			Log.i(TAG, mBackStack.get(i).getClass().getName());
		}
	}

}

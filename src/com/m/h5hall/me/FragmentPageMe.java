package com.m.h5hall.me;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.m.h5hall.MyApplication;
import com.m.h5hall.MyPreferences;
import com.m.h5hall.R;
import com.m.h5hall.main.activity.MainActivity;
import com.m.h5hall.recommend.RecommendListAdapter;
import com.m.h5hall.utils.CircleBitmapUtils;
import com.m.h5hall.utils.Const;

public class FragmentPageMe extends Fragment {
	
	private View mFragmentView = null;
	
	
	private ImageView mUserImageView;
	private TextView mUserNameTxt;

	private MyPreferences mPrefs;

	@ViewInject(R.id.list_view)
	private ListView mListView;
	

	private DownloadedListAdapter mRecentAdapter = null;
	
	public FragmentPageMe() {
		super();
	}

	@SuppressLint("NewApi") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		mPrefs = MyApplication.getInstance().getMyPrefs();
		mFragmentView = inflater.inflate(R.layout.fragment_me, null);
		ImageView share = (ImageView) mFragmentView.findViewById(R.id.share);
		share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent();  
				sendIntent.setAction(Intent.ACTION_SEND);  
				sendIntent.putExtra(Intent.EXTRA_TEXT, "More fun with GameZone! Click here to download http://h5.iappgame.com/GameZone.apk");  
				sendIntent.setType("text/plain");  
				startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));  
			}
		});

		ViewUtils.inject(this, mFragmentView);
		
		
		mRecentAdapter = new DownloadedListAdapter(getActivity());
		mListView.setAdapter(mRecentAdapter);
		
		return mFragmentView;
		
	}
	
	private void refreshView() {
//		mHeadPicFile = new File(mHeadPicPath);
//		if (mHeadPicFile.exists() && donotChange) {
//			Bitmap bitmap = com.fr.school2home.utils.BitmapUtils.loadBitmap(mHeadPicFile);
//			mUserImageView.setImageBitmap(CircleBitmapUtils.toRoundBitmap(bitmap));
//		} else {
//			mPicUrl = mPrefs.getHeadPic();
//			if (mPicUrl != "") {
//				mHttpUtils.download(mPicUrl, mHeadPicPath, new RequestCallBack<File>() {
//					@Override
//					public void onSuccess(ResponseInfo<File> responseInfo) {
//						Log.i("download pic", "success");
//						Bitmap btp = com.fr.school2home.utils.BitmapUtils.loadBitmap(new File(mHeadPicPath));
//						mUserImageView.setImageBitmap(CircleBitmapUtils.toRoundBitmap(btp));
//					}
//					@Override
//					public void onFailure(HttpException error, String msg) {
//						Log.i("download pic", "failure" + msg);
//					}
//				});
//				BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
//				bitmapUtils.display(mUserImageView, mPicUrl, new BitmapLoadCallBack<View>() {
//					@Override
//					public void onLoadCompleted(View container, String uri,
//							Bitmap bitmap, BitmapDisplayConfig config,
//							BitmapLoadFrom from) {
//						mUserImageView.setImageBitmap(CircleBitmapUtils.toRoundBitmap(bitmap));
//					}
//					@Override
//					public void onLoadFailed(View container, String uri,
//							Drawable drawable) {
//						
//					}
//				});
//			}
//		}
//		mName = mPrefs.getUserName();
//		if (mName != "") {
//			mUserNameTxt.setText(mName);
//		}
	}

	@Override
	public void onResume() {
		
//		if (!mName.equals(mPrefs.getUserName())) {
//			if (!mPicUrl.equals(mPrefs.getHeadPic())) {
//				donotChange = false;
//				System.out.println("名字和头像都变了");
//			} else {
//				donotChange = true;
//				System.out.println("名字变了，头像没变");
//			}
//			refreshView();
//		} else {
//			if (!mPicUrl.equals(mPrefs.getHeadPic())) {
//				donotChange = false;
//				System.out.println("头像变了，名字没变");
//				refreshView();
//			} else {
//				donotChange = true;
//				System.out.println("名字和头像都没变");
//			}
//		}
		
		super.onResume();
	}
	

	@Override
	public void onDestroy() {
			super.onDestroy();
	}
	
}

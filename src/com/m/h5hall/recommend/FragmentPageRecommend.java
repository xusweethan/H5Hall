package com.m.h5hall.recommend;

import java.util.Date;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.m.h5hall.MyApplication;
import com.m.h5hall.R;
import com.m.h5hall.main.activity.MainActivity;
import com.m.h5hall.main.dto.LoginData;
import com.m.h5hall.utils.Const;
import com.m.h5hall.utils.ConstUrl;
import com.m.h5hall.utils.MD5Utils;
import com.viewpagerindicator.CirclePageIndicator;

public class FragmentPageRecommend extends Fragment {
	
	private View mFragmentView = null;
	
	@ViewInject(R.id.viewpager)
	private ViewPager mViewPager;
	
	private BannerPagerAdapter mViewPagerAdapter = null;
	
	@ViewInject(R.id.circle_bg)
	private TextView mCircleRing;
	
	@ViewInject(R.id.hot_games)
	private GridView mHotView;
	
	@ViewInject(R.id.recent_games)
	private GridView mRecentView;
	
	@ViewInject(R.id.new_games)
	private GridView mNewView;
	
//	@ViewInject(R.id.specials)
//	private GridView mSubjectView;
	
	private RecommendListAdapter mHotAdapter = null;
	private RecommendListAdapter mNewAdapter = null;
	private RecommendListAdapter mRecentAdapter = null;
	
	private SubjectListAdapter mSubjectAdapter = null;
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	public FragmentPageRecommend() {
		super();
	}

	@Override
	public void onResume() {
		super.onResume();
		
		int flag = 1;
		String games = MyApplication.getInstance().getMyPrefs().getRecentGames();
		if (games.length() == 0)
			flag = 0;
		mRecentAdapter = new RecommendListAdapter(getActivity(), flag);
		mRecentView.setAdapter(mRecentAdapter);
		
		DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int cellWidth = 80;
        int itemWidth = (int) (cellWidth * density);
        int size = mRecentAdapter.getCount();
        int gridviewWidth = (int) (size * (cellWidth + 4) * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		mRecentView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
		mRecentView.setColumnWidth(itemWidth); // 设置列表项宽
		mRecentView.setStretchMode(GridView.NO_STRETCH);
		mRecentView.setNumColumns(size); // 设置列数量=列表集合数
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
	}
	
	@SuppressLint("NewApi") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		if (mFragmentView == null) {
			mFragmentView = inflater.inflate(R.layout.fragment_recommend, null);
		
			ViewUtils.inject(this, mFragmentView);
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
			mCircleRing.setX(-350.0f);
			mCircleRing.setY(-550.0f);
			
	
			mViewPagerAdapter = new BannerPagerAdapter(getActivity());
					
			mViewPager.setAdapter(mViewPagerAdapter);
			CirclePageIndicator ind = (CirclePageIndicator)mFragmentView.findViewById(R.id.indicator);
			ind.setViewPager(mViewPager);
			
			
			DisplayMetrics dm = new DisplayMetrics();
	        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
	        float density = dm.density;
	        int cellWidth = 80;
	        int itemWidth = (int) (cellWidth * density);
	        
	        mHotAdapter = new RecommendListAdapter(getActivity(), 0);
			mHotView.setAdapter(mHotAdapter);
			
	        int size = mHotAdapter.getCount();
	        int gridviewWidth = (int) (size * (cellWidth + 4) * density);
	        
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
			mHotView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
			mHotView.setColumnWidth(itemWidth); // 设置列表项宽
			mHotView.setStretchMode(GridView.NO_STRETCH);
			mHotView.setNumColumns(size); // 设置列数量=列表集合数
			
//			int flag = 1;
//			String games = MyApplication.getInstance().getMyPrefs().getRecentGames();
//			if (games.length() == 0)
//				flag = 0;
//			mRecentAdapter = new RecommendListAdapter(getActivity(), flag);
//			mRecentView.setAdapter(mRecentAdapter);
//			
//	        size = mRecentAdapter.getCount();
//	        gridviewWidth = (int) (size * (cellWidth + 4) * density);
//			params = new LinearLayout.LayoutParams(
//	                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
//			mRecentView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
//			mRecentView.setColumnWidth(itemWidth); // 设置列表项宽
//			mRecentView.setStretchMode(GridView.NO_STRETCH);
//			mRecentView.setNumColumns(size); // 设置列数量=列表集合数
//			
			
			mNewAdapter = new RecommendListAdapter(getActivity(), 2);
			mNewView.setAdapter(mNewAdapter);
			
	        size = mNewAdapter.getCount();
	        gridviewWidth = (int) (size * (cellWidth + 4) * density);
	        
			params = new LinearLayout.LayoutParams(
	                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
			mNewView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
			mNewView.setColumnWidth(itemWidth); // 设置列表项宽
			mNewView.setStretchMode(GridView.NO_STRETCH);
			mNewView.setNumColumns(size); // 设置列数量=列表集合数
			
			mSubjectAdapter = new SubjectListAdapter(getActivity());
//			mSubjectView.setAdapter(mSubjectAdapter);
			
	        size = mSubjectAdapter.getCount();
	        cellWidth = 165;
	        itemWidth = (int) (cellWidth * density);
	        gridviewWidth = (int) (size * (cellWidth + 4) * density);
	        
//			params = new LinearLayout.LayoutParams(
//	                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
//			mSubjectView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
//			mSubjectView.setColumnWidth(itemWidth); // 设置列表项宽
//			mSubjectView.setStretchMode(GridView.STRETCH_SPACING_UNIFORM);
//			mSubjectView.setNumColumns(size); // 设置列数量=列表集合数
		}
		
		ViewGroup parent = (ViewGroup) mFragmentView.getParent();  
        if (parent != null) {  
            parent.removeView(mFragmentView);  
        }
		
		return mFragmentView;
	}
		
}

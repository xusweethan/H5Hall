package com.m.h5hall.all;

import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.m.h5hall.MyApplication;
import com.m.h5hall.R;
import com.m.h5hall.comm.entity.Category;
import com.m.h5hall.main.activity.MainActivity;
import com.m.h5hall.main.dto.LoginData;
import com.m.h5hall.recommend.BannerPagerAdapter;
import com.m.h5hall.utils.Const;
import com.m.h5hall.utils.ConstUrl;
import com.m.h5hall.utils.MD5Utils;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

public class FragmentPageAll extends Fragment {
	
	private View mFragmentView = null;

	@ViewInject(R.id.viewpager)
	private ViewPager mViewPager;
	
	private List<Category> mCates = null;
	
	class TabPageIndicatorAdapter extends FragmentPagerAdapter {  
        public TabPageIndicatorAdapter(FragmentManager fm) {  
            super(fm);  
        }  
  
        @Override  
        public Fragment getItem(int position) {  
            CategoryFragment fragment = new CategoryFragment();
            return fragment;  
        }  
  
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
        	CategoryFragment f = (CategoryFragment) super.instantiateItem(container, position);
        	Category cat = mCates.get(position);
        	f.setCategoryId(cat.code);
            return f;
        }

        @Override
        public int getItemPosition(Object object) {
             return PagerAdapter.POSITION_NONE;
        }
        
        @Override  
        public CharSequence getPageTitle(int position) {  
            return mCates.get(position).name;  
        }  
  
        @Override  
        public int getCount() {  
            return mCates.size();  
        }  
    }  
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	public FragmentPageAll() {
		super();
	}

	@Override
	public void onResume() {
		super.onResume();
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		if (mFragmentView == null) {  
			mFragmentView = inflater.inflate(R.layout.fragment_all, null);
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
	        try {
	        	mCates = MyApplication.getInstance().getMyDbHelper().getDB().findAll(Category.class);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getActivity().getSupportFragmentManager());  
	        mViewPager.setAdapter(adapter);
	        
			TabPageIndicator ind = (TabPageIndicator)mFragmentView.findViewById(R.id.indicator);
			ind.setViewPager(mViewPager);
	    }  
	 //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。  
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();  
        if (parent != null) {  
            parent.removeView(mFragmentView);  
        }
		
		return mFragmentView;
	}
	
	public void updateAD(String requestUrl, RequestParams params) {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET,
			requestUrl, params,
		    new RequestCallBack<String>(){
		        @Override
		        public void onLoading(long total, long current, boolean isUploading) {
		        	
		        }

		        @Override
		        public void onSuccess(ResponseInfo<String> responseInfo) {

		        	if (responseInfo.statusCode == 200) {
//		        		AdDataDto adDto = new Gson().fromJson(responseInfo.result, AdDataDto.class);
//		        		if (adDto.status == 0) {
//		        			mAdAdapter.update(adDto.result);
//		        		}
		        	}
		        }

		        @Override
		        public void onStart() {
		        	
		        }

		        @Override
		        public void onFailure(HttpException error, String msg) {
		        	
		        }
		});
	}
}

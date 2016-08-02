package com.m.h5hall.recommend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.DbException;
import com.m.h5hall.MyApplication;
import com.m.h5hall.R;
import com.m.h5hall.comm.entity.BannerGame;
import com.m.h5hall.comm.widget.BriefDialog;
import com.m.h5hall.utils.Constant;

public class BannerPagerAdapter extends PagerAdapter{

	private LayoutInflater mInflater;
    private List<BannerGame> mBanners = null;
    private Map<Integer, View> mViews = new HashMap<Integer, View>();
    
    private Context mContext;
    
    public class GameKey {
		public String gameKey;
	};
	
    public BannerPagerAdapter(Context context){
    	this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        
        try {
			mBanners = MyApplication.getInstance().getMyDbHelper().getDB().findAll(BannerGame.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) 	{	
		container.removeView(mViews.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = container.inflate(mContext, R.layout.banner_img_cell, null);
		
		ImageView img = (ImageView)view.findViewById(R.id.image);
		img.setBackgroundResource(R.drawable.banner);
		
		BitmapUtils bitmapUtils = new BitmapUtils(mContext);
		// 加载网络图片
		final BannerGame banner = mBanners.get(position);
		bitmapUtils.display(img, banner.icon);
		
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				Gson gs = new Gson();
				GameKey gameKey = gs.fromJson(banner.gamekey, GameKey.class);

				new BriefDialog(mContext, gameKey.gameKey).show();
			}
			
		});
		
		mViews.put(position, view);
		container.addView(view);
		return view;
	}

	@Override
	public int getCount() {			
		return  mBanners == null ? 0 : mBanners.size();
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {			
		return arg0==arg1;
	}
}

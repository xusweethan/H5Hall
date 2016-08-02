package com.m.h5hall.all;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.DbException;
import com.m.h5hall.MyApplication;
import com.m.h5hall.R;
import com.m.h5hall.comm.entity.BannerGame;
import com.m.h5hall.comm.entity.Category;
import com.m.h5hall.utils.Constant;

public class CategoryFragment extends Fragment {
    private String mCateId = null;
    
    public CategoryFragment(){
    }
    
    public void setCategoryId(String id) {
    	mCateId = id;
    }
    
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
          
    	View view = inflater.inflate(R.layout.category_fragement, container, false); 
        
    	if (mCateId == null) {
    		try {
	        	List<Category> cates = MyApplication.getInstance().getMyDbHelper().getDB().findAll(Category.class);
	        	mCateId = cates.get(((ViewPager)container).getCurrentItem()).code;
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
		PullToRefreshListView listview = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
		GameListAdapter adapter = new GameListAdapter(container.getContext(), mCateId);
		listview.setAdapter(adapter);
		
        return view;  
    }  
  
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
    }  

}

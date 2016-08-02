package com.m.h5hall.recommend;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.m.h5hall.MyApplication;
import com.m.h5hall.R;
import com.m.h5hall.comm.entity.BannerGame;
import com.m.h5hall.comm.entity.GlobalGame;
import com.m.h5hall.comm.entity.SpecialSubject;
import com.m.h5hall.comm.widget.BriefDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SubjectListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	
	private List<SpecialSubject> mSubjects = null;
	
	public SubjectListAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		
		DbUtils db = MyApplication.getInstance().getMyDbHelper().getDB();
		
		try {
			mSubjects = db.findAll(SpecialSubject.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
	}
	
	@Override
	public int getCount() {
		return mSubjects == null ? 0 : mSubjects.size();
	}

	@Override
	public Object getItem(int position) {
		return mSubjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public final class ViewHolder {
		public ImageView banner;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_image_grid1, null);
			holder.banner = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		}

		SpecialSubject game = (SpecialSubject)getItem(position);
		
		BitmapUtils bitmapUtils = new BitmapUtils(mContext);
		// 加载网络图片
		bitmapUtils.display(holder.banner, game.icon);
		holder.banner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		return convertView;
	}

}

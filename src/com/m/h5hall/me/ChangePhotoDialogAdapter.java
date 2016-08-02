package com.m.h5hall.me;

import com.m.h5hall.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChangePhotoDialogAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private String[] mWays = {"拍照", "选取照片"};
	
	public ChangePhotoDialogAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return mWays.length;
	}

	@Override
	public String getItem(int position) {
		return mWays[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public final class ViewHolder {
		public TextView txt;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mine_dialog_item, null);
			holder.txt = (TextView) convertView.findViewById(R.id.mine_dialog_item_txt);
			convertView.setTag(holder);
		}

		String nameStr = getItem(position);
		holder.txt.setText(nameStr);
		return convertView;
	}

}

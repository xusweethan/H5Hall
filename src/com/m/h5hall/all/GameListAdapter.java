package com.m.h5hall.all;

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
import com.m.h5hall.comm.widget.BriefDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GameListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private int mRows;
	private BitmapUtils bitmapUtils;
	
	private List<GlobalGame> mGames = new ArrayList<GlobalGame>();
	
	public GameListAdapter(Context context, String code) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		
		bitmapUtils = new BitmapUtils(mContext);
		
		DbUtils db = MyApplication.getInstance().getMyDbHelper().getDB();
		List<DbModel> dbModels = null;
		
		try {
			String sql = "select * from t_global_games where KEY in (select KEY from t_game_classify where CATEID = ? order by SORT);";
			SqlInfo si = new SqlInfo(sql);
			si.addBindArg(code);
			
			dbModels = db.findDbModelAll(si); // 自定义sql查询
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (dbModels != null && !dbModels.isEmpty()) {
			for (int i=0; i<dbModels.size(); i++) {
				GlobalGame game = new GlobalGame();
				DbModel model = dbModels.get(i);
				game.key = model.getString("KEY");
				game.name = model.getString("NAME");
				game.resurl = model.getString("RESURL");
				game.icon = model.getString("ICON");
				game.horizontal = model.getInt("HORIZONTAL");
				game.type = model.getInt("TYPE");
				game.brief = model.getString("BRIEF");
				
				mGames.add(game);
			}
			
			mRows = mGames.size() / 4;
			if (mGames.size() % 4 > 0)
				mRows += 1;
		}
	}
	
	@Override
	public int getCount() {
		return mRows;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public final class ViewHolder {
		public TextView name1;
		public ImageView icon1;
		public TextView name2;
		public ImageView icon2;
		public TextView name3;
		public ImageView icon3;
		public TextView name4;
		public ImageView icon4;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_row_images, null);
			holder.name1 = (TextView) convertView.findViewById(R.id.image_text1);
			holder.icon1 = (ImageView) convertView.findViewById(R.id.image1);
			
			holder.name2 = (TextView) convertView.findViewById(R.id.image_text2);
			holder.icon2 = (ImageView) convertView.findViewById(R.id.image2);
			
			holder.name3 = (TextView) convertView.findViewById(R.id.image_text3);
			holder.icon3 = (ImageView) convertView.findViewById(R.id.image3);
			
			holder.name4 = (TextView) convertView.findViewById(R.id.image_text4);
			holder.icon4 = (ImageView) convertView.findViewById(R.id.image4);
			
			convertView.setTag(holder);
		}

		initCell(position*4, holder.icon1, holder.name1);
		if (mGames.size() > position*4+1)
		initCell(position*4+1, holder.icon2, holder.name2);
		if (mGames.size() > position*4+2)
		initCell(position*4+2, holder.icon3, holder.name3);
		if (mGames.size() > position*4+3)
		initCell(position*4+3, holder.icon4, holder.name4);
		
		return convertView;
	}

	private void initCell(int index, ImageView image, TextView text) {
		final GlobalGame game = (GlobalGame)mGames.get(index);
		text.setText(game.name);
		// 加载网络图片
		bitmapUtils.display(image, game.icon);
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new BriefDialog(mContext, game.key).show();
			}
			
		});
	}
}

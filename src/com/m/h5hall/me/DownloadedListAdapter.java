package com.m.h5hall.me;

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
import com.m.h5hall.main.activity.GameActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DownloadedListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	
	private List<GlobalGame> mGames = new ArrayList<GlobalGame>();
	
	public DownloadedListAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
				
		DbUtils db = MyApplication.getInstance().getMyDbHelper().getDB();
		List<DbModel> dbModels = null;
		
		String games = MyApplication.getInstance().getMyPrefs().getRecentGames();
		String []_games = games.split(",");
		String params = "";
		for (int i=0; i<_games.length; i++) {
			params += "'" + _games[i] + "'";
			if (i<_games.length-1)
				params += ",";
		}
		
		try {
			String sql = "select * from t_global_games where KEY in (" + params + ");";
			SqlInfo si = new SqlInfo(sql);
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
		}
	}
	
	@Override
	public int getCount() {
		return mGames.size();
	}

	@Override
	public Object getItem(int position) {
		return mGames.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public final class ViewHolder {
		public TextView name;
		public TextView brief;
		public ImageView icon;
		public Button play;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_row_downloaded, null);
			holder.name = (TextView) convertView.findViewById(R.id.text1);
			holder.brief = (TextView) convertView.findViewById(R.id.text2);
			holder.icon = (ImageView) convertView.findViewById(R.id.image);
			holder.play = (Button) convertView.findViewById(R.id.play_btn);
			convertView.setTag(holder);
		}

		final GlobalGame game = (GlobalGame)getItem(position);
		holder.name.setText(game.name);
		holder.brief.setText(game.brief);
		
		BitmapUtils bitmapUtils = new BitmapUtils(mContext);
		// 加载网络图片
		bitmapUtils.display(holder.icon, game.icon);
		holder.play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// start game
				Intent intent = new Intent(mContext, GameActivity.class);
				intent.putExtra("key", game.key);
				mContext.startActivity(intent);
			}
			
		});
		return convertView;
	}

}

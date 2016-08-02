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
import com.m.h5hall.comm.widget.BriefDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecommendListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private int mType;
	
	private List<GlobalGame> mGames = new ArrayList<GlobalGame>();
	
	public RecommendListAdapter(Context context, int type) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mType = type;
				
		DbUtils db = MyApplication.getInstance().getMyDbHelper().getDB();
		List<DbModel> dbModels = null;
		
		if (type == 1) {
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
		} else {
			try {
				String sql = "select * from t_global_games where KEY in (select KEY from t_game_classify where CATEID = ? order by SORT);";
				SqlInfo si = new SqlInfo(sql);
				if (type == 0)
					si.addBindArg("HOME_HOT");
				if (type == 2)
					si.addBindArg("HOME_NEW");
				dbModels = db.findDbModelAll(si); // 自定义sql查询
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		public ImageView icon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_image_grid, null);
			holder.name = (TextView) convertView.findViewById(R.id.item_image_grid_text);
			holder.icon = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		}

		final GlobalGame game = (GlobalGame)getItem(position);
		holder.name.setText(game.name);
		
		BitmapUtils bitmapUtils = new BitmapUtils(mContext);
		// 加载网络图片
		bitmapUtils.display(holder.icon, game.icon);
		holder.icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new BriefDialog(mContext, game.key).show();
			}
			
		});
		return convertView;
	}

	public void update() {
		if (mType == 1) {
			mGames.clear();
			
			String games = MyApplication.getInstance().getMyPrefs().getRecentGames();
			String []_games = games.split(",");
			String params = "";
			for (int i=0; i<_games.length; i++) {
				params += "'" + _games[i] + "'";
				if (i<_games.length-1)
					params += ",";
			}
			
			DbUtils db = MyApplication.getInstance().getMyDbHelper().getDB();
			List<DbModel> dbModels = null;
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
	}
}

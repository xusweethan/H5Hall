package com.m.h5hall.comm.widget;

import java.io.File;
import java.util.Hashtable;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.m.h5hall.MyApplication;
import com.m.h5hall.R;
import com.m.h5hall.comm.entity.BannerGame;
import com.m.h5hall.comm.entity.GlobalGame;
import com.m.h5hall.main.activity.GameActivity;
import com.m.h5hall.utils.FileStorageUtils;
import com.m.h5hall.utils.UnZip;
import com.umeng.analytics.MobclickAgent;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BriefDialog extends Dialog {
	private GlobalGame game = null;
	private Context mContext;
	private DownloadReceiver mReceiver;
	private Button mBtn;
	private boolean downloaded = false;

	public BriefDialog(Context context, String gameKey) {
		super(context);
		mContext = context;
		setContentView(R.layout.dialog_showbrief);

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		dialogWindow.setBackgroundDrawableResource(R.drawable.mine_transparent);
		BriefDialog.this.setCanceledOnTouchOutside(false);

		try {
			String sql = "select * from t_global_games where KEY = ?;";
			SqlInfo si = new SqlInfo(sql);
			si.addBindArg(gameKey);
			DbModel model = MyApplication.getInstance().getMyDbHelper().getDB()
					.findDbModelFirst(si); // 自定义sql查询
			if (model != null) {
				game = new GlobalGame();
				game.key = model.getString("KEY");
				game.name = model.getString("NAME");
				game.resurl = model.getString("RESURL");
				game.icon = model.getString("ICON");
				game.horizontal = model.getInt("HORIZONTAL");
				game.type = model.getInt("TYPE");
				game.brief = model.getString("BRIEF");
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ImageView icon = (ImageView) findViewById(R.id.icon);
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.display(icon, game.icon);

		TextView name = (TextView) findViewById(R.id.name);
		name.setText(game.name);
		TextView brief = (TextView) findViewById(R.id.brief);
		brief.setText(game.brief);

		ImageView close = (ImageView) findViewById(R.id.close);
		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BriefDialog.this.dismiss();
			}
		});

		mBtn = (Button) findViewById(R.id.dialog_btn);

		String _recentGames = MyApplication.getInstance().getMyPrefs()
				.getRecentGames();
		if (_recentGames.length() > 0) {
			String[] recentGames = _recentGames.split(",");
			for (int i = 0; i < recentGames.length; i++) {
				mBtn.setText("Download");
				mBtn.setEnabled(true);
				if (recentGames[i].equals(game.key)) {
					mBtn.setEnabled(true);
					mBtn.setText("Play");
					downloaded = true;
					break;
				}
			}

		} else {
			mBtn.setText("Download");
			mBtn.setEnabled(true);
		}

		mBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (downloaded) {
					// start game
					Intent intent = new Intent(mContext, GameActivity.class);
					intent.putExtra("key", game.key);
					mContext.startActivity(intent);

					BriefDialog.this.dismiss();

					// String _recentGames =
					// MyApplication.getInstance().getMyPrefs().getRecentGames();
					// if (!_recentGames.contains(game.key)) {
					// if (_recentGames.length() > 0)
					// _recentGames += ",";
					// _recentGames += game.key;
					// MyApplication.getInstance().getMyPrefs().setRecentGames(_recentGames);
					// }

				} else {
					final String[] _parts = game.resurl.split("/");

					File outpath = new File(FileStorageUtils.mBaseDir + "/"
							+ game.key);
					if (!outpath.isDirectory())
						outpath.mkdir();
					MobclickAgent.onEvent(mContext,"DownLoad",game.key);
					HttpUtils http = new HttpUtils();
					HttpHandler handler = http.download(game.resurl,
							FileStorageUtils.mBaseDir + "/" + game.key + "/"
									+ _parts[_parts.length - 1], true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
							true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
							new RequestCallBack<File>() {

								@Override
								public void onStart() {
									mBtn.setEnabled(false);
									mBtn.setText("Downloading...");

								}

								@Override
								public void onLoading(long total, long current,
										boolean isUploading) {
									// mBtn.setText("Downloading... " +
									// (current*100 / total) + "%");

									Intent intent = new Intent(
											"com.m.h5hall.download");
									intent.putExtra("key", game.key);
									intent.putExtra("progress", current * 100
											/ total);
									mContext.sendBroadcast(intent);
								}

								@Override
								public void onSuccess(
										ResponseInfo<File> responseInfo) {

									UnZip uz = new UnZip(
											FileStorageUtils.mBaseDir + "/"
													+ game.key + "/"
													+ _parts[_parts.length - 1],
											FileStorageUtils.mBaseDir + "/"
													+ game.key + "/");
									if (uz.getStatus()) {
										mBtn.setEnabled(true);
										mBtn.setText("Play");
										downloaded = true;

										Intent intent = new Intent(
												"com.m.h5hall.download");
										intent.putExtra("key", game.key);
										intent.putExtra("progress", (long) 101);
										mContext.sendBroadcast(intent);
										String _recentGames = MyApplication
												.getInstance().getMyPrefs()
												.getRecentGames();
										if (!_recentGames.contains(game.key)) {
											if (_recentGames.length() > 0)
												_recentGames += ",";
											_recentGames += game.key;
											MyApplication
													.getInstance()
													.getMyPrefs()
													.setRecentGames(
															_recentGames);
										}
									} else
										mBtn.setEnabled(true);
									mBtn.setText("Download");
								}

								@Override
								public void onFailure(HttpException error,
										String msg) {

									if (error.getExceptionCode() == 416) {
										UnZip uz = new UnZip(
												FileStorageUtils.mBaseDir
														+ "/"
														+ game.key
														+ "/"
														+ _parts[_parts.length - 1],
												FileStorageUtils.mBaseDir + "/"
														+ game.key + "/");
										if (uz.getStatus()) {
											mBtn.setEnabled(true);
											mBtn.setText("Play");
											downloaded = true;

											Intent intent = new Intent(
													"com.m.h5hall.download");
											intent.putExtra("key", game.key);
											intent.putExtra("progress",
													(long) 101);
											mContext.sendBroadcast(intent);
										}
									} else
										mBtn.setEnabled(true);
									mBtn.setText("Download");
								}
							});
				}
			}
		});

		mReceiver = new DownloadReceiver();
		mContext.registerReceiver(mReceiver, new IntentFilter(
				"com.m.h5hall.download"));

		setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				mContext.unregisterReceiver(mReceiver);
				mReceiver = null;
			}
		});
	}

	public class DownloadReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("com.m.h5hall.download")) {
				String key = intent.getStringExtra("key");
				long progress = intent.getLongExtra("progress", 0);
				if (key.equals(game.key)) {
					if (progress <= 100) {
						mBtn.setEnabled(false);
						mBtn.setText("Downloading... " + progress + "%");
					}

					else {
						mBtn.setEnabled(true);
						mBtn.setText("Play");
					}
				}
			}
		}

	}
}

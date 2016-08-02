package com.m.h5hall;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.lidroid.xutils.DbUtils;

public class MyDbHelper {

	private static final String DATABASE_NAME = "h5games.db";
	private static final int DATABASE_VERSION = 1;
	
	private Context mContext;
	private String mDbPath;
	private DbUtils mDB;

	public MyDbHelper(Context context) {
		this.mContext = context;
		mDbPath = mContext.getDatabasePath(DATABASE_NAME).getPath();
		Log.i("MyDbHelper", mDbPath);
		
		boolean dbExist = checkDataBase();
		if (!dbExist) {
			try {
				copyDataBase();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		mDB = DbUtils.create(context, DATABASE_NAME, DATABASE_VERSION, new DbUtils.DbUpgradeListener() {

			@Override
			public void onUpgrade(DbUtils arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				try {
					copyDataBase();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	}

	public DbUtils getDB() {
		return mDB;
	}
	
	// 检查数据库是否有效
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			checkDB = SQLiteDatabase.openDatabase(mDbPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}
	
	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = mContext.getAssets().open(DATABASE_NAME);
		// Path to the just created empty db
		
		File file = new File(mDbPath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		file.createNewFile();
		
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(file);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

}
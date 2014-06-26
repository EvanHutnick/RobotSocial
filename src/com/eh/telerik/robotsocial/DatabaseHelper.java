package com.eh.telerik.robotsocial;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "Shares.db";
	private static int DB_VERSION = 3;
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		
		try {
			ShareContainer.create(db);
			Log.d("DatabaseHelper", "Creating Database");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			Log.d("DatabaseHelper", "Database Created");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.beginTransaction();
		
		try {
			ShareContainer.drop(db);
			
			ShareContainer.create(db);
			
			Log.d("DatabaseHelper", "Re-Creating Database");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			Log.d("DatabaseHelper", "Database Re-Created");
		}
	}
	
	public SQLiteDatabase open() {
		return getWritableDatabase();
	}
	
	public void create() {
		open();
	}
}

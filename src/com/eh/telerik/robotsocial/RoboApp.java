package com.eh.telerik.robotsocial;

import android.app.Application;

public class RoboApp extends Application {

	private DatabaseHelper dbHelper;
	
	@Override
	public void onCreate() {	
		super.onCreate();
		
		dbHelper = new DatabaseHelper(getApplicationContext());
		dbHelper.create();
	}
	
	public DatabaseHelper getDBHelper() {
		if (dbHelper == null) {
			dbHelper = new DatabaseHelper(getApplicationContext());
			dbHelper.create();
		}
		
		return dbHelper;
	}
	
}

package com.eh.telerik.robotsocial;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ShareContainer {

	// db
	public static String tableName = "shares";
	
	// class	
	private int _id;
	private Activity activity;
	private String textToShare;
	private Uri imageUriToShare = Uri.EMPTY;
	private String contentType;
	private long timeShared; 
	
	public ShareContainer(Activity a) {
		this.activity = a;
	}
	
	public void setText(String tts) {
		this.textToShare = tts;
		this.contentType = "text/plain";
	}
	
	public void setPictureUri(Uri uri) {
		this.imageUriToShare = uri;
		this.contentType = "image/jpeg";
	}

	public void populate(int id, String text, Uri uri, long time) {
		this._id = id;
		this.textToShare = text;
		this.imageUriToShare = uri;
		this.timeShared = time;
	}
	
	public String describe() {
		return "ID: " + _id + " - Text (" + textToShare + "), Uri (" + imageUriToShare + "), Time (" + timeShared + ")";
	}
	
	public void sendWithIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		
		if (this.textToShare != null) {
			intent.putExtra(Intent.EXTRA_TEXT, this.textToShare);		
		}
		
		if (this.imageUriToShare != null && this.imageUriToShare != Uri.EMPTY) {
			intent.putExtra(Intent.EXTRA_STREAM, this.imageUriToShare);
		}
		
		intent.setType(this.contentType);
		
		timeShared = System.currentTimeMillis();
		
		DatabaseHelper dbHelper = ((RoboApp)activity.getApplication()).getDBHelper();
		SQLiteDatabase db = dbHelper.open();
		this.insert(db);
		
		activity.startActivity(Intent.createChooser(intent, "Share with..."));
	}

	// db
	
	public static void create (SQLiteDatabase db) {
		String createString = "create table if not exists " + tableName + 
				" ( _id integer primary key autoincrement, " +
				"sharetext text, " +
				"shareuri text, " +
				"sharetime real not null );";
		
		db.execSQL(createString);
	}
	
	public static void drop (SQLiteDatabase db) {
		String dropString = "drop table if exists " + tableName + ";";
		
		db.execSQL(dropString);
	}
	
	public void insert (SQLiteDatabase db) {
		String insertString = "insert into " + tableName + " ( sharetext, shareuri, sharetime ) " +
				"values ( '" + textToShare + "', '" + imageUriToShare.toString() + "', " + timeShared + " );";
		
		db.beginTransaction();
		
		db.execSQL(insertString);
		
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}

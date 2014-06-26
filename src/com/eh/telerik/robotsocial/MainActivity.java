package com.eh.telerik.robotsocial;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setupDatabase();
		setupEvents();
	}
	
	private void setupDatabase() {
		DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
		dbHelper.open();
	}
	
	private void setupEvents() {
		Button shareTextButton = (Button)findViewById(R.id.main_share_text_button);
		shareTextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(getApplicationContext(), ShareTextActivity.class);
				startActivity(intent);
			}
		});
		
		Button sharePictureButton = (Button)findViewById(R.id.main_share_picture_button);
		sharePictureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SharePictureActivity.class);
				startActivity(intent);
			}
		});
		
		Button debugButton = (Button)findViewById(R.id.main_debug_button);
		debugButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatabaseHelper dbHelper = ((RoboApp)getApplication()).getDBHelper();
				SQLiteDatabase db = dbHelper.open();
				
				ArrayList<ShareContainer> shares = new ArrayList<ShareContainer>();
				
				Cursor cursor = db.rawQuery("select * from " + ShareContainer.tableName, null);
				
				cursor.moveToFirst();
				
				while (!cursor.isAfterLast()) {
					ShareContainer share = shareToContainer(cursor);
					shares.add(share);
					cursor.moveToNext();
				}
				
				for (ShareContainer share : shares) {
					Log.d("SHARE_INFO", "ID: " + share.describe());
				}
			}
		});
	}
	
	private ShareContainer shareToContainer(Cursor cursor) {
		ShareContainer share = new ShareContainer(this);
		
		int id = cursor.getInt(cursor.getColumnIndex("_id"));
		String text = cursor.getString(cursor.getColumnIndex("sharetext"));
		String uriString = cursor.getString(cursor.getColumnIndex("shareuri")); 
		Uri uri = Uri.parse(uriString);
		long time = cursor.getLong(cursor.getColumnIndex("sharetime"));
		
		share.populate(id, text, uri, time);
		
		return share;
	}
	

}

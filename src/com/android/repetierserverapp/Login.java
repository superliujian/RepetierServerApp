package com.android.repetierserverapp;

import java.util.LinkedList;
import java.util.List;

import com.android.repetierserverapp.db.DbAdapter;
import com.android.repetierserverapp.db.DbHelper;
import com.android.repetierserverapp.utils.ServerInfo;
import com.android.repetierserverapp.utils.ServerListAdapter;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;


public class Login extends Activity implements OnClickListener{

	private String serverName;
	private String serverUrl;

	private DbAdapter dbAdapter;
	private Cursor cursor;

	private Button loadServer;
	private Button newServer;


	SharedPreferences pref;

	@SuppressWarnings("deprecation")
	@Override














	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		pref = PreferenceManager.getDefaultSharedPreferences(this);
		serverName = pref.getString("serverName", "");
		serverUrl = pref.getString("serverUrl", "");

		loadServer = (Button) findViewById(R.id.loadServerBtn);
		newServer = (Button) findViewById(R.id.newServerBtn);
		ListView listView = (ListView)findViewById(R.id.listView);

		loadServer.setOnClickListener(this);
		newServer.setOnClickListener(this);

		dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		cursor = dbAdapter.fetchAllServer();
		dbAdapter.close();


		startManagingCursor(cursor);
		List<ServerInfo> list = new LinkedList<ServerInfo>();
		while ( cursor.moveToNext() ) {
			serverName = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.DB_NAME));
			serverUrl = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.DB_URL));
			list.add(new ServerInfo(serverName, serverUrl));
		}    
		cursor.close();

		ServerListAdapter adapter = new ServerListAdapter(this, R.layout.serverview, list);
		listView.setAdapter(adapter);	
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.loadServerBtn:
			//TODO


			Editor edit = pref.edit();
			edit.putString("serverName", serverName);
			edit.putString("serverUrl", serverUrl);
			edit.apply(); 
			break;

		case R.id.newServerBtn:
			setContentView(R.layout.activity_add_server);
			break;
		}
	}




}

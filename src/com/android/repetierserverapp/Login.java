package com.android.repetierserverapp;

import com.android.repetierserverapp.db.DbAdapter;
import com.android.repetierserverapp.utils.ArrayAdapterItem;
import com.android.repetierserverapp.utils.ServerInfo;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.app.LoaderManager; 

public class Login extends Activity implements OnClickListener{
	private String serverName;
	private String serverUrl;

	private DbAdapter dbAdapter;
	private Cursor cursor;

	private Button loadServer;
	private Button newServer;

	LoaderManager loadermanager;

	SharedPreferences pref;


	@Override



	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Log.d("sto per aggiungere un server al db", "entrato");
		dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		dbAdapter.createServer("server", "wooo");
		dbAdapter.close();




		pref = PreferenceManager.getDefaultSharedPreferences(this);
		serverName = pref.getString("serverName", "");
		serverUrl = pref.getString("serverUrl", "");

		loadServer = (Button) findViewById(R.id.loadServerBtn);
		newServer = (Button) findViewById(R.id.newServerBtn);
		ListView listView = (ListView)findViewById(R.id.listView);

		loadServer.setOnClickListener(this);
		newServer.setOnClickListener(this);



		Log.d("sto per leggere un server al db", "entrato");
		dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		cursor = dbAdapter.fetchAllServer();
		dbAdapter.close();

		
		
		
		
		
		
		
		
		ServerInfo[] serverList = new ServerInfo[20];

		serverList[0] = new ServerInfo("NOME", "URL");
		serverList[1] = new ServerInfo("NOME", "URL");
		serverList[2] = new ServerInfo("NOME", "URL");
		serverList[3] = new ServerInfo("NOME", "URL");
		serverList[4] = new ServerInfo("NOME", "URL");
		serverList[5] = new ServerInfo("NOME", "URL");
		serverList[6] = new ServerInfo("NOME", "URL");
       

        // our adapter instance
        ArrayAdapterItem adapter = new ArrayAdapterItem(this, R.layout.serverlineview, serverList);

        // create a new ListView, set the adapter and item click listener
        ListView listViewItems = new ListView(this);
        listViewItems.setAdapter(adapter);
        // listViewItems.setOnItemClickListener(new OnItemClickListenerListViewItem());


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
			Intent myIntent = new Intent(v.getContext(), AddServer.class);
			startActivityForResult(myIntent, 0);

			//setContentView(R.layout.activity_add_server);
			break;
		}
	}




}

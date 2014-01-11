package com.android.repetierserverapp;

import com.android.repetierserverapp.db.DbAdapter;
import com.android.repetierserverapp.db.DbHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AddServer extends Activity implements OnClickListener {

	private Button createServer;
	private TextView serverName;
	private TextView serverUrl;

	private String name;
	private String url;

	private DbAdapter dbAdapter;
	private Cursor cursor;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_server);

		createServer = (Button) findViewById(R.id.createServerBtn);
		serverName = (TextView) findViewById(R.id.serverNameET);
		serverUrl = (TextView) findViewById(R.id.serverUrlET);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_server, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		//Verifica validità di nome ed indirizzo e inserisce il nuovo server nel database
		case R.id.createServerBtn:
			name = (String) serverName.getText();
			if (!isValidName(name)){ //TODO errore: nome non valido}
				;
			}
			url = (String) serverUrl.getText();
			if (!isValidUrl(url)){ //TODO errore: indirizzo non valido
				;
			}
			
			dbAdapter = new DbAdapter(this);
			dbAdapter.open();
			dbAdapter.createServer(name, url);
			dbAdapter.close();
			
			setContentView(R.layout.activity_login);
			break;

		}
	}

	private boolean isValidUrl(String url) {
		//TODO 
		return true;
	}

	private boolean isValidName(String name) {
		if (url == "") return false;
		else
			dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		cursor = dbAdapter.fetchServerByName(name);
		dbAdapter.close();

		startManagingCursor(cursor);
		if (cursor.getCount()== 1) {
			cursor.close(); 
			return false;
		}
		
		cursor.close(); 
		return true;
	}

}

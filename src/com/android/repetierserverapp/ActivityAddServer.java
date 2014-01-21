package com.android.repetierserverapp;

import com.android.repetierserverapp.ServerList.ActivityServerList;
import com.android.repetierserverapp.db.DbAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityAddServer extends Activity implements OnClickListener {
	private Button createServer;
	private TextView serverName;
	private TextView serverUrl;

	private String name;
	private String url;

	private DbAdapter dbAdapter;
	private Context context;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_server);

		context = this; 
		
		createServer = (Button) findViewById(R.id.createServerBtn);
		serverName = (EditText) findViewById(R.id.serverNameET);
		serverUrl = (EditText) findViewById(R.id.serverUrlET);
		createServer.setOnClickListener(this);
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_server, menu);
		return true;
	}

	
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.createServerBtn:

			name = serverName.getText().toString();
			if (name.isEmpty()){
				Toast.makeText(context, getString(R.string.insertServerName), Toast.LENGTH_LONG).show();
				break;
			}
			if (!isValidName(name)){
				Toast.makeText(context, getString(R.string.invalidServerName), Toast.LENGTH_LONG).show();
				break;
			}
			url = serverUrl.getText().toString();
			if (!isValidUrl(url)){ 
				break;
			}
			
			dbAdapter = new DbAdapter(this);
			dbAdapter.open();
			dbAdapter.createServer(name, url);
			dbAdapter.close();
			
			Intent myIntent = new Intent(v.getContext(), ActivityServerList.class);
			startActivityForResult(myIntent, 0);
			break;
		}
	}
	
	
	
	private boolean isValidUrl(String url) {
		//TODO 
		return true;
	}



	private boolean isValidName(String name) {
		//TODO 
		return true;
	}

}

package com.android.repetierserverapp;

import com.android.repetierserverapp.ServerList.ActivityServerList;
import com.android.repetierserverapp.db.DbAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
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

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		context = this; 
		
		createServer = (Button) findViewById(R.id.createServerBtn);
		serverName = (EditText) findViewById(R.id.serverNameET);
		serverUrl = (EditText) findViewById(R.id.serverUrlET);
		createServer.setOnClickListener(this);
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this,
					ActivityServerList.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
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

package com.android.repetierserverapp;

import java.util.ArrayList;
import java.util.List;

import com.android.repetierserverapp.ServerList.ActivityServerList;
import com.android.repetierserverapp.db.DbAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityAddServer extends Activity implements OnClickListener, OnItemSelectedListener  {
	
	private Button createServer;
	private TextView serverName;
	private TextView serverUrl;
	private Spinner spinner;
	private String name;
	private String url;
	
	private String connectionType;

	private DbAdapter dbAdapter;
	private Context context;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_server);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		context = this; 
		
		createServer = (Button) findViewById(R.id.createServerBtn);
		createServer.setOnClickListener(this);
		
		serverName = (EditText) findViewById(R.id.serverNameET);
		serverUrl = (EditText) findViewById(R.id.serverUrlET);

		
		spinner = (Spinner) findViewById(R.id.spinner);
		ArrayList<String> list = new ArrayList<String>();
		list.add("http://");
		list.add("https://");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		
		spinner.setOnItemSelectedListener(this);
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

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

			url = String.valueOf(spinner.getSelectedItem()) + serverUrl.getText().toString();
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



	//OnItemSelectedListener
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		connectionType = parent.getItemAtPosition(pos).toString();
		Log.d(connectionType, "onitemselected");
	}

	//OnItemSelectedListener
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		connectionType =  "HTTP://";
		
	}

}

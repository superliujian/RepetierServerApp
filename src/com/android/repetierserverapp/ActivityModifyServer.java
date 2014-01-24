package com.android.repetierserverapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

import com.android.repetierserverapp.ServerList.ActivityServerList;
import com.android.repetierserverapp.db.DbAdapter;
import com.android.repetierserverapp.db.DbHelper;



public class ActivityModifyServer extends Activity implements OnClickListener {

	private TextView serverName;
	private TextView serverUrl;

	private Button saveServer;
	private Button deleteServer;

	private String name;
	private String url;
	private int id;

	private DbAdapter dbAdapter;
	
	private Context context;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_server);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		context = this;
		
		Bundle bundle = getIntent().getExtras();
		id = bundle.getInt("id");

		dbAdapter = new DbAdapter(this);
		dbAdapter.openReadOnly();
		Cursor c = dbAdapter.fetchServerById(Integer.toString(id));
		c.moveToFirst();
		url = c.getString(c.getColumnIndex(DbHelper.DB_URL));
		name = c.getString(c.getColumnIndex(DbHelper.DB_NAME));
		c.close();
		dbAdapter.close();

		serverName = (EditText) findViewById(R.id.serverName);
		serverUrl = (EditText) findViewById(R.id.serverUrl);

		serverName.setText(name);
		serverUrl.setText(url);

		deleteServer = (Button) findViewById(R.id.deleteServerBtn);
		saveServer = (Button) findViewById(R.id.saveServerBtn);

		saveServer.setOnClickListener(this);
		deleteServer.setOnClickListener(this);
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
		case R.id.saveServerBtn:

			String newName = serverName.getText().toString();
			if (newName.isEmpty()){
				Toast.makeText(context, getString(R.string.insertServerName), Toast.LENGTH_LONG).show();
				break;
			}
			if (!isValidName(newName)){
				Toast.makeText(context, getString(R.string.invalidServerName), Toast.LENGTH_LONG).show();
				break;
			}
			String newUrl = serverUrl.getText().toString();
			if (!isValidUrl(newUrl)){ 
				break;
			}
			dbAdapter = new DbAdapter(this);
			dbAdapter.open();
			dbAdapter.updateServer(newName, newUrl, id);
			dbAdapter.close();

			Intent myIntent = new Intent(v.getContext(), ActivityServerList.class);
			startActivityForResult(myIntent, 0);
			break;

		case R.id.deleteServerBtn:

			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setMessage(getString(R.string.removeServer));
			
			b.setPositiveButton(getString(R.string.yes),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dbAdapter = new DbAdapter(context);
					dbAdapter.open();
					dbAdapter.deleteServer(id);
					dbAdapter.close();
					
					Intent intent = new Intent(context, ActivityServerList.class);
					startActivityForResult(intent, 0);
				}
			});
			
			b.setNegativeButton(getString(R.string.no),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int whichButton) {
					dialog.cancel();
				}
			});
			
			b.show();
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
package com.android.repetierserverapp;

import com.android.repetierserverapp.db.DbAdapter;
import android.os.Bundle;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FragAddServer extends Fragment implements OnClickListener {

	private Button createServer;
	private TextView serverName;
	private TextView serverUrl;

	private String name;
	private String url;

	private DbAdapter dbAdapter;
	private Cursor cursor;




	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {
		createServer = (Button) v.findViewById(R.id.createServerBtn);
		serverName = (TextView) v.findViewById(R.id.serverNameET);
		serverUrl = (TextView) v.findViewById(R.id.serverUrlET);
		createServer.setOnClickListener(this);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	}

	@Override
	public void onClick(View v) {
		Log.d("onClick" , "entrato");

		switch (v.getId()) {

		case R.id.createServerBtn:

			Log.d("onClick createServerBtn:" , "entrato");

			name = serverName.getText().toString();
			//if (!isValidName(name)){ //TODO errore: nome non valido}
			//	;
			//}
			url = serverUrl.getText().toString();
			//if (!isValidUrl(url)){ //TODO errore: indirizzo non valido
			//	;
			//}

			dbAdapter = new DbAdapter(getActivity());
			dbAdapter.open();
			dbAdapter.createServer(name, url);
			dbAdapter.close();

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
			dbAdapter = new DbAdapter(getActivity());
		dbAdapter.open();
		cursor = dbAdapter.fetchServerByName(name);
		dbAdapter.close();

		getActivity().startManagingCursor(cursor);
		if (cursor.getCount()== 1) {
			cursor.close(); 
			return false;
		}

		cursor.close(); 
		return true;
	}

}

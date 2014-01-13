package com.android.repetierserverapp;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.repetierserverapp.db.DbAdapter;
import com.android.repetierserverapp.db.DbHelper;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Server;
import com.grasselli.android.repetierserverapi.Server.ServerCallbacks;

/**
 * A fragment representing a single Server detail screen. This fragment is
 * either contained in a {@link ActivityServerList} in two-pane mode (on
 * tablets) or a {@link ActivityServerDetail} on handsets.
 */
public class FragServerDetail extends ListFragment implements ServerCallbacks {

	private DbAdapter dbAdapter;
	Server server;
	public static final String ARG_SERVER_ID = "item_id";

	

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public FragServerDetail() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_SERVER_ID)) {
			//TODO ricerca tramite id dell'url del server (database)
			
			//Cursor c = dbAdapter.fetchServerById(ARG_SERVER_ID);
			
			//server = new Server (c.getString(c.getColumnIndex(DbHelper.DB_URL)), c.getString(c.getColumnIndex(DbHelper.DB_NAME)));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_server_detail,
				container, false);
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		//TODO usare url server per richiedere la lista delle stampanti
		//server.getPrinterList();
		
	}

	@Override
	public void onError(String error) {		
	}

	@Override
	public void onPrinterListUpdated(ArrayList<Printer> printerList) {
		// TODO Auto-generated method stub
		
	}
}

package com.android.repetierserverapp.ServerDetailList;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.PrinterControll.ActivityPrinterControll;
import com.android.repetierserverapp.ServerDetailList.PrinterListAdapter.PrinterListAdapterCallback;
import com.android.repetierserverapp.db.DbAdapter;
import com.android.repetierserverapp.db.DbHelper;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Server;
import com.grasselli.android.repetierserverapi.Server.ServerCallbacks;

public class FragServerDetail extends ListFragment {


	private DbAdapter dbAdapter;

	private ListView listview;
	public static final String ARG_SERVER_ID = "item_id";
	public static long idPrinter;
	private PrinterListAdapter adapter;

	private Server server;

	private ArrayList<Printer> printerList;

	private PrinterListAdapterCallback printerListAdapterCallback;
	private OnItemClickListener itemClickListener;

	private Timer myTimer;

	
	
	public FragServerDetail() {
	}

	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

		
		listview = getListView();
		
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (getArguments().containsKey(ARG_SERVER_ID)) {

			//Log.d("onCreate1:", Long.toString(getArguments().getLong(ARG_SERVER_ID)));

			dbAdapter = new DbAdapter(getActivity());
			dbAdapter.openReadOnly();
			idPrinter = getArguments().getLong(ARG_SERVER_ID);
			Cursor c = dbAdapter.fetchServerById(Long.toString(idPrinter));
			c.moveToFirst();
			String url = c.getString(c.getColumnIndex(DbHelper.DB_URL));
			String name = c.getString(c.getColumnIndex(DbHelper.DB_NAME));
			c.close();
			dbAdapter.close();


			printerListAdapterCallback = new PrinterListAdapterCallback() {
				@Override
				public void updatePrinterList() {
					//Log.d("refreshListView", " richiesta dell'adapter di aggiornare la lista delle stampanti");
					server.updatePrinterList(getActivity());
				}
			};

			itemClickListener = new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapter, View view,
						int position, long id) {
					
					stopTimer();
					
					Printer p = printerList.get(position);

					Intent detailIntent = new Intent(getActivity(), ActivityPrinterControll.class);
					detailIntent.putExtra("url", p.getServer().getUrl());
					detailIntent.putExtra("alias", p.getServer().getAlias());
					detailIntent.putExtra("name", p.getName());
					detailIntent.putExtra("slug", p.getSlug());
					detailIntent.putExtra("online", p.getOnline());
					detailIntent.putExtra("currentJob", p.getCurrentJob());
					detailIntent.putExtra("active", p.getActive());
					detailIntent.putExtra("progress", p.getProgress());
					detailIntent.putExtra("position", position);
					startActivity(detailIntent);
				}
			};

			listview.setOnItemClickListener(itemClickListener);
			
			server = new Server (url, name);
			server.setCallbacks(new ServerCallbacks() {

				@Override
				public void onPrinterListUpdated(ArrayList<Printer> list) {
					//Log.d("OnPrinterListUpdated", "entrato");
					printerList = list;
					adapter = new PrinterListAdapter(getActivity(), R.layout.printer_line, list, printerListAdapterCallback); 
					listview.setAdapter(adapter);

				}

				@Override
				public void onPrinterListError(String error) {
					Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
					
				}
			});
			
			server.updatePrinterList(getActivity());
			startTimer();
		}
	}


	
	@Override
	public void onDetach() {
		super.onDetach();
	}


	public void startTimer(){
		Log.d("startTimer ",  "PrinterList");
		myTimer = new Timer();
		myTimer.schedule(new TimerTask() {          
			@Override
			public void run() {
				server.updatePrinterList(getActivity());
				Log.d("Timer", "PrinterList");
			}
		}, 0, 5000);
	}

	public void stopTimer(){
		Log.d("stopTimer", "PrinterList");
			myTimer.cancel();
	}

}
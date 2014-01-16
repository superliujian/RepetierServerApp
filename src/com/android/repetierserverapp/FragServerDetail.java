package com.android.repetierserverapp;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.repetierserverapp.FragServerList.ServerAppCallbacks;
import com.android.repetierserverapp.db.DbAdapter;
import com.android.repetierserverapp.db.DbHelper;
import com.android.repetierserverapp.utils.PrinterListAdapter;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Server;
import com.grasselli.android.repetierserverapi.Server.ServerCallbacks;

public class FragServerDetail extends ListFragment implements ServerAppCallbacks {

	public interface PrinterAppCallbacks {
		public void onPrinterSelected(long id);
	};

	private static PrinterAppCallbacks sDummyCallbacks = new PrinterAppCallbacks() {
		@Override
		public void onPrinterSelected(long id) {
		}
	};



	private DbAdapter dbAdapter;

	private ListView listview;
	public static final String ARG_SERVER_ID = "item_id";
	private PrinterListAdapter adapter;

	private ServerCallbacks callbacks;
	private Server server;

	public FragServerDetail() {
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_SERVER_ID)) {


			Log.d("onCreate1:", Long.toString(getArguments().getLong(ARG_SERVER_ID)));

			dbAdapter = new DbAdapter(getActivity());
			dbAdapter.openReadOnly();
			Cursor c = dbAdapter.fetchServerById(Long.toString(getArguments().getLong(ARG_SERVER_ID)));
			c.moveToFirst();
			String url = c.getString(c.getColumnIndex(DbHelper.DB_URL));
			String name = c.getString(c.getColumnIndex(DbHelper.DB_NAME));
			c.close();
			dbAdapter.close();

			server = new Server (url, name);
			server.setCallbacks(
					callbacks = new ServerCallbacks() {

						@Override
						public void onPrinterListUpdated(ArrayList<Printer> printerList) {
							Log.d("OnPrinterListUpdated", "entrato");
							adapter = new PrinterListAdapter(getActivity(), R.layout.printer_line, printerList);
							listview.setAdapter(adapter);

						}

						@Override
						public void onError(String error) {
						}
					});
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

		server.updatePrinterList(getActivity());
		listview = getListView();

		/*
		ArrayList<MPrinter> list = new ArrayList<MPrinter>();
		list.add(new MPrinter("uno", "uno"));
		list.add(new MPrinter("due", "due"));
		list.add(new MPrinter("tre", "tre"));
		list.add(new MPrinter("uno", "uno"));
		list.add(new MPrinter("due", "quattro"));
		list.add(new MPrinter("sei", "sei"));
		 */
	}


	@Override
	public void onServerSelected(long id) {
		// TODO Auto-generated method stub
		
	}

}
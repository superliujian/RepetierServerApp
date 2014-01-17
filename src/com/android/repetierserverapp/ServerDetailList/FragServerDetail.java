package com.android.repetierserverapp.ServerDetailList;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.PrinterControll.ActivityPrinterControll;
import com.android.repetierserverapp.PrinterControll.ModelList.FragModelList;
import com.android.repetierserverapp.R.layout;
import com.android.repetierserverapp.ServerDetailList.PrinterListAdapter.PrinterListAdapterCallback;
import com.android.repetierserverapp.ServerList.FragServerList.ServerAppCallbacks;
import com.android.repetierserverapp.db.DbAdapter;
import com.android.repetierserverapp.db.DbHelper;
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

	private ServerCallbacks serverCallbacks;
	private Server server;

	private ArrayList<Printer> printerList;

	private PrinterListAdapterCallback printerListAdapterCallback;
	private OnItemClickListener itemClickListener;

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

			printerListAdapterCallback = new PrinterListAdapterCallback() {
				@Override
				public void updatePrinterList() {
					Log.d("refreshListView", " richiesta dell'adapter di aggiornare la lista delle stampanti");
					server.updatePrinterList(getActivity());
				}
			};

			itemClickListener = new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapter, View view,
						int position, long id) {
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
					startActivity(detailIntent);
				}
			};

			server = new Server (url, name);
			server.setCallbacks(
					serverCallbacks = new ServerCallbacks() {

						@Override
						public void onPrinterListUpdated(ArrayList<Printer> list) {
							Log.d("OnPrinterListUpdated", "entrato");
							printerList = list;
							adapter = new PrinterListAdapter(getActivity(), R.layout.printer_line, list, printerListAdapterCallback); 
							listview.setAdapter(adapter);
							listview.setOnItemClickListener(itemClickListener);

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

	}


	@Override
	public void onServerSelected(long id) {
		// TODO Auto-generated method stub

	}

}
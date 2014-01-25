package com.android.repetierserverapp.ServerList;

import com.android.repetierserverapp.ActivityAddServer;
import com.android.repetierserverapp.ActivityModifyServer;
import com.android.repetierserverapp.R;
import com.android.repetierserverapp.PrinterControll.ActivityPrinterControll;
import com.android.repetierserverapp.db.DbAdapter;
import com.grasselli.android.repetierserverapi.Printer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView; 
import android.widget.Toast;

public class FragServerList extends ListFragment implements OnItemClickListener, OnItemLongClickListener{

	
	//TODO RIMUOVERE
	public interface ServerAppCallbacks {
		public void onServerSelected(long id);
	};

	private static ServerAppCallbacks sDummyCallbacks = new ServerAppCallbacks() {
		@Override
		public void onServerSelected(long id) {
		}
	};


	private DbAdapter dbAdapter;
	private Cursor cursor;

	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private int mActivatedPosition = ListView.INVALID_POSITION;

	private ListView listView;

	private ServerAppCallbacks mCallbacks;

	
	
	 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_server_selection, container, false);
	}

	
	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {

		listView = getListView();

		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}

		super.onViewCreated(v, savedInstanceState);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		dbAdapter = new DbAdapter(getActivity());
		dbAdapter.openReadOnly();
		cursor = dbAdapter.fetchAllServer();

		ServerListAdapter adapter = new ServerListAdapter(getActivity(), cursor);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof ServerAppCallbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (ServerAppCallbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}


	@Override
	public void onItemClick(AdapterView<?> listView, View view, int position, long id) {

		mCallbacks.onServerSelected(id);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		listView.setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			listView.setItemChecked(mActivatedPosition, false);
		} else {
			listView.setItemChecked(position, true);
		}
		mActivatedPosition = position;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> listView, View view, int position,
			long id) {

		Intent detailIntent = new Intent(getActivity(), ActivityModifyServer.class);
		int idServer = (int) id;
		detailIntent.putExtra("id", idServer);
		startActivity(detailIntent);
		return true;
	}

}

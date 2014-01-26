package com.android.repetierserverapp.ServerList;

import com.android.repetierserverapp.ActivityModifyServer;
import com.android.repetierserverapp.R;
import com.android.repetierserverapp.db.DbAdapter;
import com.android.repetierserverapp.db.DbHelper;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class FragServerList extends ListFragment implements OnItemClickListener, OnItemLongClickListener{


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

		if (!(activity instanceof ServerAppCallbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (ServerAppCallbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
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
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	public void setActivateOnItemClick(boolean activateOnItemClick) {
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
			final long id) {

		Cursor c = (Cursor) listView.getAdapter().getItem(position);
		String name = c.getString(c.getColumnIndex(DbHelper.DB_NAME));

		String[] array = new String[2];
		array[0] = getString(R.string.popModifyServer);
		array[1] = getString(R.string.popDeleteServer);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(name)
		.setItems(array, 
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					Intent detailIntent = new Intent(getActivity(), ActivityModifyServer.class);
					int idServer = (int) id;
					detailIntent.putExtra("id", idServer);
					startActivity(detailIntent);
					break;

				case 1:
				{
					deleteServer((int) id);
					break;
				}

				}
			}
		});

		builder.create().show();
		return true;
	}


	public void deleteServer(final int id){

		AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
		b.setMessage(getString(R.string.removeServer));

		b.setPositiveButton(getString(R.string.yes),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dbAdapter = new DbAdapter(getActivity());
				dbAdapter.open();
				dbAdapter.deleteServer(id);
				dbAdapter.close();

				Intent intent = new Intent(getActivity(), ActivityServerList.class);
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
	}


}

package com.android.repetierserverapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.repetierserverapp.dummy.DummyContent;

/**
 * A fragment representing a single Server detail screen. This fragment is
 * either contained in a {@link ActivityServerList} in two-pane mode (on
 * tablets) or a {@link ActivityServerDetail} on handsets.
 */
public class FragServerDetail extends ListFragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
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
		//TODO usare url server per richiedere la lista delle stampanti
		super.onViewCreated(view, savedInstanceState);
	}
}

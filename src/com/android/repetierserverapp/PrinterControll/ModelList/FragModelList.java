package com.android.repetierserverapp.PrinterControll.ModelList;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ListView;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.PrinterControll.ActivityPrinterControll;
import com.grasselli.android.repetierserverapi.Model;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Printer.ModelCallbacks;
import com.grasselli.android.repetierserverapi.Server;

public class FragModelList extends ListFragment {


	public interface FragModelCallbacks {
		public void updateJobList(Printer printer);
	};

	private static FragModelCallbacks sDummyCallbacks = new FragModelCallbacks() {
		@Override
		public void updateJobList(Printer printer) {
		}

	};


	private FragModelCallbacks callback;

	private ListView listview;
	private ImageButton refresh;

	private ModelListAdapter adapter;

	private Printer printer;

	private static Timer myTimer;
	public static int modelInterval;
	private static boolean timerRunning;


	public FragModelList() {
	}



	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (!(activity instanceof FragModelCallbacks )) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		callback = (FragModelCallbacks) activity;
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("frag0", "frag0");
		modelInterval = 5000;
		timerRunning = false;
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_model_list,
				container, false);
		return rootView;
	}



	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);


		listview = getListView();
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {

				final int idMod = ((Model) listview.getAdapter().getItem(position)).getId();

				String[] array = new String[2];
				array[1] = getString(R.string.popModelDelete);
				array[0] = getString(R.string.popModelToQueue);

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(((Model) listview.getAdapter().getItem(position)).getName())
				.setItems(array, 
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 1:
							printer.deleteModel(getActivity(), idMod);
							printer.updateJobList(getActivity());
							break;

						case 0:
							printer.copyModel(getActivity(), idMod);
							callback.updateJobList(printer);
							break;
						}
					}
				});
				builder.create().show();

				return true;
			}
		});
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		String url = getArguments().getString("url");
		String alias = getArguments().getString("alias");
		String name = getArguments().getString("name");
		String slug = getArguments().getString("slug");
		int online = getArguments().getInt("online");
		String currentJob = getArguments().getString("currentJob");
		Boolean active = getArguments().getBoolean("active");
		double progress = getArguments().getDouble("progress");

		printer = new Printer(new Server(url, alias),name, slug, online, currentJob, active, progress);

		printer.setModelCallbacks(new ModelCallbacks() {
			@Override
			public void onModelUploaded() {
				printer.updateModelList(getActivity());
				Toast.makeText(getActivity(), getString(R.string.onModelUploaded), Toast.LENGTH_LONG).show();			}

			@Override
			public void onModelListUpdated(ArrayList<Model> newModelList) {

				adapter = new ModelListAdapter(getActivity(), R.layout.model_line, newModelList); 
				listview.setAdapter(adapter);
			}

			@Override
			public void onModelDeleted() {
				printer.updateModelList(getActivity());
				Toast.makeText(getActivity(), getString(R.string.onModelDeleted), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onModelCopied() {
				Toast.makeText(getActivity(), getString(R.string.onModelCopied), Toast.LENGTH_LONG).show();	
			}

			@Override
			public void onModelError(String error) {
				Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();				
			}
		});

		printer.updateModelList(getActivity());
	}



	@Override
	public void onDetach() {
		super.onDetach();
		callback = sDummyCallbacks;
	}

	

	public void startTimer(){
		Log.d("startTimer", "Model");
		myTimer = new Timer();
		timerRunning = true;
		myTimer.schedule(new TimerTask() {          
			@Override
			public void run() {
				printer.updateModelList(getActivity().getApplicationContext());
				Log.d("Timer", "Model");
			}
		}, 0, modelInterval);
	}

	public void stopTimer(){
		Log.d("stopTimer", "Model");
		if (timerRunning){
			myTimer.cancel();
		}
	}
}
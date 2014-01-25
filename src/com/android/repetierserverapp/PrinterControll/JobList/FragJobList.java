package com.android.repetierserverapp.PrinterControll.JobList;

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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.PrinterControll.ActivityPrinterControll;
import com.grasselli.android.repetierserverapi.Job;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Printer.JobCallbacks;
import com.grasselli.android.repetierserverapi.ReplyMessage;
import com.grasselli.android.repetierserverapi.Server;

public class FragJobList extends ListFragment implements OnItemLongClickListener, JobCallbacks{

	private ListView listview;
	private JobListAdapter adapter;

	private TextView warning;

	private static Printer printer;

	private static Timer myTimer;
	

	public FragJobList() {
	}



	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}



	@Override
	public void onDetach() {
		super.onDetach();
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("frag1", "frag1");
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_job_list,
				container, false);
		return rootView;
	}



	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		listview = getListView();
		listview.setOnItemLongClickListener(this);

		warning = (TextView) view.findViewById(R.id.warningTv);
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

		printer = new Printer(new Server(url, alias), name, slug, online, currentJob, active, progress);

		printer.setJobCallbacks(this);

		printer.updateJobList(getActivity());
	}



	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
			long id) {

		if(printer.getOnline()== 1){

			final Job job = (Job) listview.getAdapter().getItem(position);
			if(job.getState().equals("stored")){
				String[] array = new String[2];
				array[0] = getString(R.string.popJobStart);
				array[1] = getString(R.string.popJobDelete);

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(((Job) listview.getAdapter().getItem(position)).getName())
				.setItems(array, 
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							printer.StartJob(getActivity(), job.getId());
							break;

						case 1:
							printer.removeJob(getActivity(), job.getId());
							break;
						}
					}
				});

				builder.create().show();
				return true;

			}
			else{

				String[] array = new String[1];
				array[0] = getString(R.string.popJobStop);

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(((Job) listview.getAdapter().getItem(position)).getName())
				.setItems(array, 
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							printer.stopJob(getActivity(), job.getId());
							break;
						}
					}
				});

				builder.create().show();
				return true;
			}
		} else{

			final int idJob = ((Job) listview.getAdapter().getItem(position)).getId();

			String[] array = new String[1];
			array[0] = getString(R.string.popJobDelete);

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(((Job) listview.getAdapter().getItem(position)).getName())
			.setItems(array, 
					new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						printer.removeJob(getActivity(), idJob);
						break;
					}
				}
			});
			builder.create().show();

			return true;
		}
	}



	public void updateListView (Printer printer){
		//Log.d("updateListView Frag Job", "entrato");
		printer.setJobCallbacks(this);
		printer.updateJobList(getActivity().getApplicationContext());
	}



	@Override
	public void onJobError(String error) {
		Toast.makeText(getActivity(), "JOB ERROR", Toast.LENGTH_LONG).show();	
		
	}



	@Override
	public void onJobListUpdated(ArrayList<Job> newJobList) {

		adapter = new JobListAdapter(getActivity(), R.layout.job_line, newJobList); 
		listview = getListView();
		listview.setAdapter(adapter);	

		warning.setText(getString(R.string.warningOffline));
		if (printer.getOnline() == 0){
			warning.setVisibility(View.VISIBLE);
		}else{
			warning.setVisibility(View.GONE);
		}
	}

	@Override
	public void onJobRemoved() {
		printer.updateJobList(getActivity());
		Toast.makeText(getActivity(), getString(R.string.onJobRemoved), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onJobStarted() {
		printer.updateJobList(getActivity());
		Toast.makeText(getActivity(), getString(R.string.onJobStarted), Toast.LENGTH_LONG).show();				
	}

	@Override
	public void onJobStopped() {
		printer.updateJobList(getActivity());
		Toast.makeText(getActivity(), getString(R.string.onJobStopped), Toast.LENGTH_LONG).show();				
	}

	@Override
	public void onMessages(ArrayList<ReplyMessage> newMessages) {
		// TODO Auto-generated method stub
	}


	public void startTimer(int interval){
		Log.d("startTimer",  "intervallo: " + Integer.toString(interval));
		myTimer = new Timer();
		myTimer.schedule(new TimerTask() {          
			@Override
			public void run() {
				printer.updateJobList(getActivity().getApplicationContext());
				Log.d("Timer", "Job");
			}
		}, 0, interval);
	}

	public void stopTimer(){
		Log.d("stopTimer", "Job");
			myTimer.cancel();
	}



}
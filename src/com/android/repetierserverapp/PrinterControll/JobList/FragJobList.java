package com.android.repetierserverapp.PrinterControll.JobList;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.ListView;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.PrinterControll.JobList.JobListAdapter.JobListAdapterCallback;
import com.grasselli.android.repetierserverapi.Job;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Printer.JobCallbacks;
import com.grasselli.android.repetierserverapi.ReplyMessage;
import com.grasselli.android.repetierserverapi.Server;

public class FragJobList extends ListFragment{

	private ListView listview;
	private JobListAdapter adapter;

	private Printer printer;

	private JobListAdapterCallback jobListAdapterCallback;
	//private OnItemClickListener itemClickListener;


	
	public FragJobList() {
	}


	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		
		jobListAdapterCallback = new JobListAdapterCallback() {

			@Override
			public void updateJobList(Printer printer) {
				printer.updateJobList(getActivity());
			}

			@Override
			public void startJob(Printer printer, int id) {
				printer.StartJob(getActivity(), id);
			}

			@Override
			public void stopJob(Printer printer, int id) {
				printer.stopJob(getActivity(), id);
			}

			@Override
			public void removeJob(Printer printer, int id) {
				printer.removeJob(getActivity(), id);			
			}
		};

		/*
		itemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		};
		listview.setOnItemClickListener(itemClickListener);
		 */

		printer.setJobCallbacks(new JobCallbacks() {

			@Override
			public void onError(String error) {
				Toast.makeText(getActivity(), "JOB ERROR", Toast.LENGTH_LONG).show();	
			}

			@Override
			public void onJobListUpdated(ArrayList<Job> newJobList) {
				
				adapter = new JobListAdapter(getActivity(), R.layout.job_line, newJobList, jobListAdapterCallback, printer); 
				listview = getListView();
				listview.setAdapter(adapter);	
				Toast.makeText(getActivity(), "ATTENZIONE: stampante disattivata o scollegata", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onJobRemoved() {
				printer.updateJobList(getActivity());
				Toast.makeText(getActivity(), "Il lavoro è stato rimosso", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onJobStarted() {
				printer.updateJobList(getActivity());
				Toast.makeText(getActivity(), "Il lavoro è stato avviato", Toast.LENGTH_LONG).show();				
			}

			@Override
			public void onJobStopped() {
				printer.updateJobList(getActivity());
				Toast.makeText(getActivity(), "Il lavoro è stato fermato", Toast.LENGTH_LONG).show();				
			}

			@Override
			public void onMessages(ArrayList<ReplyMessage> newMessages) {
				// TODO Auto-generated method stub
			}		
		});
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

		printer.updateJobList(getActivity());
	}
}
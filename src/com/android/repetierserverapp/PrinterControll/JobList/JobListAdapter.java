package com.android.repetierserverapp.PrinterControll.JobList;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.repetierserverapp.R;
import com.grasselli.android.repetierserverapi.Job;

public class JobListAdapter extends ArrayAdapter<Job> {

	private Context context;
	private ArrayList<Job> jobList;
	private Job job;



	public JobListAdapter(Context context, int textViewResourceId, ArrayList<Job> list) {
		super(context, textViewResourceId, list);
		this.context = context;
		this.jobList = list;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;
		if (rowView == null){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.job_line, null);
			//parent, false
		}

		TextView jobName = (TextView) rowView.findViewById(R.id.jobName);
		TextView dimenJob = (TextView) rowView.findViewById(R.id.dimenJob);
		TextView JobStatus = (TextView) rowView.findViewById(R.id.jobStatus);

		TextView progress = (TextView) rowView.findViewById(R.id.progress);
		TextView perc = (TextView) rowView.findViewById(R.id.perc);

		job = jobList.get(position);

		jobName.setText(job.getName());

		JobStatus.setText(job.getState());

		double size = job.getLength();
		String dimen = Double.toString(Math.round(size/1048576*100)/100);
		dimenJob.setText(dimen);

		if (job.getState().equals("stored")){
			progress.setVisibility(View.INVISIBLE);
			perc.setVisibility(View.INVISIBLE);
		}
		else {
			progress.setVisibility(View.VISIBLE);
			perc.setVisibility(View.VISIBLE);
			double d = job.getDone();
			String done = Double.toString(Math.round(d*100)/100);
			progress.setText(done);
		}

		return rowView;
	}

}
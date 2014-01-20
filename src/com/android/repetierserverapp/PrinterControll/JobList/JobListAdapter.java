package com.android.repetierserverapp.PrinterControll.JobList;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.android.repetierserverapp.R;
import com.grasselli.android.repetierserverapi.Job;
import com.grasselli.android.repetierserverapi.Printer;

public class JobListAdapter extends ArrayAdapter<Job> implements OnClickListener{

	private Context context;
	private ArrayList<Job> jobList;
	private Job job;
	private JobListAdapterCallback listener;
	private Printer printer;



	public JobListAdapter(Context context, int textViewResourceId, ArrayList<Job> list, JobListAdapterCallback listener, Printer printer) {
		super(context, textViewResourceId, list);
		this.context = context;
		this.jobList = list;
		this.listener = listener;
		this.printer = printer;
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

		Button startBtt = (Button) rowView.findViewById(R.id.startBtt);
		Button removeBtt = (Button) rowView.findViewById(R.id.removeBtt);
		Button stopBtt = (Button) rowView.findViewById(R.id.stopBtt);

		startBtt.setOnClickListener(this);
		removeBtt.setOnClickListener(this);
		stopBtt.setOnClickListener(this);

		job = jobList.get(position);

		jobName.setText(job.getName());

		JobStatus.setText(job.getState());

		double size = job.getLength();
		String dimen = Double.toString(Math.round(size/1048576*100)/100);
		dimenJob.setText(dimen);

		if (job.getState().equals("stored")){
			progress.setVisibility(View.INVISIBLE);
			perc.setVisibility(View.INVISIBLE);
			stopBtt.setEnabled(false);
			removeBtt.setEnabled(true);
		}
		else {
			progress.setVisibility(View.VISIBLE);
			perc.setVisibility(View.VISIBLE);
			stopBtt.setEnabled(true);
			removeBtt.setEnabled(false);

			double d = job.getDone();
			String done = Double.toString(Math.round(d*100)/100);
			progress.setText(done);
		}

		if (printer.getOnline() == 1){
			startBtt.setEnabled(true);
		} else		{
			startBtt.setEnabled(false);
		}
		
		return rowView;
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.startBtt:
			listener.startJob(printer, job.getId());
			break;
		case R.id.stopBtt:
			listener.stopJob(printer, job.getId());
			break;
		case R.id.removeBtt:
			listener.removeJob(printer, job.getId());
			break;
		}
	}



	public interface JobListAdapterCallback {
		public void updateJobList(Printer printer);
		public void startJob(Printer printer, int id);
		public void stopJob(Printer printer, int id);
		public void removeJob(Printer printer, int id);
	}
}
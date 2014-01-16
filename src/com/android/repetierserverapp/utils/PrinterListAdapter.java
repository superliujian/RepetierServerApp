package com.android.repetierserverapp.utils;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.repetierserverapp.R;
import com.grasselli.android.repetierserverapi.Printer;

public class PrinterListAdapter extends ArrayAdapter<Printer> {

	private Context context;
	private ArrayList<Printer> printerList;

	

	public PrinterListAdapter(Context context, int textViewResourceId, ArrayList<Printer> list) {
		super(context, textViewResourceId, list);
		this.context = context;
		this.printerList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;
		if (rowView == null){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.printer_line, null);
			//parent, false
		}
				
		TextView printerName = (TextView) rowView.findViewById(R.id.printerName);
		TextView printerStatus = (TextView) rowView.findViewById(R.id.printerStatus);
		
		TextView printerCurrent = (TextView) rowView.findViewById(R.id.current);
		TextView printerCurrentJob = (TextView) rowView.findViewById(R.id.currentJob);
		TextView printerProgress = (TextView) rowView.findViewById(R.id.progress);
		TextView printerProgressJob = (TextView) rowView.findViewById(R.id.progressJob);
		TextView perc = (TextView) rowView.findViewById(R.id.perc);
		
		printerName.setText(printerList.get(position).getName());
		
		int online = printerList.get(position).getOnline();
		switch (online) {

		case 0:
			printerStatus.setText("Offline");
			printerStatus.setTextAppearance(getContext(), R.style.offline);
			break;
		case 1:
			printerStatus.setText("Online");
			printerStatus.setTextAppearance(getContext(), R.style.online);
			break;
		default:
			printerStatus.setText("Error");
			printerStatus.setTextAppearance(getContext(), R.style.offline);
			break;
		}
				
		String currentJob = printerList.get(position).getCurrentJob();
		if (isWorking(currentJob)){
			printerCurrent.setVisibility(View.VISIBLE);
			printerCurrentJob.setVisibility(View.VISIBLE);
			printerProgress.setVisibility(View.VISIBLE);
			printerProgressJob.setVisibility(View.VISIBLE);
			perc.setVisibility(View.VISIBLE);
			
			printerCurrentJob.setText(currentJob);
			
			String progress = Double.toString(Math.round(printerList.get(position).getProgress()*100)/100);
			printerProgressJob.setText(progress);
		}
		else {
			printerCurrent.setVisibility(View.INVISIBLE);
			printerCurrentJob.setVisibility(View.INVISIBLE);
			printerProgress.setVisibility(View.INVISIBLE);
			printerProgressJob.setVisibility(View.INVISIBLE);
			perc.setVisibility(View.INVISIBLE);
		}
		
		return rowView;
	}

	private Boolean isWorking (String currentJob){
		if (currentJob == "none") return false;
		else return true;		
	}
	
} 






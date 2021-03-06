package com.android.repetierserverapp.ServerDetailList;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.repetierserverapp.R;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Printer.PrinterCallbacks;

public class PrinterListAdapter extends ArrayAdapter<Printer> implements OnClickListener{

	private Context context;
	private ArrayList<Printer> printerList;
	private Printer printer;
	private PrinterListAdapterCallback listener;
	private ImageButton power;


	public PrinterListAdapter(Context context, int textViewResourceId, ArrayList<Printer> list, PrinterListAdapterCallback listener) {
		super(context, textViewResourceId, list);
		this.context = context;
		this.printerList = list;
		this.listener = listener;
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
		/*
		Switch sw = (Switch) rowView.findViewById(R.id.switch1);
		sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					Log.d("on", "on");
					printer.turnOn(getContext());

				}else{
					Log.d ("off","off");
					printer.turnOff(getContext());
				}

			}
		});
		 */


		power = (ImageButton) rowView.findViewById(R.id.powerButton);
		power.setFocusable(false);
		power.setOnClickListener(this);

		printer = printerList.get(position);

		printer.setPrinterCallbacks(new PrinterCallbacks() {

			@Override
			public void onCommandExecuted() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onChangeState() {
				listener.updatePrinterList();
			}

			@Override
			public void onPrinterError(String error) {
			}

			@Override
			public void onChangeFanState() {
				// TODO Auto-generated method stub
			}
		});

		printerName.setText(printer.getName());

		int online = printer.getOnline();
		boolean active = printer.getActive();

		//		sw.setChecked(active);

		if(active) {
			power.setImageResource(R.drawable.off);
			switch (online){
			case 0:
				printerStatus.setText("Scollegata");
				printerStatus.setTextAppearance(getContext(), R.style.offline);
				break;
			case 1:
				printerStatus.setText("Connessa");
				printerStatus.setTextAppearance(getContext(), R.style.online);
				break;
			default:
				printerStatus.setText("Error");
				printerStatus.setTextAppearance(getContext(), R.style.offline);
				break;
			}
		}else {
			printerStatus.setText("Disattivata");
			printerStatus.setTextAppearance(getContext(), R.style.offline);

			power.setImageResource(R.drawable.on);
		}

		if(printer.getCurrentJob().equals("none")){
			printerCurrentJob.setText(R.string.noJobRunning2);
		} else {
			printerCurrentJob.setText(printer.getCurrentJob());
		}

		String progress = Double.toString(Math.round(printer.getProgress()*100)/100);
		printerProgressJob.setText(progress);

		return rowView;
	}



	public interface PrinterListAdapterCallback {
		public void updatePrinterList();
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.powerButton){
			if (printer.getActive()){
				printer.turnOff(getContext());
			}
			else {
				printer.turnOn(getContext());		
			}
		}		
	}
}
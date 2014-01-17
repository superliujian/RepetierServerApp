package com.android.repetierserverapp.ServerDetailList;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.repetierserverapp.R;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Printer.PrinterCallbacks;

public class PrinterListAdapter extends ArrayAdapter<Printer> implements OnClickListener  {

	private Context context;
	private ArrayList<Printer> printerList;
	private Printer printer;
	private PrinterCallbacks callbacks;


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
		
		TextView activePrinter = (TextView) rowView.findViewById(R.id.activePrinter);
		activePrinter.setOnClickListener(this);
		
		printer = printerList.get(position);

		printer.setPrinterCallbacks(callbacks = new PrinterCallbacks() {
			
			@Override
			public void onError(String error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCommandExecuted() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChangeState() {
				Toast.makeText(getContext(), "Lo stato è cambiato", Toast.LENGTH_LONG).show();
				
			}
		});
		
		printerName.setText(printer.getName());

		int online = printer.getOnline();
		boolean active = printer.getActive();

		if(active) {
			switch (online){

			case 0:
				printerStatus.setText("Scollegata");
				printerStatus.setTextAppearance(getContext(), R.style.offline);
				activePrinter.setText("Disattiva Stampante");
				activePrinter.setTextAppearance(getContext(), R.style.offline);
				break;
			case 1:
				printerStatus.setText("Connessa");
				printerStatus.setTextAppearance(getContext(), R.style.online);
				activePrinter.setText("Disattiva Stampante");
				activePrinter.setTextAppearance(getContext(), R.style.offline);
				break;
			default:
				printerStatus.setText("Error");
				printerStatus.setTextAppearance(getContext(), R.style.offline);
				activePrinter.setText("Disattiva Stampante");
				activePrinter.setTextAppearance(getContext(), R.style.offline);
				break;
			}
		}else {
			printerStatus.setText("Disattivata");
			printerStatus.setTextAppearance(getContext(), R.style.offline);
			activePrinter.setText("Attiva Stampante");
			activePrinter.setTextAppearance(getContext(), R.style.online);
		}
	

	String currentJob = printer.getCurrentJob();

	if (isWorking(currentJob)){
		printerCurrent.setVisibility(View.VISIBLE);
		printerCurrentJob.setVisibility(View.VISIBLE);
		printerProgress.setVisibility(View.VISIBLE);
		printerProgressJob.setVisibility(View.VISIBLE);
		perc.setVisibility(View.VISIBLE);

		printerCurrentJob.setText(currentJob);

		String progress = Double.toString(Math.round(printer.getProgress()*100)/100);
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

@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.activePrinter:
		if (printer.getActive())
			printer.turnOff(getContext());
		else 
			printer.turnOn(getContext());
		break;
	}
}

} 






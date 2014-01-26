package com.android.repetierserverapp.PrinterControll;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.android.repetierserverapp.R;
import com.grasselli.android.repetierserverapi.Line;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Printer.PrinterCallbacks;
import com.grasselli.android.repetierserverapi.Printer.PrinterStatusCallbacks;
import com.grasselli.android.repetierserverapi.PrinterStatus;
import com.grasselli.android.repetierserverapi.Server;
import com.grasselli.android.repetierserverapi.Server.ServerCallbacks;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FragPrinterControl2 extends Fragment implements OnSeekBarChangeListener, OnClickListener, PrinterStatusCallbacks, ServerCallbacks, PrinterCallbacks, OnCheckedChangeListener{

	private TextView feedrateValue;
	private TextView flowrateValue;				

	private SeekBar feedrateSeek;
	private SeekBar flowrateSeek;

	private Switch extruderSwitch;
	private Switch bedSwitch;

	private TextView extrRead;
	private TextView extrSet;
	private TextView bedRead;
	private TextView bedSet;

	private TextView textViewError;
	
	private Button newExtrTempBtn;
	private Button newBedTempBtn;

	private EditText newExtrTempEt;
	private EditText newBedTempEt;

	private Printer printer;
	private Server server;
	private int position;	
	
	private Toast toast;

	private static Timer myTimer;
	private Timer printerTimer;
	
	private SharedPreferences prefs;


	public FragPrinterControl2(){
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d("FragPrinterControl2", "FragPrinterControl2");

		String url = getArguments().getString("url");
		String alias = getArguments().getString("alias");
		String name = getArguments().getString("name");
		String slug = getArguments().getString("slug");
		int online = getArguments().getInt("online");
		String currentJob = getArguments().getString("currentJob");
		Boolean active = getArguments().getBoolean("active");
		double progress = getArguments().getDouble("progress");
		position = getArguments().getInt("position");

		server = new Server(url, alias);
		printer = new Printer(server, name, slug, online, currentJob, active, progress);
		
		server.setCallbacks(this);
		printer.setPrinterStatusCallbacks(this);
		printer.setPrinterCallbacks(this);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_control2,
				container, false);
		return rootView;
	}



	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {

		feedrateValue = (TextView) v.findViewById(R.id.feedrateValueTextView);
		flowrateValue = (TextView) v.findViewById(R.id.flowrateValueTextView);

		feedrateSeek = (SeekBar) v.findViewById(R.id.feedrateSeekBar);
		feedrateSeek.setOnSeekBarChangeListener(this);
		feedrateSeek.setMax(200);

		flowrateSeek = (SeekBar) v.findViewById(R.id.flowrateSeekBar);
		flowrateSeek.setOnSeekBarChangeListener(this);
		flowrateSeek.setMax(200);	

		extruderSwitch = (Switch) v.findViewById(R.id.extruderSwitch);
		bedSwitch = (Switch) v.findViewById(R.id.bedSwitch);

		bedSwitch.setOnCheckedChangeListener(this);
		extruderSwitch.setOnCheckedChangeListener(this);

		extrRead = (TextView) v.findViewById(R.id.extrTempReadTextView);
		extrSet = (TextView) v.findViewById(R.id.extrTempSetTextView);
		bedRead = (TextView) v.findViewById(R.id.bedTempReadTextView);
		bedSet = (TextView) v.findViewById(R.id.bedTempSetTextView);

		textViewError = (TextView) v.findViewById(R.id.errorTV);
		
		newBedTempBtn = (Button) v.findViewById(R.id.newBedTempBtn);
		newExtrTempBtn = (Button) v.findViewById(R.id.newExtrTempBtn);

		newExtrTempBtn.setOnClickListener(this);
		newBedTempBtn.setOnClickListener(this);

		newExtrTempEt = (EditText) v.findViewById(R.id.newExtrTempEt);
		newBedTempEt = (EditText) v.findViewById(R.id.newBedTempEt);

		updateFrag(printer);
		
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.newBedTempBtn:
			int bedtemp = Integer.parseInt(newBedTempEt.getText().toString());
			if(checkTemp(bedtemp, 1)){
				printer.setBedTemp(getActivity(), bedtemp);
				printer.updatePrinterStatus(getActivity(), prefs.getInt("LAST_ID", 0), ActivityPrinterControll.FILTER);
			}
			break;

		case R.id.newExtrTempBtn:
			int extrtemp = Integer.parseInt(newExtrTempEt.getText().toString());
			if(checkTemp(extrtemp, 0)){
				printer.setExtrTemp(getActivity(), extrtemp);
				printer.updatePrinterStatus(getActivity(), prefs.getInt("LAST_ID", 0), ActivityPrinterControll.FILTER);
			}
			break;
		}
	}



	//type == 0 temperatura estrusore
	//type == 1 temperatura letto riscaldato
	private boolean checkTemp(int temp, int type){
		if (temp < 0) {
			toast= Toast.makeText(getActivity(),"La temperatura non può essere negativa", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		if (temp > 250 && type ==0 ){
			toast= Toast.makeText(getActivity(),"Temperatura estrusore eccessiva", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		if (temp < 160 && type ==0 ){
			toast= Toast.makeText(getActivity(),"Temperatura estrusore troppo bassa", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		if (temp > 100 && type ==0 ){
			toast= Toast.makeText(getActivity(),"Temperatura letto riscaldato eccessiva", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		return true;
	}


	//PrinterStatus Callback
	@Override
	public void onStatusError(String error) {
		textViewError.setVisibility(View.VISIBLE);

		if(error.equals("Printer offline")){
			textViewError.setText(getString(R.string.warningOffline));
		} else {
			textViewError.setText(error);
		}
	}


	//PrinterStatus Callback
	@Override
	public void onPrinterStatusUpdated(PrinterStatus status, int lastId,
			ArrayList<Line> tempLines) {

		textViewError.setVisibility(View.GONE);
		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("LAST_ID", lastId);
		editor.commit();
		
		Log.d("lastId stored: ", Integer.toString(lastId));
		
		flowrateValue.setText(Integer.toString(status.getFlow_multiply()));
		feedrateValue.setText(Integer.toString(status.getSpeed_multiply()));
		extrRead.setText(Double.toString(status.getTemp_read()));
		extrSet.setText(Double.toString(status.getTemp_set()));
		bedRead.setText(Double.toString(status.getBed_temp_read()));
		bedSet.setText(Double.toString(status.getBed_temp_set()));
		
		if (status.getTemp_set()==0)	
			turnOff(extruderSwitch);
		else turnOn(extruderSwitch);
		
		if (status.getBed_temp_set()==0)	
			turnOff(bedSwitch);
		else turnOn(bedSwitch);
	}



	public void turnOn (Switch s){
		s.setChecked(true);
	}

	public void turnOff (Switch s){
		s.setChecked(false);
	}



	//Accendi/spegni estrusore/letto
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	/*	
		switch (buttonView.getId()) {
		case R.id.extruderSwitch:
			if (extruderSwitch.isChecked()){
				printer.setExtrTemp(getActivity(), 0);
				printer.updatePrinterStatus(getActivity(), prefs.getInt("LAST_ID", 0), ActivityPrinterControll.FILTER);
			}
			else {
				int extrtemp = Integer.parseInt(newExtrTempEt.getText().toString());
				if(checkTemp(extrtemp, 0)){
					printer.setExtrTemp(getActivity(), extrtemp);
					printer.updatePrinterStatus(getActivity(), prefs.getInt("LAST_ID", 0), ActivityPrinterControll.FILTER);
				}
			}
			break;

		case R.id.bedSwitch:
			int bedtemp = Integer.parseInt(newBedTempEt.getText().toString());
			if(checkTemp(bedtemp, 1)){
				printer.setBedTemp(getActivity(), bedtemp);
				printer.updatePrinterStatus(getActivity(), prefs.getInt("LAST_ID", 0), ActivityPrinterControll.FILTER);
			}
			break;
		}
		*/
	}
	


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO inserire valore cambiamento
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		switch (seekBar.getId()) {

		case R.id.feedrateSeekBar:
			feedrateSeek.setSecondaryProgress(feedrateSeek.getProgress());
			//TODO printer.setFeedRate(seekBar.getProgress());
			break;

		case R.id.flowrateSeekBar:
			flowrateSeek.setSecondaryProgress(flowrateSeek.getProgress());
			//TODO printer.setFlowRate(seekBar.getProgress());
			break;
		}
	}



	public void startTimer(int interval){
		Log.d("startTimer", "intervallo: " + Integer.toString(interval));
		myTimer = new Timer();
		myTimer.schedule(new TimerTask() {          
			@Override
			public void run() {
				Log.d("Timer", "control2");
				printer.updatePrinterStatus(getActivity().getApplicationContext(), prefs.getInt("LAST_ID", 0), ActivityPrinterControll.FILTER);				
			}
		}, 0, interval);
		
		printerTimer = new Timer();
		printerTimer.schedule(new TimerTask() {          
			@Override
			public void run() {
				server.updatePrinterList(getActivity());
			}
		}, 0, 10000);
	}

	public void stopTimer(){
		Log.d("stopTimer", "control2");
			myTimer.cancel();
			printerTimer.cancel();
	}



	@Override
	public void onPrinterListError(String error) {
		Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();	
	}



	@Override
	public void onPrinterListUpdated(ArrayList<Printer> printerList) {
		printer = (printerList.get(position));
		printer.setPrinterCallbacks(this);
		printer.setPrinterStatusCallbacks(this);
		updateFrag(printer);
	}



	private void updateFrag(Printer printer) {
		boolean isOnLine;

		if (printer.getOnline() == 0)
			isOnLine = false;
		else 
			isOnLine = true;

		feedrateValue.setActivated(isOnLine);
		flowrateValue.setActivated(isOnLine);
		newBedTempBtn.setClickable(isOnLine);
		newExtrTempBtn.setClickable(isOnLine);
		extruderSwitch.setClickable(isOnLine);
		bedSwitch.setClickable(isOnLine);
		
	}


	// PrinterCallback
	@Override
	public void onPrinterError(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChangeState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCommandExecuted() {
		// TODO Auto-generated method stub
		
	}

}
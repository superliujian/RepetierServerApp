package com.android.repetierserverapp.PrinterControll;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.utils.Utils;
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

public class FragPrinterControl2 extends Fragment implements OnSeekBarChangeListener, OnClickListener, PrinterStatusCallbacks, ServerCallbacks, PrinterCallbacks{

	private TextView feedrateValue;
	private TextView flowrateValue;				

	private SeekBar feedrateSeek;
	private SeekBar flowrateSeek;

	private TextView newSpeed;
	private TextView newFlow;

	private Button turnOffExtr;
	private Button turnOffBed;

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

	public boolean fragLoaded = false;

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

		newSpeed = (TextView) v.findViewById(R.id.newSpeedTV);
		newFlow = (TextView) v.findViewById(R.id.newFlowTV);
		
		newSpeed.setVisibility(View.INVISIBLE);
		newFlow.setVisibility(View.INVISIBLE);

		feedrateSeek = (SeekBar) v.findViewById(R.id.feedrateSeekBar);
		feedrateSeek.setOnSeekBarChangeListener(this);
		feedrateSeek.setMax(200);

		flowrateSeek = (SeekBar) v.findViewById(R.id.flowrateSeekBar);
		flowrateSeek.setOnSeekBarChangeListener(this);
		flowrateSeek.setMax(200);	

		turnOffExtr = (Button) v.findViewById(R.id.powerExtrButton);
		turnOffBed = (Button) v.findViewById(R.id.powerBedButton);

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

		fragLoaded = true; 

		updateFrag(printer);

	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.newBedTempBtn:

			String bedValue = newBedTempEt.getText().toString();
			if (bedValue.equals("") || bedValue == null){
				toast= Toast.makeText(getActivity(),getString(R.string.notValue), Toast.LENGTH_LONG);
				toast.show();
				break;
			}
			if (!Utils.isNumeric(bedValue)){
				toast= Toast.makeText(getActivity(),getString(R.string.notNumericValue), Toast.LENGTH_LONG);
				toast.show();
				break;
			}
			int bedtemp = Integer.parseInt(newBedTempEt.getText().toString());
			if(checkTemp(bedtemp, 1)){
				printer.setBedTemp(getActivity(), bedtemp);
				printer.updatePrinterStatus(getActivity(), prefs.getInt("LAST_ID", 0), ActivityPrinterControll.FILTER);
			}
			break;

		case R.id.newExtrTempBtn:

			String extrValue = newExtrTempEt.getText().toString();
			if (extrValue.equals("") || extrValue == null){
				toast= Toast.makeText(getActivity(),getString(R.string.notValue), Toast.LENGTH_LONG);
				toast.show(); 
				break;
			}
			if (!Utils.isNumeric(extrValue)){
				toast= Toast.makeText(getActivity(),getString(R.string.notNumericValue), Toast.LENGTH_LONG);
				toast.show();
				break;
			}
			int extrtemp = Integer.parseInt(newExtrTempEt.getText().toString());
			if(checkTemp(extrtemp, 0)){
				printer.setExtrTemp(getActivity(), extrtemp);
				printer.updatePrinterStatus(getActivity(), prefs.getInt("LAST_ID", 0), ActivityPrinterControll.FILTER);
			}
			break;

		case R.id.powerExtrButton:
			printer.setExtrTemp(getActivity(), 0);
			printer.updatePrinterStatus(getActivity(), prefs.getInt("LAST_ID", 0), ActivityPrinterControll.FILTER);
			break;

		case R.id.powerBedButton:
			printer.setBedTemp(getActivity(), 0);
			printer.updatePrinterStatus(getActivity(), prefs.getInt("LAST_ID", 0), ActivityPrinterControll.FILTER);
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
		if (temp > 100 && type ==1 ){
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

		int flow = status.getFlow_multiply();
		flowrateValue.setText(Integer.toString(flow));
		int feed = status.getSpeed_multiply();
		feedrateValue.setText(Integer.toString(feed));

		if (flow == Integer.parseInt(newFlow.getText().toString()))
			newFlow.setVisibility(View.INVISIBLE);
		
		if (feed == Integer.parseInt(newSpeed.getText().toString()))
			newSpeed.setVisibility(View.INVISIBLE);
		
		
		extrRead.setText(Double.toString(status.getTemp_read()));
		extrSet.setText(Double.toString(status.getTemp_set()));
		bedRead.setText(Double.toString(status.getBed_temp_read()));
		bedSet.setText(Double.toString(status.getBed_temp_set()));

		flowrateSeek.setProgress(flow);
		feedrateSeek.setProgress(feed);

		if (status.getTemp_set()==0)	
			turnOffExtr.setVisibility(View.INVISIBLE);
		else turnOffExtr.setVisibility(View.VISIBLE);

		if (status.getBed_temp_set()==0)	
			turnOffBed.setVisibility(View.INVISIBLE);
		else turnOffBed.setVisibility(View.VISIBLE);
	}


	// Seekbar callback
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

		switch (seekBar.getId()) {

		case R.id.feedrateSeekBar:
			newSpeed.setVisibility(View.VISIBLE);
			newSpeed.setText("" + progress);
			break;

		case R.id.flowrateSeekBar:
			newFlow.setVisibility(View.VISIBLE);
			newFlow.setText("" + progress);
			break;
		}
	}

	// Seekbar callback
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	// Seekbar callback
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		switch (seekBar.getId()) {

		case R.id.feedrateSeekBar:
			//feedrateSeek.setSecondaryProgress(feedrateSeek.getProgress());
			printer.setSpeed(getActivity(), seekBar.getProgress());
			break;

		case R.id.flowrateSeekBar:
			//flowrateSeek.setSecondaryProgress(flowrateSeek.getProgress());
			printer.setFlowrate(getActivity(), seekBar.getProgress());
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
		turnOffBed.setClickable(isOnLine);
		turnOffExtr.setClickable(isOnLine);
	}


	// PrinterCallback
	@Override
	public void onPrinterError(String error) {
		// TODO Auto-generated method stub		
	}

	// PrinterCallback
	@Override
	public void onChangeState() {
		// TODO Auto-generated method stub
	}

	// PrinterCallback
	@Override
	public void onCommandExecuted() {
		// TODO Auto-generated method stub		
	}

	// PrinterCallback
	@Override
	public void onChangeFanState() {
		// TODO Auto-generated method stub		
	}

}
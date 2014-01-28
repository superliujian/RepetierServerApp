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
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FragPrinterControl2 extends Fragment implements OnSeekBarChangeListener,  PrinterStatusCallbacks, ServerCallbacks, PrinterCallbacks{

	private TextView feedrateValue;
	private TextView flowrateValue;		
	private TextView fanValue;		

	private SeekBar feedrateSeek;
	private SeekBar flowrateSeek;
	private SeekBar fanSeek;
	private SeekBar extrSeek;
	private SeekBar bedSeek;


	private TextView newSpeed;
	private TextView newFlow;
	private TextView newFan;
	private TextView newExtr;
	private TextView newBed;

	private int speed;
	private int flow;
	private int fan;

	private int settedSpeed = -1;
	private int settedFlow = -1;
	private int settedFan = -1;
	private int settedExtr = -1;
	private int settedBed = -1;

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
		fanValue = (TextView) v.findViewById(R.id.fanValueTextView);

		newSpeed = (TextView) v.findViewById(R.id.newSpeedTV);
		newFlow = (TextView) v.findViewById(R.id.newFlowTV);
		newFan = (TextView) v.findViewById(R.id.newFanTV);
		newExtr = (TextView) v.findViewById(R.id.newExtrTV);
		newBed = (TextView) v.findViewById(R.id.newBedTV);

		newSpeed.setVisibility(View.INVISIBLE);
		newFlow.setVisibility(View.INVISIBLE);
		newFan.setVisibility(View.INVISIBLE);
		newExtr.setVisibility(View.INVISIBLE);
		newBed.setVisibility(View.INVISIBLE);

		feedrateSeek = (SeekBar) v.findViewById(R.id.feedrateSeekBar);
		feedrateSeek.setOnSeekBarChangeListener(this);
		feedrateSeek.setMax(200);

		flowrateSeek = (SeekBar) v.findViewById(R.id.flowrateSeekBar);
		flowrateSeek.setOnSeekBarChangeListener(this);
		flowrateSeek.setMax(200);	

		fanSeek = (SeekBar) v.findViewById(R.id.fanSeekBar);
		fanSeek.setOnSeekBarChangeListener(this);
		fanSeek.setMax(100);	

		extrSeek = (SeekBar) v.findViewById(R.id.extruderSeekBar);
		extrSeek.setOnSeekBarChangeListener(this);
		extrSeek.setMax(250);

		bedSeek = (SeekBar) v.findViewById(R.id.bedSeekBar);
		bedSeek.setOnSeekBarChangeListener(this);
		bedSeek.setMax(100);


		extrRead = (TextView) v.findViewById(R.id.extrTempReadTextView);
		extrSet = (TextView) v.findViewById(R.id.extrTempSetTextView);
		bedRead = (TextView) v.findViewById(R.id.bedTempReadTextView);
		bedSet = (TextView) v.findViewById(R.id.bedTempSetTextView);

		textViewError = (TextView) v.findViewById(R.id.errorTV);

		fragLoaded = true; 

		updateFrag(printer);

	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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

		flow = status.getFlow_multiply();
		speed = status.getSpeed_multiply();
		fan = status.getFan_voltage()*100/255;

		flowrateValue.setText(Integer.toString(flow));
		feedrateValue.setText(Integer.toString(speed));
		fanValue.setText(Integer.toString(fan));

		flowrateSeek.setProgress(flow);
		feedrateSeek.setProgress(flow);
		fanSeek.setProgress(fan);

		if (flow == settedFlow){
			newFlow.setVisibility(View.INVISIBLE);
		}			

		if (speed == settedSpeed){
			newSpeed.setVisibility(View.INVISIBLE);	
		}

		if (fan == settedFan){
			newFan.setVisibility(View.INVISIBLE);	
		}

		extrRead.setText(Double.toString(status.getTemp_read()));
		extrSet.setText(Double.toString(status.getTemp_set()));

		bedRead.setText(Double.toString(status.getBed_temp_read()));
		bedSet.setText(Double.toString(status.getBed_temp_set()));

		if(settedExtr != (int) status.getTemp_set()){
			extrSeek.setProgress(((int) status.getTemp_set()));
		}

		if(settedBed != (int) status.getBed_temp_set()){
			bedSeek.setProgress(((int) status.getBed_temp_set()));
		}
	}


	// Seekbar callback
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

		switch (seekBar.getId()) {

		case R.id.feedrateSeekBar:
			settedSpeed = progress;
			newSpeed.setVisibility(View.VISIBLE);
			newSpeed.setText("" + progress);
			break;

		case R.id.flowrateSeekBar:
			settedFlow = progress;
			newFlow.setVisibility(View.VISIBLE);
			newFlow.setText("" + progress);
			break;

		case R.id.fanSeekBar:
			settedFan = progress;
			newFan.setVisibility(View.VISIBLE);
			newFan.setText("" + progress);
			break;

		case R.id.extruderSeekBar:
			settedExtr = progress;
			newExtr.setVisibility(View.VISIBLE);
			newExtr.setText("" + progress);
			break;

		case R.id.bedSeekBar:
			settedBed = progress;
			newBed.setVisibility(View.VISIBLE);
			newBed.setText("" + progress);
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
			printer.setSpeed(getActivity(), settedSpeed);
			break;

		case R.id.flowrateSeekBar:
			//flowrateSeek.setSecondaryProgress(flowrateSeek.getProgress());
			printer.setFlowrate(getActivity(), settedFlow);
			break;

		case R.id.fanSeekBar:
			printer.setFan(getActivity(), settedFan);
			Log.d("settedfan", Integer.toString(settedFan));
			break;

		case R.id.extruderSeekBar:
			printer.setExtrTemp(getActivity(), settedExtr);
			break;

		case R.id.bedSeekBar:
			printer.setBedTemp(getActivity(), settedBed);
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
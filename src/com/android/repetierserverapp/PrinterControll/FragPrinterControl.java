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
import android.widget.TextView;
import android.widget.Toast;

public class FragPrinterControl extends Fragment implements  PrinterStatusCallbacks, OnClickListener, PrinterCallbacks, ServerCallbacks {

	private Button buttonXp10;
	private Button buttonXp1;
	private Button buttonX_1;
	private Button buttonX_10;
	private Button buttonXhome;

	private Button buttonYp10;
	private Button buttonYp1;
	private Button buttonY_1;
	private Button buttonY_10;
	private Button buttonYhome;

	private Button buttonZp10;
	private Button buttonZp1;
	private Button buttonZ_1;
	private Button buttonZ_10;
	private Button buttonZhome;
	private Button buttonHome;

	private Button extr1;
	private Button extr10;
	private Button retr1;
	private Button retr10;


	private TextView textViewXvalue;
	private TextView textViewYvalue;
	private TextView textViewZvalue;

	private TextView textViewStatus;
	private TextView textViewError;

	private Printer printer;
	private Server server;
	private int position;	

	private static Timer myTimer;
	private static Timer printerTimer;

	private SharedPreferences prefs;

	public FragPrinterControl(){
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d("fragPrinterControl", "fragPrinterControl");

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

		printer.setPrinterCallbacks(this);
		printer.setPrinterStatusCallbacks(this);	
		server.setCallbacks(this);

		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_control,
				container, false);
		return rootView;
	}



	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {
		super.onViewCreated(v, savedInstanceState);

		printer.updatePrinterStatus(getActivity(), prefs.getInt("LAST_ID", 0), ActivityPrinterControll.FILTER);

		buttonXp10 = (Button) v.findViewById(R.id.xp10button);
		buttonXp1 = (Button) v.findViewById(R.id.xp1button);
		buttonX_1 = (Button) v.findViewById(R.id.x_1button);
		buttonX_10 = (Button) v.findViewById(R.id.x_10button);
		buttonXhome = (Button) v.findViewById(R.id.homexbutton);

		buttonYp10 = (Button) v.findViewById(R.id.yp10button);
		buttonYp1 = (Button) v.findViewById(R.id.yp1button);
		buttonY_1 = (Button) v.findViewById(R.id.y_1button);
		buttonY_10 = (Button) v.findViewById(R.id.y_10button);
		buttonYhome = (Button) v.findViewById(R.id.homeybutton);

		buttonZp10 = (Button) v.findViewById(R.id.zp10button);
		buttonZp1 = (Button) v.findViewById(R.id.zp1button);
		buttonZ_1 = (Button) v.findViewById(R.id.z_1button);
		buttonZ_10 = (Button) v.findViewById(R.id.z_10button);
		buttonZhome = (Button) v.findViewById(R.id.homezbutton);

		buttonHome = (Button) v.findViewById(R.id.homeButton);

		extr1 = (Button) v.findViewById(R.id.extrudep1button);
		extr10 = (Button) v.findViewById(R.id.extrudep10button);
		retr1 = (Button) v.findViewById(R.id.retract1button);
		retr10 = (Button) v.findViewById(R.id.retract10button);


		textViewXvalue = (TextView) v.findViewById(R.id.xValueTextView);
		textViewYvalue = (TextView) v.findViewById(R.id.yValueTextView);
		textViewZvalue = (TextView) v.findViewById(R.id.zValueTextView);

		textViewStatus = (TextView) v.findViewById(R.id.statusTextView);
		textViewError = (TextView) v.findViewById(R.id.errorTV);

		buttonXp10.setOnClickListener(this);
		buttonXp1.setOnClickListener(this);
		buttonX_1.setOnClickListener(this);
		buttonX_10.setOnClickListener(this);
		buttonXhome.setOnClickListener(this);

		buttonYp10.setOnClickListener(this);
		buttonYp1.setOnClickListener(this);
		buttonY_1.setOnClickListener(this);
		buttonY_10.setOnClickListener(this);
		buttonYhome.setOnClickListener(this);

		buttonZp10.setOnClickListener(this);
		buttonZp1.setOnClickListener(this);
		buttonZ_1.setOnClickListener(this);
		buttonZ_10.setOnClickListener(this);
		buttonZhome.setOnClickListener(this);
		buttonHome.setOnClickListener(this);

		extr1.setOnClickListener(this);
		extr10.setOnClickListener(this);
		retr1.setOnClickListener(this);
		retr10.setOnClickListener(this);

		updateFrag(printer);
	}



	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.xp10button:
			printer.move(getActivity(), 10, 0, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.xp1button:
			printer.move(getActivity(), 1, 0, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.x_1button:
			printer.move(getActivity(), -1, 0, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.x_10button:
			printer.move(getActivity(), -10, 0, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.homexbutton:
			printer.moveHomeX(getActivity());
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.yp10button:
			printer.move(getActivity(), 0, 10, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.yp1button:
			printer.move(getActivity(), 0, 1, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.y_1button:
			printer.move(getActivity(), 0, -1, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.y_10button:
			printer.move(getActivity(), 0, -10, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.homeybutton:
			printer.moveHomeY(getActivity());
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.zp10button:
			printer.move(getActivity(), 0, 0, 10, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.zp1button:
			printer.move(getActivity(), 0, 0, 1, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.z_1button:
			printer.move(getActivity(), 0, 0, -1, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.z_10button:
			printer.move(getActivity(), 0, 0, -10, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.homezbutton:
			printer.moveHomeZ(getActivity());
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.homeButton:
			printer.moveHome(getActivity());
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.extrudep1button:
			printer.move(getActivity(), 0, 0, 0, 1);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.extrudep10button:
			printer.move(getActivity(), 0, 0, 0, 10);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.retract1button:
			printer.move(getActivity(), 0, 0, 0, -1);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.retract10button:
			printer.move(getActivity(), 0, 0, 0, -10);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;
		default:
			break;
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

		String x = Double.toString(Math.round(status.getX()*100)/100);
		String y = Double.toString(Math.round(status.getY()*100)/100);
		String z = Double.toString(Math.round(status.getZ()*100)/100);

		//TODO String current = 
		textViewXvalue.setText(x);	
		textViewYvalue.setText(y);	
		textViewZvalue.setText(z);
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

	// PrinterCallback
	@Override
	public void onChangeState() {
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


	public void startTimer(int interval){
		Log.d("startTimer", "intervallo: " + Integer.toString(interval));
		myTimer = new Timer();
		myTimer.schedule(new TimerTask() {          
			@Override
			public void run() {
				Log.d("Timer", "control1");
				printer.updatePrinterStatus(getActivity().getApplicationContext(), prefs.getInt("LAST_ID", 0), ActivityPrinterControll.FILTER);
			}
		}, 1000, interval);

		printerTimer = new Timer();
		printerTimer.schedule(new TimerTask() {          
			@Override
			public void run() {
				server.updatePrinterList(getActivity());
			}
		}, 0, 10000);
	}

	public void stopTimer(){
		Log.d("stopTimer", "control1");
		myTimer.cancel();
		printerTimer.cancel();
	}



	@Override
	public void onPrinterError(String error) {
		Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
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

		if(printer.getCurrentJob().equals("none")){
			textViewStatus.setTextAppearance(getActivity(), R.style.offline);
			textViewStatus.setText(R.string.noJobRunning);
		} else {
			textViewStatus.setTextAppearance(getActivity(), R.style.online);
			textViewStatus.setText(R.string.jobRunning);
			textViewStatus.append(" " + Double.toString(Math.round(printer.getProgress()*100)/100) + " %");
		}


		updateFrag(printer);
	}


	private void updateFrag(Printer newPrinter) {

		boolean isOnLine;

		if (newPrinter.getOnline() == 0 || !newPrinter.getCurrentJob().equals("none"))
			isOnLine = false;
		else 
			isOnLine = true;

		buttonXp10.setEnabled(isOnLine);
		buttonXp1.setEnabled(isOnLine);
		buttonX_1.setEnabled(isOnLine);
		buttonX_10.setEnabled(isOnLine);
		buttonXhome.setEnabled(isOnLine);

		buttonYp10.setEnabled(isOnLine);
		buttonYp1.setEnabled(isOnLine);
		buttonY_1.setEnabled(isOnLine);
		buttonY_10.setEnabled(isOnLine);
		buttonYhome.setEnabled(isOnLine);

		buttonZp10.setEnabled(isOnLine);
		buttonZp1.setEnabled(isOnLine);
		buttonZ_1.setEnabled(isOnLine);
		buttonZ_10.setEnabled(isOnLine);
		buttonZhome.setEnabled(isOnLine);

		buttonHome.setEnabled(isOnLine);	

		extr1.setEnabled(isOnLine);
		extr10.setEnabled(isOnLine);
		retr1.setEnabled(isOnLine);
		retr10.setEnabled(isOnLine);
	}




}
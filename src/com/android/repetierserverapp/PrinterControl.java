package com.android.repetierserverapp;


import java.util.ArrayList;

import com.android.repetierserverapp.R;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Printer.PrinterCallbacks;
import com.grasselli.android.repetierserverapi.Printer.PrinterStatusCallbacks;
import com.grasselli.android.repetierserverapi.Server.ServerCallbacks;
import com.grasselli.android.repetierserverapi.Server;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PrinterControl extends Activity implements PrinterStatusCallbacks, OnClickListener, PrinterCallbacks, ServerCallbacks {

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

	private TextView textViewXvalue;
	private TextView textViewYvalue;
	private TextView textViewZvalue;

	private TextView textViewStatus;

	SharedPreferences pref;
	private String serverName;
	private String serverUrl;
	
	Server s;

	Printer printer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_control);

		buttonXp10 = (Button) findViewById(R.id.xp10button);
		buttonXp1 = (Button) findViewById(R.id.xp1button);
		buttonX_1 = (Button) findViewById(R.id.x_1button);
		buttonX_10 = (Button) findViewById(R.id.x_10button);
		buttonXhome = (Button) findViewById(R.id.homexbutton);

		buttonYp10 = (Button) findViewById(R.id.yp10button);
		buttonYp1 = (Button) findViewById(R.id.yp1button);
		buttonY_1 = (Button) findViewById(R.id.y_1button);
		buttonY_10 = (Button) findViewById(R.id.y_10button);
		buttonYhome = (Button) findViewById(R.id.homeybutton);

		buttonZp10 = (Button) findViewById(R.id.zp10button);
		buttonZp1 = (Button) findViewById(R.id.zp1button);
		buttonZ_1 = (Button) findViewById(R.id.z_1button);
		buttonZ_10 = (Button) findViewById(R.id.z_10button);
		buttonZhome = (Button) findViewById(R.id.homezbutton);

		buttonHome = (Button) findViewById(R.id.homeButton);

		textViewXvalue = (TextView) findViewById(R.id.xValueTextView);
		textViewYvalue = (TextView) findViewById(R.id.yValueTextView);
		textViewZvalue = (TextView) findViewById(R.id.zValueTextView);

		textViewStatus = (TextView) findViewById(R.id.statusTextView);

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

		pref = PreferenceManager.getDefaultSharedPreferences(this);
		 serverName = pref.getString("serverName", "");
		 serverUrl = pref.getString("serverUrl", "");
		 
		s = new Server("http://192.168.1.241:8080");
		s.setCallbacks(this);

		s.updatePrinterList(this);
		


	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.xp10button:
			printer.move(this, 10, 0, 0, 0);
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.xp1button:
			printer.move(this, 1, 0, 0, 0);
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.x_1button:
			printer.move(this, -1, 0, 0, 0);
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.x_10button:
			printer.move(this, -10, 0, 0, 0);
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.homexbutton:
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.yp10button:
			printer.move(this, 0, 10, 0, 0);
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.yp1button:
			printer.move(this, 0, 1, 0, 0);
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.y_1button:
			printer.move(this, 0, -1, 0, 0);
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.y_10button:
			printer.move(this, 0, -10, 0, 0);
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.homeybutton:
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.zp10button:
			printer.move(this, 0, 0, 10, 0);
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.zp1button:
			printer.move(this, 0, 0, 1, 0);
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.z_1button:
			printer.move(this, 0, 0, -1, 0);
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.z_10button:
			printer.move(this, 0, 0, -10, 0);
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.homezbutton:
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			break;

		case R.id.homeButton:
			printer.updatePrinterStatus(this, printer.getLastId(), 13);
			printer.moveHome(this);
			break;

		default:
			break;
		}

	}

	
	@Override
	public void onPrinterStatusUpdated() {
		String x = Double.toString(printer.getStatus().getX());
		
		String y = Double.toString(printer.getStatus().getY());
			
		String z = Double.toString(printer.getStatus().getZ());
		
		textViewXvalue.setText(x);
		
		textViewYvalue.setText(y);
		
		textViewZvalue.setText(z);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.printer_control, menu);
		return true;
	}

	@Override
	public void onError(String error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChangeState() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCommandExecuted() {
		Log.d("onCommandExecuted", "entrato");
		return;    		
	}

	@Override
	public void onPrinterListUpdated(ArrayList<Printer> printerList) {
		printer = s.getPrinter(0);
		printer.setPrinterCallbacks(this);
		printer.setPrinterStatusCallbacks(this);
		return;
	}

	
}
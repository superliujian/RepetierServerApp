package com.android.repetierserverapp;

import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Printer.PrinterStatusCallbacks;
import com.grasselli.android.repetierserverapi.PrinterStatus;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class PrinterControl2 extends Activity implements OnClickListener, OnSeekBarChangeListener, PrinterStatusCallbacks, OnCheckedChangeListener{
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

	Printer printer;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control2_ui);

		feedrateValue = (TextView) findViewById(R.id.feedrateValueTextView);
		flowrateValue = (TextView) findViewById(R.id.flowrateValueTextView);

		feedrateSeek = (SeekBar) findViewById(R.id.feedrateSeekBar);
		feedrateSeek.setOnSeekBarChangeListener(this);
		feedrateSeek.setMax(200);


		flowrateSeek = (SeekBar) findViewById(R.id.flowrateSeekBar);
		flowrateSeek.setOnSeekBarChangeListener(this);
		flowrateSeek.setMax(200);

		extruderSwitch = (Switch) findViewById(R.id.extruderSwitch);
		bedSwitch = (Switch) findViewById(R.id.bedSwitch);

		extrRead = (TextView) findViewById(R.id.extrTempReadTextView);
		extrSet = (TextView) findViewById(R.id.extrTempSetTextView);
		bedRead = (TextView) findViewById(R.id.bedTempReadTextView);
		bedSet = (TextView) findViewById(R.id.bedTempSetTextView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.printer_control2, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

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





	@Override
	public void onError(String error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrinterStatusUpdated() {

		PrinterStatus status = printer.getStatus();

		flowrateValue.setText(status.getFlow_multiply());
		feedrateValue.setText(status.getSpeed_multiply());
		extrRead.setText((int) status.getTemp_read());
		extrSet.setText((int) status.getTemp_set());
		bedRead.setText((int) status.getBed_temp_read());
		bedSet.setText((int) status.getBed_temp_set());
		
		if (status.getTemp_set()==0)	turnOff(extruderSwitch);
		else turnOn(extruderSwitch);
		
		if (status.getBed_temp_set()==0)	turnOff(bedSwitch);
		else turnOn(bedSwitch);
		
	}

	public void turnOn (Switch s){
		s.setActivated(true);
	}

	public void turnOff (Switch s){
		s.setActivated(false);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {

		case R.id.extruderSwitch:
		break;
		
		case R.id.bedSwitch:
		break;
	}
}

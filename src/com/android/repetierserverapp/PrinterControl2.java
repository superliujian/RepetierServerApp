package com.android.repetierserverapp;

import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Printer.PrinterStatusCallbacks;
import com.grasselli.android.repetierserverapi.PrinterStatus;
import com.grasselli.android.repetierserverapi.Server;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
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

	private Button newExtrTempBtn;
	private Button newBedTempBtn;

	private EditText newExtrTempEt;
	private EditText newBedTempEt;

	Server s;
	Printer printer;
	Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_control2);

		feedrateValue = (TextView) findViewById(R.id.feedrateValueTextView);
		flowrateValue = (TextView) findViewById(R.id.flowrateValueTextView);

		feedrateSeek = (SeekBar) findViewById(R.id.feedrateSeekBar);
		feedrateSeek.setOnSeekBarChangeListener(this);
		feedrateSeek.setMax(200);
		feedrateSeek.setOnSeekBarChangeListener(this);

		flowrateSeek = (SeekBar) findViewById(R.id.flowrateSeekBar);
		flowrateSeek.setOnSeekBarChangeListener(this);
		flowrateSeek.setMax(200);	
		flowrateSeek.setOnSeekBarChangeListener(this);

		extruderSwitch = (Switch) findViewById(R.id.extruderSwitch);
		bedSwitch = (Switch) findViewById(R.id.bedSwitch);

		bedSwitch.setOnCheckedChangeListener(this);
		extruderSwitch.setOnCheckedChangeListener(this);

		extrRead = (TextView) findViewById(R.id.extrTempReadTextView);
		extrSet = (TextView) findViewById(R.id.extrTempSetTextView);
		bedRead = (TextView) findViewById(R.id.bedTempReadTextView);
		bedSet = (TextView) findViewById(R.id.bedTempSetTextView);

		newBedTempBtn = (Button) findViewById(R.id.newBedTempBtn);
		newExtrTempBtn = (Button) findViewById(R.id.newExtrTempBtn);

		newExtrTempBtn.setOnClickListener(this);
		newBedTempBtn.setOnClickListener(this);

		newExtrTempEt = (EditText) findViewById(R.id.newExtrTempEt);
		newBedTempEt = (EditText) findViewById(R.id.newBedTempEt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.printer_control2, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.newBedTempBtn:
			int bedtemp = Integer.parseInt(newBedTempEt.getText().toString());
			if(checkTemp(bedtemp, 1)){
				printer.setBedTemp(this, bedtemp);
			}
			break;

		case R.id.newExtrTempBtn:
			int extrtemp = Integer.parseInt(newExtrTempEt.getText().toString());
			if(checkTemp(extrtemp, 0)){
				printer.setExtrTemp(this, extrtemp);
			}
			break;
		}
	}

	//type == 0 temperatura estrusore
	//type == 1 temperatura letto riscaldato
	private boolean checkTemp(int temp, int type){
		if (temp < 0) {
			toast= Toast.makeText(this,"La temperatura non può essere negativa", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		if (temp > 250 && type ==0 ){
			toast= Toast.makeText(this,"Temperatura estrusore eccessiva", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		if (temp < 160 && type ==0 ){
			toast= Toast.makeText(this,"Temperatura estrusore troppo bassa", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		if (temp > 100 && type ==0 ){
			toast= Toast.makeText(this,"Temperatura letto riscaldato eccessiva", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		return true;
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

	//aggiorna valori interfaccia
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

	//Accendi/spegni estrusore/letto
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.extruderSwitch:
			if (extruderSwitch.isActivated()){
				printer.setExtrTemp(this, 0);
			}
			else {
				int extrtemp = Integer.parseInt(newExtrTempEt.getText().toString());
				if(checkTemp(extrtemp, 0)){
					printer.setExtrTemp(this, extrtemp);
				}
			}
			break;

		case R.id.bedSwitch:
			int bedtemp = Integer.parseInt(newBedTempEt.getText().toString());
			if(checkTemp(bedtemp, 1)){
				printer.setBedTemp(this, bedtemp);
			}
			break;
		}
	}
}
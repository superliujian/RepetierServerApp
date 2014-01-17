package com.android.repetierserverapp.PrinterControll;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.R.id;
import com.android.repetierserverapp.R.layout;
import com.android.repetierserverapp.R.menu;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Printer.PrinterStatusCallbacks;
import com.grasselli.android.repetierserverapi.PrinterStatus;
import com.grasselli.android.repetierserverapi.Server;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
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

public class FragPrinterControl2 extends Fragment implements OnClickListener, OnSeekBarChangeListener, PrinterStatusCallbacks, OnCheckedChangeListener{
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

	
	public FragPrinterControl2(){
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {
		getActivity().setContentView(R.layout.fragment_control2);

		feedrateValue = (TextView) v.findViewById(R.id.feedrateValueTextView);
		flowrateValue = (TextView) v.findViewById(R.id.flowrateValueTextView);

		feedrateSeek = (SeekBar) v.findViewById(R.id.feedrateSeekBar);
		feedrateSeek.setOnSeekBarChangeListener(this);
		feedrateSeek.setMax(200);
		feedrateSeek.setOnSeekBarChangeListener(this);

		flowrateSeek = (SeekBar) v.findViewById(R.id.flowrateSeekBar);
		flowrateSeek.setOnSeekBarChangeListener(this);
		flowrateSeek.setMax(200);	
		flowrateSeek.setOnSeekBarChangeListener(this);

		extruderSwitch = (Switch) v.findViewById(R.id.extruderSwitch);
		bedSwitch = (Switch) v.findViewById(R.id.bedSwitch);

		bedSwitch.setOnCheckedChangeListener(this);
		extruderSwitch.setOnCheckedChangeListener(this);

		extrRead = (TextView) v.findViewById(R.id.extrTempReadTextView);
		extrSet = (TextView) v.findViewById(R.id.extrTempSetTextView);
		bedRead = (TextView) v.findViewById(R.id.bedTempReadTextView);
		bedSet = (TextView) v.findViewById(R.id.bedTempSetTextView);

		newBedTempBtn = (Button) v.findViewById(R.id.newBedTempBtn);
		newExtrTempBtn = (Button) v.findViewById(R.id.newExtrTempBtn);

		newExtrTempBtn.setOnClickListener(this);
		newBedTempBtn.setOnClickListener(this);

		newExtrTempEt = (EditText) v.findViewById(R.id.newExtrTempEt);
		newBedTempEt = (EditText) v.findViewById(R.id.newBedTempEt);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.newBedTempBtn:
			int bedtemp = Integer.parseInt(newBedTempEt.getText().toString());
			if(checkTemp(bedtemp, 1)){
				printer.setBedTemp(getActivity(), bedtemp);
			}
			break;

		case R.id.newExtrTempBtn:
			int extrtemp = Integer.parseInt(newExtrTempEt.getText().toString());
			if(checkTemp(extrtemp, 0)){
				printer.setExtrTemp(getActivity(), extrtemp);
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
				printer.setExtrTemp(getActivity(), 0);
			}
			else {
				int extrtemp = Integer.parseInt(newExtrTempEt.getText().toString());
				if(checkTemp(extrtemp, 0)){
					printer.setExtrTemp(getActivity(), extrtemp);
				}
			}
			break;

		case R.id.bedSwitch:
			int bedtemp = Integer.parseInt(newBedTempEt.getText().toString());
			if(checkTemp(bedtemp, 1)){
				printer.setBedTemp(getActivity(), bedtemp);
			}
			break;
		}
	}
}
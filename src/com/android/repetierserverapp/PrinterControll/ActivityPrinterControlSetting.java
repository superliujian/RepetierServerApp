package com.android.repetierserverapp.PrinterControll;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.utils.PrefsActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityPrinterControlSetting extends Activity /*implements OnClickListener*/ {

	private Button save;
	private EditText modelFreq;
	private EditText jobFreq;
	private EditText statusFreq;
	private int newModel;
	private int newJob;
	private int newStatus;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_printer_control_setting);

		Intent intent = new Intent(ActivityPrinterControlSetting.this, PrefsActivity.class);
				startActivity(intent);
		
		
	}
	/*	save = (Button) findViewById(R.id.saveBtt);
		save.setOnClickListener(this);

		modelFreq = (EditText) findViewById(R.id.modelFreqET);
		jobFreq = (EditText) findViewById(R.id.jobFreqET);
		statusFreq = (EditText) findViewById(R.id.statusFreqET);

		modelFreq.setText(ActivityPrinterControll.MODEL_INTERVAL/1000);
		jobFreq.setText(ActivityPrinterControll.JOB_INTERVAL/1000);
		statusFreq.setText(ActivityPrinterControll.STATUS_INTERVAL/1000);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.saveBtt)	{

			String newModelFreq = modelFreq.getText().toString();
			String newJobFreq = jobFreq.getText().toString();
			String newStatusFreq = statusFreq.getText().toString();
			
			if (isNumeric(newModelFreq)){
				newModel = Integer.parseInt(newModelFreq);
				if(!isGoodValue(newModel)){
					Toast.makeText(getApplicationContext(), getString(R.string.notGoodValue), Toast.LENGTH_LONG).show();
					return;
				}
			} else {
				Toast.makeText(getApplicationContext(), getString(R.string.notNumeric), Toast.LENGTH_LONG).show();
				return;
			}
			
			if (isNumeric(newJobFreq)){
				newJob = Integer.parseInt(newJobFreq);
				if(!isGoodValue(newJob)){
					Toast.makeText(getApplicationContext(), getString(R.string.notGoodValue), Toast.LENGTH_LONG).show();
					return;
				}
			} else {
				Toast.makeText(getApplicationContext(), getString(R.string.notNumeric), Toast.LENGTH_LONG).show();
				return;
			}
			
			if (isNumeric(newStatusFreq)){
				newStatus = Integer.parseInt(newStatusFreq);
				if(!isGoodValue(newStatus)){
					Toast.makeText(getApplicationContext(), getString(R.string.notGoodValue), Toast.LENGTH_LONG).show();
					return;
				}
			} else {
				Toast.makeText(getApplicationContext(), getString(R.string.notNumeric), Toast.LENGTH_LONG).show();
				return;
			}

			
			ActivityPrinterControll.MODEL_INTERVAL = newModel*1000;
			ActivityPrinterControll.JOB_INTERVAL = newJob*1000;
			ActivityPrinterControll.STATUS_INTERVAL = newStatus*1000;
			
			Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_LONG).show();
		}
	}

	private boolean isNumeric (String value){
		try{
			Integer.parseInt(value);
		}
		catch(NumberFormatException nfe)
		{  
			return false;  
		}  
		return true;  
	}
	
	private boolean isGoodValue (int value){
		if (value <= 0 || value > 10){
			return false;
		}
		return true;
	}
*/

}

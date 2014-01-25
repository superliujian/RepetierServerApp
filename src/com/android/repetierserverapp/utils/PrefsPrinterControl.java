package com.android.repetierserverapp.utils;

import com.android.repetierserverapp.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefsPrinterControl extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   addPreferencesFromResource(R.xml.prefs_printer_control);
	}
	
}

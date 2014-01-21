package com.android.repetierserverapp.utils;

import com.android.repetierserverapp.PrinterControll.ModelList.FragModelList;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class Utils {
	InputMethodManager mgr;
	Context context;
	
	public Utils (Context context){
		this.context = context;
	};
	
	public void hide (){
	InputMethodManager mgr = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
	//mgr.hideSoftInputFromWindow(FragModelList.fi.getWindowToken(), 0);
	}
}

package com.android.repetierserverapp.utils;

import com.android.repetierserverapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
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



	public static boolean isNumeric(String str)  
	{  
		try  {  
			Integer.parseInt(str);  
		}  
		catch(NumberFormatException nfe)  {  
			return false;  
		}  

		return true;  
	}


	//TODO
	public static void setOrentation(Activity activity){

		boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
		
		if (tabletSize) {
			Log.d("the device is a tablet!", "the device is a tablet!");
		} else {
			Log.d("the device is a smartphone!", "the device is a smartphone!");
			activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

	}



}

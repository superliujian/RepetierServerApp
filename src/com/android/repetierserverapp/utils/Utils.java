package com.android.repetierserverapp.utils;

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









}

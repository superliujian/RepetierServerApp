package com.android.repetierserverapp;

import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

public class ToDo {

	/*
	 *
	 * 
	 * Gestione eccezzioni su connessioni
	 * 
	 * 
	 * Caricare modello
	 * 
	 * 
	 * titolo/icona
	 * 
	 * 
	 * callback fra fragment
	 * printerControllCallback
	 * implementato ma non funziona bene
	 * 
	 * timer chiamate
	 * 
	 * 
	 * grafica
	 * 
	 * printercontroll2
	 * 
	 * 

	 * 
	 * 
	 * onAttach
	 * onCreate
	 * onCreatView
	 * onViewCrated
	 * onActivityCreated
	 * 
	 * 
	 * 
	 * <TextView
        android:id="@+id/activePrinter"
        style="@style/boldText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_above="@+id/status"
        android:layout_alignLeft="@+id/printerStatus"
        android:background="@drawable/view_green"
        android:clickable="true"
        android:gravity="center_horizontal"
        android:onClick="onClick"
        android:text=" ACCENDI "
        android:textSize="@dimen/bigText" />
        
        
        
        	public void startTimer(){
		Log.d("startTimer", "startTimer");
		myTimer = new Timer();
		myTimer.schedule(new TimerTask() {          
			@Override
			public void run() {
				printer.updateJobList(getActivity().getApplicationContext());
				Log.d("Timer", "Timer");
			}
		}, 0, 5000);
	}

	public void stopTimer(){
		Log.d("stopTimer", "stopTimer");
		myTimer.cancel();
	}
	
	
	/*
				if (position == 0){
//					fragModelList.startTimer();
					modelTimer = true;
				} 
				
				if (position == 1 && first == false){
					fragJobList.startTimer();
					jobTimer = true;
				}
				
				if (position == 2){
//					fragPrinterControl.startTimer();
					controlTimer = true;
				}
				
				if (position != 0 && modelTimer == true){
//					fragModelList.stopTimer();
					modelTimer = false;
				} 
				
				if (position != 1 && jobTimer == true){
					fragJobList.stopTimer();
					jobTimer = false;
				} 
				
				if (position != 2 && controlTimer == true){
//					fragPrinterControl.stopTimer();
					controlTimer = false;
				} 
				
*/
	 
}

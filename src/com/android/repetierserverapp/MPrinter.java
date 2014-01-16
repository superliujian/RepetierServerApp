package com.android.repetierserverapp;

public class MPrinter {

	public String name;
	public String online;
	
	
	MPrinter(String name, String online){
		
		this.name = name;
		this.online = online;
	}
	
	public String getName(){
		return this.name;
		
		
	}
	
	public String getOnline(){
		return online;
	}
}

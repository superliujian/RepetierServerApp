package com.android.repetierserverapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.db.DbHelper;

public class PrinterListAdapter extends CursorAdapter{

	public PrinterListAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View v, Context arg1, Cursor c) {
		// TODO Auto-generated method stub
		//Recuperare informazioni dal cursore
		((TextView) v.findViewById(R.id.printerName)).setText()));
		((TextView) v.findViewById(R.id.printerStatus)).setText()));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);

		View v = inflater.inflate(R.layout.server_line, parent, false);
		bindView(v, context, cursor);
		return v;
	}




}



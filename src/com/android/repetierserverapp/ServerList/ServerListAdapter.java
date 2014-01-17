package com.android.repetierserverapp.ServerList;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.db.DbHelper;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ServerListAdapter extends CursorAdapter{

	public ServerListAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View v, Context arg1, Cursor c) {
		// TODO Auto-generated method stub
		((TextView) v.findViewById(R.id.nameServer)).setText(c.getString(c.getColumnIndex(DbHelper.DB_NAME)));
		((TextView) v.findViewById(R.id.urlServer)).setText(c.getString(c.getColumnIndex(DbHelper.DB_URL)));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		
		LayoutInflater inflater = LayoutInflater.from(context);

	    View v = inflater.inflate(R.layout.server_line, parent, false);
	            bindView(v, context, cursor);
	           return v;
	}
	
	
	

}

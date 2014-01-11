package com.android.repetierserverapp.db;

import android.content.Context;
import android.database.sqlite.*;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	public static String DB_URL = "url";
	public static String DB_NAME = "name";
	public static String DB_ID = "_id";
	public static String DB_TABLE = "server";
	
	
	private static final String DATABASE_NAME = "server.db";
	private static final int DATABASE_VERSION = 1;

	// Lo statement SQL di creazione del database
	private static final String DATABASE_CREATE = "create table " + DB_TABLE + " (" + DB_ID + " integer primary key autoincrement," + DB_NAME + "text not null," + DB_URL + "text not null);";
	
	
	
	// Costruttore
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Questo metodo viene chiamato durante la creazione del database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		Log.d("DbHelper onCreate()", DbHelper.DATABASE_CREATE);
	}

	// Questo metodo viene chiamato durante l'upgrade del database, ad esempio quando viene incrementato il numero di versione
	@Override
	public void onUpgrade( SQLiteDatabase database, int oldVersion, int newVersion ) {

		database.execSQL("DROP TABLE IF EXISTS server");
		onCreate(database);
	}
	
	@Override
	public void onOpen(SQLiteDatabase database){
		Log.d("DbHelper onOpen()", "entrato");
		//TODO
	}
}

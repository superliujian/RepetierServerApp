package com.android.repetierserverapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapter {
	//private static final String LOG_TAG = DbAdapter.class.getSimpleName();

	private Context context;
	private SQLiteDatabase database;
	private DbHelper dbHelper;


	public DbAdapter(Context context) {
		this.context = context;
	}

	public DbAdapter open() throws SQLException {
		dbHelper = new DbHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}
	
	public DbAdapter openReadOnly() throws SQLException {
		dbHelper = new DbHelper(context);
		database = dbHelper.getReadableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	private ContentValues createContentValues(String name, String url) {
		ContentValues values = new ContentValues();
		values.put( DbHelper.DB_NAME, name );
		values.put( DbHelper.DB_URL, url );
		return values;
	}

	//create a Server
	public long createServer(String name, String url) {
		ContentValues initialValues = createContentValues(name, url);
		return database.insertOrThrow(DbHelper.DB_TABLE, null, initialValues);
	}

	//update a Server
	public boolean updateServer(String newName, String newUrl, int id) {
		ContentValues updateValues = createContentValues(newName, newUrl);
		return database.update(DbHelper.DB_TABLE, updateValues, DbHelper.DB_ID + "=" + id, null) > 0;
	}

	//delete a Server      
	public boolean deleteServer(int id) {
		return database.delete(DbHelper.DB_TABLE, DbHelper.DB_ID + "=" + id, null) > 0;
	}

	//fetch all Server
	public Cursor fetchAllServer() {
		return database.query(DbHelper.DB_TABLE, new String[] { DbHelper.DB_NAME, DbHelper.DB_URL, DbHelper.DB_ID}, null, null, null, null, null);
	}

	
	public Cursor fetchServerByName(String name) {
		return database.query(DbHelper.DB_TABLE, new String[] { DbHelper.DB_NAME, DbHelper.DB_URL, DbHelper.DB_ID}, DbHelper.DB_NAME + "=" + name, null, null, null, null);
	}
	
	public Cursor fetchServerById(String id) {
		return database.query(DbHelper.DB_TABLE, new String[] { DbHelper.DB_NAME, DbHelper.DB_URL, DbHelper.DB_ID}, DbHelper.DB_ID + "=" + id, null, null, null, null);
	}
}

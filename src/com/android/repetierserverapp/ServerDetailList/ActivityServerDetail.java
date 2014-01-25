package com.android.repetierserverapp.ServerDetailList;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.ServerDetailList.FragServerDetail.PrinterAppCallbacks;
import com.android.repetierserverapp.ServerList.ActivityServerList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;


public class ActivityServerDetail extends FragmentActivity implements PrinterAppCallbacks {
	private FragServerDetail fragment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_detail);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (savedInstanceState == null) {

			Bundle arguments = new Bundle();
			arguments.putLong(FragServerDetail.ARG_SERVER_ID, getIntent()
					.getLongExtra(FragServerDetail.ARG_SERVER_ID, -1));
					//.getStringExtra(FragServerDetail.ARG_SERVER_ID));
			fragment = new FragServerDetail();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.server_detail_container, fragment).commit();
		}
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			fragment.stopTimer();
			NavUtils.navigateUpTo(this, new Intent(this,
					ActivityServerList.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	
	@Override
	public void onBackPressed(){

		fragment.stopTimer();		
		
		Intent detailIntent = new Intent(this, ActivityServerList.class);
		startActivity(detailIntent);
	}

	
	
	
	@Override
	public void onPrinterSelected(long id) {
		// TODO Auto-generated method stub
		
	}


}

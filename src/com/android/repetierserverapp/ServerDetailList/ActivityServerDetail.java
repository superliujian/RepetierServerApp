package com.android.repetierserverapp.ServerDetailList;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.ServerDetailList.FragServerDetail.PrinterAppCallbacks;
import com.android.repetierserverapp.ServerList.ActivityServerList;
import com.android.repetierserverapp.ServerList.FragServerList.ServerAppCallbacks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;


public class ActivityServerDetail extends FragmentActivity implements PrinterAppCallbacks {

	
	
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
			FragServerDetail fragment = new FragServerDetail();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.server_detail_container, fragment).commit();
		}
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this,
					ActivityServerList.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	public void onPrinterSelected(long id) {
		// TODO Auto-generated method stub
		
	}


}

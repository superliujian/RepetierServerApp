package com.android.repetierserverapp.ServerList;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.R.id;
import com.android.repetierserverapp.R.layout;
import com.android.repetierserverapp.ServerDetailList.ActivityServerDetail;
import com.android.repetierserverapp.ServerDetailList.FragServerDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class ActivityServerList extends FragmentActivity implements
		FragServerList.ServerAppCallbacks {


	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_list);

		if (findViewById(R.id.server_detail_container) != null) {

			mTwoPane = true;

		
			((FragServerList) getSupportFragmentManager().findFragmentById(
					R.id.server_list)).setActivateOnItemClick(true);
		}
	}

		
	@Override
	public void onServerSelected(long id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putLong(FragServerDetail.ARG_SERVER_ID, id);
			FragServerDetail fragment = new FragServerDetail();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.server_detail_container, fragment).commit();

		} else {
			Intent detailIntent = new Intent(this, ActivityServerDetail.class);
			detailIntent.putExtra(FragServerDetail.ARG_SERVER_ID, id);
			startActivity(detailIntent);
		}
	}
}

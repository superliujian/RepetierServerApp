package com.android.repetierserverapp.PrinterControll;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.R.layout;
import com.android.repetierserverapp.R.menu;
import com.android.repetierserverapp.ServerDetailList.FragServerDetail;
import com.android.repetierserverapp.PrinterControll.JobList.FragJobList;
import com.android.repetierserverapp.PrinterControll.ModelList.FragModelList;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Server;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;

public class ActivityPrinterControll extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_printer_controll);

		Bundle bundle = getIntent().getExtras();

		String url = bundle.getString("url");
		String alias = bundle.getString("alias");
		String name = bundle.getString("name");
		String slug = bundle.getString("slug");
		int online = bundle.getInt("online");
		String currentJob = bundle.getString("currentJob");
		boolean active = bundle.getBoolean("active");
		double progress = bundle.getDouble("progress");

		Printer printer = new Printer(new Server(url, alias), name, slug, online, currentJob, active, progress);

		Bundle arguments = newInstance(printer);

		FragModelList frag1 = new FragModelList();
		frag1.setArguments(arguments);
		
		FragJobList frag2 = new FragJobList();
		//frag2.setArguments(arguments);
		
		FragPrinterControl frag3 = new FragPrinterControl();
		frag3.setArguments(arguments);
		
		FragPrinterControl2 frag4 = new FragPrinterControl2();
		frag4.setArguments(arguments);

		getSupportFragmentManager().beginTransaction()
		.add(R.id.server_detail_container, frag1).commit();
	}


	public static Bundle newInstance(final Printer p) {
		Bundle args = new Bundle();
		args.putString("url", p.getServer().getUrl());
		args.putString("alias", p.getServer().getAlias());
		args.putString("name", p.getName());
		args.putString("slug", p.getSlug());
		args.putInt("online", p.getOnline());
		args.putString("currentJob", p.getCurrentJob());
		args.putBoolean("active", p.getActive());
		args.putDouble("progress", p.getProgress());

		return args;		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_printer_controll, menu);
		return true;
	}







	/*

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new FragModelList();
				break;

			case 1:
				fragment = new FragJobList();
				break;
			case 2:
				fragment = new FragPrinterControl();
				break;
			case 3:
				fragment = new FragPrinterControl2();
				break;

			}

			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}
	}
	 */

}

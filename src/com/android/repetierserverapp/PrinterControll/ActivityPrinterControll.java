package com.android.repetierserverapp.PrinterControll;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.PrinterControll.JobList.FragJobList;
import com.android.repetierserverapp.PrinterControll.ModelList.FragModelList;
import com.android.repetierserverapp.PrinterControll.ModelList.FragModelList.FragModelCallbacks;
import com.android.repetierserverapp.ServerList.ActivityServerList;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Server;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MenuItem;

public class ActivityPrinterControll extends FragmentActivity implements FragModelCallbacks {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	private OnPageChangeListener pageChangeListener;

	private FragJobList fragJobList;
	private FragModelList fragModelList;
	private FragPrinterControl fragPrinterControl;
	private FragPrinterControl2 fragPrinterControl2;
	private boolean created0;
	private boolean created1;
	private boolean created2;
	private boolean created3;
	
	public static int LAST_ID;
	public static int FILTER;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_printer_controll);

		created0 = false;
		created1 = false;
		created2 = false;
		created3 = false;
		FILTER = 13;

		Bundle bundle = getIntent().getExtras();
		String url = bundle.getString("url");
		String alias = bundle.getString("alias");
		String name = bundle.getString("name");
		String slug = bundle.getString("slug");
		int online = bundle.getInt("online");
		String currentJob = bundle.getString("currentJob");
		boolean active = bundle.getBoolean("active");
		double progress = bundle.getDouble("progress");

		final Printer printer = new Printer(new Server(url, alias), name, slug, online, currentJob, active, progress);

		Bundle arguments = newInstance(printer);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager(), arguments);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);


		pageChangeListener = new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				Log.d("onPageSelected", Integer.toString(position));

				if (created0 == true){
					if (position == 0){
						FragModelList frag0 = (FragModelList) getSupportFragmentManager().findFragmentByTag(makeFragmentName(0));
						frag0.startTimer();
					} else {
						FragModelList frag0 = (FragModelList) getSupportFragmentManager().findFragmentByTag(makeFragmentName(0));
						frag0.stopTimer();
					}
				}
				if (created1 == true){
					if (position == 1){
						FragJobList frag1 = (FragJobList) getSupportFragmentManager().findFragmentByTag(makeFragmentName(1));
						frag1.startTimer();
					} else {
						FragJobList frag1 = (FragJobList) getSupportFragmentManager().findFragmentByTag(makeFragmentName(1));
						frag1.stopTimer();
					}
				}
				if (created2 == true){
					if (position == 2){
						FragPrinterControl frag2 = (FragPrinterControl) getSupportFragmentManager().findFragmentByTag(makeFragmentName(2));
						frag2.startTimer();
					} else {
						FragPrinterControl frag2 = (FragPrinterControl) getSupportFragmentManager().findFragmentByTag(makeFragmentName(2));
						frag2.stopTimer();
					}
				}
				if (created3 == true){
					if (position == 3){
						FragPrinterControl2 frag3 = (FragPrinterControl2) getSupportFragmentManager().findFragmentByTag(makeFragmentName(3));
						frag3.startTimer();
					} else {
						FragPrinterControl2 frag3 = (FragPrinterControl2) getSupportFragmentManager().findFragmentByTag(makeFragmentName(3));
						frag3.stopTimer();
					}
				}

				mSectionsPagerAdapter.getItem(position);
			}
		};

		mViewPager.setOnPageChangeListener(pageChangeListener);
	}



	public static Bundle newInstance(final Printer p) {
		Log.d("newInstance", "entrato");

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



	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private Bundle args;

		public SectionsPagerAdapter(FragmentManager fm, Bundle args) {
			super(fm);
			this.args = args;
		}

		@Override
		public Fragment getItem(int position) {

			switch (position) {
			case 0:
				Log.d("getItem0", "0getitem");
				created0 = true;
				fragModelList = new FragModelList();
				fragModelList.setArguments(args);
				return fragModelList;

			case 1:
				Log.d("getItem1", "1getitem");
				created1 = true;
				fragJobList = new FragJobList();
				fragJobList.setArguments(args);
				return fragJobList;

			case 2:
				Log.d("getItem2", "2getitem");
				created2 = true;
				fragPrinterControl = new FragPrinterControl();
				fragPrinterControl.setArguments(args);
				return fragPrinterControl;

			case 3:
				Log.d("getItem3", "3getitem");
				created3 = true;
				fragPrinterControl2 = new FragPrinterControl2();
				fragPrinterControl2.setArguments(args);
				return fragPrinterControl2;
			}
			Fragment frag = new Fragment();
			return frag;
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				String  modelTitle= getString(R.string.tab_model);
				return modelTitle;
			case 1:
				String jobTitle = getString(R.string.tab_job);
				return jobTitle;
			case 2:
				String printerControl = getString(R.string.tab_control);
				return printerControl;

			case 3:
				String printerControl2 = getString(R.string.tab_control2);
				return printerControl2;
			}
			return null;
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
	public void updateJobList(Printer printer) {
		Log.d("updateJobList Callback", "entrato");
		//fragJobList.updateListView(printer);	
	}




	/*
	public Fragment findFragmentByPosition(int position) {
	    FragmentPagerAdapter fragmentPagerAdapter = getFragmentPagerAdapter();
	    return getSupportFragmentManager().findFragmentByTag(
	            "android:switcher:" + getViewPager().getId() + ":"
	                    + fragmentPagerAdapter.getItemId(position));
	}
	 */

	private static String makeFragmentName(int index)
	{
		Log.d("makeFragmentName", "android:switcher:" + R.id.pager + ":" + index);
		return "android:switcher:" + R.id.pager + ":" + index;
	}

}

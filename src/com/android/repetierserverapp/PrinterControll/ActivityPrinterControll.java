package com.android.repetierserverapp.PrinterControll;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.PrinterControll.JobList.FragJobList;
import com.android.repetierserverapp.PrinterControll.ModelList.FragModelList;
import com.android.repetierserverapp.ServerDetailList.ActivityServerDetail;
import com.android.repetierserverapp.ServerDetailList.FragServerDetail;
import com.android.repetierserverapp.utils.PrefsActivity;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Server;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ActivityPrinterControll extends FragmentActivity {

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
	private boolean jobTimerRunning;
	private boolean modelTimerRunning;
	private boolean controlTimerRunning;
	private boolean control2TimerRunning;

	public static final String ARG_SERVER_ID = "item_id";
	public static int LAST_ID;
	public static int FILTER;
	public int JOB_INTERVAL;
	public int MODEL_INTERVAL;
	public int STATUS_INTERVAL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_printer_controll);

		created0 = false;
		created1 = false;
		created2 = false;
		created3 = false;
		jobTimerRunning = false;
		modelTimerRunning = false;
		controlTimerRunning = false;
		control2TimerRunning = false;
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


				if (position == 0 && created0 == true){
					FragModelList frag = (FragModelList) getSupportFragmentManager().findFragmentByTag(makeFragmentName(0));
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ActivityPrinterControll.this);
					String inter = prefs.getString("modelFreq", "5000");
					MODEL_INTERVAL =  Integer.parseInt(inter);
					frag.startTimer(MODEL_INTERVAL);
					modelTimerRunning = true;
				}

				
				if (position == 1 && created1 == true){
					FragJobList frag = (FragJobList) getSupportFragmentManager().findFragmentByTag(makeFragmentName(1));
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ActivityPrinterControll.this);
					String inter = prefs.getString("jobFreq", "5000");
					JOB_INTERVAL =  Integer.parseInt(inter);
					frag.startTimer(JOB_INTERVAL);
					jobTimerRunning = true;
				}

				if (position == 2 && created2 == true){
					FragPrinterControl frag = (FragPrinterControl) getSupportFragmentManager().findFragmentByTag(makeFragmentName(2));
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ActivityPrinterControll.this);
					String inter = prefs.getString("statusFreq", "3000");
					STATUS_INTERVAL =  Integer.parseInt(inter);
					frag.startTimer(STATUS_INTERVAL);
					controlTimerRunning = true;
				}

				if (position == 3 && created3 == true){
					FragPrinterControl2 frag = (FragPrinterControl2) getSupportFragmentManager().findFragmentByTag(makeFragmentName(3));
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ActivityPrinterControll.this);
					String inter = prefs.getString("statusFreq", "3000");
					STATUS_INTERVAL =  Integer.parseInt(inter);
					frag.startTimer(STATUS_INTERVAL);
					control2TimerRunning = true;
				}

				
				if(position != 0 && modelTimerRunning == true){
					FragModelList frag = (FragModelList) getSupportFragmentManager().findFragmentByTag(makeFragmentName(0));
					frag.stopTimer();
					modelTimerRunning = false;
				}

				if(position != 1 && jobTimerRunning == true){
					FragJobList frag = (FragJobList) getSupportFragmentManager().findFragmentByTag(makeFragmentName(1));
					frag.stopTimer();
					jobTimerRunning = false;
				}

				if(position != 2 && controlTimerRunning == true){
					FragPrinterControl frag = (FragPrinterControl) getSupportFragmentManager().findFragmentByTag(makeFragmentName(2));
					frag.stopTimer();
					controlTimerRunning = false;
				}

				if(position != 3 && control2TimerRunning == true){
					FragPrinterControl2 frag = (FragPrinterControl2) getSupportFragmentManager().findFragmentByTag(makeFragmentName(3));
					frag.stopTimer();
					control2TimerRunning = false;
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
	public void onBackPressed(){

		turnOffTimers();		
		
		Intent detailIntent = new Intent(this, ActivityServerDetail.class);
		detailIntent.putExtra(FragServerDetail.ARG_SERVER_ID, FragServerDetail.idPrinter);
		startActivity(detailIntent);
	}


	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			
			turnOffTimers();
			
			Intent detailIntent = new Intent(this, ActivityServerDetail.class);
			detailIntent.putExtra(FragServerDetail.ARG_SERVER_ID, FragServerDetail.idPrinter);
			
			NavUtils.navigateUpTo(this, detailIntent);
			return true;
			
		case R.id.PrinterControlSettings:
			
			Intent intent = new Intent(ActivityPrinterControll.this, PrefsActivity.class);
			startActivity(intent);
			
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater=getMenuInflater();
	    inflater.inflate(R.menu.printer_control, menu);
	    return super.onCreateOptionsMenu(menu);
	}



	private static String makeFragmentName(int index)
	{
		Log.d("makeFragmentName", "android:switcher:" + R.id.pager + ":" + index);
		return "android:switcher:" + R.id.pager + ":" + index;
	}

	
	
	private void turnOffTimers(){
		
		if(modelTimerRunning == true){
			FragModelList frag = (FragModelList) getSupportFragmentManager().findFragmentByTag(makeFragmentName(0));
			frag.stopTimer();
			modelTimerRunning = false;
		}

		if(jobTimerRunning == true){
			FragJobList frag = (FragJobList) getSupportFragmentManager().findFragmentByTag(makeFragmentName(1));
			frag.stopTimer();
			jobTimerRunning = false;
		}

		if(controlTimerRunning == true){
			FragPrinterControl frag = (FragPrinterControl) getSupportFragmentManager().findFragmentByTag(makeFragmentName(2));
			frag.stopTimer();
			controlTimerRunning = false;
		}

		if(control2TimerRunning == true){
			FragPrinterControl2 frag = (FragPrinterControl2) getSupportFragmentManager().findFragmentByTag(makeFragmentName(3));
			frag.stopTimer();
			control2TimerRunning = false;
		}	
	}

}

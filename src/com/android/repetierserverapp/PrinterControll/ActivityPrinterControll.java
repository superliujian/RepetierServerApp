package com.android.repetierserverapp.PrinterControll;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.PrinterControll.JobList.FragJobList;
import com.android.repetierserverapp.PrinterControll.ModelList.FragModelList;
import com.android.repetierserverapp.PrinterControll.ModelList.FragModelList.PrinterControlCallbacks;
import com.android.repetierserverapp.ServerList.FragServerList.ServerAppCallbacks;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Server;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;

public class ActivityPrinterControll extends FragmentActivity implements PrinterControlCallbacks {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	private OnPageChangeListener pageChangeListener;

	private FragJobList fragJobList;
	private FragModelList fragModelList;
	private FragPrinterControl fragPrinterControl;
	private FragPrinterControl2 fragPrinterControl2;


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
				
				if (position == 1){
					//fragJobList.startTimer();
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



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.activity_printer_controll, menu);
		return true;
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
				fragModelList = new FragModelList();
				fragModelList.setArguments(args);
				return fragModelList;

			case 1:
				fragJobList = new FragJobList();
				fragJobList.setArguments(args);
				return fragJobList;

			case 2:
				fragPrinterControl = new FragPrinterControl();
				fragPrinterControl.setArguments(args);
				return fragPrinterControl;

			case 3:
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
	public void updateJobList(Printer printer) {
		Log.d("updateJobList Callback", "entrato");
		fragJobList.updateListView(printer);	
	}
}

package com.android.repetierserverapp.PrinterControll;


import java.util.ArrayList;

import com.android.repetierserverapp.R;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Printer.PrinterCallbacks;
import com.grasselli.android.repetierserverapi.Printer.PrinterStatusCallbacks;
import com.grasselli.android.repetierserverapi.Server.ServerCallbacks;
import com.grasselli.android.repetierserverapi.Server;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FragPrinterControl extends Fragment implements PrinterStatusCallbacks, OnClickListener, PrinterCallbacks, ServerCallbacks {

	private Button buttonXp10;
	private Button buttonXp1;
	private Button buttonX_1;
	private Button buttonX_10;
	private Button buttonXhome;

	private Button buttonYp10;
	private Button buttonYp1;
	private Button buttonY_1;
	private Button buttonY_10;
	private Button buttonYhome;

	private Button buttonZp10;
	private Button buttonZp1;
	private Button buttonZ_1;
	private Button buttonZ_10;
	private Button buttonZhome;
	private Button buttonHome;

	private TextView textViewXvalue;
	private TextView textViewYvalue;
	private TextView textViewZvalue;

	private TextView textViewStatus;

	Printer printer;

	
	
	public FragPrinterControl(){
	}
	
		
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String url = getArguments().getString("url");
		String alias = getArguments().getString("alias");
		String name = getArguments().getString("name");
		String slug = getArguments().getString("slug");
		int online = getArguments().getInt("online");
		String currentJob = getArguments().getString("currentJob");
		Boolean active = getArguments().getBoolean("active");
		double progress = getArguments().getDouble("progress");

		printer = new Printer(new Server(url, alias),name, slug, online, currentJob, active, progress);

		printer.setPrinterCallbacks(this);
		printer.setPrinterStatusCallbacks(this);	
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_control,
				container, false);
		return rootView;
	}
	
	
	
	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {
		super.onViewCreated(v, savedInstanceState);

		printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
		
		buttonXp10 = (Button) v.findViewById(R.id.xp10button);
		buttonXp1 = (Button) v.findViewById(R.id.xp1button);
		buttonX_1 = (Button) v.findViewById(R.id.x_1button);
		buttonX_10 = (Button) v.findViewById(R.id.x_10button);
		buttonXhome = (Button) v.findViewById(R.id.homexbutton);

		buttonYp10 = (Button) v.findViewById(R.id.yp10button);
		buttonYp1 = (Button) v.findViewById(R.id.yp1button);
		buttonY_1 = (Button) v.findViewById(R.id.y_1button);
		buttonY_10 = (Button) v.findViewById(R.id.y_10button);
		buttonYhome = (Button) v.findViewById(R.id.homeybutton);

		buttonZp10 = (Button) v.findViewById(R.id.zp10button);
		buttonZp1 = (Button) v.findViewById(R.id.zp1button);
		buttonZ_1 = (Button) v.findViewById(R.id.z_1button);
		buttonZ_10 = (Button) v.findViewById(R.id.z_10button);
		buttonZhome = (Button) v.findViewById(R.id.homezbutton);

		buttonHome = (Button) v.findViewById(R.id.homeButton);

		textViewXvalue = (TextView) v.findViewById(R.id.xValueTextView);
		textViewYvalue = (TextView) v.findViewById(R.id.yValueTextView);
		textViewZvalue = (TextView) v.findViewById(R.id.zValueTextView);

		textViewStatus = (TextView) v.findViewById(R.id.statusTextView);

		buttonXp10.setOnClickListener(this);
		buttonXp1.setOnClickListener(this);
		buttonX_1.setOnClickListener(this);
		buttonX_10.setOnClickListener(this);
		buttonXhome.setOnClickListener(this);

		buttonYp10.setOnClickListener(this);
		buttonYp1.setOnClickListener(this);
		buttonY_1.setOnClickListener(this);
		buttonY_10.setOnClickListener(this);
		buttonYhome.setOnClickListener(this);

		buttonZp10.setOnClickListener(this);
		buttonZp1.setOnClickListener(this);
		buttonZ_1.setOnClickListener(this);
		buttonZ_10.setOnClickListener(this);
		buttonZhome.setOnClickListener(this);
		buttonHome.setOnClickListener(this);
	}

	
	
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.xp10button:
			printer.move(getActivity(), 10, 0, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.xp1button:
			printer.move(getActivity(), 1, 0, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.x_1button:
			printer.move(getActivity(), -1, 0, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.x_10button:
			printer.move(getActivity(), -10, 0, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.homexbutton:
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.yp10button:
			printer.move(getActivity(), 0, 10, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.yp1button:
			printer.move(getActivity(), 0, 1, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.y_1button:
			printer.move(getActivity(), 0, -1, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.y_10button:
			printer.move(getActivity(), 0, -10, 0, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.homeybutton:
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.zp10button:
			printer.move(getActivity(), 0, 0, 10, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.zp1button:
			printer.move(getActivity(), 0, 0, 1, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.z_1button:
			printer.move(getActivity(), 0, 0, -1, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.z_10button:
			printer.move(getActivity(), 0, 0, -10, 0);
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.homezbutton:
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			break;

		case R.id.homeButton:
			printer.updatePrinterStatus(getActivity(), printer.getLastId(), 13);
			printer.moveHome(getActivity());
			break;

		default:
			break;
		}
	}

	
	
	@Override
	public void onPrinterStatusUpdated() {
		
		String x = Double.toString(printer.getStatus().getX());		
		String y = Double.toString(printer.getStatus().getY());		
		String z = Double.toString(printer.getStatus().getZ());
		
		textViewXvalue.setText(x);	
		textViewYvalue.setText(y);	
		textViewZvalue.setText(z);
	}

	
	
	@Override
	public void onError(String error) {
		// TODO Auto-generated method stub

	}



	@Override
	public void onPrinterListUpdated(ArrayList<Printer> printerList) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onChangeState() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onCommandExecuted() {
		// TODO Auto-generated method stub
		
	}
}
package com.android.repetierserverapp.PrinterControll.ModelList;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;
import android.widget.ListView;

import com.android.repetierserverapp.R;
import com.android.repetierserverapp.PrinterControll.ModelList.ModelListAdapter.ModelListAdapterCallback;
import com.grasselli.android.repetierserverapi.Model;
import com.grasselli.android.repetierserverapi.Printer;
import com.grasselli.android.repetierserverapi.Printer.ModelCallbacks;
import com.grasselli.android.repetierserverapi.Server;

public class FragModelList extends ListFragment {

	private ListView listview;
	private ModelListAdapter adapter;

	private Printer printer;

	private ModelListAdapterCallback modelListAdapterCallback;




	public FragModelList() {
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		String url = getArguments().getString("url");
		String alias = getArguments().getString("alias");
		String name = getArguments().getString("name");
		String slug = getArguments().getString("slug");
		int online = getArguments().getInt("online");
		String currentJob = getArguments().getString("currentJob");
		Boolean active = getArguments().getBoolean("active");
		double progress = getArguments().getDouble("progress");

		printer = new Printer(new Server(url, alias),name, slug, online, currentJob, active, progress);

		printer.setModelCallbacks(new ModelCallbacks() {
			@Override
			public void onModelUploaded() {
				printer.updateModelList(getActivity());
				Toast.makeText(getActivity(), getString(R.string.onModelUploaded), Toast.LENGTH_LONG).show();			}

			@Override
			public void onModelListUpdated(ArrayList<Model> newModelList) {

				adapter = new ModelListAdapter(getActivity(), R.layout.model_line, newModelList, modelListAdapterCallback, printer); 
				listview.setAdapter(adapter);
			}

			@Override
			public void onModelDeleted() {
				printer.updateModelList(getActivity());
				Toast.makeText(getActivity(), getString(R.string.onModelDeleted), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onModelCopied() {
				Toast.makeText(getActivity(), getString(R.string.onModelCopied), Toast.LENGTH_LONG).show();	
			}

			@Override
			public void onError(String error) {
				Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
			}
		});

		printer.updateModelList(getActivity());

		modelListAdapterCallback = new ModelListAdapterCallback() {
			@Override
			public void updateModelList(Printer printer) {
				printer.updateModelList(getActivity());
			}

			@Override
			public void copyModel(Printer printer, int id) {
			}

			@Override
			public void deleteModel(Printer printer, int id) {
				printer.deleteModel(getActivity(), id);				
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_model_list,
				container, false);
		return rootView;
	}



	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		listview = getListView();
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, final long id) {
				String[] array = new String[2];
				array[1] = getString(R.string.popModelDelete);
				array[0] = getString(R.string.popModelToQueue);

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(((Model) listview.getAdapter().getItem(position)).getName())
				.setItems(array, 
						new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 1:
							printer.deleteModel(getActivity(), (int) id);
							break;

						case 0:
							printer.copyModel(getActivity(), (int) id);
							break;
						}
					}
				});
				builder.create().show();

				return true;
			}
		});
	}

}
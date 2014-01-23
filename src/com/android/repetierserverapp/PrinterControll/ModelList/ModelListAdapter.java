package com.android.repetierserverapp.PrinterControll.ModelList;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.repetierserverapp.R;
import com.grasselli.android.repetierserverapi.Model;

public class ModelListAdapter extends ArrayAdapter<Model>{

	private Context context;
	private ArrayList<Model> modelList;
	private Model model;
	
	

	public ModelListAdapter(Context context, int textViewResourceId, ArrayList<Model> list) {
		super(context, textViewResourceId, list);
		this.context = context;
		this.modelList = list;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;
		if (rowView == null){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.model_line, null);
			//parent, false
		}

		TextView modelName = (TextView) rowView.findViewById(R.id.modelName);
		TextView modelStatus = (TextView) rowView.findViewById(R.id.modelStatus);
		TextView dimenModel = (TextView) rowView.findViewById(R.id.dimenModel);


		model = modelList.get(position);

		modelName.setText(model.getName());

		modelStatus.setText(model.getState());

		double size = model.getLength();
		String dimen = Double.toString(Math.round(size/1048576*100)/100);
		dimenModel.setText(dimen);

		return rowView;
	}

}
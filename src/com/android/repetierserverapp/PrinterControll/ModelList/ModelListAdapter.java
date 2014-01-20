package com.android.repetierserverapp.PrinterControll.ModelList;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.android.repetierserverapp.R;
import com.grasselli.android.repetierserverapi.Model;
import com.grasselli.android.repetierserverapi.Printer;

public class ModelListAdapter extends ArrayAdapter<Model> implements OnClickListener{

	private Context context;
	private ArrayList<Model> modelList;
	private Model model;
	private ModelListAdapterCallback listener;
	private Printer printer;


	public ModelListAdapter(Context context, int textViewResourceId, ArrayList<Model> list, ModelListAdapterCallback listener, Printer printer) {
		super(context, textViewResourceId, list);
		this.context = context;
		this.modelList = list;
		this.listener = listener;
		this.printer = printer;
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

		Button copyBtt = (Button) rowView.findViewById(R.id.copyBtt);
		Button deleteBtt = (Button) rowView.findViewById(R.id.deleteBtt);

		copyBtt.setOnClickListener(this);
		deleteBtt.setOnClickListener(this);

		model = modelList.get(position);

		modelName.setText(model.getName());

		modelStatus.setText(model.getState());

		double size = model.getLength();
		String dimen = Double.toString(Math.round(size/1048576*100)/100);
		dimenModel.setText(dimen);

		return rowView;
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.copyBtt:
			listener.copyModel(printer, model.getId());
			break;
		case R.id.deleteBtt:
			listener.deleteModel(printer, model.getId());
			break;
		}
	}



	public interface ModelListAdapterCallback {
		public void updateModelList(Printer p);
		public void copyModel(Printer p, int id);
		public void deleteModel(Printer p, int id);
	}

	public interface ModelAdapterCallback {
		public void updateJobList(Printer p);
	}
}
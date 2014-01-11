package com.android.repetierserverapp.utils;

import java.util.List;

import com.android.repetierserverapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ServerListAdapter extends ArrayAdapter<ServerInfo>{

    public ServerListAdapter(Context context, int textViewResourceId,
            List<ServerInfo> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
             .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.serverview, null);
        TextView name = (TextView)convertView.findViewById(R.id.textViewName);
        TextView url = (TextView)convertView.findViewById(R.id.textViewUrl);
        ServerInfo info = getItem(position);
        name.setText(info.name);
        url.setText(info.url);
        return convertView;
    }

}
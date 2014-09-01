package com.aidiapp.salonbike.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import com.aidiapp.salonbike.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BikeLaneListAdapter extends ArrayAdapter<HashMap<String,String>> {
private ArrayList<HashMap<String,String>> coleccion;
private int resource;
	public BikeLaneListAdapter(Context context, int resource,
			ArrayList<HashMap<String, String>> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.coleccion=objects;
		this.resource=resource;
	}

	@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		HashMap<String,String> e=this.getItem(position);
		if(convertView==null){
			
			LayoutInflater inf=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inf.inflate(resource, parent,false);
			convertView.setBackgroundColor(Integer.valueOf(e.get("Color")));
			
			//((TextView)convertView.findViewById(R.id.laneitem_title)).setTextSize(24);
			
		}
		
		
		((TextView)convertView.findViewById(R.id.laneitem_title)).setText(e.get("Nombre"));
		
			return convertView;
		}

@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.coleccion.size();
	}
	
@Override
	public HashMap<String, String> getItem(int position) {
		// TODO Auto-generated method stub
		return this.coleccion.get(position);
	}



}

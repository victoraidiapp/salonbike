package com.aidiapp.salonbike.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.aidiapp.salonbike.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LanesPage extends Fragment implements OnItemClickListener {
	public static final String LISTACARRILES="com.aidiapp.salonbike.LISTACARRILES";
	public interface Listener{
		public void onSelectLane(Integer l);
	}
private ListView listado;
private ArrayList<HashMap<String, String>> lanes;
private Listener listener;
public LanesPage(Listener listener) {
	// TODO Auto-generated constructor stub
	this.setListener(listener);
}
public LanesPage() {
	// TODO Auto-generated constructor stub
	this.setListener(null);
}
	public void setLanes(
			ArrayList<HashMap<String, String>> lanesHashMaptoLanesList) {
		// TODO Auto-generated method stub
		this.lanes=lanesHashMaptoLanesList;
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(savedInstanceState!=null){
			Log.d("LANESPAGE","Hay algo para recuperar");
		}
		super.onCreate(savedInstanceState);
	}
@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	Log.d("LANESPAGE","Creamos la vista");
	View v=inflater.inflate(R.layout.lanespage, container,false);
	this.listado=(ListView) v.findViewById(R.id.listView_BikesLanes);
	if(savedInstanceState!=null){
		//this.lanes=new ArrayList();
		this.lanes=(ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable(LISTACARRILES);
		//this.lanes.addAll(savedInstanceState.getString(LISTACARRILES));
	}
	BikeLaneListAdapter adaptador=new BikeLaneListAdapter(this.getActivity().getBaseContext(),R.layout.lanelist_item,this.lanes);
	this.listado.setOnItemClickListener(this);
	this.listado.setAdapter(adaptador);
	
		return v;
	}

@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
	Log.d("LANESPAGE","Adjuntamos el fragment");
		super.onAttach(activity);
	}
@Override
public void onItemClick(AdapterView<?> adaptador, View arg1, int arg2, long arg3) {
	// TODO Auto-generated method stub
	HashMap<String,String> h=(HashMap<String, String>) adaptador.getItemAtPosition(arg2);
	this.getListener().onSelectLane(Integer.valueOf(h.get("IdLane")));
}

@Override
public void onSaveInstanceState(Bundle outState) {
	// TODO Auto-generated method stub
	//String r=this.lanes.toString();
	outState.putSerializable(LISTACARRILES, this.lanes);
	
	//Log.d("LANESPAGE","Queremos guardar esto "+r);
	super.onSaveInstanceState(outState);
}
public Listener getListener() {
	return listener;
}
public void setListener(Listener listener) {
	this.listener = listener;
}
}

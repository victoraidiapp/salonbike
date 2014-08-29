package com.aidiapp.salonbike.ui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidiapp.salonbike.R;
import com.aidiapp.salonbike.core.BikeLane;
import com.aidiapp.salonbike.core.DataManager;

public class MapManager extends SupportMapFragment implements OnMapLoadedCallback,DataManager.DataListener {
	private static final String SUPPORT_MAP_BUNDLE_KEY = "MapOptions";
	public interface ActivityListener{
		public void onLoadingContent(Boolean estado);
		public void onLoadingMap(Boolean estado);
	}
	private ActivityListener activityListener;
	private HashMap<String,BikeLane>lanesZones;
	private HashMap<String,PolyLineGroup>lanesZonesMaplines;
	private Boolean flagLanesLayer=false;
	private LinearLayout lanesInfoBox;
	private ViewGroup container;
	public static MapManager newInstance(GoogleMapOptions opciones,ActivityListener activityListener){
		Bundle arguments = new Bundle();
	    arguments.putParcelable(SUPPORT_MAP_BUNDLE_KEY, opciones);

	    MapManager fragment = new MapManager();
	    
	    fragment.setArguments(arguments);
	    
	    DataManager.getLanes(fragment,((Activity)activityListener).getAssets());
	    
	    return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View r=super.onCreateView(inflater, container, savedInstanceState);
		this.container=container;
		this.lanesInfoBox=new LinearLayout(this.getActivity());
		this.lanesInfoBox.setOrientation(LinearLayout.HORIZONTAL);
		this.lanesInfoBox.setPadding(10, 10, 10, 0);
		/*TextView tv=new TextView(this.getActivity());
		tv.setText("Hola");
		this.lanesInfoBox.addView(tv, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));*/
		
		//this.lanesInfoBox.setVisibility(View.INVISIBLE);
		this.getMap().setOnMapLoadedCallback(this);
		return r;
	}
	public void showLaneLayer() {
		// Muestra la capa de carriles bici
		this.flagLanesLayer=true;
		if(this.lanesZonesMaplines==null){
			this.loadLanesLines();
		}else{
		if(!this.lanesZonesMaplines.isEmpty()){
			this.lanesInfoBox.setVisibility(View.VISIBLE);
		Set coleccion=this.lanesZonesMaplines.entrySet();
		Iterator it=coleccion.iterator();
		while (it.hasNext()){
			Entry e=(Entry) it.next();
			((PolyLineGroup)e.getValue()).setVisible(true);
		}
		}
		}
	}
		
	private void loadLanesLines() {
		// TODO Auto-generated method stub
		
		if(this.lanesZones!=null){
			
			
		this.lanesZonesMaplines=new HashMap();
		Iterator it=this.lanesZones.entrySet().iterator();
		int i=1;
		while(it.hasNext()){
			TextView tv=new TextView(this.getActivity());
			tv.setText(String.valueOf(i));
			tv.setGravity(Gravity.CENTER);
			Entry e=(Entry) it.next();
			BikeLane bl=(BikeLane) e.getValue();
			tv.setBackgroundResource(R.drawable.rounded_box);
			tv.setTextColor(Color.WHITE);
			GradientDrawable dr=(GradientDrawable) tv.getBackground();
			dr.setColor(bl.getColor());
			//tv.setBackgroundColor(bl.getColor());
			
			//tv.setBackgroundDrawable(this.getActivity().getResources().getDrawable(R.drawable.rounded_box));
			tv.setPadding(5, 15, 5, 15);
			LinearLayout.LayoutParams llParams=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f);
			llParams.setMargins(5, 5, 5, 5);
			this.lanesInfoBox.addView(tv,llParams );
			
			PolylineOptions opciones=new PolylineOptions();
			opciones.color(bl.getColor());
			
			opciones.width(10f);
			PolyLineGroup plg=new PolyLineGroup(this.getMap(),opciones);
			ArrayList carriles=bl.getCarriles();
			Iterator itc=carriles.iterator();
			while (itc.hasNext()){
				List puntos=(List) itc.next();
				plg.addPolyline(puntos);
			}
			this.lanesZonesMaplines.put(bl.getName(), plg);
			i++;
		}
		if(this.flagLanesLayer) this.showLaneLayer();
		}
	}

	
	public void showBikeStationsLayer() {
		// Muestra la capa de intercambiadores
		
	}
	public void hideBikeStationsLayer() {
		// TODO Auto-generated method stub
		
	}
	public void hideBikeLanesLayer() {
		// TODO Auto-generated method stub
		if(this.lanesZonesMaplines!=null){
			this.lanesInfoBox.setVisibility(View.INVISIBLE);
		Set coleccion=this.lanesZonesMaplines.entrySet();
		Iterator it=coleccion.iterator();
		while (it.hasNext()){
			Entry e=(Entry) it.next();
			((PolyLineGroup)e.getValue()).setVisible(false);
		}
		}
		this.flagLanesLayer=false;
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		this.getActivityListener().onLoadingMap(false);
		LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		
		
		this.container.addView(lanesInfoBox,llp );
		
	}

	public ActivityListener getActivityListener() {
		return activityListener;
	}

	public void setActivityListener(ActivityListener activityListener) {
		this.activityListener = activityListener;
	}


	@Override
	public void onLanesResult(HashMap<String, BikeLane> result) {
		// TODO Auto-generated method stub
		Log.d("MAPMANAGER","Hemos Obtenido el resultado");
		this.lanesZones=result;
		if(this.flagLanesLayer){
			this.showLaneLayer();
		}
	}


	
}

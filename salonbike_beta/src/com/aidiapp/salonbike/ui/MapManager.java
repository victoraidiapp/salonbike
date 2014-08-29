package com.aidiapp.salonbike.ui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
		this.getMap().setOnMapLoadedCallback(this);
		return r;
	}
	public void showLaneLayer() {
		// Muestra la capa de carriles bici
		if(this.lanesZonesMaplines==null){
			this.loadLanesLines();
		}
		Set coleccion=this.lanesZonesMaplines.entrySet();
		Iterator it=coleccion.iterator();
		while (it.hasNext()){
			Entry e=(Entry) it.next();
			((PolyLineGroup)e.getValue()).setVisible(true);
		}
	}
		
	private void loadLanesLines() {
		// TODO Auto-generated method stub
		this.lanesZonesMaplines=new HashMap();
		Iterator it=this.lanesZones.entrySet().iterator();
		while(it.hasNext()){
			Entry e=(Entry) it.next();
			BikeLane bl=(BikeLane) e.getValue();
			PolylineOptions opciones=new PolylineOptions();
			opciones.color(bl.getColor());
			opciones.width(3f);
			PolyLineGroup plg=new PolyLineGroup(this.getMap(),opciones);
			ArrayList carriles=bl.getCarriles();
			Iterator itc=carriles.iterator();
			while (itc.hasNext()){
				List puntos=(List) itc.next();
				plg.addPolyline(puntos);
			}
			this.lanesZonesMaplines.put(bl.getName(), plg);
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
		Set coleccion=this.lanesZonesMaplines.entrySet();
		Iterator it=coleccion.iterator();
		while (it.hasNext()){
			Entry e=(Entry) it.next();
			((PolyLineGroup)e.getValue()).setVisible(false);
		}
		}
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		this.getActivityListener().onLoadingMap(false);
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
		Set<Entry<String, BikeLane>> coleccion=this.lanesZones.entrySet();
		Iterator it=coleccion.iterator();
		while (it.hasNext()){
			Entry<String,BikeLane> et=(Entry<String, BikeLane>) it.next();
			Log.d("MAPMANAGER","Aquí tenemos la zona "+et.getKey());
		}
	}
}

package com.aidiapp.salonbike.ui;


import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapManager extends SupportMapFragment implements OnMapLoadedCallback {
	private static final String SUPPORT_MAP_BUNDLE_KEY = "MapOptions";
	public interface ActivityListener{
		public void onLoadingContent(Boolean estado);
		public void onLoadingMap(Boolean estado);
	}
	private ActivityListener activityListener;
	public static MapManager newInstance(GoogleMapOptions opciones,ActivityListener activityListener){
		Bundle arguments = new Bundle();
	    arguments.putParcelable(SUPPORT_MAP_BUNDLE_KEY, opciones);

	    MapManager fragment = new MapManager(activityListener);
	    
	    fragment.setArguments(arguments);
	    return fragment;
	}
	
	public MapManager(ActivityListener activityListener) {
		// TODO Auto-generated constructor stub
		this.activityListener=activityListener;
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View r=super.onCreateView(inflater, container, savedInstanceState);
		this.activityListener.onLoadingMap(true);
		this.getMap().setOnMapLoadedCallback(this);
		return r;
	}
	public void showLaneLayer() {
		// Muestra la capa de carriles bici
		
	}
	public void showBikeStationsLayer() {
		// Muestra la capa de intercambiadores
		
	}
	public void hideBikeStationsLayer() {
		// TODO Auto-generated method stub
		
	}
	public void hideBikeLanesLayer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		this.activityListener.onLoadingMap(false);
	}
}

package com.aidiapp.salonbike.ui;


import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MapManager extends SupportMapFragment {
	private static final String SUPPORT_MAP_BUNDLE_KEY = "MapOptions";
	public static MapManager newInstance(GoogleMapOptions opciones){
		Bundle arguments = new Bundle();
	    arguments.putParcelable(SUPPORT_MAP_BUNDLE_KEY, opciones);

	    MapManager fragment = new MapManager();
	    
	    fragment.setArguments(arguments);
	    return fragment;
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
}

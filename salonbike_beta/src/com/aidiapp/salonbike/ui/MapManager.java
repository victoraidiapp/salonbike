package com.aidiapp.salonbike.ui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidiapp.salonbike.R;
import com.aidiapp.salonbike.core.BikeLane;
import com.aidiapp.salonbike.core.BikeStation;
import com.aidiapp.salonbike.core.DataManager;
import com.aidiapp.salonbike.ui.LaneInfoDialog.Listener;
import com.aidiapp.salonbike.ui.utils.ImageUtils;
import com.aidiapp.salonbike.ui.utils.TypefaceSpan;

public class MapManager extends SupportMapFragment implements OnMapLoadedCallback,DataManager.DataListener, OnMarkerClickListener, Listener, com.aidiapp.salonbike.ui.BikeStationDialog.Listener {
	private static final String SUPPORT_MAP_BUNDLE_KEY = "MapOptions";
	public interface ActivityListener{
		public void onLoadingContent(Boolean estado);
		public void onLoadingMap(Boolean estado);
		public void onLoadLanesResult(HashMap<Integer,BikeLane> col);
	}
	private ActivityListener activityListener;
	private HashMap<Integer,BikeLane>lanesZones;
	private HashMap<Integer,BikeStation>bikeStations;
	private HashMap<Integer,Marker> stationMarkersGroup;
	private HashMap<Integer,PolyLineGroup>lanesZonesMaplines;
	private Boolean flagLanesLayer=false;
	private Boolean flagStationsLayer=false;
	
	private ViewGroup container;
	private LaneInfoDialog laneInfoDialog;
	private BikeStationDialog bikeStationDialog;
	public static MapManager newInstance(GoogleMapOptions opciones,ActivityListener activityListener){
		Bundle arguments = new Bundle();
	    arguments.putParcelable(SUPPORT_MAP_BUNDLE_KEY, opciones);

	    MapManager fragment = new MapManager();
	    
	    fragment.setArguments(arguments);
	    
	    DataManager.getLanes(fragment,((Activity)activityListener).getAssets());
	    DataManager.getBikeStations(fragment);
	    return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d("MAPMANAGER","Creamos la vidsta");
		View r=super.onCreateView(inflater, container, savedInstanceState);
		this.container=container;
		this.laneInfoDialog=new LaneInfoDialog();
		this.bikeStationDialog=new BikeStationDialog();
		this.getMap().setOnMapLoadedCallback(this);
		return r;
	}
	public void showLaneLayer() {
		// Muestra la capa de carriles bici
		this.flagLanesLayer=true;
		if(this.lanesZonesMaplines==null){
			Log.d("MAPMANAGER", "TEnemos que cargar las lineas");
			this.loadLanesLines();
		}else{
			
		if(!this.lanesZonesMaplines.isEmpty()){
			//this.lanesInfoBox.setVisibility(View.VISIBLE);
			Log.d("MAPMANAGER", "Hay lineas");
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
		if(this.getMap()==null)return;
		if(this.lanesZones!=null){
			
			
		this.lanesZonesMaplines=new HashMap();
		Iterator it=this.lanesZones.entrySet().iterator();
		int i=1;
		
		
		while(it.hasNext()){
			
			Entry e=(Entry) it.next();
			BikeLane bl=(BikeLane) e.getValue();
			
			Log.d("MAPMANAGER","Vamos a cargar la linea del carril "+bl.getName());
			
			PolylineOptions opciones=new PolylineOptions();
			opciones.color(bl.getColor());
			
			opciones.width(10f);
			PolyLineGroup plg=new PolyLineGroup(this.getMap(),opciones);
			ArrayList carriles=bl.getCarriles();
			Iterator itc=carriles.iterator();
			if(this.getActivityListener()==null){
				Log.d("MAPMANAGER","No hay contexto ");
			}
			while (itc.hasNext()){
				List puntos=(List) itc.next();
				
			
				plg.addPolyline(puntos,String.valueOf(bl.getIdLane()),(Activity)this.getActivityListener());
			}
			this.lanesZonesMaplines.put(bl.getIdLane(), plg);
			i++;
		}
		if(this.flagLanesLayer) this.showLaneLayer();
		}
	}

	
	public void showBikeStationsLayer() {
		// Muestra la capa de intercambiadores
		if(this.getMap()==null)return;
		this.flagStationsLayer=true;
		if(this.stationMarkersGroup==null){
			this.loadBikeStations();
		}else{
			if(!this.stationMarkersGroup.isEmpty()){
				Iterator it=this.stationMarkersGroup.entrySet().iterator();
				while(it.hasNext()){
					Entry e=(Entry) it.next();
					((Marker)e.getValue()).setVisible(true);
				}
			}
		}
		
	}
	private void loadBikeStations() {
		// TODO Auto-generated method stub
		if(this.bikeStations!=null){
			this.stationMarkersGroup=new HashMap();
			Iterator it=this.bikeStations.entrySet().iterator();
			int i=101;
			while(it.hasNext()){
				Entry e=(Entry) it.next();
				BikeStation bs=(BikeStation) e.getValue();
				Marker m=this.getMap().addMarker(new MarkerOptions().position(bs.getUbicacion()));
				this.stationMarkersGroup.put(i, m);
				m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.show_bike_stations));
				m.setVisible(false);
				m.setTitle(String.valueOf(i));
				i++;
			}
			if(this.flagStationsLayer) this.showBikeStationsLayer();
		}
	}


	public void hideBikeStationsLayer() {
		// TODO Auto-generated method stub
		if(this.stationMarkersGroup!=null){
			Iterator it=this.stationMarkersGroup.entrySet().iterator();
			while(it.hasNext()){
				Entry e=(Entry) it.next();
				((Marker)e.getValue()).setVisible(false);
			}
		}
		this.flagStationsLayer=false;
	}
	public void hideBikeLanesLayer() {
		// TODO Auto-generated method stub
		if(this.lanesZonesMaplines!=null){
			//this.lanesInfoBox.setVisibility(View.INVISIBLE);
		Set coleccion=this.lanesZonesMaplines.entrySet();
		Iterator it=coleccion.iterator();
		while (it.hasNext()){
			Entry e=(Entry) it.next();
			((PolyLineGroup)e.getValue()).setVisible(false);
		}
		}
		this.flagLanesLayer=false;
	}
public void showBikeLaneInfoDialog(Integer lane){
	
	this.laneInfoDialog.setBikeLane(this.lanesZones.get(lane));
	Location loc=this.getMap().getMyLocation();
	this.laneInfoDialog.setCurrent(new LatLng(loc.getLatitude(),loc.getLongitude()));
	this.laneInfoDialog.setListener(this);
	this.laneInfoDialog.show(getFragmentManager(), "BikeLaneInfoDialog");
}
	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		this.getActivityListener().onLoadingMap(false);
		LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		this.getMap().setMyLocationEnabled(true);
		this.getMap().setOnMarkerClickListener(this);
		if(this.flagLanesLayer){
			Log.d("MAPMANAGER","Tenemos que mostrar la capa de carriles");
			this.showLaneLayer();
			
		}
		
		if(this.flagStationsLayer){
			this.showBikeStationsLayer();
		}
		
	}

	public ActivityListener getActivityListener() {
		return activityListener;
	}

	public void setActivityListener(ActivityListener activityListener) {
		this.activityListener = activityListener;
	}


	@Override
	public void onLanesResult(HashMap<Integer, BikeLane> result) {
		// TODO Auto-generated method stub
		Log.d("MAPMANAGER","Hemos Obtenido el resultado");
		this.lanesZones=result;
		
		this.activityListener.onLoadLanesResult(result);
		if(this.flagLanesLayer){
			this.showLaneLayer();
		}
	}


	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		Log.d("MAPMANAGER","Has picado en el marcador "+marker.getTitle());
		int i=Integer.valueOf(marker.getTitle());
		if(i<100){
		this.showBikeLaneInfoDialog(i);
		}else{
			this.showBikeStationInfoDialog(i);
		}
		return true;
	}

private void showBikeStationInfoDialog(int i) {
		// TODO Auto-generated method stub
	Log.d("MAPMANAGER","Queremos mostrar la station "+i);
	BikeStation bs=this.bikeStations.get(Integer.valueOf(i-100));
	Log.d("MAPMANAGER","La station es "+bs.getNombre());
	this.bikeStationDialog.setBikeStation(bs);
	Location loc=this.getMap().getMyLocation();
	this.bikeStationDialog.setCurrent(new LatLng(loc.getLatitude(),loc.getLongitude()));
	this.bikeStationDialog.setListener(this);
	this.bikeStationDialog.show(getFragmentManager(), "BikeStationInfoDialog");
	}


@Override
public void onAttach(Activity activity) {
	// TODO Auto-generated method stub
	Log.d("MAPMANAGER","Me vuelven a adjuntar a la activity");
	
	this.lanesZonesMaplines=null;
	
	super.onAttach(activity);
}


public void showNearestLane() {
	// TODO Auto-generated method stub
	Location loc=this.getMap().getMyLocation();
	int l=BikeLane.getNearestLane(loc, this.lanesZones);
	Log.d("MAPAMANAGER", "El lane más cercano es "+l);
	this.showBikeLaneInfoDialog(l);
}


@Override
public void onInitRouteToLane(Integer l) {
	// TODO Auto-generated method stub
	Location current=this.getMap().getMyLocation();
	LatLng p=BikeLane.getNearestLanePosition(current, this.lanesZones.get(l).getCarriles());
	Log.d("MAP MANAGER","El punto más cercano es "+p.toString());
	/*Intent intent = new Intent( Intent.ACTION_VIEW, 
			
            Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d" +
            "&saddr="+current.getLatitude()+","+current.getLongitude()+"&daddr="+p.latitude+","+p.longitude+"&hl=zh&t=m&dirflg=w")); 
Intent intent = new Intent( Intent.ACTION_VIEW, 
			
            Uri.parse("google.navigation:ll="+p.latitude+","+p.longitude+"&title="+this.lanesZones.get(l).getName()+"&mode=w")); 
	
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK&Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");*/
	String lauri="google.navigation:ll="+p.latitude+","+p.longitude+"&mode=w";
	//Log.d("MAPMANAGER","Tratamos de abrir "+lauri);
	Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(lauri));
    startActivity(intent);
}


@Override
public void onBikeResult(HashMap<Integer, BikeStation> result) {
	// TODO Auto-generated method stub
	this.bikeStations=result;
	if(this.flagStationsLayer){
		this.showBikeStationsLayer();
	}
}


@Override
public void onInitRouteToStation(Integer l) {
	// TODO Auto-generated method stub
	// TODO Auto-generated method stub
		Location current=this.getMap().getMyLocation();
		LatLng p=this.bikeStations.get(l).getUbicacion();
		Log.d("MAP MANAGER","El punto más cercano es "+p.toString());
		/*
		Intent intent = new Intent( Intent.ACTION_VIEW, 
	            Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d" +
	            "&saddr="+current.getLatitude()+","+current.getLongitude()+"&daddr="+p.latitude+","+p.longitude+"&hl=zh&t=m&dirflg=w")); 
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK&Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
	    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");*/
	    String lauri="google.navigation:ll="+p.latitude+","+p.longitude+"&mode=w";
	//Log.d("MAPMANAGER","Tratamos de abrir "+lauri);
	Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(lauri));
	    startActivity(intent);
}


public void showNearestStation() {
	// TODO Auto-generated method stub
	// TODO Auto-generated method stub
		Location loc=this.getMap().getMyLocation();
		int l=BikeStation.getNearestStation(loc, this.bikeStations);
		Log.d("MAPAMANAGER", "La station más cercano es "+l);
		this.showBikeStationInfoDialog(l+100);
}



	
}

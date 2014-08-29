package com.aidiapp.salonbike.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class PolyLineGroup {
private ArrayList<Polyline> lineas;
private PolylineOptions opciones;
private GoogleMap gmap;
public PolyLineGroup(GoogleMap gmap,PolylineOptions opciones) {
	// TODO Auto-generated constructor stub
	this.lineas=new ArrayList();
	this.opciones=opciones;
	this.gmap=gmap;
}


public void setVisible(Boolean visible){
	Iterator it=this.lineas.iterator();
	while(it.hasNext()){
		((Polyline)it.next()).setVisible(visible);
	}
}

public void addPolyline(List<LatLng> puntos){
	Polyline poli=this.gmap.addPolyline(this.opciones);
	poli.setPoints(puntos);
	poli.setVisible(false);
	
	this.lineas.add(poli);
	
}

}

package com.aidiapp.salonbike.core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
public class BikeLane {
	private int idLane;
	private String name,description,length;
	private ArrayList<ArrayList<LatLng>> carriles;
	private int color;
public static int getNearestLane(Location loc,HashMap<Integer,BikeLane> col){
		int r=0;
		float distance=100000000;
		Iterator<Entry<Integer, BikeLane>> it=col.entrySet().iterator();
		while(it.hasNext()){
			Entry e=it.next();
			Integer b=(Integer) e.getKey();
			Iterator itb=((BikeLane)e.getValue()).getCarriles().iterator();
			while(itb.hasNext()){
				ArrayList lista=(ArrayList) itb.next();
				//ArrayList<LatLng> puntos=(ArrayList<LatLng>) lista.iterator();
				Iterator itp=lista.iterator();
				while(itp.hasNext()){
					LatLng pnt=(LatLng) itp.next();
					Location loTarget=new Location("SALONBIKE");
					loTarget.setLatitude(pnt.latitude);
					loTarget.setLongitude(pnt.longitude);
					if(loc.distanceTo(loTarget)<distance){
						r=b;
						distance=loc.distanceTo(loTarget);
					}
				}
			}
		}
		return r;
	}
public static LatLng getNearestLanePosition(Location loc, ArrayList<ArrayList<LatLng>> carriles){
	LatLng r=null;
	float distancia=10000000;
	Iterator it=carriles.iterator();
	while(it.hasNext()){
		ArrayList<LatLng> lista=(ArrayList<LatLng>) it.next();
		Iterator itp=lista.iterator();
		while(itp.hasNext()){
			LatLng pnt=(LatLng) itp.next();
			Location loTarget=new Location("SALONBIKE");
			loTarget.setLatitude(pnt.latitude);
			loTarget.setLongitude(pnt.longitude);
			if(loc.distanceTo(loTarget)<distancia){
				r=pnt;
				distancia=loc.distanceTo(loTarget);
			}
		}
	}
	return r;
}
	public BikeLane() {
		// TODO Auto-generated constructor stub
		this.carriles=new ArrayList();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void addCoordinatesToCarriles(String coords){
		String pts[]=coords.split(" ");
		ArrayList<LatLng> carril=new ArrayList();
		for(int i=0;i<pts.length;i++){
			
			//Log.d("BIKELANE","Queremos recoger las coordenadas "+pts[i]);
			String co[]=pts[i].split(",");
			
			carril.add(new LatLng(Float.valueOf(co[1]),Float.valueOf(co[0])));
		}
		this.carriles.add(carril);
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public ArrayList getCarriles(){
		return this.carriles;
	}
	public int getIdLane() {
		return idLane;
	}
	public void setIdLane(int idLane) {
		this.idLane = idLane;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	
	
}

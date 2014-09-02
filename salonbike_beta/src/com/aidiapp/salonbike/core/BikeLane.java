package com.aidiapp.salonbike.core;
import java.util.ArrayList;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
public class BikeLane {
	private int idLane;
	private String name,description;
	private ArrayList<ArrayList<LatLng>> carriles;
	private int color;
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
}

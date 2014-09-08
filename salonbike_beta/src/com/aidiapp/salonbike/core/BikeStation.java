package com.aidiapp.salonbike.core;

import com.google.android.gms.maps.model.LatLng;

public class BikeStation {
	private int candados,bicicletas,estado,idStation;
	private double lat=0;
	private double lng=0;
	private LatLng ubicacion;
	private String nombre;
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public int getBicicletas() {
		return bicicletas;
	}
	public void setBicicletas(int bicicletas) {
		this.bicicletas = bicicletas;
	}
	public int getCandados() {
		return candados;
	}
	public void setCandados(int candados) {
		this.candados = candados;
	}
	public LatLng getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(LatLng ubicacion) {
		this.ubicacion = ubicacion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getIdStation() {
		return idStation;
	}
	public void setIdStation(int idStation) {
		this.idStation = idStation;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
		if(this.lng!=0)this.ubicacion=new LatLng(this.lat,this.lng);
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
		if(this.lat!=0)this.ubicacion=new LatLng(this.lat,this.lng);
	}
	

}

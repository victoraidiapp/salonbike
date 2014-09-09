package com.aidiapp.salonbike.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

public class BikeStation implements Serializable {
	public interface CalculatorListener{
		public void onResult(String r);
	}
	private int candados,bicicletas,estado,idStation;
	private double lat=0;
	private double lng=0;
	private LatLng ubicacion;
	private String nombre;
	public DistanceCalculator calculador;
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
	
	public DistanceCalculator getCalculador() {
		return calculador;
	}
	public void setCalculador(DistanceCalculator calculador) {
		this.calculador = calculador;
	}

	public class DistanceCalculator extends AsyncTask<LatLng,Integer,String> {
		private CalculatorListener listener;
		public DistanceCalculator(CalculatorListener listener) {
			// TODO Auto-generated constructor stub
			this.listener=listener;
		}
		@Override
		protected String doInBackground(LatLng... arg0) {
			// TODO Auto-generated method stub
			String r=null;
			HttpClient httpCliente=new DefaultHttpClient();
			HttpGet httpGet=new HttpGet("http://maps.googleapis.com/maps/api/directions/json?origin="+arg0[0].latitude+","+arg0[0].longitude+"&destination="+arg0[1].latitude+","+arg0[1].longitude+"&sensor=false");
			HttpResponse response;
			try {
				response = httpCliente.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				StringBuilder json = new StringBuilder();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					json.append(line);
				}
				final JSONObject json1 = new JSONObject(json.toString());
			    JSONArray routeArray = json1.getJSONArray("routes");
			    JSONObject routes = routeArray.getJSONObject(0);

			    JSONArray newTempARr = routes.getJSONArray("legs");
			    JSONObject newDisTimeOb = newTempARr.getJSONObject(0);
			    
			    JSONObject distOb = newDisTimeOb.getJSONObject("distance");
			    JSONObject timeOb = newDisTimeOb.getJSONObject("duration");
			    r=distOb.getString("text");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return r;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			this.listener.onResult(result);
			super.onPostExecute(result);
		}
	}

}

package com.aidiapp.salonbike.core;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.aidiapp.salonbike.core.BikeStation.CalculatorListener;
import com.google.android.gms.maps.model.LatLng;
public class BikeLane implements Serializable {
	private int idLane;
	private String name,description,length;
	private ArrayList<ArrayList<LatLng>> carriles;
	private int color;
	public DistanceCalculator calculator;
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
		HttpGet httpGet=new HttpGet("http://maps.googleapis.com/maps/api/directions/json?origin="+arg0[0].latitude+","+arg0[0].longitude+"&destination="+arg0[1].latitude+","+arg0[1].longitude+"&sensor=false&mode=walking");
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
	public DistanceCalculator getCalculator() {
		return calculator;
	}
	public void setCalculator(DistanceCalculator calculator) {
		this.calculator = calculator;
	}
	
	
}

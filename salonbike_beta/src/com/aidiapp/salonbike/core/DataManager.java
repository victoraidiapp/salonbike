package com.aidiapp.salonbike.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.xml.sax.InputSource;

import android.content.res.AssetManager;
import android.util.Log;

public class DataManager {
	public interface DataListener{
		public void onLanesResult(HashMap<Integer,BikeLane> result);
		public void onBikeResult(HashMap<Integer,BikeStation> result);
	}
	public static final String BIKESTATIONURI="http://www.aidiapp.com/salonbike/bikestations.xml";
	
	public static void getLanes(final DataListener dataListener,AssetManager assetManager){
		try {
			InputStream is=assetManager.open("BikeLanesZones.xml");
			InputSource inStream = new InputSource(is);
			KMLReader reader=new KMLReader(KMLReader.BIKELANE,new KMLReader.Listener() {
				
				@Override
				public void onDataResult(Object resultado) {
					// TODO Auto-generated method stub
					
					dataListener.onLanesResult((HashMap<Integer, BikeLane>) resultado);
				}
			});
			reader.execute(inStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void getBikeStations(final DataListener datalistener){
		BikeStationReader bikeStationReader=new BikeStationReader(new BikeStationReader.Listener() {
			
			@Override
			public void onDataResult(Object resultado) {
				if(resultado==null)Log.d("DATAMANAGER","El resultado es nulo");
				// TODO Auto-generated method stub
				KMLReader reader=new KMLReader(KMLReader.BIKESTATION,new KMLReader.Listener() {
					
					@Override
					public void onDataResult(Object resultado) {
						// TODO Auto-generated method stub
						datalistener.onBikeResult((HashMap<Integer, BikeStation>) resultado);
					}
				});
				Log.d("DATAMANAGER","Vamos a pasarle el resultado al reader de bikestation");
				InputStream stream = new ByteArrayInputStream(((String)resultado).getBytes());
				reader.execute(new InputSource(stream));
			}
		});
		try {
			bikeStationReader.execute(new URI(BIKESTATIONURI));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static ArrayList<HashMap<String,String>> lanesHashMaptoLanesList(HashMap<Integer,BikeLane> col){
		ArrayList<HashMap<String,String>> r=new ArrayList();
		Iterator it=col.entrySet().iterator();
		
		while(it.hasNext()){
			Entry e=(Entry) it.next();
			BikeLane bl=(BikeLane) e.getValue();
			HashMap<String,String> valores=new HashMap();
			valores.put("Nombre", bl.getName());
			valores.put("Color", String.valueOf(bl.getColor()));
			valores.put("IdLane", String.valueOf(e.getKey()));
			r.add(valores);
		}
		return r;
	}
}

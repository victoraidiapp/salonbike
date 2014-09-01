package com.aidiapp.salonbike.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.xml.sax.InputSource;

import android.content.res.AssetManager;

public class DataManager {
	public interface DataListener{
		public void onLanesResult(HashMap<String,BikeLane> result);
	}
	
	
	public static void getLanes(final DataListener dataListener,AssetManager assetManager){
		try {
			InputStream is=assetManager.open("BikeLanesZones.xml");
			InputSource inStream = new InputSource(is);
			KMLReader reader=new KMLReader(KMLReader.BIKELANE,new KMLReader.Listener() {
				
				@Override
				public void onDataResult(Object resultado) {
					// TODO Auto-generated method stub
					dataListener.onLanesResult((HashMap<String, BikeLane>) resultado);
				}
			});
			reader.execute(inStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static ArrayList<HashMap<String,String>> lanesHashMaptoLanesList(HashMap<String,BikeLane> col){
		ArrayList<HashMap<String,String>> r=new ArrayList();
		Iterator it=col.entrySet().iterator();
		
		while(it.hasNext()){
			Entry e=(Entry) it.next();
			BikeLane bl=(BikeLane) e.getValue();
			HashMap<String,String> valores=new HashMap();
			valores.put("Nombre", bl.getName());
			valores.put("Color", String.valueOf(bl.getColor()));
			r.add(valores);
		}
		return r;
	}
}

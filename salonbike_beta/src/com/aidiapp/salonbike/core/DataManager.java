package com.aidiapp.salonbike.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

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
}

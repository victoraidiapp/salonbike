package com.aidiapp.salonbike.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.os.AsyncTask;
import android.util.Log;

public class KMLReader extends AsyncTask<InputSource,Integer,HashMap> {
	public static final int BIKELANE=1;
	public static final int BIKESTATION=2;
	public interface Listener{
		public void onDataResult(Object resultado);
		
	}
	private int kmlType;
	private Listener listener;
	public KMLReader(int kmlType,Listener listener) {
		// TODO Auto-generated constructor stub
		this.kmlType=kmlType;
		this.listener=listener;
	}
	@Override
	protected HashMap doInBackground(InputSource... params) {
		// TODO Auto-generated method stub
		if(params[0]==null){
			Log.d("KMLREADER", "No recibimos inputsource");
			return null;
		}
		HashMap coleccion=null;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		   SAXParser sp;
		try {
			sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			if(this.kmlType==BIKELANE){
				coleccion=new HashMap<Integer,BikeLane>();
				KMLSaxHandler handler=new KMLSaxHandler(coleccion);
				xr.setContentHandler(handler); 
				xr.parse(params[0]);
			}else if(this.kmlType==BIKESTATION){
				params[0].setEncoding("UTF-8");
				coleccion=new HashMap<Integer,BikeStation>();
				KMLSaxHandler handler=new KMLSaxHandler(coleccion);
				xr.setContentHandler(handler); 
				xr.parse(params[0]);
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		
		return coleccion;
	}

	@Override
	protected void onPostExecute(HashMap result) {
		// TODO Auto-generated method stub
		this.listener.onDataResult(result);
		super.onPostExecute(result);
	}
	

}

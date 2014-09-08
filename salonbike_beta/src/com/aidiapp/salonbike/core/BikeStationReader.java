package com.aidiapp.salonbike.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;

import android.os.AsyncTask;
import android.util.Log;

public class BikeStationReader extends AsyncTask<URI,Integer,String> {
	public interface Listener{
		public void onDataResult(Object resultado);
		
	}
	private Listener listener;
	public BikeStationReader(Listener l) {
		// TODO Auto-generated constructor stub
		this.listener=l;
	}
	@Override
	protected String doInBackground(URI... arg0) {
		// TODO Auto-generated method stub
		String is=null;
		is=this.getData(arg0[0]);
		return is;
	}
	
	private String getData(URI u){
		String is=null;
		HttpClient httpCliente=new DefaultHttpClient();
		HttpGet httpGet=new HttpGet(u);
		try {
			HttpResponse res=httpCliente.execute(httpGet);
			HttpEntity entity=res.getEntity();
			is=EntityUtils.toString(entity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(is==null)Log.d("BIKESTATIONREADER", "No generamos inputsource");
		return is;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		
		if(result==null)Log.d("BIKESTATIONREADER","El resultado es nulo");
		
		this.listener.onDataResult(result);
		super.onPostExecute(result);
	}

}

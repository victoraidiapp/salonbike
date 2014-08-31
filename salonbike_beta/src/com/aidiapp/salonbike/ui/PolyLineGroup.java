package com.aidiapp.salonbike.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.Log;

import com.google.android.gms.internal.bl;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class PolyLineGroup {
private ArrayList<Polyline> lineas;
private PolylineOptions opciones;
private GoogleMap gmap;

private Marker marcador;
public PolyLineGroup(GoogleMap gmap,PolylineOptions opciones) {
	// TODO Auto-generated constructor stub
	this.lineas=new ArrayList();
	this.opciones=opciones;
	this.gmap=gmap;
}


public void setVisible(Boolean visible){
	Iterator it=this.lineas.iterator();
	while(it.hasNext()){
		((Polyline)it.next()).setVisible(visible);
	}
	this.marcador.setVisible(visible);
	Log.d("POLYLINE","Al carril "+this.marcador.getTitle()+" le asignamos la visibilidad "+String.valueOf(visible));
}

public void addPolyline(List<LatLng> puntos,String title,Context context){
	if(this.marcador==null){
		
		float[]h={0f,0f,0f};
		Color.colorToHSV(this.opciones.getColor(), h);
		Bitmap.Config conf = Bitmap.Config.ARGB_8888;
		Bitmap bmp = Bitmap.createBitmap(60, 90, conf);
		Canvas canvas1 = new Canvas(bmp);

		// paint defines the text color,
		// stroke width, size
		Paint color = new Paint();
		
		color.setColor(opciones.getColor());
		color.setStyle(Paint.Style.FILL_AND_STROKE);
		//modify canvas
		canvas1.drawCircle(30, 30, 30, color);
		Path path = new Path();
		path.moveTo(15, 50);
		path.lineTo(30, 90);
		path.lineTo(45, 50);
		path.lineTo(15, 50);
		path.close();
		canvas1.drawPath(path, color);
		color.setTextSize(45);
		color.setTypeface(Typeface.createFromAsset(context.getApplicationContext()
                .getAssets(), "fonts/vitor.otf"));
		color.setColor(Color.WHITE);
		canvas1.drawText("i", 22, 45, color);
		if(this.gmap==null)Log.d("POLYLINE","El mapa es nulo");
		this.marcador=this.gmap.addMarker(new MarkerOptions().position(puntos.get(0)));
		this.marcador.setIcon(BitmapDescriptorFactory.fromBitmap(bmp));
		this.marcador.setTitle(title);
		
		
	}
	Polyline poli=this.gmap.addPolyline(this.opciones);
	poli.setPoints(puntos);
	poli.setVisible(false);
	this.marcador.setVisible(false);
	this.lineas.add(poli);
	
}

}

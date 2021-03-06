package com.aidiapp.salonbike.core;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



import android.graphics.Color;
import android.util.Log;



public class KMLSaxHandler extends DefaultHandler {
	private Boolean in_laneszonetag=false;
	private Boolean in_nametag=false;
	private Boolean in_descriptiontag=false;
	private Boolean in_placemarktag=false;
	private Boolean in_paradatag=false;
	private Boolean in_candadostag=false;
	private Boolean in_bicicletastag=false;
	private Boolean in_estadotag=false;
	private Boolean in_lattag=false;
	private Boolean in_lngtag=false;	
	private Boolean in_linestringtag=false;
	private Boolean in_coordinatestag=false;
	private Boolean in_colortag=false;
	private Boolean in_lengthtag=false;
	private HashMap coleccion;
	private StringBuilder buffer;
	private Object currentElement;
	private int count;
	
	public KMLSaxHandler(HashMap col) {
		// TODO Auto-generated constructor stub
		this.coleccion=col;
	}
	
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		this.count=1;
		super.startDocument();
	}
	
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		this.count=0;
		super.endDocument();
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
	//	Log.d("SAXHANDLER", "Queremos abrir la etiqueta "+localName);
	      if (localName.equals("LanesZone")) { 
	           this.in_laneszonetag = true;
	           this.currentElement=new BikeLane();
	      } else if (localName.equals("Placemark")) { 
	           this.in_placemarktag = true; 
	           
	           
	      } else if (localName.equals("name")) { 
	           this.in_nametag = true;
	      } else if (localName.equals("description")) { 
	          this.in_descriptiontag = true;
	      } else if (localName.equals("LineString")) { 
	          this.in_linestringtag = true;                      
	      } else if (localName.equals("coordinates")) {
	          buffer = new StringBuilder();
	          this.in_coordinatestag = true;                        
	      } else if(localName.equals("color")){
	    	  this.in_colortag=true;
	      } else if( localName.equals("length")){
	    	  
	    	  this.in_lengthtag=true;
	      } else if( localName.equals("parada")){
	    	  this.currentElement=new BikeStation();
	    	//  Log.d("SAXHANDLER","Creamos una nueva BikeStation");
	    	  ((BikeStation)this.currentElement).setNombre(attributes.getValue("nombre"));
	    	  ((BikeStation)this.currentElement).setBicicletas(Integer.valueOf(attributes.getValue("bicicletas")));
	    	  ((BikeStation)this.currentElement).setCandados(Integer.valueOf(attributes.getValue("candadosLibres")));
	    	  ((BikeStation)this.currentElement).setEstado(Integer.valueOf(attributes.getValue("estado")));
	    	  ((BikeStation)this.currentElement).setLat(Double.valueOf(attributes.getValue("lat")));
	    	  ((BikeStation)this.currentElement).setLng(Double.valueOf(attributes.getValue("lng")));
	    	  this.in_paradatag=true;
	      }
		super.startElement(uri, localName, qName, attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		 //Log.d("SAXHANDLER", "Queremos cerrar la etiqueta "+localName);
	       if (localName.equals("LanesZone")) {
	           this.in_laneszonetag = false; 
	           if(this.currentElement instanceof BikeLane)
	        	//   Log.d("SAXHANDLER","añadimos el carril "+((BikeLane)this.currentElement).getIdLane()+" que es el de "+((BikeLane)this.currentElement).getName());
	        	   this.coleccion.put(((BikeLane)this.currentElement).getIdLane(), this.currentElement);
	        	   
	           this.currentElement=null;
	           this.count++;
	       } else if (localName.equals("Placemark")) { 
	           this.in_placemarktag = false;

	        

	       } else if (localName.equals("name")) { 
	           this.in_nametag = false;           
	       } else if (localName.equals("description")) { 
	           this.in_descriptiontag = false;
	       
	       } else if (localName.equals("LineString")) { 
	           this.in_linestringtag = false;              
	               
	       } else if (localName.equals("coordinates")) { 
	    	   if(this.currentElement instanceof BikeLane)
		        	((BikeLane)currentElement).addCoordinatesToCarriles(this.buffer.toString().trim());
	           this.in_coordinatestag = false;
	           
	           
	       }else if(localName.equals("color")){
		    	  this.in_colortag=false;
		      }else if( localName.equals("length")){
		    	  this.in_lengthtag=false;
		      }
		      else if( localName.equals("parada")){
		    	  ((BikeStation)this.currentElement).setIdStation(count);
		    	  this.coleccion.put(((BikeStation)this.currentElement).getIdStation(), this.currentElement);
		    	  this.currentElement=null;
		    	  this.count++;
		    	  this.in_paradatag=false;
		      } 
	       
		super.endElement(uri, localName, qName);
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		
		 if(this.in_nametag){ 
		        if(this.currentElement instanceof BikeLane && !this.in_placemarktag){
		        	((BikeLane)currentElement).setName(new String(ch, start, length));
		    //    Log.d("SAXHANDLER","Estamos creando el lane "+this.count+" con el nombre "+((BikeLane)currentElement).getName());
		        ((BikeLane)currentElement).setIdLane(this.count);
		        }else if(this.currentElement instanceof BikeStation){
		        	((BikeStation)currentElement).setIdStation(count);
			    //	((BikeStation)currentElement).setNombre(new String(ch, start, length));
			  //  	Log.d("SAXHANDLER","Estamos creando el lane "+this.count+" con el nombre "+((BikeStation)currentElement).getNombre());
		        }
		                  
		    } else if(this.in_descriptiontag){
		    	if(this.currentElement instanceof BikeLane)
		        	((BikeLane)currentElement).setDescription(new String(ch, start, length));
		    } else if(this.in_colortag){
		    	if(this.currentElement instanceof BikeLane)
		        	((BikeLane)currentElement).setColor(Color.parseColor(new String(ch,start,length)));
		    }else if(this.in_lengthtag){
		    	if(this.currentElement instanceof BikeLane)
		    	((BikeLane)currentElement).setLength(new String(ch, start, length));
		    }else if(this.in_coordinatestag){  
		    	
		        
		        buffer.append(new String(ch,start,length));
		    }
	} 
}

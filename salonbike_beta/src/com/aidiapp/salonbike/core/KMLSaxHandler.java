package com.aidiapp.salonbike.core;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



import android.util.Log;



public class KMLSaxHandler extends DefaultHandler {
	private Boolean in_laneszonetag=false;
	private Boolean in_nametag=false;
	private Boolean in_descriptiontag=false;
	private Boolean in_placemarktag=false;
	private Boolean in_linestringtag=false;
	private Boolean in_coordinatestag=false;
	private HashMap coleccion;
	private StringBuilder buffer;
	private Object currentElement;
	
	public KMLSaxHandler(HashMap<String,BikeLane> carriles) {
		// TODO Auto-generated constructor stub
		this.coleccion=carriles;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		 Log.d("SAXHANDLER", "Queremos abrir la etiqueta "+localName);
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
	      }
		super.startElement(uri, localName, qName, attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		 Log.d("SAXHANDLER", "Queremos cerrar la etiqueta "+localName);
	       if (localName.equals("LanesZone")) {
	           this.in_laneszonetag = false; 
	           if(this.currentElement instanceof BikeLane)
	        	   this.coleccion.put(((BikeLane)this.currentElement).getName(), this.currentElement);
	           this.currentElement=null;
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
	           
	           
	       }
		super.endElement(uri, localName, qName);
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		
		 if(this.in_nametag){ 
		        if(this.currentElement instanceof BikeLane)
		        	((BikeLane)currentElement).setName(new String(ch, start, length));
		        
		                  
		    } else if(this.in_descriptiontag){
		    	if(this.currentElement instanceof BikeLane)
		        	((BikeLane)currentElement).setDescription(new String(ch, start, length));
		    } else
		    
		    if(this.in_coordinatestag){  
		    	
		        
		        buffer.append(new String(ch,start,length));
		    }
	} 
}

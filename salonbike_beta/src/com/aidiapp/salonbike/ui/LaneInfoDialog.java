package com.aidiapp.salonbike.ui;

import com.aidiapp.salonbike.R;
import com.aidiapp.salonbike.core.BikeLane;
import com.aidiapp.salonbike.core.BikeStation;
import com.aidiapp.salonbike.core.BikeStation.CalculatorListener;
import com.aidiapp.salonbike.core.BikeStation.DistanceCalculator;
import com.aidiapp.salonbike.ui.utils.TypefaceSpan;
import com.google.android.gms.maps.model.LatLng;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LaneInfoDialog extends DialogFragment implements OnClickListener, CalculatorListener {
	public static final String CARRIL="com.aidiapp.salonbike.carril";
	public static final String DISTANCIA_CARRIL="com.aidiapp.salonbike.distanciacarril";
public interface Listener{
	public void onInitRouteToLane(Integer l);
}
	private BikeLane bl;	
	private String distancia;
private Dialog d;
private LatLng current;
private Listener listener;
private ProgressBar loadingDistance;
private TextView tvDistancia;
	public void setBikeLane(BikeLane bikeLane) {
		// TODO Auto-generated method stub
this.bl=bikeLane;

	}
	/*
@Override
public View onCreateView(LayoutInflater inflater,
		@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	View v=inflater.inflate(R.layout.laneinfodialog, container);
	return v;
}
 */
 @Override
@NonNull
public Dialog onCreateDialog(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
View v=inflater.inflate(R.layout.laneinfodialog, null);
if(savedInstanceState!=null){
	this.bl=(BikeLane) savedInstanceState.getSerializable(CARRIL);
}else{
	Log.d("BIKESTATIONDIALOG","Iniciamos el calculo de la distancia");
	this.bl.calculator= this.bl.new DistanceCalculator(this);
	if(this.current!=null){
	Location loc=new Location("");
	loc.setLatitude(this.current.latitude);
	loc.setLongitude(this.current.longitude);
	this.bl.calculator.execute(this.getCurrent(),BikeLane.getNearestLanePosition(loc, this.bl.getCarriles()));
	}
}
SpannableString s = new SpannableString(bl.getName());
s.setSpan(new TypefaceSpan(this.getActivity(), "vitor.otf"), 0, s.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
((TextView)v.findViewById(R.id.station_title)).setText(s);
((TextView)v.findViewById(R.id.distanceToStation)).setText(bl.getLength());
this.tvDistancia=(TextView) v.findViewById(R.id.distanceToLane);
this.loadingDistance=(ProgressBar) v.findViewById(R.id.loadingDistance);
((LinearLayout)v.findViewById(R.id.title_station_Dialog)).setBackgroundColor(bl.getColor());
	    builder.setView(v);
	    builder.setPositiveButton(R.string.dialog_route, this);
	    builder.setNegativeButton(R.string.button_back, this);
	    this.d=builder.create();
	    if(savedInstanceState!=null){
	    	this.distancia=savedInstanceState.getString(DISTANCIA_CARRIL);
	    	this.updateDistancia();
	    }
	    return d;
}

 @Override
 public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     //setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
 }
@Override
public void onClick(DialogInterface arg0, int arg1) {
	// TODO Auto-generated method stub
	Log.d("LANEINFODIALOG","Has picado en "+arg1);
	if(arg1==-1){
	this.listener.onInitRouteToLane(bl.getIdLane());
	}
}
public Listener getListener() {
	return listener;
}
public void setListener(Listener listener) {
	this.listener = listener;
}
 
@Override
	public void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
	arg0.putSerializable(CARRIL, this.bl);
	arg0.putString(DISTANCIA_CARRIL, this.distancia);
		super.onSaveInstanceState(arg0);
	}
@Override
public void onResult(String r) {
	// TODO Auto-generated method stub
	this.distancia=r;
	this.updateDistancia();
}
private void updateDistancia() {
	// TODO Auto-generated method stub
	this.tvDistancia.setText(this.distancia);
	this.tvDistancia.setVisibility(View.VISIBLE);
	this.loadingDistance.setVisibility(View.GONE);
}
public LatLng getCurrent() {
	return current;
}
public void setCurrent(LatLng current) {
	this.current = current;
}

}

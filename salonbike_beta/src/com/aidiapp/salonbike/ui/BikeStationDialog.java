package com.aidiapp.salonbike.ui;

import com.aidiapp.salonbike.R;
import com.aidiapp.salonbike.core.BikeLane;
import com.aidiapp.salonbike.core.BikeStation;
import com.aidiapp.salonbike.ui.utils.TypefaceSpan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BikeStationDialog extends DialogFragment implements OnClickListener{
	public static final String STATION="com.aidiapp.salonbike.station";
	public interface Listener{
		public void onInitRouteToStation(Integer l);
	}
	private BikeStation bikeStation;
	private Listener listener;
	private Dialog dialog;
	public BikeStation getBikeStation() {
		return bikeStation;
	}
	public void setBikeStation(BikeStation bikeStation) {
		this.bikeStation = bikeStation;
	}
	public Listener getListener() {
		return listener;
	}
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
View v=inflater.inflate(R.layout.bikestationdialog, null);
if(savedInstanceState!=null){
	this.bikeStation=(BikeStation) savedInstanceState.getSerializable(STATION);
}
SpannableString s = new SpannableString(this.bikeStation.getNombre());
s.setSpan(new TypefaceSpan(this.getActivity(), "vitor.otf"), 0, s.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
((TextView)v.findViewById(R.id.station_title)).setText(s);

((TextView)v.findViewById(R.id.candadosValue)).setText(String.valueOf(this.bikeStation.getCandados()));
((TextView)v.findViewById(R.id.bikesValue)).setText(String.valueOf(this.bikeStation.getBicicletas()));
	    builder.setView(v);
	    builder.setPositiveButton(R.string.dialog_route, this);
	    builder.setNegativeButton(R.string.button_back, this);
	    this.dialog=builder.create();
	    
	    return dialog;
	}
	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
	arg0.putSerializable(STATION, this.bikeStation);
		super.onSaveInstanceState(arg0);
	}
}

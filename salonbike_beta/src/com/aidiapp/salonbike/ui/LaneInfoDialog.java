package com.aidiapp.salonbike.ui;

import com.aidiapp.salonbike.R;
import com.aidiapp.salonbike.core.BikeLane;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LaneInfoDialog extends DialogFragment implements OnClickListener {
private BikeLane bl;	
private Dialog d;
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
((TextView)v.findViewById(R.id.station_title)).setText(bl.getName());
((LinearLayout)v.findViewById(R.id.title_station_Dialog)).setBackgroundColor(bl.getColor());
	    builder.setView(v);
	    builder.setPositiveButton(R.string.dialog_route, this);
	    builder.setNegativeButton(R.string.button_back, this);
	    this.d=builder.create();
	    
	    return d;
}

 @Override
 public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
 }
@Override
public void onClick(DialogInterface arg0, int arg1) {
	// TODO Auto-generated method stub
	Log.d("LANEINFODIALOG","Has picado en "+arg1);
	
}
 
}

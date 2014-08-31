package com.aidiapp.salonbike.ui;

import com.aidiapp.salonbike.R;
import com.aidiapp.salonbike.core.BikeLane;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LaneInfoDialog extends DialogFragment {

	public void setBikeLane(BikeLane bikeLane) {
		// TODO Auto-generated method stub
		
	}
 @Override
public View onCreateView(LayoutInflater inflater,
		@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	 View r=inflater.inflate(R.layout.laneinfodialog, container);
	return r;
}
}

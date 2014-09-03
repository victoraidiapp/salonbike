package com.aidiapp.salonbike.ui;






import java.util.HashMap;

import com.aidiapp.salonbike.R;
import com.aidiapp.salonbike.core.BikeLane;
import com.aidiapp.salonbike.core.DataManager;
import com.aidiapp.salonbike.ui.LanesPage.Listener;
import com.aidiapp.salonbike.ui.MapManager.ActivityListener;
import com.aidiapp.salonbike.ui.utils.TypefaceSpan;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;


import android.app.Activity;
import android.graphics.drawable.Drawable;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


public class MainUI extends ActionBarActivity implements ActivityListener, Listener {
	public final static String BIKESTATIONLAYER_STATE="com.aidiapp.salonbike.bikestationlayerstate";
	public final static String BIKELANELAYER_STATE="com.aidiapp.salonbike.bikelanelayerstate";
	public final static String CURRENTFRAGMENT="com.aidiapp.salonbike.currentfragment";
	private MapManager mapMngr;
	private LanesPage lanesPage;
	private LinearLayout llLoadingIndicator;
	private FrameLayout flContenedorFragment;
	private FragmentManager gestorFragment;
	private String currentFragmentTag;
	private Menu m;
	private Integer selectLane=null;
	private boolean flagBikeStationLayer,flagBikeLaneLayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_ui);
		/*REFERENCIAMOS EL loadingIndicator*/
		this.llLoadingIndicator=(LinearLayout) this.findViewById(R.id.loadingIndicator);
		/*REFERENCIAMOS EL CONTENEDOR DE FRAGMENTS*/
		this.flContenedorFragment=(FrameLayout) this.findViewById(R.id.contenedorFragmnt);
		/*CAMBIAMOS LA TIPOGRAFÍA DEL TÍTULO DE LA ACTION BAR*/
		SpannableString s = new SpannableString("SalOnBike");
	    s.setSpan(new TypefaceSpan(this, "vitor.otf"), 0, s.length(),
	            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	 
	    // Update the action bar title with the TypefaceSpan instance
	    ActionBar actionBar = this.getSupportActionBar();
	    actionBar.setTitle(s);
       
		this.gestorFragment=this.getSupportFragmentManager();
		/*DECLARAMOS EL MAP FRAGMENT*/
		GoogleMapOptions mapaOpts=new GoogleMapOptions();
		LatLng sal=new LatLng(40.9705347,-5.6637995);
		CameraPosition camera=new CameraPosition(sal,12.5f,0,0);
		mapaOpts.camera(camera);
		this.mapMngr=MapManager.newInstance(mapaOpts,this);
		this.mapMngr.setActivityListener(this);
		//showLanesPage();
		Log.d("MAINUI","Comprobamos el estado");
		if(savedInstanceState!=null){
			this.flagBikeLaneLayer=savedInstanceState.getBoolean(BIKELANELAYER_STATE);
			this.flagBikeStationLayer=savedInstanceState.getBoolean(BIKESTATIONLAYER_STATE);
			if(savedInstanceState.get(CURRENTFRAGMENT).equals("MapManager")){
				//this.showMapManager();
				
				this.lanesPage=new LanesPage(this);
				showMapManager();
			}else if(savedInstanceState.get(CURRENTFRAGMENT).equals("LanesPage")){
				
				//this.gestorFragment.executePendingTransactions();
				this.onLoadingMap(false);
				this.lanesPage=(LanesPage) this.gestorFragment.findFragmentByTag("LanesPage");
				this.lanesPage.setListener(this);
				this.showLanesPage();
				//this.showLanesPage();
			}
			
		}else{
			
			
			
			/*DECLARAMOS EL LANESPAGE*/
			this.lanesPage=new LanesPage(this);
			showMapManager();
		}
	}
	private void showMapManager() {
		this.currentFragmentTag="MapManager";
		Fragment mapFragment=this.gestorFragment.findFragmentByTag(this.currentFragmentTag);
		if(mapFragment !=null && mapFragment.isVisible()){
		}else{
			this.onLoadingMap(true);
		FragmentTransaction transicion=gestorFragment.beginTransaction();
		transicion.replace(R.id.contenedorFragmnt, this.mapMngr,this.currentFragmentTag);
		transicion.commit();
		
		}
	}
	private void showLanesPage(){
		this.currentFragmentTag="LanesPage";
		FragmentTransaction transicion=gestorFragment.beginTransaction();
		transicion.replace(R.id.contenedorFragmnt, this.lanesPage,this.currentFragmentTag);
		transicion.commit();
	}
@Override
protected void onSaveInstanceState(Bundle outState) {
	// TODO Auto-generated method stub
	outState.putBoolean(MainUI.BIKELANELAYER_STATE, this.flagBikeLaneLayer);
	outState.putBoolean(MainUI.BIKESTATIONLAYER_STATE, this.flagBikeStationLayer);
	outState.putString(MainUI.CURRENTFRAGMENT, this.currentFragmentTag);
	super.onSaveInstanceState(outState);
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		if(this.currentFragmentTag.equals("LanesPage")){
			Log.d("MAINUI","Generamos el menu del lane page");
			
			getMenuInflater().inflate(R.menu.lanepagemenu, menu);
			
		}
		if(this.currentFragmentTag.equals("MapManager")){
			Log.d("MAINUI","Generamos el menu del mapmanager");
			this.flagBikeLaneLayer=!this.flagBikeLaneLayer;
			this.flagBikeStationLayer=!this.flagBikeStationLayer;
			getMenuInflater().inflate(R.menu.mapmanagermenu, menu);
			MenuItem item=menu.findItem(R.id.btnShowBikeLanesLayer);
			this.checkBikeLaneLayer(item);
			item=menu.findItem(R.id.btnShowBikeStationLayer);
			this.checkBikeStationLayer(item);
			this.m=menu;
		}
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if(id== R.id.btnShowBikeLanesList){
			this.showLanesPage();
			this.supportInvalidateOptionsMenu();
		}
		if(id== R.id.btnShowMapManager){
			this.showMapManager();
			this.supportInvalidateOptionsMenu();
		}
		if (id == R.id.btnShowBikeLanesLayer) {
			//MOSTRAMOS LA CAPA DE CARRILES BICI
			checkBikeLaneLayer(item);
			
			return true;
		}
		
		if (id == R.id.btnShowBikeStationLayer) {
			//MOSTRAMOS LA CAPA DE INTERCAMBIADORES
			checkBikeStationLayer(item);
			
			return true;
		}
		if(id==R.id.btnShowBikeLanesNearest){
			
			this.mapMngr.showNearestLane();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	private void checkBikeLaneLayer(MenuItem item) {
		if(this.flagBikeLaneLayer){
			item.setTitle(this.getResources().getText(R.string.show_bikes_lanes));
			item.setChecked(false);
				this.mapMngr.hideBikeLanesLayer();
				this.flagBikeLaneLayer=false;
		}else{
			item.setTitle(this.getResources().getText(R.string.hide_bikes_lanes));
			
			item.setChecked(true);
			this.mapMngr.showLaneLayer();
			this.flagBikeLaneLayer=true;
		}
	}

	private void checkBikeStationLayer(MenuItem item) {
		if(this.flagBikeStationLayer){
			item.setTitle(this.getResources().getText(R.string.show_bikes_stations));
		item.setChecked(false);
			this.mapMngr.hideBikeStationsLayer();
			this.flagBikeStationLayer=false;
		}else{
			item.setTitle(this.getResources().getText(R.string.hide_bikes_stations));
				
			item.setChecked(true);
			this.mapMngr.showBikeStationsLayer();
			this.flagBikeStationLayer=true;
		}
	}
	private void loading(Boolean estado){
		Log.d("MAINUI","LLaman al loading");
		if(estado){
			this.llLoadingIndicator.setVisibility(View.VISIBLE);
		}else{
			this.llLoadingIndicator.setVisibility(View.GONE);
		}
	}
	@Override
	public void onLoadingContent(Boolean estado) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onLoadingMap(Boolean estado) {
		// TODO Auto-generated method stub
		Log.d("MAINUI","LLaman al loadingMap");
		if(estado){
			this.llLoadingIndicator.setVisibility(View.VISIBLE);
			
		}else{
			this.llLoadingIndicator.setVisibility(View.GONE);
			if(this.selectLane!=null){
				if(!this.flagBikeLaneLayer)this.flagBikeLaneLayer=true;
				this.supportInvalidateOptionsMenu();
				this.mapMngr.showBikeLaneInfoDialog(this.selectLane);
				
				this.selectLane=null;
			}
			
		}
	}
	@Override
	public void onLoadLanesResult(HashMap<Integer, BikeLane> col) {
		// TODO Auto-generated method stub
		this.lanesPage.setLanes(DataManager.lanesHashMaptoLanesList(col));
	}
	@Override
	public void onSelectLane(Integer l) {
		// TODO Auto-generated method stub
		
		
		
		
		
		this.selectLane=l;
		this.showMapManager();
		this.supportInvalidateOptionsMenu();
		
		
		
		
	}
	
	
}

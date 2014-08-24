package com.aidiapp.salonbike.ui;






import com.aidiapp.salonbike.R;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainUI extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private MapManager mapMngr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_ui);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
               //getSupportActionBar().setTitle("Cerrado");
               supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Abierto");
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		/*DECLARAMOS EL MAP FRAGMENT*/
		GoogleMapOptions mapaOpts=new GoogleMapOptions();
		LatLng sal=new LatLng(40.9705347,-5.6637995);
		CameraPosition camera=new CameraPosition(sal,12.5f,0,0);
		mapaOpts.camera(camera);
		this.mapMngr=MapManager.newInstance(mapaOpts);
		
		FragmentManager gestorFragments=this.getSupportFragmentManager();
		FragmentTransaction transicion=gestorFragments.beginTransaction();
		transicion.add(R.id.contenedorFragmnt, this.mapMngr);
		transicion.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapmanagermenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		if (id == R.id.btnShowBikeLanesLayer) {
			//MOSTRAMOS LA CAPA DE CARRILES BICI
			return true;
		}
		
		if (id == R.id.btnShowBikeStationLayer) {
			//MOSTRAMOS LA CAPA DE INTERCAMBIADORES
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

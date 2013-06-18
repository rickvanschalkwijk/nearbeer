package com.supremecodemonkeys.nearbeer;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.supremecodemonkeys.core.Gps;
import com.supremecodemonkeys.core.IGPSActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MapActivity extends Activity implements IGPSActivity {
	
	private GoogleMap mMap;
	private Gps gps;
	public LatLng latlong;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		setupActionBar();
		mMap = ( (MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		if(mMap != null){
			mMap.setMyLocationEnabled(true);
	
		}
		gps = new Gps(this);
	}
	

	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		gps.resumeGPS();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		gps.stopGPS();
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
		gps.resumeGPS();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		gps.stopGPS();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}


	@Override
	public void locationChanged(double longitude, double latitude) {
		latlong = new LatLng(latitude, longitude);
		mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong));
		mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	
	}

}

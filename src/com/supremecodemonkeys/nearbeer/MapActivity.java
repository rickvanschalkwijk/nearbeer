package com.supremecodemonkeys.nearbeer;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.supremecodemonkeys.core.Gps;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

public class MapActivity extends Activity {
	
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
	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		gps.stopUsingGPS();
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();

	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		gps.stopUsingGPS();
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

}

package com.supremecodemonkeys.nearbeer;

import java.util.List;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.supremecodemonkeys.core.Gps;
import com.supremecodemonkeys.models.Place;
import com.supremecodemonkeys.models.PlacesList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MapActivity extends Activity {
	
	private GoogleMap mMap;
	private Gps gps;
	public LatLng latlong;
	PlacesList nearPlaces;
	List<Overlay> mapOverlays;
	GeoPoint geoPoint;
	MapController mc;
	double latitude;
	double longitude;
	OverlayItem overlayItem;
	public Marker mMarkers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		setupActionBar();
		gps = new Gps(this);
		Intent i = getIntent();
		//String user_latString
		
		nearPlaces = (PlacesList) i.getSerializableExtra("places");
		Log.d("Places"," " + nearPlaces);
		mMap = ( (MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.clear();
		if(nearPlaces.results != null){
			for(Place place : nearPlaces.results){
				latitude = place.geometry.location.lat;
				longitude = place.geometry.location.lng;
				LatLng latLng = new LatLng(latitude, longitude);
				mMap.addMarker(new MarkerOptions().
						position(latLng).
						title(place.name).
						
						icon(BitmapDescriptorFactory.fromResource(R.drawable.beer_marker1)));
			}
		}
		
		if(mMap != null){
			mMap.setMyLocationEnabled(true);
			mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(gps.getLatitude(), gps.getLongitude())));
			mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
			mMap.getUiSettings().setZoomControlsEnabled(true);
		}
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

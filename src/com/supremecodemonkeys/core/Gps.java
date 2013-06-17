package com.supremecodemonkeys.core;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.LocationListener;

public class Gps {
	private IGPSActivity main;
	private TheLocationListener mlocListener;
	private LocationManager mlocManager;
	
	private boolean isRunning;
	
	public Gps(IGPSActivity main){
		this.main = main;
		mlocManager = (LocationManager) ((Activity) this.main).getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new TheLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		this.isRunning = true;
	}
	
	public void stopGPS(){
		if(isRunning){
			mlocManager.removeUpdates( mlocListener);
			this.isRunning = false;
		}
	}
	
	public void resumeGPS(){
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		this.isRunning = true;
	}
	
	public boolean isRunning(){
		return this.isRunning;
	}
	
	public class TheLocationListener implements LocationListener, android.location.LocationListener{
		
		@Override
		public void onLocationChanged(Location loc) {
			Gps.this.main.locationChanged(loc.getLongitude(), loc.getLatitude());
		}
		
		@Override
		public void onProviderDisabled(String provider){
			
		}
		
		public void onProviderEnabled(String privder){
			
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras){
			
		}
	}
}
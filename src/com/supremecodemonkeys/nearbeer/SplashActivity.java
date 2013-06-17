package com.supremecodemonkeys.nearbeer;

import android.location.LocationManager;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MotionEvent;

@SuppressLint({ "ValidFragment", "NewApi" })
public class SplashActivity extends Activity {

	protected int _splashTime = 500;
	private Thread splashThread;
	private static SplashActivity selfReferance = null;
	final Context context = this;
	private String locationProvider = LocationManager.GPS_PROVIDER;
	//final Class<SplashActivity> SplashActivity = SplashActivity.class;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		splashThread = new Thread() {
			@Override
			public void run() {
				isGpsOn();
			}
		};
		splashThread.start();
	}
	
	public void onResume(){
		super.onResume();
		isGpsOn();
	}
	
	protected void onStart () {
		super.onStart();
	}

	private void isGpsOn(){
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = locationManager
				.isProviderEnabled(locationProvider);
		if (!gpsEnabled) {
			turnGPSOn(locationManager);
		}else{
			Intent intent = new Intent(SplashActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	private void turnGPSOn(LocationManager locationManager) {
		  //String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		    boolean gpsEnabled = false;
		    try{
				gpsEnabled = locationManager.isProviderEnabled(locationProvider);
			    if (!gpsEnabled){
			    	new EnableGpsDialogFragment().show(getFragmentManager(), "enableGpsDialog");
			    }
		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    	// Gotha catch them all 
		    	// TODO: handle this (!)
		    	
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
				alertDialogBuilder.setTitle("GPS Usage");
				alertDialogBuilder
						.setMessage("Please enable GPS before using maps.")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
		    }
		
	}
	
	private class EnableGpsDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setTitle(R.string.enable_gps)
					.setMessage(R.string.enable_gps_dialog)
					.setPositiveButton(R.string.enable_gps,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
								}
							}).create();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (splashThread) {
				// splashThread.notifyAll();
			}
		}
		return true;
	}

	public static Context getContext() {
		if (selfReferance != null) {
			return selfReferance.getApplicationContext();
		}
		return null;
	}
}

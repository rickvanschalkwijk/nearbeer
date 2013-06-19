package com.supremecodemonkeys.nearbeer;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.SetMultimap;
import com.supremecodemonkeys.core.GooglePlaces;
import com.supremecodemonkeys.core.Gps;
import com.supremecodemonkeys.models.Place;
import com.supremecodemonkeys.models.PlacesList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BarNearActivity extends Activity {
	
	public Gps gps;
	Button btnShowOnMap;
	ProgressDialog pDialog;
	AlertDialog alertDialog;
	ListView lv;
	PlacesList nearPlaces;
	ArrayList<HashMap<String, String>> placeListItems = new ArrayList<HashMap<String,String>>();
	GooglePlaces googlePlaces;
	public String BeerRatingIcon;
	public static String KEY_REFERENCE 	= "reference";
	public static String KEY_NAME		= "name";
 	public static String KEY_VICINITY	= "vicinity";
	public static String KEY_ICON		= "icon";
	
	int[] icons = new int[]{
			R.drawable.beer_point_five,
			R.drawable.beer_point_nine,
			R.drawable.beer_point_three
	};
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bar_near);
		
		gps = new Gps(this);
		lv = (ListView) findViewById(R.id.list);
		btnShowOnMap = (Button) findViewById(R.id.btn_show_map);
		new LoadPlaces().execute();
		
		btnShowOnMap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mapIntent = new Intent(BarNearActivity.this, MapActivity.class);
				mapIntent.putExtra("user_lat", Double.toString(gps.getLatitude()));
				mapIntent.putExtra("user_lng", Double.toString(gps.getLongitude()));
				mapIntent.putExtra("places", nearPlaces);
				startActivity(mapIntent);
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				TextView c = (TextView) view.findViewById(R.id.reference);
				String reference = c.getText().toString();
				Intent detailIntent = new Intent(BarNearActivity.this, SinglePlaceActivity.class);
				detailIntent.putExtra(KEY_REFERENCE, reference);
				startActivity(detailIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bar_near, menu);
		return true;
	}
	
	class LoadPlaces extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(BarNearActivity.this);
			pDialog.setMessage(Html.fromHtml("<b>Search</b><br />Loading beer... Please wait!"));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... args) {
			googlePlaces = new GooglePlaces();
			try {
				String types = "cafe|restaurant|bar|liquor_store|night_club|grocery_or_supermarket";
				double radius = 1000;
				nearPlaces = googlePlaces.search(gps.getLatitude(), gps.getLongitude(), radius, types);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String file_url){
			pDialog.dismiss();
			alertDialog = new AlertDialog.Builder(BarNearActivity.this).create();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String status = nearPlaces.status;
					if(status.equals("OK")){
						if(nearPlaces.results != null){
							for(Place p : nearPlaces.results){
								for(int i = 0; i < p.types.length; i++ ){
									if(p.types[i].equals("bar") || p.types[i].equals("cafe") 
											|| p.types[i].equals("liquor_store") || p.types[i].equals("grocery_or_supermarket")){
										BeerRatingIcon = Integer.toString(R.drawable.beer_point_nine);
										break;
									}else if(p.types[i].equals("restaurant")){
										BeerRatingIcon = Integer.toString(R.drawable.beer_point_five);
										break;
									}else{
										BeerRatingIcon = Integer.toString(R.drawable.beer_point_three);
									}
								}
								HashMap<String, String> map = new HashMap<String, String>();
								map.put(KEY_REFERENCE, p.reference);
								map.put(KEY_NAME, p.name);
								map.put(KEY_ICON, BeerRatingIcon);
								placeListItems.add(map);
							}
							
							ListAdapter adapter = new SimpleAdapter(BarNearActivity.this, placeListItems, 
																R.layout.list_item, new String[]{KEY_REFERENCE, KEY_NAME, KEY_ICON}, 
																new int[]{R.id.reference, R.id.name, R.id.icon});
							lv.setAdapter(adapter);
						}
					}else if(status.equals("ZERO_RESULTS")){
							alertDialog.setTitle("");
							alertDialog.setMessage("");
							alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									}
							});
							alertDialog.show();
					}else if(status.equals("REQUEST_DENIED")){
						Log.d("Places request","DENIED");
					}else if(status.equals("INVALID_REQUEST")){
						Log.d("Places request","INVALID REQUEST");
					}else{
						Log.d("Places request","UNKNOW ERROR");
					}
				}
			});
		}
	}
}
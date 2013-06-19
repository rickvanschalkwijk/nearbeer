package com.supremecodemonkeys.core;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


public class AddItemizedOverlay  extends ItemizedOverlay<OverlayItem>{

	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
	private Context context;
	
	public AddItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public AddItemizedOverlay(Drawable defaultMarker, Context context) {
		this(defaultMarker);
		this.context = context;
	}
	
	@Override
	public boolean onTap(int index) {
		OverlayItem item = mapOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {	
			}
		});
		dialog.show();
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		if(event.getAction() == 1){
			GeoPoint geopoint = mapView.getProjection().fromPixels(
					(int) event.getX(),
					(int) event.getY());
			double lat = geopoint.getLatitudeE6() / 1E6;
			double lng = geopoint.getLongitudeE6() / 1E6;
		}
		return false;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mapOverlays.get(i);
	}

	@Override
	public int size() {
		return mapOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay){
		mapOverlays.add(overlay);
	}
	
	public void populateNow(){
		this.populate();
	}
}

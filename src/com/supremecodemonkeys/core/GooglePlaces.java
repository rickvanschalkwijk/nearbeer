package com.supremecodemonkeys.core;

import java.io.IOException;

import org.apache.http.client.HttpResponseException;

import android.util.Log;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.supremecodemonkeys.models.PlacesList;

public class GooglePlaces {
	
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final String API_KEY = "AIzaSyAUwkYDs4k2x96Mft5A-T_3ATBxK1ehYUc";
	private static final String PLACES_SEARCH_URL		= "https://maps.googleapis.com/maps/api/place/search/json?";
	private static final String PLACES_TEXT_SEARCH_URL 	= "https://maps.googleapis.com/maps/api/place/search/json?";
	private static final String PLACES_DETAILS_URL 		= "https://maps.googleapis.com/maps/api/place/details/json?";
	
	private double _latitudel;
	private double _longitude;
	private double _radius;
	
	public PlacesList search(double latitude, double longitude, double radius, String types) throws Exception{
			
		this._latitudel = latitude;
		this._longitude	= longitude;
		this._radius 	= radius;
			
		try {
			
			HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
			HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
			request.getUrl().put("key", API_KEY);
			request.getUrl().put("location", _latitudel + "," + _longitude);
			request.getUrl().put("radius", _radius);
			request.getUrl().put("sensor", "false");
			
			if(types != null){
				request.getUrl().put("types",types);
			}
			
			PlacesList list = request.execute().parseAs(PlacesList.class);
			Log.d("Place status: ", list.status);
			return list;
			
		} catch (HttpResponseException e) {
			Log.d("Error: ", e.getMessage());
			return null;
		}
	}

	private HttpRequestFactory createRequestFactory(final HttpTransport transport) {
		return  transport.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) throws IOException {
				GoogleHeaders headers = new GoogleHeaders();
				headers.setApplicationName("nearbeer");
				request.setHeaders(headers);
				JsonHttpParser parser = new JsonHttpParser(new JacksonFactory());
				request.addParser(parser);
			}
		});
	}
}

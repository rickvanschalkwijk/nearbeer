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
import com.supremecodemonkeys.models.PlaceDetails;
import com.supremecodemonkeys.models.PlacesList;

public class GooglePlaces {
	
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final String API_KEY = "AIzaSyC7V7i8nXnaDycoClSw09DDQJ-dYFIeFV4";
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
			request.getUrl().put("location", _latitudel + "," + _longitude);
			request.getUrl().put("radius", _radius);
			request.getUrl().put("sensor", "false");
			request.getUrl().put("key", API_KEY);
			
			if(types != null){
				request.getUrl().put("types",types);
			}
			PlacesList list = request.execute().parseAs(PlacesList.class);
			Log.d("Place status: ", "" + list.status);
			return list;
			
		} catch (HttpResponseException e) {
			Log.d("Error: ", e.getMessage());
			return null;
		}
	}
	
	public PlaceDetails getPlaceDetails(String reference) throws Exception{
		try {
			HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
			HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_DETAILS_URL));
			request.getUrl().put("key", API_KEY);
			request.getUrl().put("reference", reference);
			request.getUrl().put("sensor", "false");
			
			PlaceDetails place = request.execute().parseAs(PlaceDetails.class);
			return place;
		} catch (HttpResponseException e) {
			Log.e("Error in place detail", e.getMessage());
			throw e;
		}
	}

	public static HttpRequestFactory createRequestFactory(final HttpTransport transport) {
		return  transport.createRequestFactory(new HttpRequestInitializer() {
			public void initialize(HttpRequest request) {
				GoogleHeaders headers = new GoogleHeaders();
				headers.setApplicationName("nearbeer");
				request.setHeaders(headers);
				JsonHttpParser parser = new JsonHttpParser(new JacksonFactory());
				request.addParser(parser);
			}
		});
	}
}

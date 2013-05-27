package com.android.taxifare.google.apis;

import java.io.Serializable;

import com.google.api.client.util.Key;

public class Location implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Key
	public double lat;

	@Key
	public double lng;
	
	public double getLatitude(){
		return lat;
	}
	
	public double getLongitude(){
		return lng;
	}
}
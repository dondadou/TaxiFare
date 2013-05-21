package com.android.taxifare;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;

/**
 * 
 * @author Daniel Olivier
 *
 */
public class Directions implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Key
	private String status;
	
	@Key
	private List<Route> routes;
	
	public String getStatus(){
		return status;
	}
	
	public List<Route> getRoute(){
		return routes;
	}

}

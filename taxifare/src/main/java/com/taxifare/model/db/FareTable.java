package com.taxifare.model.db;

public class FareTable {
	private long id;
	private String city;
	private String country;
	private double baseFare;
	private double farePerKm;
	private double waitFees;
	private String currency;
	
	// Getters
	
	public long getId(){
		return id;
	}
	
	public String getCity(){
		return city;
	}
	
	public String getCountry(){
		return country;
	}
	
	public double getBaseFare(){
		return baseFare;
	}
	
	public double getFarePerKilometers(){
		return farePerKm;
	}
	
	public double getWaitingFees(){
		return waitFees;
	}
	
	public String getCurrency(){
		return currency;
	} 
}

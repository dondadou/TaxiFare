package com.taxifare.model.pojos;

public class FareTable {
    private long id;
    private String city;
    private String country;
    private double minimumFare;
    private double farePerKm;
    private double waitFees;
    private String currency;


    public long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public double getMinimumFare() {
        return minimumFare;
    }

    public double getFarePerKilometers() {
        return farePerKm;
    }

    public double getWaitingFees() {
        return waitFees;
    }

    public String getCurrency() {
        return currency;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setMinimumFare(double minimumFare) {
        this.minimumFare = minimumFare;
    }

    public void setFarePerKilometers(double farePerKm) {
        this.farePerKm = farePerKm;
    }

    public void setWaitingFees(double waitingFees) {
        this.waitFees = waitingFees;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

package com.taxifare.model;

public class Location extends android.location.Location {
    public Location(String provider) {
        super(provider);
    }

    public Location(android.location.Location location) {
        super(location);
    }

    @Override
    public String toString() {
        return this.getLatitude() + "," + this.getLongitude();
    }
}

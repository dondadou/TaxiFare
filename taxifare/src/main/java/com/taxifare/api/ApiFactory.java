package com.taxifare.api;

public class ApiFactory {
    public static PlaceAPI createPlaceApi(){
        return new GooglePlaceAPI();
    }

    public static DirectionAPI createDirectionApi(){
        return new GoogleDirectionAPI();
    }
}

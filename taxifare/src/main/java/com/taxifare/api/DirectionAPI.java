package com.taxifare.api;

import com.taxifare.model.pojos.Directions;

public interface DirectionAPI {
    public Directions getDirections(String origin, String destination);
}

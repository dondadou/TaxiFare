package com.taxifare.api;

import com.taxifare.model.pojos.AutoCompleteResponse;

import java.util.List;

public interface PlaceAPI {
    public AutoCompleteResponse autoComplete(String placeQuery);
}

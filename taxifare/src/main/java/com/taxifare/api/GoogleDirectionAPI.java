package com.taxifare.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.taxifare.model.pojos.Directions;

import java.io.IOException;
import java.net.URLEncoder;

class GoogleDirectionAPI implements DirectionAPI {
    private static final String DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions/json?";

    private OkHttpClient client = new OkHttpClient();

    @Override
    public Directions getDirections(String origin, String destination) {
        Directions result = Directions.NULL;
        try {
            StringBuilder url = new StringBuilder(DIRECTIONS_URL);
            url.append("origin=" + origin.replaceAll(" ", "%20"));
            url.append("&destination=" + destination.replaceAll(" ", "%20"));
            url.append("&sensor=false");

            Request request = new Request.Builder()
                    .url(url.toString())
                    .build();
            Response response = client.newCall(request).execute();
            ObjectMapper mapper = new ObjectMapper();

            result = mapper.readValue(response.body().string(), Directions.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}

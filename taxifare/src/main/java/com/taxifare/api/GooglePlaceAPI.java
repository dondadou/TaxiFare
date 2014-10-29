package com.taxifare.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.taxifare.model.pojos.AutoCompleteResponse;
import com.taxifare.model.pojos.PlacePOJO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

class GooglePlaceAPI implements PlaceAPI {
    private static final String API_KEY = "AIzaSyDPgC7JxPs8KiL9TuWqyMyboJ3ujw9iXdU";
    private static final String PLACES_AUTOCOMPLETE = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";

    private OkHttpClient client = new OkHttpClient();

    @Override
    public AutoCompleteResponse autoComplete(String placeQuery) {
        //todo apply null pattern
        AutoCompleteResponse resultList = new AutoCompleteResponse();
        try {
            StringBuilder url = new StringBuilder(PLACES_AUTOCOMPLETE);
            url.append("key=" + API_KEY);
            url.append("&input=" + URLEncoder.encode(placeQuery, "utf8"));

            Request request = new Request.Builder()
                    .url(url.toString())
                    .build();

            Response response = client.newCall(request).execute();
            ObjectMapper mapper = new ObjectMapper();

            AutoCompleteResponse requestResponse = mapper.readValue(response.body().string(), AutoCompleteResponse.class);
            resultList = requestResponse;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}

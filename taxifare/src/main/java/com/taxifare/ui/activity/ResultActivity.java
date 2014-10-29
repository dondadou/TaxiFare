package com.taxifare.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.taxifare.R;

import java.util.ArrayList;

public class ResultActivity extends Activity {
    private GoogleMap map;
    ArrayList<LatLng> decodedPolyLine = new ArrayList<LatLng>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        Intent intent = getIntent();
        String encodedPolyLine = intent.getStringExtra(MainActivity.POLYLINE_EXTRA);
        String origin = intent.getStringExtra(MainActivity.ORIGIN_EXTRA);
        String destination = intent.getStringExtra(MainActivity.DESTINATION_EXTRA);

        decodedPolyLine = decodePolyLine(encodedPolyLine);
        setUpMapIfNeeded();
        drawOnMap(decodedPolyLine);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_id))
                    .getMap();
        }
    }

    //TODO: Need to put it somewhere else (Presenter maybe?)
    // source:  jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
    private ArrayList<LatLng> decodePolyLine(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int decodedLatitude = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += decodedLatitude;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int decodedLongitude = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += decodedLongitude;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }

    //TODO:  Need to put it somewhere else (Presenter maybe?)
    private void drawOnMap(ArrayList<LatLng> decodedPolyLine){
        PolylineOptions rectLine = new PolylineOptions().width(5).color(
                Color.argb(116, 61, 50, 255));

        for (int i = 0; i < decodedPolyLine.size(); i++) {
            rectLine.add(decodedPolyLine.get(i));
        }
        map.clear();
        map.addPolyline(rectLine);
        map.addMarker(new MarkerOptions()
                .position(decodedPolyLine.get(0))
                .title(getString(R.string.origin_marker))
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        int polyLineSize = decodedPolyLine.size();
        map.addMarker(new MarkerOptions()
                .position(decodedPolyLine.get(polyLineSize-1))
                .title("Destination")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(decodedPolyLine.get(0), 12.0f));
    }
}

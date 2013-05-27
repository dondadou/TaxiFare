package com.android.taxifare.ui;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.android.taxifare.Localisateur;
import com.android.taxifare.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends FragmentActivity{

	private GoogleMap mMap;
	private Localisateur mLocalisateur;
	private PolylineOptions mPolyline;
	private LatLng mOrigin;
	private LatLng mDestination;
 	
	/**
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getParcelableExtra("bundle");
		mPolyline = bundle.getParcelable("rectline");
		mOrigin = bundle.getParcelable("origin");
		mDestination = bundle.getParcelable("destination");
		
		mLocalisateur = new Localisateur(this);
		setContentView(R.layout.map_fragment);
		setUpMapIfNeeded();
		
		
		mMap.addMarker(new MarkerOptions()
        .position(mOrigin)
        .title("Origine")
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		
		mMap.addMarker(new MarkerOptions()
        .position(mDestination)
        .title("Destination")
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		
		Location myLocation = mLocalisateur.obtenirPosition();
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				myLocation.getLatitude(), myLocation.getLongitude()), 12.0f));
		
		if(mPolyline != null){
			mMap.addPolyline(mPolyline);
		}
	}
	
	/**
	 * 
	 * @param v
	 */
	public void onClick(View v){
		switch(v.getId()){
		case R.id.afficher_boite_button:
			this.finish();
			break;
		}
		
	}
	
	/**
	 * Fonction permettant d'appeler le Google map.
	 */
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * <p>
	 * This should only be called once and when we are sure that {@link #mMap}
	 * is not null.
	 */
	private void setUpMap() {
		mMap.setMyLocationEnabled(true);
	}
}

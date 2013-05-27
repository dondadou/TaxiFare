package com.android.taxifare;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.android.taxifare.FareCalculatorFragment.onDisplayDirectionsListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * 
 * @author Daniel Olivier
 * 
 */
public class MainActivity extends FragmentActivity implements
		onDisplayDirectionsListener {
	private GoogleMap mMap;
	private Localisateur mLocalisateur;
	private FareCalculatorFragment mFareCalculatorFragment;
	private Directions mDirection;
	private boolean isTablet;

	/**
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isTablet = false;

		// comme specifié dans le fichier refs.xml, activity_main appelle des
		// layout différents
		// dépendant du type de support (télephone ou tablette)
		setContentView(R.layout.activity_main);
		setFareCalculatorFragment();

		// Vérifie si le support est une tablette ou non
		if (findViewById(R.id.map) != null) {
			mLocalisateur = new Localisateur(this);
			isTablet = true;
			setUpMapIfNeeded();
			Location myLocation = mLocalisateur.obtenirPosition();

			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					myLocation.getLatitude(), myLocation.getLongitude()), 12.0f));
		}
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Cette fonction est appelée par les boutons se trouvant dams le mapFragment et le
	 * fare_calculator_fragment.
	 * @param v
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bouton_afficherBoiteCalcul:
			displayFareCalculatorFragment();
			break;
		default:
			mFareCalculatorFragment.onClick(v);
		}
	}

	/**
	 * Affiche le fragment contenant la boite de calcul des estimations.
	 */
	private void displayFareCalculatorFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.show(mFareCalculatorFragment);
		ft.addToBackStack(null);
		ft.commit();
		setDisplayBoxButtonVisible(false);
	}

	/**
	 * Fonction permettant d'appeler le Google Map.
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

	/**
	 * Met en place le fragment fare_calculator.
	 */
	private void setFareCalculatorFragment() {
		FragmentManager fm = getSupportFragmentManager();
		mFareCalculatorFragment = (FareCalculatorFragment) fm
				.findFragmentById(R.id.boite_calcul);
	}

	/**
	 * Fonction permettant de faire le decodage des points retournés par Google
	 * Direction.
	 * 
	 * @param encoded
	 * @return
	 */
	private ArrayList<LatLng> decodePoly(String encoded) {
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
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
			poly.add(position);
		}
		return poly;
	}

	/**
	 * Cette fonction dessine sur la carte le chemin retourné par le
	 * fare_calculator_fragment.
	 */
	@Override
	public void displayDirection(Directions direction) {
		// TODO Auto-generated method stub
		mDirection = direction;
		PolylineOptions rectLine = new PolylineOptions().width(5).color(
				Color.argb(116, 61, 50, 255));

		if (mDirection != null && mDirection.getStatus().equals("OK")) {

			String encoded = mDirection.getRoute().get(0).getPolyline()
					.getPoints();
			// TODO: Trouver une meilleure facon...si possible
			LatLng origin =new LatLng(mDirection.getRoute().get(0).getLegs().get(0).getStartLocation().lat,
					mDirection.getRoute().get(0).getLegs().get(0).getStartLocation().lng);
			LatLng destination =new LatLng(mDirection.getRoute().get(0).getLegs().get(0).getEndLocation().lat,
					mDirection.getRoute().get(0).getLegs().get(0).getEndLocation().lng);

			List<LatLng> poly = decodePoly(encoded);

			for (int i = 0; i < poly.size(); i++) {
				rectLine.add(poly.get(i));
			}
			
			if (isTablet) {
				mMap.clear();
				mMap.addPolyline(rectLine);
				mMap.addMarker(new MarkerOptions()
                .position(origin)
                .title("Origine")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
				
				mMap.addMarker(new MarkerOptions()
                .position(destination)
                .title("Destination")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

				// Cache le FareCalculatorFragment
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ft.hide(mFareCalculatorFragment);
				ft.addToBackStack(null);
				ft.commit();
				setDisplayBoxButtonVisible(true);
			} else {				
				Bundle args = new Bundle();
				args.putParcelable("rectline", rectLine);
				args.putParcelable("origin", origin);
				args.putParcelable("destination", destination);
				Intent i = new Intent(getApplicationContext(), MapActivity.class);
				i.putExtra("bundle", args);
				startActivity(i);
			}
		}		
	}

	/**
	 * Permet de cacher ou d'afficher le bouton permettant d'afficher
	 * le fragment fare_calculator.
	 * @param isVisible
	 */
	private void setDisplayBoxButtonVisible(boolean isVisible) {
		Button displayBox = (Button) findViewById(R.id.bouton_afficherBoiteCalcul);
		FrameLayout frameBox = (FrameLayout) findViewById(R.id.cadre_boite_calcul); 
		if (isVisible) {
			displayBox.setVisibility(View.VISIBLE);
			frameBox.setVisibility(View.INVISIBLE);
			
		} else {
			displayBox.setVisibility(View.INVISIBLE);
			frameBox.setVisibility(View.VISIBLE);
		}
	}
}

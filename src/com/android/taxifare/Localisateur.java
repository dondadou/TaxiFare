package com.android.taxifare;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class Localisateur extends Service implements LocationListener
{
	private final Context mContext;
	
	//Position trouv� de l'utilisateur
	Location positionTrouve;

	// Status du gps
	boolean gpsActif = false;

	// Status de la connexion internet
	boolean reseauActif = false;

	// Verifie si la position a ete trouvee
	boolean localisationDisponible = false;

	Location position; // position
	double latitude; // latitude
	double longitude; // longitude

	// Distance minimale pour mettre a jour la position
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// Temps minimal pour mettre a jour la position
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute

	// Declaration de Location Manager
	protected LocationManager locationManager;

	/**
	 * Constructeur
	 * 
	 * */
	public Localisateur(Context context) 
	{
		this.mContext = context;

		//Gestionnaire des fournisseurs de position
		locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
		
		// Voir si GPS est activŽ
		gpsActif = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		// Voir si la connexion internet est activŽ
		reseauActif = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	/**
	 * Recherche de la position de l'utilisateur
	 * 
	 * */
	public Location obtenirPosition()
	{
		try 
		{
			// Voir si GPS est actif
			gpsActif = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			// Voir si la connexion internet est active
			reseauActif = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if (!gpsActif && !reseauActif) 
			{
				this.localisationDisponible = false;
			} 
			else 
			{
				// Obtenir la localisation en utilisant la connexion internet en premier
				if (reseauActif) 
				{
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					obtenirPositionParInternet();
				}
				// Si le GPS est actif, utiliser sa position car elle est plus precise
				if (gpsActif) 
				{
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					obtenirPositionParGPS();
				}
			}

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return position;
	}

	/**
	 * Arret de l'utilisation de la geolocalisation
	 * 
	 * */
	public void arretUtilisationLocalisateur()
	{
		if(locationManager != null)
		{
			locationManager.removeUpdates(Localisateur.this);
		}
	}

	/**
	 * Retourne la latitude
	 * */
	public double obtenirLatitude()
	{
		if(position != null)
		{
			latitude = position.getLatitude();
		}

		return latitude;
	}

	/**
	 * Retourne la longitude
	 * */
	public double obtenirLongitude()
	{
		if(position != null)
		{
			longitude = position.getLongitude();
		}

		return longitude;
	}

	/**
	 * Verifie si la position est disponible
	 * @return boolean
	 * */
	public boolean positionDisponible()
	{
		return this.localisationDisponible;
	}
	
	/**
	 * Verifie si la GPS est actif
	 * @return boolean
	 * */
	public boolean gpsEstIlActif()
	{
		return this.gpsActif;
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) 
	{
		return null;
	}

	/**
	 * Recherche la localisation par internet.
	 */
	private void obtenirPositionParInternet(){
		if (locationManager != null) 
		{
			position = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (position != null) 
			{
				this.localisationDisponible = true;
				latitude = position.getLatitude();
				longitude = position.getLongitude();
			}
		}
	}
	
	/**
	 * Recherche la localisation avec le GPS.
	 */
	private void obtenirPositionParGPS(){
		if (locationManager != null)
		{
			position = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (position != null)
			{
				this.localisationDisponible = true;
				latitude = position.getLatitude();
				longitude = position.getLongitude();
			}
		}
	}
}

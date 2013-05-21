package com.android.taxifare;

import java.io.IOException;

import javax.net.ssl.SSLException;

import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;

/**
 * Utilisation de la librairie Google API Client pour le tri des 
 * resultat en format JSON.
 * @author Daniel Olivier
 *
 */
public class GoogleServicesFunction {

	// creation de notre transport
	private static final HttpTransport TRANSPORT = new ApacheHttpTransport();

	private static final String API_KEY = "AIzaSyDPgC7JxPs8KiL9TuWqyMyboJ3ujw9iXdU";

	// Pour Google Place API
	private static final String PLACES_AUTOCOMPLETE = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";

	// Pour Google Direction API
	private static final String DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions/json?";

	/**
	 * 
	 * @param transport
	 * @return
	 */
	public static HttpRequestFactory createRequestFactory(
			final HttpTransport transport) {
		return transport.createRequestFactory(new HttpRequestInitializer() {
			public void initialize(HttpRequest request) {
				HttpHeaders headers = new HttpHeaders();
				headers.setUserAgent("Info mobile");
				request.setHeaders(headers);

				// JsonHttpParser parser = new JsonHttpParser(new
				// JacksonFactory());
				// request.addParser(parser);
				JsonObjectParser parser = new JsonObjectParser(
						new JacksonFactory());
				request.setParser(parser);
			}
		});
	}

	/**
	 * 
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public PlacesAutoCompleteList autocomplete(String search) throws Exception {
		PlacesAutoCompleteList autoCompleteResult = null;
		try {
			HttpRequestFactory httpRequestFactory = createRequestFactory(TRANSPORT);
			HttpRequest request = httpRequestFactory
					.buildGetRequest(new GenericUrl(PLACES_AUTOCOMPLETE));
			request.getUrl().put("input", search);
			request.getUrl().put("sensor", "false");
			request.getUrl().put("key", API_KEY);

			autoCompleteResult = request.execute().parseAs(
					PlacesAutoCompleteList.class);
		} catch (HttpResponseException e) {
			Log.e("HTTPResponse Exception", e.getMessage());
			throw e;
		} catch (IOException e) {
			Log.e("I/O Exception", e.getMessage());
		}
		return autoCompleteResult;
	}

	/**
	 * 
	 * @param origin
	 * @param destination
	 * @return
	 * @throws Exception
	 */
	public Directions getDirection(String origin, String destination)
			throws Exception {

		try {
			Directions result = new Directions();
			HttpRequestFactory httpRequestFactory = createRequestFactory(TRANSPORT);
			HttpRequest request = httpRequestFactory
					.buildGetRequest(new GenericUrl(DIRECTIONS_URL));
			request.getUrl().put("origin", origin);
			request.getUrl().put("destination", destination);
			request.getUrl().put("sensor", "false");

			result = request.execute().parseAs(Directions.class);

			request.execute().disconnect();

			return result;

		} catch (HttpResponseException e) {
			Log.e("HTTP Error in getDirections", e.getMessage());
			throw e;
		} catch (SSLException s) {
			Log.e("Error ssl in getDirections:", s.getMessage());
			return null;
		}

		catch (Exception e) {
			Log.e("Unknown Error in getDirections:", e.getMessage());
			return null;
		}
	}

}

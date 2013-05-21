package com.android.taxifare;

import java.util.List;

import com.google.api.client.util.Key;

public class PlacesAutoCompleteList {

	@Key
	private List<PlaceAutoComplete> predictions;

	public List<PlaceAutoComplete> getPrediction() {
		return predictions;
	}

	public int getSize() {
		return predictions.size();
	}

	public static class PlaceAutoComplete {

		@Key
		private String id;

		@Key
		private String description;

		@Key
		private String reference;

		@Override
		public String toString() {
			return description + " - " + id + " - " + reference;
		}

		// Getters
		public String getId() {
			return id;
		}

		public String getReference() {
			return reference;
		}

		public String getDescription() {
			return description;
		}

	}
}

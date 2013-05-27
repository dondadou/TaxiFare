package com.android.taxifare;

import java.text.DecimalFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class FareCalculatorFragment extends Fragment {
	private View mView;
	private GoogleServicesFunction mGoogleService;
	private Directions mDirections;
	private onDisplayDirectionsListener mDirectionListener;

	@Override
	public void onAttach(Activity a) {
	    super.onAttach(a);
	    mDirectionListener = (onDisplayDirectionsListener) a;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mView = inflater.inflate(R.layout.fare_calculator_fragment, container, false);
		mView.findViewById(R.id.mainLayout).requestFocus();

		setResultVisibility(false);
		mGoogleService = new GoogleServicesFunction();
		mDirections = null;

		AutoCompleteTextView autoCompView = (AutoCompleteTextView) mView
				.findViewById(R.id.depart);
		AutoCompleteTextView autoCompView2 = (AutoCompleteTextView) mView
				.findViewById(R.id.arrivee);

		autoCompView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(),
				R.layout.list_item));
		autoCompView2.setAdapter(new PlacesAutoCompleteAdapter(getActivity(),
				R.layout.list_item));

		setCurrentTimeAndDate();

		mView.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		
		mView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }

        });

		return mView;
	}

/*
 * 	Affiche la date courante dans les boutons
 */
public void setCurrentTimeAndDate() {
		Button dateButton = (Button) mView.findViewById(R.id.date_bouton);
		Button timeButton = (Button) mView.findViewById(R.id.heure_bouton);

		Calendar now = Calendar.getInstance();

		dateButton.setText(now.get(Calendar.DAY_OF_MONTH) + "/"
				+ now.get(Calendar.MONTH) + "/" + now.get(Calendar.YEAR));
		timeButton.setText(now.get(Calendar.HOUR_OF_DAY) + ":"
				+ now.get(Calendar.MINUTE));
	}

	/*
	 * pour la communication activity/fragment.
	 */
	public interface onDisplayDirectionsListener{
		public void displayDirection(Directions direction);
	}
	
/*
 * Gestion des evenement relié au clics des btons.
 */
	public boolean onClick(View v) {
		hideKeyboard();
		switch (v.getId()) {
		case R.id.date_bouton:
			showDatePickerDialog(v);
			break;
		case R.id.heure_bouton:
			showTimePickerDialog(v);
			break;
		case R.id.button_eval:
			new LoadingResult().execute();
			setResultVisibility(true);
			hideDisplayMapButton();
			break;
		case R.id.afficherCarte:
			drawOnMap();
			break;
		}

		return true;
	}
	
/**
 * 
 * @param v
 */
public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}
/**
 * 
 * @param v
 */
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}
	
	/**
	 * 
	 */
	private void hideDisplayMapButton(){
		Button displayMap = (Button)mView.findViewById(R.id.afficherCarte);
		
		if(displayMap.getVisibility() == View.INVISIBLE){
			displayMap.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Dessine le trajet.
	 */
 	private void drawOnMap(){
		mDirectionListener.displayDirection(mDirections);
	}
	
 	/**
 	 * 
 	 * @param isVisible
 	 */
	private void setResultVisibility(boolean isVisible) {
		TextView estimationTextview = (TextView) mView
				.findViewById(R.id.text_estimation);
		TextView val_estimationTextview = (TextView) mView
				.findViewById(R.id.text_valeur_estimation);
		TextView dureeTextview = (TextView) mView.findViewById(R.id.text_duree);
		TextView val_dureeTextview = (TextView) mView
				.findViewById(R.id.text_valeur_duree);
		TextView distanceTextview = (TextView) mView
				.findViewById(R.id.text_distance);
		TextView val_distanceTextview = (TextView) mView
				.findViewById(R.id.text_valeur_distance);
		if (isVisible) {
			if (estimationTextview.getVisibility() != View.VISIBLE) {
				estimationTextview.setVisibility(View.VISIBLE);
				val_estimationTextview.setVisibility(View.VISIBLE);
				dureeTextview.setVisibility(View.VISIBLE);
				val_dureeTextview.setVisibility(View.VISIBLE);
				distanceTextview.setVisibility(View.VISIBLE);
				val_distanceTextview.setVisibility(View.VISIBLE);
			}
		} else {
			estimationTextview.setVisibility(View.GONE);
			val_estimationTextview.setVisibility(View.GONE);
			dureeTextview.setVisibility(View.GONE);
			val_dureeTextview.setVisibility(View.GONE);
			distanceTextview.setVisibility(View.GONE);
			val_distanceTextview.setVisibility(View.GONE);
		}

	}
	
	/**
	 * 
	 */
	private void calculateFare(){
		TextView val_estimationTextview = (TextView) mView
				.findViewById(R.id.text_valeur_estimation);
		TextView val_distanceTextview = (TextView) mView
				.findViewById(R.id.text_valeur_distance);
		
		if (mDirections != null && mDirections.getStatus().equals("OK")){
			double totalDistance = mDirections.getRoute().get(0).getLegs().get(0).getDistance().getValue();
			String totalDistanceText = mDirections.getRoute().get(0).getLegs().get(0).getDistance().getText();
		
			double estimation = 3.45 + 1.75*(totalDistance/1000);
			DecimalFormat df = new DecimalFormat("#.##");
			
			val_distanceTextview.setText(totalDistanceText);
			val_estimationTextview.setText(df.format(estimation) + "$");
			
			
		}
	}

	/**
	 * 
	 * @author Daniel Olivier
	 *
	 */
	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String>
			implements Filterable {
		private PlacesAutoCompleteList resultList;

		/**
		 * 
		 * @param context
		 * @param textViewResourceId
		 */
		public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			return resultList.getSize();
		}

		@Override
		public String getItem(int index) {
			return resultList.getPrediction().get(index).getDescription();
		}

		/**
		 * 
		 */
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						// Retrieve the autocomplete results.
						try {
							resultList = mGoogleService.autocomplete(constraint
									.toString());
						} catch (NullPointerException e) {
							Log.e("NullPointer Error:", e.getMessage());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.getSize();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}
	
	/**
	 * Permet de cacher le clavier
	 */
	private void hideKeyboard(){

		if(getActivity().getCurrentFocus()!=null && getActivity().getCurrentFocus() instanceof AutoCompleteTextView){
			 InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
			    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
	    }
	}

	
	/**
	 * 
	 * 
	 * */
	private class LoadingResult extends AsyncTask<String, Void, Void> {

		private ProgressDialog dialog = new ProgressDialog(getActivity());
    	
		/**
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
    	protected void onPreExecute() 
    	{
    		this.dialog.setMessage("Recherche en cours...");
            this.dialog.show();
    	}
    	
    	/**
    	 * (non-Javadoc)
    	 * @see android.os.AsyncTask#doInBackground(Params[])
    	 */
		@Override
		protected Void doInBackground(String... arg0) {
			AutoCompleteTextView depart = (AutoCompleteTextView) mView
					.findViewById(R.id.depart);
			AutoCompleteTextView arrivee = (AutoCompleteTextView) mView
					.findViewById(R.id.arrivee);

			String mOrigin = depart.getText().toString();
			String mDestination = arrivee.getText().toString();

			try {
				mDirections = mGoogleService
						.getDirection(mOrigin, mDestination);
			} catch (Exception e) {
				Log.e("Error : ", e.getMessage());
			}
			return null;

		}

		/**
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {
			calculateFare();
			dialog.dismiss();
		}

	}

}

package com.taxifare.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.taxifare.R;
import com.taxifare.api.ApiFactory;
import com.taxifare.api.DirectionAPI;
import com.taxifare.api.LocationService;
import com.taxifare.api.PlaceAPI;
import com.taxifare.model.Location;
import com.taxifare.model.pojos.Directions;
import com.taxifare.ui.adapter.PlacesAutoCompleteAdapter;
import com.taxifare.ui.dialog.DirectionAPIsErrorHandler;

import java.util.List;

public class HomeFragment extends Fragment {

    public static final int MIN_TIME_IN_MILLI_SECONDS = 4000;
    public static final int MIN_DISTANCE_IN_METER = 5;
    private OnCalculateButtonListener listener;
    private Button calculateButton;
    private ImageButton locateMeButtonOrigin;
    private ImageButton locateMeButtonDestination;
    private AutoCompleteTextView originTextView;
    private AutoCompleteTextView destinationTextView;
    private LocationService locationService;
    private Location lastLocation;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        initLocationService();
        initButtons(view);

        return view;
    }

    private void initLocationService() {
        locationService = new LocationService(getActivity());
    }

    private void initButtons(View view) {
        originTextView = (AutoCompleteTextView) view.findViewById(R.id.origin_textview);
        originTextView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.autocomplete_list_item));

        destinationTextView = (AutoCompleteTextView) view.findViewById(R.id.destination_textview);
        destinationTextView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.autocomplete_list_item));

        calculateButton = (Button) view.findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateFareRequest();
            }
        });

        locateMeButtonOrigin = (ImageButton) view.findViewById(R.id.locate_me_origin);
        locateMeButtonOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originTextView.setText(R.string.my_position);
                lastLocation = locationService.getLocation();
            }
        });

        locateMeButtonDestination = (ImageButton) view.findViewById(R.id.locate_me_destination);
        locateMeButtonDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destinationTextView.setText(R.string.my_position);
            }
        });
    }

    private void calculateFareRequest() {
        String origin = originTextView.getText().toString();
        String destination = destinationTextView.getText().toString();
        boolean currentLocationFound = true;
        if (origin.equalsIgnoreCase(getString(R.string.my_position))) {
            if (lastLocation != null) {
                origin = lastLocation.toString();
            } else {
                currentLocationFound = false;
            }
        }

        if (destination.equalsIgnoreCase(getString(R.string.my_position))) {
            if (lastLocation != null) {
                destination = lastLocation.toString();
            } else {
                currentLocationFound = false;
            }
        }

        if (currentLocationFound) {
            CalculateFareTask fareTask = new CalculateFareTask(origin, destination);
            fareTask.execute();
        } else {
            showCurrentLocationNotFoundDialog();
        }
    }

    private void showCurrentLocationNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog alertDialog = builder.setMessage(R.string.no_location_found_error)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();

        alertDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        locationService.connect();

        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(getActivity(), "Enable location services for accurate data", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        locationService.disconnect();
    }

    public interface OnCalculateButtonListener {
        public void onResultFound(String origin, String destination, String polyLine);
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnCalculateButtonListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCalculateButtonListener");
        }
    }

    private class CalculateFareTask extends AsyncTask<Void, Void, Directions> {
        private String origin;
        private String destination;
        private ProgressDialog dialog;

        CalculateFareTask(String origin, String destination) {
            this.origin = origin;
            this.destination = destination;
        }

        @Override
        protected Directions doInBackground(Void... voids) {
            DirectionAPI directionAPI = ApiFactory.createDirectionApi();
            Directions result = directionAPI.getDirections(origin, destination);
            //TODO: Need to find a better way to handle errors...
            return result;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getResources().getString(R.string.searching));
            dialog.show();
        }

        @Override
        protected void onPostExecute(Directions directions) {
            dialog.dismiss();
            if(directions.getStatus().equalsIgnoreCase("OK")) {
                listener.onResultFound(originTextView.getText().toString(),
                        destinationTextView.getText().toString(),
                        directions.getEncodedPolyLine());
            }
            else{
                new DirectionAPIsErrorHandler(getActivity(), directions.getStatus()).showError();
            }
        }
    }
}

package com.android.taxifare;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements
DatePickerDialog.OnDateSetListener {

	protected Date mDate = null;
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current date as the default date in the picker
final Calendar c = Calendar.getInstance();
int year = c.get(Calendar.YEAR);
int month = c.get(Calendar.MONTH);
int day = c.get(Calendar.DAY_OF_MONTH);


// Create a new instance of DatePickerDialog and return it
return new DatePickerDialog(getActivity(), this, year, month, day);
}

public void onDateSet(DatePicker view, int year, int month, int day) {
// Do something with the date chosen by the user
	Calendar newDate = Calendar.getInstance();
	newDate.clear();
	
	newDate.set(Calendar.YEAR, year);
	newDate.set(Calendar.MONTH, month);
	newDate.set(Calendar.DAY_OF_MONTH, day);
	
	mDate = newDate.getTime();
	((TextView) getActivity().findViewById(R.id.date_bouton)).setText(day + "/" + month +"/" + year);
	   
}
}
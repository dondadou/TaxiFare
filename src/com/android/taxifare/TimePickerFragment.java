package com.android.taxifare;

import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements
TimePickerDialog.OnTimeSetListener {
protected Date mDate;
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current time as the default values for the picker
final Calendar c = Calendar.getInstance();
int hour = c.get(Calendar.HOUR_OF_DAY);
int minute = c.get(Calendar.MINUTE);

// Create a new instance of TimePickerDialog and return it
return new TimePickerDialog(getActivity(), this, hour, minute,
		DateFormat.is24HourFormat(getActivity()));
}

public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
// Do something with the time chosen by the user
	Calendar newTime = Calendar.getInstance();
	newTime.clear();

	
	newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
	newTime.set(Calendar.MINUTE, minute);
	
	mDate = newTime.getTime();
	
	((TextView) getActivity().findViewById(R.id.heure_bouton)).setText(hourOfDay + ":" + minute);
}
}
package com.marsdayjam.eventplanner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener listener;

    public DatePickerFragment(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();

        return new DatePickerDialog(getActivity(), listener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
    }
}

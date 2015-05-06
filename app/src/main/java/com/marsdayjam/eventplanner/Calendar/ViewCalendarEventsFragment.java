package com.marsdayjam.eventplanner.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Beaster on 4/28/2015.
 */
public class ViewCalendarEventsFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CalendarFragment calFrag = (CalendarFragment) getParentFragment().getParentFragment();
        ArrayList<CharSequence> items = new ArrayList<>();
        DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);
        Date selectedDate = calFrag.getSelectedDate();

        for (CalendarEvent event : calFrag.getEvents()) {
            if (event.getStart().getDate() == selectedDate.getDate() ||
                    event.getEnd().getDate() == selectedDate.getDate())
                items.add(String.format("%s\n%s - %s",
                        event.getDescription(),
                        tf.format(event.getStart()),
                        tf.format(event.getEnd())));
        }

        return new AlertDialog.Builder(getActivity())
                .setItems(items.toArray(new CharSequence[items.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
    }
}

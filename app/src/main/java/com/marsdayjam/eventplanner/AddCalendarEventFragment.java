package com.marsdayjam.eventplanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

public class AddCalendarEventFragment extends DialogFragment {
    private Date start, end;
    private CalendarFragment parent;
    private TextView description;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_add_calendar_event, null);
        final TextView startDate = (TextView) view.findViewById(R.id.start_date);
        final TextView startTime = (TextView) view.findViewById(R.id.start_time);
        final TextView endDate = (TextView) view.findViewById(R.id.end_date);
        final TextView endTime = (TextView) view.findViewById(R.id.end_time);
        description = (TextView) view.findViewById(R.id.description);
        parent = ((CalendarFragment) getParentFragment().getParentFragment());

        final DateFormat dfDate = DateFormat.getDateInstance();
        DateFormat dfTime = DateFormat.getTimeInstance(DateFormat.SHORT);

        if (parent.getSelectedDate() != null) {
            start = new Date(parent.getSelectedDate().getTime());
            end = new Date(parent.getSelectedDate().getTime());
            start.setHours(new Date().getHours());
        }
        else {
            start = new Date();
            end = new Date();
        }
        end.setHours(new Date().getHours() + 1);

        startDate.setText(dfDate.format(start));
        startTime.setText(dfTime.format(start));
        endTime.setText(dfTime.format(end));
        endDate.setText(dfDate.format(end));

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().add(
                        new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                start.setYear(i - 1900);
                                start.setMonth(i1);
                                start.setDate(i2);
                                startDate.setText(dfDate.format(start));
                            }
                        }),
                        null
                ).commit();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().add(
                        new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                end.setYear(i - 1900);
                                end.setMonth(i1);
                                end.setDate(i2);
                                endDate.setText(dfDate.format(start));
                            }
                        }),
                        null
                ).commit();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CalendarEvent event = new CalendarEvent();
                        DBController dbController = DBController.getInstance(getActivity());
                        event.setDescription(description.getText().toString());
                        event.setStart(start);
                        event.setEnd(end);
                        if (parent.getType() == CalendarFragment.EMPLOYEE_TYPE)
                            event.setEmployee(dbController.getEmployee(parent.getTypeId()));
                        else
                            event.setEvent(dbController.getEvent(parent.getTypeId()));
                        dbController.insertCalendarEvent(event);
                    }
                })
                .create();
    }
}

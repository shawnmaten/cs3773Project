package com.marsdayjam.eventplanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.marsdayjam.eventplanner.Calendar.CalendarFragment;
import com.marsdayjam.eventplanner.DB.DBController;
import com.marsdayjam.eventplanner.Employee.Employee;
import com.marsdayjam.eventplanner.Employee.HRFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditEventFragment extends DialogFragment {
    private Date start, end;
    private Event event;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_edit_event, null);
        final TextView name = (TextView) root.findViewById(R.id.name);
        final TextView host = (TextView) root.findViewById(R.id.host);
        final TextView location = (TextView) root.findViewById(R.id.location);
        final TextView startDate = (TextView) root.findViewById(R.id.start_date);
        final TextView startTime = (TextView) root.findViewById(R.id.start_time);
        final TextView endDate = (TextView) root.findViewById(R.id.end_date);
        final TextView endTime = (TextView) root.findViewById(R.id.end_time);

        final DateFormat dfDate = DateFormat.getDateInstance();
        final DateFormat dfTime = DateFormat.getTimeInstance(DateFormat.SHORT);

        if (event != null) {
            name.setText(event.getName());
            host.setText(event.getHost());
            location.setText(event.getLocation());
            start = event.getStart();
            end = event.getEnd();
        } else {
            start = new Date();
            end = new Date();
            end.setHours(new Date().getHours() + 1);
        }

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

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().add(
                        new TimePickerFragment(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                start.setHours(i);
                                start.setMinutes(i1);
                                startTime.setText(dfTime.format(start));
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

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().add(
                        new TimePickerFragment(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                end.setHours(i);
                                end.setMinutes(i1);
                                endTime.setText(dfTime.format(end));
                            }
                        }),
                        null
                ).commit();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(root)
                .setNeutralButton(R.string.cancel, null)
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBController dbController = DBController.getInstance(getActivity());
                        if (event != null) {
                            dbController.deleteEvent(event);
                            ((EventFragment2) getParentFragment()).refreshList();
                        }
                    }
                })
                .setPositiveButton(R.string.edit_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBController dbController = DBController.getInstance(getActivity());
                        Event newEvent = new Event();

                        newEvent.setName(name.getText().toString());
                        newEvent.setHost(host.getText().toString());
                        newEvent.setLocation(location.getText().toString());
                        newEvent.setStart(start);
                        newEvent.setEnd(end);
                        newEvent.setManager(LoginActivity.getUser());

                        if (event == null) {
                            dbController.insertEvent(newEvent);
                        } else {
                            dbController.deleteEvent(event);
                            dbController.insertEvent(newEvent);
                        }
                        ((EventFragment2) getParentFragment()).refreshList();
                    }
                })
                .create();
    }

    public EditEventFragment() {
    }

    public static EditEventFragment getInstance(Context context, long id) {
        DBController dbController = DBController.getInstance(context);
        EditEventFragment fragment = new EditEventFragment();
        fragment.setEvent(dbController.getEvent(id));
        return fragment;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}

package com.marsdayjam.eventplanner;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarFragment extends MainActivity.PlaceholderFragment {
    private static final String CALENDAR_FRAGMENT = "calendarFragment";
    private static final String ARG_TYPE = "type";
    private static final String ARG_ID = "id";
    public static final int EMPLOYEE_TYPE = 0;
    public static final int EVENT_TYPE = 1;


    public static CalendarFragment newInstance(int sectionNumber, int type, long id) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_TYPE, type);
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        CaldroidFragment fragment;
        Bundle fragmentArgs = new Bundle();
        Calendar cal = Calendar.getInstance();
        FragmentManager cfm = getChildFragmentManager();
        FragmentTransaction ft = cfm.beginTransaction();
        DBController dbController = DBController.getInstance(getActivity());
        Bundle calendarArgs = getArguments();
        ArrayList<CalendarEvent> events;

        super.onCreate(savedInstanceState);

        fragmentArgs = getArguments();
        if (fragmentArgs.getInt(ARG_TYPE) == EMPLOYEE_TYPE)
            events = dbController.getEmployee(calendarArgs.getLong(ARG_ID)).getCalendarEvents();
        else
            events = dbController.getEvent(calendarArgs.getLong(ARG_ID)).getCalendarEvents();

        if (savedInstanceState == null) {
            fragment = new CaldroidFragment();
            calendarArgs.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            calendarArgs.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            fragment.setArguments(calendarArgs);
            ft.add(R.id.calendar, fragment, CALENDAR_FRAGMENT);
            ft.commit();
            cfm.executePendingTransactions();
        } else {
            fragment = (CaldroidFragment) cfm.findFragmentByTag(CALENDAR_FRAGMENT);
        }

        for (CalendarEvent event : events) {
            fragment.setBackgroundResourceForDate(R.color.green, event.getStart());
            fragment.setBackgroundResourceForDate(R.color.green, event.getEnd());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }
}

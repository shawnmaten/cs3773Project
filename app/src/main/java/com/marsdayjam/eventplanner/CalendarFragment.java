package com.marsdayjam.eventplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends MainActivity.PlaceholderFragment{
    private static final String CALENDAR_FRAGMENT = "calendarFragment";
    private static final String ARG_TYPE = "type";
    private static final String ARG_ID = "id";
    public static final int EMPLOYEE_TYPE = 0;
    public static final int EVENT_TYPE = 1;
    private static Boolean dateSelected = Boolean.FALSE;
    private static String selectedDate;
    CaldroidFragment caldroidFragment;
    private static Date prevDate;
    private CalendarOptionsFragment calendarOptions;
    static Context context;
    private AddCalendarEventFragment addCalEvent;
    private EditCalendarEventsFragment editCalEvent;

    public static CalendarFragment newInstance(int sectionNumber, int type, long id, Context context) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_TYPE, type);
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        CalendarFragment.context = context;
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
            this.caldroidFragment = fragment;
            CalendarOptionsFragment calendarOptions = new CalendarOptionsFragment(this, context);
            this.calendarOptions = calendarOptions;
            ft.add(R.id.calendar, calendarOptions);
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

        caldroidFragment.setCaldroidListener(new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                if (dateSelected == Boolean.TRUE)
                    caldroidFragment.clearBackgroundResourceForDate(prevDate);
                dateSelected = Boolean.TRUE;
                calendarOptions.setDateSelected(Boolean.TRUE);
                prevDate = date;
                selectedDate = date.toString();
                caldroidFragment.setBackgroundResourceForDate(R.color.yellow, date);
                caldroidFragment.refreshView();
            }

            @Override
            public void onChangeMonth(int month, int year) {
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    public void addEvent(){
        FragmentManager cfm = getChildFragmentManager();
        FragmentTransaction ft = cfm.beginTransaction();
        ft.remove(calendarOptions);
        ft.remove(caldroidFragment);
        AddCalendarEventFragment addCalEvent = new AddCalendarEventFragment(this);
        this.addCalEvent = addCalEvent;
        ft.add(R.id.calendar, addCalEvent);
        ft.commit();
    }

    public void viewEvent(){
        FragmentManager cfm = getChildFragmentManager();
        FragmentTransaction ft = cfm.beginTransaction();
        ft.remove(calendarOptions);
        ft.remove(caldroidFragment);
        EditCalendarEventsFragment editCalEvent = new EditCalendarEventsFragment();
        this.editCalEvent = editCalEvent;
        ft.add(R.id.calendar, editCalEvent);
        ft.commit();
    }

    // 1 if coming back from AddCalenderEventFragment, 1 if coming back from EditCalendarEvents
    public void recreate(int i){
        FragmentManager cfm = getChildFragmentManager();
        FragmentTransaction ft = cfm.beginTransaction();
        switch(i){
            case 1:
                ft.remove(addCalEvent);
                ft.add(R.id.calendar, this);
                ft.add(R.id.calendar, calendarOptions);
                ft.commit();
                break;
            case 2:
                ft.remove(editCalEvent);
                ft.add(R.id.calendar, this);
                ft.add(R.id.calendar, calendarOptions);
                ft.commit();
                break;
        }
    }
}

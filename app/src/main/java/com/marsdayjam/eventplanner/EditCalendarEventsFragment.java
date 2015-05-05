package com.marsdayjam.eventplanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Beaster on 4/28/2015.
 */
public class EditCalendarEventsFragment extends Fragment{

    private static Button delete, back;
    private static CalendarFragment calendarFragment;
    private static Boolean eventSelected = false;


    //empty Constructor
    public EditCalendarEventsFragment(CalendarFragment calendarFragment){
        this.calendarFragment = calendarFragment;
    }


    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_calendar_event, container, false);
        delete = (Button) view.findViewById (R.id.deleteEvents);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do deleteing operations


                      remove();
                //return to calendar


            }
        });

        back = (Button) view.findViewById (R.id.backToCalendar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
        return view;
    }

    public void remove(){
        eventSelected = Boolean.FALSE;
        calendarFragment.recreate(this);
    }
}

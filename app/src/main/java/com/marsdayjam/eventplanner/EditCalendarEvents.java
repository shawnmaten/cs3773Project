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
public class EditCalendarEvents extends Fragment implements View.OnClickListener {

    private static Button delete, back;


    //empty Constructor
    public EditCalendarEvents(){}


    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_calendar_event, container, false);
        delete = (Button) view.findViewById (R.id.deleteEvents);
        back = (Button) view.findViewById (R.id.backToCalendar);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.deleteEvents) {

        }

        else if(v.getId() == R.id.backToCalendar){

        }
    }
}

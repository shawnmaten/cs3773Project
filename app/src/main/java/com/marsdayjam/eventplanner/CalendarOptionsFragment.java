package com.marsdayjam.eventplanner;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import java.util.Date;

import static android.view.View.OnClickListener;


@SuppressLint("ValidFragment")
public class CalendarOptionsFragment extends Fragment{

    private static CalendarFragment calendarFragment;
    private static Context context;
    private static Boolean dateSelected = Boolean.FALSE;
    private static Date prevDate;
    private static Button edit, viewDelete;


    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_options, container, false);
        this.edit = (Button) view.findViewById (R.id.editCalendar);
        edit.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if(dateSelected == Boolean.TRUE){
                    calendarFragment.addEvent();
                }
            }
        });
        this.viewDelete = (Button) view.findViewById (R.id.viewEvents);
        viewDelete.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if(dateSelected == Boolean.TRUE){
                    calendarFragment.viewEvent();
                }
            }
        });
        return view;
    }

    @SuppressLint("ValidFragment")
    public CalendarOptionsFragment(CalendarFragment calendarFragment, Context context){
        this.calendarFragment = calendarFragment;
        this.context = context;
    }

    public void setDateSelected(Boolean dateSelected) {
        this.dateSelected = dateSelected;
    }

    public static void setContext(Context context) {
        CalendarOptionsFragment.context = context;
    }
}

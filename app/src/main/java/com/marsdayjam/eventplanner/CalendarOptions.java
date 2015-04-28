package com.marsdayjam.eventplanner;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Date;


public class CalendarOptions extends Fragment implements View.OnClickListener {

    private static CaldroidFragment caldroidFragment;
    private static Context context;
    private static Boolean dateSelected = Boolean.FALSE;
    private static String selectedDate;
    private static Button edit, viewDelete;

    public static String getSelectedDate() {
        return selectedDate;
    }

    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_options, container, false);
        edit = (Button) view.findViewById (R.id.editCalendar);
        viewDelete = (Button) view.findViewById (R.id.viewEvents);
        return view;
    }

    public CalendarOptions(CaldroidFragment caldroidFragment, final Context context){
        this.caldroidFragment = caldroidFragment;
        this.context = context;

        caldroidFragment.setCaldroidListener(new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                dateSelected = Boolean.TRUE;
                selectedDate = date.toString();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(context, text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(context,
                        "Long click " + date.toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                Toast.makeText(context,
                        "Caldroid view is created",
                        Toast.LENGTH_SHORT).show();
            }

        });
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.editCalendar){

        }
        else if (v.getId() == R.id.viewEvents){

        }
    }
}

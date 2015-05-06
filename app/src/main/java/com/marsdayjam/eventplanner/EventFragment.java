package com.marsdayjam.eventplanner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Beaster on 5/5/2015.
 */
public class EventFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private Date startD, endD;
    private Spinner spinner;
    private static String[] events;
    private ArrayList<String> ev;
    private static ArrayAdapter<String> adapter;
    private Context context;
    private String eventName;
    private String host;
    private String location;
    private String start;
    private String end;
    private int managerID;
    private View view;
    private Button add, team, delete;
    private TeamFragment teamFragment;
    final DateFormat dfDate = DateFormat.getDateInstance();


    public EventFragment(Context context){
        //passed in context for toast
        this.context = context;
    }

    public View onCreateView( LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        setDropDownList();
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        final EditText endDate = (EditText) view.findViewById(R.id.eventEnd);
        final EditText startDate = (EditText) view.findViewById(R.id.eventStart);
        spinner = (Spinner)view.findViewById(R.id.events);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, events);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        add = (Button) view.findViewById (R.id.addEvent);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create new Event and EventCalendar
                //there is no error checking for invalid input


            }
        });
        delete = (Button) view.findViewById (R.id.removeEvent);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove an event from the database


            }
        });
        team = (Button) view.findViewById (R.id.eventTeams);
        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        endD = new Date();
        startD = new Date();
        endDate.setText(dfDate.format(endD));
        startDate.setText(dfDate.format(startD));
        endDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().add(
                        new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                endD.setYear(i - 1900);
                                endD.setMonth(i1);
                                endD.setDate(i2);
                                endDate.setText(dfDate.format(endD));
                            }
                        }),
                        null
                ).commit();
            }
        });
        startDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                    getChildFragmentManager().beginTransaction().add(
                            new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                    startD.setYear(i - 1900);
                                    startD.setMonth(i1);
                                    startD.setDate(i2);
                                    startDate.setText(dfDate.format(startD));
                                }
                            }),
                            null
                    ).commit();
            }
        });

        this.view = view;
        return view;
    }

    public void setDropDownList(){
        ev = new ArrayList<String>();
        //put checking for manager
        /*
        if(manager){
            ev.add("Add Event");
            view.findViewBYId(R.id.addEvent).setVisibility(View.VISIBLE);
        }
         */
        //Find all events in database and add them to the ArrayList use


        //Implementing this for testing, delete when populating with database events
        ev.add("Add Event");
        ev.add("psudo event");
        ev.add("psudo");

        //convert from ArrayList to String Array
        events = new String[ev.size()];
        events = ev.toArray(events);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        String text = spinner.getSelectedItem().toString();
        Toast.makeText(context,text, Toast.LENGTH_LONG).show();
        if(text.equalsIgnoreCase("Add Event")){
            view.findViewById(R.id.addEvent).setVisibility(View.VISIBLE);
            view.findViewById(R.id.eventTeams).setVisibility(View.GONE);
            view.findViewById(R.id.removeEvent).setVisibility(View.GONE);
            EditText eName = (EditText)view.findViewById(R.id.eventName);
            eName.setText("Event Name");
            EditText eHost = (EditText)view.findViewById(R.id.hostName);
            eHost.setText("Host");
            EditText eLocation = (EditText)view.findViewById(R.id.eventLocation);
            eLocation.setText("Location");
            EditText eStart = (EditText)view.findViewById(R.id.eventStart);
            eStart.setText("Start: MM-DD-YYYY");
            EditText eEnd = (EditText)view.findViewById(R.id.eventEnd);
            eEnd.setText("End: MM-DD-YYYY");
            EditText eManagerID = (EditText)view.findViewById(R.id.eventManagerID);
            eManagerID.setText("Manager ID");
            setEditable(true);
        }
        else{
            view.findViewById(R.id.addEvent).setVisibility(View.GONE);
            view.findViewById(R.id.eventTeams).setVisibility(View.VISIBLE);
            view.findViewById(R.id.removeEvent).setVisibility(View.VISIBLE);
            //populate local variables with event in for event retrieved with the "text" string

            /*
            this.host = host;
            this.location = location;
            this.eventName = eventName;
            this.start = start;
            this.end = end;
            this.managerID = managerID;
            EditText eName = (EditText)view.findViewById(R.id.eventName);
            eName.setText(eventName);
            EditText eHost = (EditText)view.findViewById(R.id.eventName);
            eHost.setText(eventName);
            EditText eLocation = (EditText)view.findViewById(R.id.eventName);
            eLocation.setText(eventName);
            EditText eStart = (EditText)view.findViewById(R.id.eventName);
            eStart.setText(eventName);
            EditText eEnd = (EditText)view.findViewById(R.id.eventName);
            eEnd.setText(eventName);
            EditText eManagerID = (EditText)view.findViewById(R.id.eventName);
            eManagerID.setText(eventName);
            */
            setEditable(false);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //1 if coming back from calendar, 2 if coming back from team
    public void restore(int i){
        switch(i){
            case 1:
                break;
            case 2:
                FragmentManager cfm = getChildFragmentManager();
                FragmentTransaction ft = cfm.beginTransaction();
                ft.commit();
                cfm.executePendingTransactions();
                break;
        }
    }

    public void setEditable(Boolean b){
        view.findViewById(R.id.eventName).setEnabled(b);
        view.findViewById(R.id.hostName).setEnabled(b);
        view.findViewById(R.id.eventLocation).setEnabled(b);
        view.findViewById(R.id.eventStart).setEnabled(b);
        view.findViewById(R.id.eventEnd).setEnabled(b);
        view.findViewById(R.id.eventManagerID).setEnabled(b);
    }
}

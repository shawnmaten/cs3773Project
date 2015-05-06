package com.marsdayjam.eventplanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static android.view.View.OnClickListener;

public class CalendarOptionsFragment extends Fragment {
    private static final String ADD_FRAGMENT = "addFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final CalendarFragment calendarFragment = (CalendarFragment) getParentFragment();
        View root = inflater.inflate(R.layout.fragment_calendar_options, container, false);
        root.findViewById (R.id.add_event).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction()
                        .add(new AddCalendarEventFragment(), ADD_FRAGMENT)
                        .commit();
            }
        });
        root.findViewById (R.id.view_events).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),
                        String.format("# Events: %d", calendarFragment.getEvents().size()),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}

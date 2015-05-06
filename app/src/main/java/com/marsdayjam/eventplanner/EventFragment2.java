package com.marsdayjam.eventplanner;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.marsdayjam.eventplanner.DB.DBContract;
import com.marsdayjam.eventplanner.DB.DBController;
import com.marsdayjam.eventplanner.Employee.Employee;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventFragment2 extends MainActivity.PlaceholderFragment {
    public static final int TYPE_MANAGER = 0;
    public static final int TYPE_EMPLOYEE = 1;
    public static final String ARG_TYPE = "type";
    public static final String ARG_ID = "id";

    private ListView listView;

    public static EventFragment2 newInstance(int sectionNumber, int type, long id) {
        EventFragment2 fragment = new EventFragment2();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_TYPE, type);
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event_2, container, false);
        listView = (ListView) root.findViewById(R.id.list);

        listView.setAdapter(new SimpleListAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getChildFragmentManager().beginTransaction()
                        .add(EditEventFragment.getInstance(getActivity(), l), null)
                        .commit();
            }
        });
        root.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction()
                        .add(EditEventFragment.getInstance(getActivity(), 0), null)
                        .commit();
            }
        });
        return root;
    }

    private class SimpleListAdapter implements ListAdapter {
        private ArrayList<Event> events;

        public SimpleListAdapter() {
            DBController dbController = DBController.getInstance(getActivity());
            Bundle args = getArguments();
            Employee employee = dbController.getEmployee(args.getLong(ARG_ID));
            if (args.getInt(ARG_TYPE) == TYPE_MANAGER)
                events = dbController.getEventsForManager(employee);
            else
                events = dbController.getEventsForEmployee(employee);
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int i) {
            return true;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Object getItem(int i) {
            return events.get(i);
        }

        @Override
        public long getItemId(int i) {
            return events.get(i).getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            DateFormat df = DateFormat.getDateTimeInstance();

            if (view == null)
                view = inflater.inflate(R.layout.list_text_view, viewGroup, false);
            Event event = events.get(i);
            ((TextView)view).setText(String.format("%s\n%s\n%s\n%s\n%s\n%s %s",
                    event.getName(), event.getHost(), event.getLocation(),
                    df.format(event.getStart()), df.format(event.getEnd()),
                    event.getManager().getFirst(), event.getManager().getLast()
            ));
            return view;
        }

        @Override
        public int getItemViewType(int i) {
            return 1;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }

    public void refreshList() {
        listView.setAdapter(new SimpleListAdapter());
    }
}

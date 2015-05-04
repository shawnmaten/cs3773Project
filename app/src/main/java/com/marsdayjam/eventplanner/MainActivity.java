package com.marsdayjam.eventplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final int LOG_IN_REQUEST = 0;

    private String EMPLOYEE_FRAGMENT = "employeeFragment";
    private String CALENDAR_FRAGMENT = "calendarFragment";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LOG_IN_REQUEST:
                recreate();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                fm.findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (LoginActivity.getUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOG_IN_REQUEST);
        } else {
            DBController dbController = DBController.getInstance(this);
            CalendarEvent calendarEvent = new CalendarEvent();
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            Date start = new Date();
            Date end = new Date();
            try {
                start = dateFormat.parse("May 6, 2015 12:00:00 PM");
                end = dateFormat.parse("May 6, 2015 1:00:00 PM");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendarEvent.setDescription("Meeting with Nancy");
            calendarEvent.setStart(start);
            calendarEvent.setEnd(end);
            calendarEvent.setEmployee(LoginActivity.getUser());
            dbController.insertCalendarEvent(calendarEvent);
        }
    }

    /*
    private void setupFragments() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Employee user = LoginActivity.getUser();
        Fragment fragment;

        fragment = EmployeeFragment.newInstance(1, user.getId());
        ft.add(R.id.container, fragment, EMPLOYEE_FRAGMENT);
        fragment = CalendarFragment.newInstance(1, CalendarFragment.EMPLOYEE_TYPE, user.getId());
        ft.add(R.id.container, fragment, CALENDAR_FRAGMENT);

        ft.commit();
        fm.executePendingTransactions();
    }
    */

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment;
        Employee user = LoginActivity.getUser();

        if (user != null) {
            switch (position) {
                case 0:
                    fragment = EmployeeFragment.newInstance(position+1, user.getId());
                    ft.replace(R.id.container, fragment, EMPLOYEE_FRAGMENT);
                    break;
                case 1:
                    fragment = CalendarFragment.newInstance(position+1,
                            CalendarFragment.EMPLOYEE_TYPE, user.getId());
                    ft.replace(R.id.container, fragment, CALENDAR_FRAGMENT);
                    break;
                default:
                    ft.replace(R.id.container, PlaceholderFragment.newInstance(position + 1));
                    break;
            }
            ft.commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}

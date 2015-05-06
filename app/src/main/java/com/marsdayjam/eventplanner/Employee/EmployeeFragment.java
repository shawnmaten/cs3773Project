package com.marsdayjam.eventplanner.Employee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marsdayjam.eventplanner.DB.DBController;
import com.marsdayjam.eventplanner.MainActivity;
import com.marsdayjam.eventplanner.R;

public class EmployeeFragment extends MainActivity.PlaceholderFragment {
    private static final String ARG_EMPLOYEE_ID = "employeeId";

    public static EmployeeFragment newInstance(int sectionNumber, long employeeId) {
        EmployeeFragment fragment = new EmployeeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putLong(ARG_EMPLOYEE_ID, employeeId);
        fragment.setArguments(args);
        return fragment;
    }

    public EmployeeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout root;
        TextView textView;
        DBController dbController = DBController.getInstance(getActivity());
        Bundle args = getArguments();
        Employee employee = dbController.getEmployee(args.getLong(ARG_EMPLOYEE_ID));

        root = (LinearLayout) inflater.inflate(R.layout.fragment_employee_info, container, false);
        if (employee != null) {
            textView = (TextView) inflater.inflate(R.layout.text_view, root, false);
            textView.setText(String.format("%s: %s %s\n\n%s: %s",
                    getString(R.string.employee), employee.getFirst(), employee.getLast(),
                    getString(R.string.role), employee.getRoleTitle()
            ));
            root.addView(textView);
        }

        return root;
    }
}

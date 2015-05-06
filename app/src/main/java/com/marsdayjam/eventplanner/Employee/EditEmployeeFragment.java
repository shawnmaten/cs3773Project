package com.marsdayjam.eventplanner.Employee;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.marsdayjam.eventplanner.DB.DBController;
import com.marsdayjam.eventplanner.R;

import java.util.ArrayList;

public class EditEmployeeFragment extends DialogFragment {
    private Employee employee;
    private ArrayList<String> roles;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_edit_employee, null);
        final TextView email = (TextView) root.findViewById(R.id.email);
        final TextView password = (TextView) root.findViewById(R.id.password);
        final TextView first = (TextView) root.findViewById(R.id.first);
        final TextView last = (TextView) root.findViewById(R.id.last);
        final Spinner spinner = (Spinner) root.findViewById(R.id.role);

        spinner.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                roles.toArray(new String[roles.size()])));

        if (employee != null) {
            email.setText(employee.getEmail());
            password.setText(employee.getPassword());
            first.setText(employee.getFirst());
            last.setText(employee.getLast());
            spinner.setSelection((int) employee.getRoleCode() - 1);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(root)
                .setNeutralButton(R.string.cancel, null)
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBController dbController = DBController.getInstance(getActivity());
                        if (employee != null) {
                            dbController.deleteEmployee(employee);
                            ((HRFragment) getParentFragment()).refreshList();
                        }
                    }
                })
                .setPositiveButton(R.string.edit_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBController dbController = DBController.getInstance(getActivity());
                        Employee newEmployee = new Employee();

                        newEmployee.setEmail(email.getText().toString());
                        newEmployee.setPassword(password.getText().toString());
                        newEmployee.setFirst(first.getText().toString());
                        newEmployee.setLast(last.getText().toString());
                        newEmployee.setRoleCode(spinner.getSelectedItemId() + 1);

                        if (employee == null) {
                            dbController.insertEmployee(newEmployee);
                        } else {
                            newEmployee.setId(employee.getId());
                            dbController.updateEmployee(newEmployee);
                        }
                        ((HRFragment) getParentFragment()).refreshList();
                    }
                })
                .create();
    }

    public EditEmployeeFragment() {
        DBController dbController = DBController.getInstance(getActivity());
        roles = dbController.getRoles();
    }

    public static EditEmployeeFragment getInstance(Context context, long id) {
        DBController dbController = DBController.getInstance(context);
        EditEmployeeFragment fragment = new EditEmployeeFragment();
        fragment.setEmployee(dbController.getEmployee(id));
        return fragment;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}

package com.marsdayjam.eventplanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Beaster on 5/5/2015.
 */
public class TeamFragment extends Fragment{

    private Button back;
    private EventFragment eventFragment;

    public TeamFragment(EventFragment eventFragment){
        this.eventFragment = eventFragment;
    }

    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        back = (Button) view.findViewById (R.id.addEvent);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back to EventFragment
                eventFragment.restore(2);
            }
        });

        return view;
    }

}

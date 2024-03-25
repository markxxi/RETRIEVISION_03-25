package com.example.retrievision;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class homefragment extends Fragment {
    //3. fragment documentation: creates and returns the view hierarchy associated with the fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // fragment instantiate user interface view of home fragment
        View view =inflater.inflate(R.layout.fragment_homefragment, container, false);

        //function for report lost object button
        Button RLO = (Button)view.findViewById(R.id.reportLostObjectButton);
        RLO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), reportlostobject.class);
                startActivity(intent);
            }
        });
        //function for lost object button
        Button RFO = (Button) view.findViewById(R.id.reportFoundObjectButton);
        RFO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), reportfoundobject.class);
                startActivity(intent);
            }
        });
    return view;
    }
}
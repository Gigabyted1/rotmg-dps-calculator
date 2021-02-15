package com.waynebloom.rotmgdpscalculator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class LoadoutFragment extends Fragment {

    private MainActivity mActivity;
    private RecyclerView loadoutView;
    private RecyclerView selectorView;
    private View filterView;
    private Button addLoadoutButton;
    private LoadoutAdapter loadoutAdpt;
    private boolean selectorVisible = false;

    public LoadoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loadout, container, false);

        mActivity = ((MainActivity) getActivity());
        loadoutView = view.findViewById(R.id.rV_loadout);
        selectorView = view.findViewById(R.id.selector);
        filterView = view.findViewById(R.id.filter);
        addLoadoutButton = view.findViewById(R.id.button_add_loadout);

        loadoutAdpt = new LoadoutAdapter(getContext(), mActivity.getLoadoutList());
        loadoutView.setAdapter(loadoutAdpt);
        loadoutView.setLayoutManager(new LinearLayoutManager(getContext()));

        addLoadoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Randomly select the default class
                CharClass randomClass = mActivity.getClassData().get(new Random().nextInt(16));

                if(mActivity.getLoadoutList().size() < 8) {
                    Loadout newLoadout = new Loadout(getContext(),
                            getActivity(),
                            mActivity.getLoadoutList().size(),
                            randomClass
                    );
                    mActivity.addToLoadoutList(newLoadout);
                    loadoutAdpt.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(mActivity, "There is only support for 8 loadouts at a time.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public void toggleSelector() {
        if(selectorVisible) {
            selectorVisible = false;
        }
        else {
            selectorVisible = true;
        }
    }
}
package com.waynebloom.rotmgdpscalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DpsFragment extends Fragment {
    MainActivity mActivity;
    LoadoutFragment loadoutFragment;

    List<List<DpsEntry>> dpsDataTable = new ArrayList<>();
    RecyclerView dpsTableView;
    DpsAdapter dpsAdpt;

    public DpsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        loadoutFragment = (LoadoutFragment) mActivity.getFragmentHolder().getFragment(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dps, container, false);
        dpsTableView = view.findViewById(R.id.dps_table_view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayDpsTable();
    }

    public void displayDpsTable() {
        if(dpsTableView != null) {
            populateDpsTable(loadoutFragment.getLoadouts());
        }
    }

    // Generates an ascending order list of each current loadout's damage per second for each level of defense up to the maximum
    private void populateDpsTable(List<Loadout> loadouts) {
        dpsDataTable.clear();

        for(int currentDefLevel = 0; currentDefLevel <= 150; currentDefLevel++) {
            ArrayList<DpsEntry> dpsTableAtCurrentDefLevel = new ArrayList<>();
            for(Loadout currentLoadout : loadouts) {
                dpsTableAtCurrentDefLevel.add(currentLoadout.getDps().get(currentDefLevel));
            }
            dpsDataTable.add(dpsTableAtCurrentDefLevel);
        }

        sortDpsTable(dpsDataTable);

        if(dpsAdpt == null) {
            dpsAdpt = new DpsAdapter(getActivity(), dpsDataTable);
            dpsTableView.setAdapter(dpsAdpt);
            dpsTableView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else {
            dpsAdpt.notifyDataSetChanged();
        }
    }


    private List<List<DpsEntry>> sortDpsTable(List<List<DpsEntry>> data) {
        // Insert sort
        for(int i = 0; i <= 150; i++) {
            for(int j = 1; j < data.get(i).size(); j++) {
                DpsEntry currentEntry = data.get(i).get(j);
                int k = j;
                while(k > 0 && data.get(i).get(k - 1).getDps() < currentEntry.getDps()) {
                    data.get(i).set(k, data.get(i).get(k - 1));
                    k--;
                }
                data.get(i).set(k, currentEntry);
            }
        }

        return data;
    }

    public void notifyLoadoutRemoved() {
        if(dpsAdpt != null) {
            dpsAdpt = new DpsAdapter(getContext(), dpsDataTable);
            dpsTableView.setAdapter(dpsAdpt);
        }
    }
}
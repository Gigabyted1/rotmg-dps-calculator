package com.waynebloom.rotmgdpscalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.List;

public class DpsFragment extends Fragment {
    MainActivity mActivity;
    LoadoutFragment loadoutFragment;

    List<List<DpsEntry>> dpsDataTable = new ArrayList<>();
    BottomNavigationView dpsViewMenu;
    GraphView dpsGraphView;
    RecyclerView dpsTableView;
    LinearLayout detailView;
    DpsAdapter dpsAdpt;
    LinearLayoutManager dpsAdptManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) requireActivity();
        loadoutFragment = (LoadoutFragment) mActivity.getFragmentHolder().getFragment(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dps, container, false);
        dpsViewMenu = view.findViewById(R.id.dps_view_menu);
        dpsTableView = view.findViewById(R.id.dps_table_view);
        detailView = view.findViewById(R.id.details_container);

        dpsGraphView = view.findViewById(R.id.graph);
        dpsGraphView.getViewport().setScalable(true);
        dpsGraphView.getViewport().setScrollable(true);
        dpsGraphView.getViewport().setMinX(0);
        dpsGraphView.getViewport().setMaxX(40);
        GridLabelRenderer renderer = dpsGraphView.getGridLabelRenderer();
        int gridColor = ContextCompat.getColor(requireContext(), R.color.gridColor);
        int titleColor = ContextCompat.getColor(requireContext(), R.color.colorAccent);
        renderer.setGridColor(gridColor);
        renderer.setVerticalAxisTitleColor(titleColor);
        renderer.setVerticalLabelsColor(gridColor);
        renderer.setNumVerticalLabels(6);
        renderer.setVerticalAxisTitle("DPS");
        renderer.setHorizontalAxisTitleColor(titleColor);
        renderer.setHorizontalLabelsColor(gridColor);
        renderer.setHorizontalAxisTitle("Defense");

        dpsViewMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.graph) {
                    displayGraph();
                    return true;
                }
                else if(itemId == R.id.table) {
                    displayTable();
                    return true;
                }
                else {
                    return false;
                }
            }
        });

        updateDpsViews();

        return view;
    }

    public void onBackPressed() {
        mActivity.setViewPagerPosition(0);
    }

    private void displayTable() {
        dpsGraphView.setVisibility(View.INVISIBLE);
        detailView.setVisibility(View.INVISIBLE);
        dpsTableView.setVisibility(View.VISIBLE);
    }

    private void displayGraph() {
        dpsGraphView.setVisibility(View.VISIBLE);
        detailView.setVisibility(View.VISIBLE);
        dpsTableView.setVisibility(View.INVISIBLE);
    }

    public void updateDpsViews() {
        if(dpsGraphView != null) {
            dpsGraphView.removeAllSeries();
            detailView.removeAllViews();
            populateMatrix(loadoutFragment.getLoadouts());
            for(int i = 0; i < dpsDataTable.get(0).size(); i++) {
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                for(int j = 0; j < dpsDataTable.size(); j++) {
                    series.appendData(new DataPoint(j, dpsDataTable.get(j).get(i).getDps()), false, 151, true);
                }
                int colorId;
                switch(i) {
                    case 0:
                        colorId = ContextCompat.getColor(requireContext(), R.color.graphLoadout1);
                        break;
                    case 1:
                        colorId = ContextCompat.getColor(requireContext(), R.color.graphLoadout2);
                        break;
                    case 2:
                        colorId = ContextCompat.getColor(requireContext(), R.color.graphLoadout3);
                        break;
                    case 3:
                        colorId = ContextCompat.getColor(requireContext(), R.color.graphLoadout4);
                        break;
                    case 4:
                        colorId = ContextCompat.getColor(requireContext(), R.color.graphLoadout5);
                        break;
                    case 5:
                        colorId = ContextCompat.getColor(requireContext(), R.color.graphLoadout6);
                        break;
                    case 6:
                        colorId = ContextCompat.getColor(requireContext(), R.color.graphLoadout7);
                        break;
                    case 7:
                        colorId = ContextCompat.getColor(requireContext(), R.color.graphLoadout8);
                        break;
                    default:
                        colorId = -1;
                }
                series.setColor(colorId);
                series.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        detailView.removeAllViews();
                        DpsAdapter.ViewHolder viewHolder = dpsAdpt.onCreateViewHolder(detailView, 0);
                        dpsAdpt.onBindViewHolder(viewHolder, (int) Math.round(dataPoint.getX()));
                        detailView.addView(viewHolder.itemView);
                    }
                });
                dpsGraphView.addSeries(series);
            }
        }

        if(dpsTableView != null) {
            sortMatrix(dpsDataTable);

            if(dpsAdpt == null) {
                dpsAdpt = new DpsAdapter(getActivity(), dpsDataTable);
                dpsTableView.setAdapter(dpsAdpt);
                dpsAdptManager = new LinearLayoutManager(getContext());
                dpsTableView.setLayoutManager(dpsAdptManager);
            }
            else {
                dpsAdpt.notifyDataSetChanged();
            }
        }
    }

    // Generates an ascending order list of each current loadout's damage per second for each level of defense up to the maximum
    private void populateMatrix(List<Loadout> loadouts) {
        dpsDataTable.clear();

        for(int currentDefLevel = 0; currentDefLevel <= 150; currentDefLevel++) {
            ArrayList<DpsEntry> dpsTableAtCurrentDefLevel = new ArrayList<>();
            for(Loadout currentLoadout : loadouts) {
                dpsTableAtCurrentDefLevel.add(currentLoadout.getDps().get(currentDefLevel));
            }
            dpsDataTable.add(dpsTableAtCurrentDefLevel);
        }
    }

    private List<List<DpsEntry>> sortMatrix(List<List<DpsEntry>> data) {
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
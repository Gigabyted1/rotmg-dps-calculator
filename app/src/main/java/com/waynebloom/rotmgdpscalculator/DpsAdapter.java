package com.waynebloom.rotmgdpscalculator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

public class DpsAdapter extends ArrayAdapter<ArrayList<Double>> {
    private Context dContext;
    private ArrayList<ArrayList<Double>> dpsTables = new ArrayList<>();
    private Typeface myFont;

    public DpsAdapter(Context context, ArrayList<ArrayList<Double>> data) {
        super(context, R.layout.list_item_dpstable, data);
        dContext = context;
        dpsTables = data;
        myFont = Typeface.createFromAsset(context.getAssets(), "myfont.ttf");
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(dContext).inflate(R.layout.list_item_dpstable, parent, false);
        }

        ArrayList<Double> currDpsList = dpsTables.get(position);

        TextView[] dpsViews = { listItem.findViewById(R.id.dps1), listItem.findViewById(R.id.dps2), listItem.findViewById(R.id.dps3),
                                listItem.findViewById(R.id.dps4), listItem.findViewById(R.id.dps5), listItem.findViewById(R.id.dps6),
                                listItem.findViewById(R.id.dps7), listItem.findViewById(R.id.dps8)};
        TextView defense = listItem.findViewById(R.id.defense);
        defense.setTypeface(myFont);
        defense.setTextColor(Color.parseColor("#CCCCCC"));
        defense.setTextSize(17f);

        for(int i = 0; i < currDpsList.size(); i++) {
            dpsViews[i].setText(String.format(Locale.US, "%.2f", currDpsList.get(i)));
            dpsViews[i].setTypeface(myFont);
            dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"));
            dpsViews[i].setTextSize(8f);
            dpsViews[i].setVisibility(View.VISIBLE);
        }

        defense.setText(String.format(Locale.US, "%d", position));

        return listItem;
    }
}

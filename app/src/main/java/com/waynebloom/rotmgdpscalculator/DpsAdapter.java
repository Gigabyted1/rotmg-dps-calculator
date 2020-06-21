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

public class DpsAdapter extends ArrayAdapter<ArrayList<DpsEntry>> {
    private Context dContext;
    private ArrayList<ArrayList<DpsEntry>> dpsTables;
    private Typeface myFont;

    public DpsAdapter(Context context, ArrayList<ArrayList<DpsEntry>> data) {
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

        ArrayList<DpsEntry> currDpsList = dpsTables.get(position);

        TextView[] dpsViews = { listItem.findViewById(R.id.dps1), listItem.findViewById(R.id.dps2), listItem.findViewById(R.id.dps3),
                                listItem.findViewById(R.id.dps4), listItem.findViewById(R.id.dps5), listItem.findViewById(R.id.dps6),
                                listItem.findViewById(R.id.dps7), listItem.findViewById(R.id.dps8) };

        TextView[] shadows = { listItem.findViewById(R.id.dps1_s), listItem.findViewById(R.id.dps2_s), listItem.findViewById(R.id.dps3_s),
                                listItem.findViewById(R.id.dps4_s), listItem.findViewById(R.id.dps5_s), listItem.findViewById(R.id.dps6_s),
                                listItem.findViewById(R.id.dps7_s), listItem.findViewById(R.id.dps8_s) };

        TextView defense = listItem.findViewById(R.id.defense);
        defense.setTypeface(myFont);
        defense.setTextColor(Color.parseColor("#EEEEEE"));
        defense.setTextSize(17f);

        for(int i = 0; i < currDpsList.size(); i++) {
            dpsViews[i].setText(String.format(Locale.US, "%.2f", currDpsList.get(i).dps));
            shadows[i].setText(String.format(Locale.US, "%.2f", currDpsList.get(i).dps));

            switch (currDpsList.get(i).color) {
                case 0:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_red);
                    break;

                case 1:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_orange);
                    break;

                case 2:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_yellow);
                    break;

                case 3:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_green);
                    break;

                case 4:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_cyan);
                    break;

                case 5:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_blue);
                    break;

                case 6:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_pink);
                    break;

                case 7:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_black);
                    break;
            }

            dpsViews[i].setTypeface(myFont);
            shadows[i].setTypeface(myFont);
            dpsViews[i].setTextColor(Color.parseColor("#333333"));
            shadows[i].setTextColor(Color.parseColor("#EEEEEE"));
            dpsViews[i].setTextSize(8f);
            shadows[i].setTextSize(8f);
            dpsViews[i].setVisibility(View.VISIBLE);
            shadows[i].setVisibility(View.VISIBLE);
        }

        defense.setText(String.format(Locale.US, "%d", position));

        return listItem;
    }
}

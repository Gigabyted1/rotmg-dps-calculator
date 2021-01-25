package com.waynebloom.rotmgdpscalculator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

class DpsAdapter extends RecyclerView.Adapter<DpsAdapter.ViewHolder> {
    private Context dContext;
    private ArrayList<ArrayList<DpsEntry>> dpsTables;
    private Typeface myFont;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View view) {
            super(view);

        }
    }

    DpsAdapter(Context context, ArrayList<ArrayList<DpsEntry>> data) {
        dContext = context;
        dpsTables = data;
        myFont = Typeface.createFromAsset(context.getAssets(), "myfont.ttf");
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(dContext).inflate(R.layout.list_item_dpstable, parent, false);
        }

        ArrayList<DpsEntry> currDpsList = dpsTables.get(position);

        TextView[] dpsViews = { convertView.findViewById(R.id.dps1), convertView.findViewById(R.id.dps2), convertView.findViewById(R.id.dps3),
                                convertView.findViewById(R.id.dps4), convertView.findViewById(R.id.dps5), convertView.findViewById(R.id.dps6),
                                convertView.findViewById(R.id.dps7), convertView.findViewById(R.id.dps8) };

        TextView defense = convertView.findViewById(R.id.defense);
        defense.setTypeface(myFont);
        defense.setTextColor(Color.parseColor("#EEEEEE"));
        defense.setTextSize(17f);

        for(int i = 0; i < currDpsList.size(); i++) {
            dpsViews[i].setText(String.format(Locale.US, "%.2f", currDpsList.get(i).dps));

            switch (currDpsList.get(i).color) {
                case 0:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_red);
                    dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"));
                    break;

                case 1:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_orange);
                    dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"));
                    break;

                case 2:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_yellow);
                    dpsViews[i].setTextColor(Color.parseColor("#444444"));
                    break;

                case 3:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_green);
                    dpsViews[i].setTextColor(Color.parseColor("#444444"));
                    break;

                case 4:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_cyan);
                    dpsViews[i].setTextColor(Color.parseColor("#444444"));
                    break;

                case 5:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_blue);
                    dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"));
                    break;

                case 6:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_pink);
                    dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"));
                    break;

                case 7:
                    dpsViews[i].setBackgroundResource(R.drawable.dps_black);
                    dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"));
                    break;

            }

            dpsViews[i].setTypeface(myFont);
            dpsViews[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
            dpsViews[i].setVisibility(View.VISIBLE);
        }

        defense.setText(String.format(Locale.US, "%d", position));

        return convertView;
    }
}

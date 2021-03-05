package com.waynebloom.rotmgdpscalculator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

class DpsAdapter extends RecyclerView.Adapter<DpsAdapter.ViewHolder> {
    private final Context mContext;
    private final List<List<DpsEntry>> data;
    private final Typeface myFont;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView defense;
        final TextView[] dpsViews;

        public ViewHolder(View view) {
            super(view);
            defense = view.findViewById(R.id.defense);
            dpsViews = new TextView[] {
                    view.findViewById(R.id.dps1),
                    view.findViewById(R.id.dps2),
                    view.findViewById(R.id.dps3),
                    view.findViewById(R.id.dps4),
                    view.findViewById(R.id.dps5),
                    view.findViewById(R.id.dps6),
                    view.findViewById(R.id.dps7),
                    view.findViewById(R.id.dps8)};
        }
    }

    DpsAdapter(Context context, List<List<DpsEntry>> data) {
        mContext = context;
        this.data = data;
        myFont = Typeface.createFromAsset(context.getAssets(), "myfont.ttf");
    }

    @NonNull
    @Override
    public DpsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DpsAdapter.ViewHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_dps, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        populateFields(holder, position, data.get(position));
    }

    private void populateFields(ViewHolder holder, int position, List<DpsEntry> currentDefLevel) {
        holder.defense.setText(String.format(Locale.US, "%d", position));
        holder.defense.setTypeface(myFont);
        holder.defense.setTextColor(Color.parseColor("#EEEEEE"));
        holder.defense.setTextSize(17f);

        for(int i = 0; i < currentDefLevel.size(); i++) {
            holder.dpsViews[i].setText(String.format(Locale.US, "%.2f", currentDefLevel.get(i).getDps()));

            // Assign each view the background color that corresponds with their loadout
            switch (currentDefLevel.get(i).getLoadoutId()) {
                case 0:
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_red);
                    holder.dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"));
                    break;

                case 1:
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_orange);
                    holder.dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"));
                    break;

                case 2:
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_yellow);
                    holder.dpsViews[i].setTextColor(Color.parseColor("#444444"));
                    break;

                case 3:
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_green);
                    holder.dpsViews[i].setTextColor(Color.parseColor("#444444"));
                    break;

                case 4:
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_cyan);
                    holder.dpsViews[i].setTextColor(Color.parseColor("#444444"));
                    break;

                case 5:
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_blue);
                    holder.dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"));
                    break;

                case 6:
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_pink);
                    holder.dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"));
                    break;

                case 7:
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_black);
                    holder.dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"));
                    break;

            }

            holder.dpsViews[i].setTypeface(myFont);
            holder.dpsViews[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
            holder.dpsViews[i].setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

package com.waynebloom.rotmgdpscalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LoadoutAdapter extends RecyclerView.Adapter<LoadoutAdapter.ViewHolder> {
    private Context dContext;
    private ArrayList<Loadout> loadouts;
    private ClickListeners myActivityInterface;
    private LayoutInflater mInflater;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View view) {
            super(view);

        }
    }

    LoadoutAdapter(Context context, ArrayList<Loadout> data, ClickListeners inter) {
        loadouts = data;
        myActivityInterface = inter;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_itemsel, parent, false);

            // Creates a ViewHolder
            viewHolder = new LoadoutAdapter.ViewHolder(convertView);

            // Bind the data efficiently with the holder.
            convertView.setTag(viewHolder);

        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            viewHolder = (LoadoutAdapter.ViewHolder) convertView.getTag();
        }

        Loadout currLoadout;
        ConstraintLayout background = convertView.findViewById(R.id.background);

        if (position < loadouts.size()) {
            currLoadout = loadouts.get(position);

            //Assign the elements of each loadout to the corresponding class object
            myActivityInterface.setupLoadoutViews((ImageView)convertView.findViewById(R.id.class_view), (ImageView)convertView.findViewById(R.id.weapon_view),
                    (ImageView)convertView.findViewById(R.id.ability_view), (ImageView)convertView.findViewById(R.id.armor_view),
                    (ImageView)convertView.findViewById(R.id.ring_view), (TextView)convertView.findViewById(R.id.total_att),
                    (TextView)convertView.findViewById(R.id.total_dex), (ConstraintLayout)convertView.findViewById(R.id.status),
                    (Button)convertView.findViewById(R.id.delete), position);

            //myActivityInterface.setLoadoutViewListeners(position);

            //Initial setting of loadout elements to reflect stats loaded from file
            currLoadout.updateViews();
        }

        switch (position) {
            case 0:
                background.setBackgroundResource(R.drawable.bg_loadout_red);
                break;

            case 1:
                background.setBackgroundResource(R.drawable.bg_loadout_orange);
                break;

            case 2:
                background.setBackgroundResource(R.drawable.bg_loadout_yellow);
                break;

            case 3:
                background.setBackgroundResource(R.drawable.bg_loadout_green);
                break;

            case 4:
                background.setBackgroundResource(R.drawable.bg_loadout_cyan);
                break;

            case 5:
                background.setBackgroundResource(R.drawable.bg_loadout_blue);
                break;

            case 6:
                background.setBackgroundResource(R.drawable.bg_loadout_purple);
                break;

            case 7:
                background.setBackgroundResource(R.drawable.bg_loadout_black);
                break;
        }

        return convertView;
    }
}


package com.waynebloom.rotmgdpscalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class LoadoutAdapter extends ArrayAdapter<Loadout> {
    private Context dContext;
    private ArrayList<Loadout> loadouts;
    private ClickListeners myActivityInterface;

    LoadoutAdapter(Context context, ArrayList<Loadout> data, ClickListeners inter) {
        super(context, R.layout.list_item_loadout, data);
        dContext = context;
        loadouts = data;
        myActivityInterface = inter;
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(dContext).inflate(R.layout.list_item_loadout, parent, false);
        }

        Loadout currLoadout;
        ConstraintLayout background = listItem.findViewById(R.id.background);

        if (position < loadouts.size()) {
            currLoadout = loadouts.get(position);

            //Assign the elements of each loadout to the corresponding class object
            myActivityInterface.setupLoadoutViews((ImageView)listItem.findViewById(R.id.class_view), (ImageView)listItem.findViewById(R.id.weapon_view),
                    (ImageView)listItem.findViewById(R.id.ability_view), (ImageView)listItem.findViewById(R.id.armor_view),
                    (ImageView)listItem.findViewById(R.id.ring_view), (TextView)listItem.findViewById(R.id.total_att),
                    (TextView)listItem.findViewById(R.id.total_dex), (ConstraintLayout)listItem.findViewById(R.id.status),
                    (Button)listItem.findViewById(R.id.delete), position);

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

        return listItem;
    }
}


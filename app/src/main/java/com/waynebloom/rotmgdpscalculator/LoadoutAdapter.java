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
import java.util.List;

public class LoadoutAdapter extends RecyclerView.Adapter<LoadoutAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Loadout> loadouts;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView classView;
        private final ImageView weaponView;
        private final ImageView abilityView;
        private final ImageView armorView;
        private final ImageView ringView;
        private final TextView attView;
        private final TextView dexView;
        private final ConstraintLayout statusView;
        private final Button deleteButton;
        private final ConstraintLayout background;

        public ViewHolder(View view) {
            super(view);
            classView = view.findViewById(R.id.class_view);
            weaponView = view.findViewById(R.id.weapon_view);
            abilityView = view.findViewById(R.id.ability_view);
            armorView = view.findViewById(R.id.armor_view);
            ringView = view.findViewById(R.id.ring_view);
            attView = view.findViewById(R.id.total_att);
            dexView = view.findViewById(R.id.total_dex);
            statusView = view.findViewById(R.id.status);
            deleteButton = view.findViewById(R.id.delete);
            background = view.findViewById(R.id.background);
        }
    }

    LoadoutAdapter(Context context, ArrayList<Loadout> data) {
        loadouts = data;
        mContext = context;
    }

    @NonNull
    @Override
    public LoadoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LoadoutAdapter.ViewHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_loadout, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Assign different background colors
        switch (position) {
            case 0:
                holder.background.setBackgroundResource(R.drawable.bg_loadout_red);
                break;

            case 1:
                holder.background.setBackgroundResource(R.drawable.bg_loadout_orange);
                break;

            case 2:
                holder.background.setBackgroundResource(R.drawable.bg_loadout_yellow);
                break;

            case 3:
                holder.background.setBackgroundResource(R.drawable.bg_loadout_green);
                break;

            case 4:
                holder.background.setBackgroundResource(R.drawable.bg_loadout_cyan);
                break;

            case 5:
                holder.background.setBackgroundResource(R.drawable.bg_loadout_blue);
                break;

            case 6:
                holder.background.setBackgroundResource(R.drawable.bg_loadout_purple);
                break;

            case 7:
                holder.background.setBackgroundResource(R.drawable.bg_loadout_black);
                break;
        }

        // Sends holder view references to the loadout class
        Loadout currentLoadout = loadouts.get(position);
        currentLoadout.setViews(this, holder.classView, holder.weaponView, holder.abilityView, holder.armorView, holder.ringView, holder.attView, holder.dexView, holder.statusView, holder.deleteButton);
    }

    @Override
    public int getItemCount() {
        return loadouts.size();
    }

    public void removeAt(int position) {
        loadouts.remove(position);
        if(position != loadouts.size()) {
            for(int loadoutPos = position; loadoutPos < loadouts.size(); loadoutPos++) {
                loadouts.get(loadoutPos).setLoadoutId(loadoutPos);
            }
        }
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, loadouts.size());
    }
}


// Heavy structural help from https://gist.github.com/fjfish/3024308

package com.waynebloom.rotmgdpscalculator;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private final List<Item> originalData;
    private List<Item> filteredData;
    private final Typeface myFont;
    private final Context mContext;
    private final Loadout callingLoadout;
    private final int type;
    private final ItemFilter mFilter = new ItemFilter();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView dmg;
        TextView rof;
        TextView shots;
        View parent;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.item_picture);
            name = view.findViewById(R.id.item_name);
            /*dmg = view.findViewById(R.id.item_dmg);
            rof = view.findViewById(R.id.item_rof);
            shots = view.findViewById(R.id.item_shots);*/
            parent = view;
        }
    }

    ItemAdapter(Context context, List<Item> data, Loadout callingLoadout, int type) {
        originalData = data;
        filteredData = data;
        myFont = Typeface.createFromAsset(context.getAssets(), "myfont.ttf");
        mContext = context;
        this.callingLoadout = callingLoadout;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                        .from(mContext)
                        .inflate(R.layout.list_item_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        populateFields(holder, filteredData.get(position));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingLoadout.informSelected(filteredData.get(position), null, type);
                callingLoadout.makeSelectorViewGone();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }


    private void populateFields(ViewHolder mViewHolder, Item currentItem) {
        final int MAX_NAME_LENGTH = 27;
        final float FONT_SIZE_NAME = 10;
        final float FONT_SIZE_DESC = 8;

        mViewHolder.image.setImageResource(currentItem.getImageId());

        // Fit the name in the UI by capping its length, with ... to note that the full name isn't shown
        String temp = currentItem.getName();
        if(temp.length() > MAX_NAME_LENGTH) {
            temp = temp.substring(0, MAX_NAME_LENGTH - 3) + "...";
        }
        mViewHolder.name.setText(temp);
        mViewHolder.name.setTypeface(myFont);
        mViewHolder.name.setTextSize(FONT_SIZE_NAME);

       /* mViewHolder.dmg.setText(String.format(Locale.US,"%.2f", currentItem.getAvgDamage()));
        mViewHolder.dmg.setTypeface(myFont);
        mViewHolder.dmg.setTextSize(FONT_SIZE_DESC);

        mViewHolder.rof.setText(String.format(Locale.US,"%.2f", currentItem.getRateOfFire()));
        mViewHolder.rof.setTypeface(myFont);
        mViewHolder.rof.setTextSize(FONT_SIZE_DESC);

        mViewHolder.shots.setText(String.format(Locale.US,"%d", currentItem.getNoOfShots()));
        mViewHolder.shots.setTypeface(myFont);
        mViewHolder.shots.setTextSize(FONT_SIZE_DESC);*/
    }

    public void enactCategories(List<String> mCategories) {
        mFilter.publishResults(mFilter.performFiltering(mCategories));
    }

    private class ItemFilter {

        protected List<Item> performFiltering(List<String> mCategories) {

            ArrayList<Item> results = new ArrayList<>();

            for(Item i : originalData) {
                boolean match = true;
                for(String itemCat : i.getCategories()) {               // The item's categories
                    boolean found = false;
                    for(String selectedCat : mCategories) {             // The selected categories
                        if(selectedCat.equals(itemCat)) {               // Category is found
                            found = true;
                            break;
                        }                                               // If not found, 'found' remains false
                    }
                    if(!found) {
                        match = false;                                  // Change match to false and immediately break if any category is not found
                        break;
                    }
                }
                if(match) {                                             // If no category is not found, match remains true and the item is added to results
                    results.add(i);
                }
            }

            return results;
        }

        protected void publishResults(List<Item> results) {
            filteredData = results;
            notifyDataSetChanged();
        }

    }
}

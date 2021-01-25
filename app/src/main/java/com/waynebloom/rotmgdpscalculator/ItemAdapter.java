// Heavy structural help from https://gist.github.com/fjfish/3024308

package com.waynebloom.rotmgdpscalculator;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> originalData;
    private List<Item> filteredData;
    private Typeface myFont;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView dmg;
        TextView rof;
        TextView shots;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.item_picture);
            name = view.findViewById(R.id.item_name);
            dmg = view.findViewById(R.id.item_dmg);
            rof = view.findViewById(R.id.item_rof);
            shots = view.findViewById(R.id.item_shots);
        }
    }

    ItemAdapter(Context context, List<Item> data) {
        originalData = data;
        filteredData = data;
        myFont = Typeface.createFromAsset(context.getAssets(), "myfont.ttf");
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
        return filteredData.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_itemsel, parent, false);

            // Creates a ViewHolder
            viewHolder = new ViewHolder(convertView);

            // Bind the data efficiently with the holder.
            convertView.setTag(viewHolder);

        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(position < filteredData.size()) {
            populateFields(viewHolder, filteredData.get(position));
        }

        return convertView;
    }

    private void populateFields(ViewHolder mViewHolder, Item currentItem) {
        final int MAX_NAME_LENGTH = 29;
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

        mViewHolder.dmg.setText(String.format(Locale.US,"%.2f", currentItem.getAvgDmg()));
        mViewHolder.dmg.setTypeface(myFont);
        mViewHolder.dmg.setTextSize(FONT_SIZE_DESC);

        mViewHolder.rof.setText(String.format(Locale.US,"%.2f", currentItem.getRateOfFire()));
        mViewHolder.rof.setTypeface(myFont);
        mViewHolder.rof.setTextSize(FONT_SIZE_DESC);

        mViewHolder.shots.setText(String.format(Locale.US,"%d", currentItem.getNoOfShots()));
        mViewHolder.shots.setTypeface(myFont);
        mViewHolder.shots.setTextSize(FONT_SIZE_DESC);
    }

    public void enactCategories(List<String> mCategories) {
        mFilter.publishResults(mFilter.performFiltering(mCategories));
    }

    private class ItemFilter {

        protected List<Item> performFiltering(List<String> mCategories) {

            final List<Item> itemList = originalData;
            ArrayList<Item> results = new ArrayList<>();

            for(Item i : itemList) {
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

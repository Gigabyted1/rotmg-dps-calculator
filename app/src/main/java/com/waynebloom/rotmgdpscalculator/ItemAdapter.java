package com.waynebloom.rotmgdpscalculator;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class ItemAdapter extends ArrayAdapter<Item> {
    private Context dContext;
    private ArrayList<Item> itemList;
    private Typeface myFont;

    ItemAdapter(Context context, ArrayList<Item> data) {
        super(context, R.layout.list_item_itemsel, data);
        dContext = context;
        itemList = data;
        myFont = Typeface.createFromAsset(context.getAssets(), "myfont.ttf");
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        Item currentItem;

        if (listItem == null) {
            listItem = LayoutInflater.from(dContext).inflate(R.layout.list_item_itemsel, parent, false);
        }

        if (position < itemList.size()) {
            currentItem = itemList.get(position);

            ImageView picture = listItem.findViewById(R.id.item_picture);
            picture.setImageResource(currentItem.imageId);

            TextView name = listItem.findViewById(R.id.item_name);
            String temp1 = currentItem.name;
            if(temp1.length() > 29) {
                temp1 = temp1.substring(0, 26);
                temp1 += "...";
            }
            name.setText(temp1);
            name.setTypeface(myFont);
            name.setTextSize(10f);

            TextView dmg = listItem.findViewById(R.id.item_dmg);
            dmg.setText(String.format(Locale.US,"%.2f", currentItem.avgDmg));
            dmg.setTypeface(myFont);
            dmg.setTextSize(8f);

            TextView rof = listItem.findViewById(R.id.item_rof);
            rof.setText(String.format(Locale.US,"%.2f", currentItem.rateOfFire));
            rof.setTypeface(myFont);
            rof.setTextSize(8f);

            TextView shots = listItem.findViewById(R.id.item_shots);
            shots.setText(String.format(Locale.US,"%d", currentItem.noOfShots));
            shots.setTypeface(myFont);
            shots.setTextSize(8f);
        }
        return listItem;
    }
}

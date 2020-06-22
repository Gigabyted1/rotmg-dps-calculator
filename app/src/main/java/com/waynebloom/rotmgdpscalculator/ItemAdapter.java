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
            name.setText(currentItem.name);

            name.setTypeface(myFont);
            name.setTextSize(8f);
        }
        return listItem;
    }
}

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

public class ClassAdapter extends ArrayAdapter<CharClass> {
    private Context dContext;
    private ArrayList<CharClass> classList;
    private Typeface myFont;

    ClassAdapter(Context context, ArrayList<CharClass> data) {
        super(context, R.layout.list_item_itemsel, data);
        dContext = context;
        classList = data;
        myFont = Typeface.createFromAsset(context.getAssets(), "myfont.ttf");
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        CharClass currClass;

        if (listItem == null) {
            listItem = LayoutInflater.from(dContext).inflate(R.layout.list_item_classsel, parent, false);
        }

        if (position < classList.size()) {
            currClass = classList.get(position);

            ImageView picture = listItem.findViewById(R.id.class_picture);
            picture.setImageResource(currClass.imageId);

            TextView name = listItem.findViewById(R.id.class_name);
            name.setText(currClass.name);

            name.setTypeface(myFont);
            name.setTextSize(12f);
        }
        return listItem;
    }
}


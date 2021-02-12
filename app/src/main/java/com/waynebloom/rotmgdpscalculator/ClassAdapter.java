package com.waynebloom.rotmgdpscalculator;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private final Context mContext;
    private final List<CharClass> data;
    private final Loadout callingLoadout;
    private final Typeface myFont;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        View parent;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.class_picture);
            name = view.findViewById(R.id.class_name);
            parent = view;

        }
    }

    ClassAdapter(Context context, List<CharClass> data, Loadout callingLoadout) {
        this.data = data;
        this.callingLoadout = callingLoadout;
        mContext = context;
        myFont = Typeface.createFromAsset(context.getAssets(), "myfont.ttf");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClassAdapter.ViewHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_classsel, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        populateFields(holder, data.get(position));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingLoadout.informSelected(null, data.get(position), Loadout.CLASS);
                callingLoadout.makeSelectorViewGone();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void populateFields(ViewHolder holder, CharClass mClass) {
        holder.image.setImageResource(mClass.getImageId());
        holder.name.setText(mClass.getName());
        holder.name.setTypeface(myFont);
        holder.name.setTextSize(12f);
    }
}


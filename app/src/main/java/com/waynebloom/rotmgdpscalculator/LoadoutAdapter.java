package com.waynebloom.rotmgdpscalculator;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class LoadoutAdapter extends ArrayAdapter<Loadout> {
    private Context dContext;
    private ListView itemSelView;
    private ArrayList<Loadout> loadouts;
    private ArrayList<CharClass> classes;
    private ClickListeners myActivityInterface;
    private Typeface myFont;

    public LoadoutAdapter(Context context, ListView listView, ArrayList<Loadout> data, ArrayList<CharClass> classData, ClickListeners inter) {
        super(context, R.layout.list_item_loadout, data);
        dContext = context;
        loadouts = data;
        itemSelView = listView;
        classes = classData;
        myActivityInterface = inter;
        myFont = Typeface.createFromAsset(context.getAssets(), "myfont.ttf");
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        View listItem = convertView;
        String temp;

        if (listItem == null) {
            listItem = LayoutInflater.from(dContext).inflate(R.layout.list_item_loadout, parent, false);
        }

        final Loadout currLoadout;
        final ConstraintLayout background = listItem.findViewById(R.id.background);
        final ImageView classView = listItem.findViewById(R.id.class_view);
        final ImageView wepView = listItem.findViewById(R.id.weapon_view);
        final ImageView abilView = listItem.findViewById(R.id.ability_view);
        final ImageView armView = listItem.findViewById(R.id.armor_view);
        final ImageView ringView = listItem.findViewById(R.id.ring_view);
        final TextView attView = listItem.findViewById(R.id.total_att);
        final TextView dexView = listItem.findViewById(R.id.total_dex);
        final Button delButton = listItem.findViewById(R.id.delete);

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

        if (position < loadouts.size()) {
            currLoadout = loadouts.get(position);
            classView.setImageResource(currLoadout.charClass.imageId);
            wepView.setImageResource(currLoadout.wep.imageId);
            abilView.setImageResource(currLoadout.abil.imageId);
            armView.setImageResource(currLoadout.arm.imageId);
            ringView.setImageResource(currLoadout.ring.imageId);

            classView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClassAdapter classSelAdpt = new ClassAdapter(dContext.getApplicationContext(), classes);
                    itemSelView.setAdapter(classSelAdpt);
                    itemSelView.setVisibility(View.VISIBLE);

                    itemSelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String temp;
                            currLoadout.charClass = classes.get(position);
                            currLoadout.classId = position;
                            currLoadout.baseAtt = currLoadout.charClass.baseAtt;
                            currLoadout.baseDex = currLoadout.charClass.baseDex;
                            currLoadout.updateStats();
                            temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                            attView.setText(temp);
                            temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                            dexView.setText(temp);

                            if(!currLoadout.wep.subType.equals(currLoadout.charClass.weps.get(0).subType)) {
                                wepView.setImageResource(currLoadout.charClass.weps.get(0).imageId);
                                currLoadout.wep = currLoadout.charClass.weps.get(0);
                                currLoadout.wepId = 0;
                            }
                            if(!currLoadout.abil.subType.equals(currLoadout.charClass.abils.get(0).subType)) {
                                abilView.setImageResource(currLoadout.charClass.abils.get(0).imageId);
                                currLoadout.abil = currLoadout.charClass.abils.get(0);
                                currLoadout.abilId = 0;
                            }
                            if(!currLoadout.arm.subType.equals(currLoadout.charClass.arms.get(0).subType)) {
                                armView.setImageResource(currLoadout.charClass.arms.get(0).imageId);
                                currLoadout.arm = currLoadout.charClass.arms.get(0);
                                currLoadout.armId = 0;
                            }

                            itemSelView.setVisibility(View.GONE);
                            classView.setImageResource(classes.get(position).imageId);
                        }
                    });
                }
            });

            currLoadout.updateStats();

            temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
            attView.setText(temp);

            temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
            dexView.setText(temp);

            myActivityInterface.onAttClicked(attView, currLoadout);
            myActivityInterface.onDexClicked(dexView, currLoadout);
            myActivityInterface.onDelClicked(delButton, currLoadout);

            wepView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemListOffset = currLoadout.wepId;
                    ItemAdapter itemSelAdpt = new ItemAdapter(dContext.getApplicationContext(), currLoadout.charClass.weps);
                    itemSelView.setAdapter(itemSelAdpt);
                    itemSelView.setX(-1000f);
                    itemSelView.setVisibility(View.VISIBLE);
                    itemSelView.animate().translationXBy(1000f).setDuration(500);
                    itemSelView.setSelectionFromTop(itemListOffset, 0);

                    itemSelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String temp;
                            currLoadout.wep = currLoadout.charClass.weps.get(position);
                            currLoadout.wepId = position;
                            currLoadout.updateStats();

                            temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                            attView.setText(temp);
                            temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                            dexView.setText(temp);

                            itemSelView.setVisibility(View.GONE);
                            wepView.setImageResource(currLoadout.wep.imageId);
                        }
                    });
                }
            });


            abilView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemListOffset = currLoadout.abilId;
                    ItemAdapter itemSelAdpt = new ItemAdapter(dContext.getApplicationContext(), currLoadout.charClass.abils);
                    itemSelView.setAdapter(itemSelAdpt);
                    itemSelView.setVisibility(View.VISIBLE);
                    itemSelView.setSelectionFromTop(itemListOffset, 0);

                    itemSelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String temp;
                            currLoadout.abil = currLoadout.charClass.abils.get(position);
                            currLoadout.abilId = position;
                            currLoadout.updateStats();

                            temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                            attView.setText(temp);
                            temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                            dexView.setText(temp);

                            itemSelView.setVisibility(View.GONE);
                            abilView.setImageResource(currLoadout.abil.imageId);
                        }
                    });
                }
            });


            armView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemListOffset = currLoadout.armId;
                    ItemAdapter itemSelAdpt = new ItemAdapter(dContext.getApplicationContext(), currLoadout.charClass.arms);
                    itemSelView.setAdapter(itemSelAdpt);
                    itemSelView.setVisibility(View.VISIBLE);
                    itemSelView.setSelectionFromTop(itemListOffset, 0);

                    itemSelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String temp;
                            currLoadout.arm = currLoadout.charClass.arms.get(position);
                            currLoadout.armId = position;
                            currLoadout.updateStats();

                            temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                            attView.setText(temp);
                            temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                            dexView.setText(temp);

                            itemSelView.setVisibility(View.GONE);
                            armView.setImageResource(currLoadout.arm.imageId);
                        }
                    });
                }
            });

            ringView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemListOffset = currLoadout.ringId;
                    ItemAdapter itemSelAdpt = new ItemAdapter(dContext.getApplicationContext(), currLoadout.charClass.rings);
                    itemSelView.setAdapter(itemSelAdpt);
                    itemSelView.setVisibility(View.VISIBLE);
                    itemSelView.setSelectionFromTop(itemListOffset, 0);

                    itemSelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String temp;
                            currLoadout.ring = currLoadout.charClass.rings.get(position);
                            currLoadout.ringId = position;
                            currLoadout.updateStats();

                            temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                            attView.setText(temp);
                            temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                            dexView.setText(temp);

                            itemSelView.setVisibility(View.GONE);
                            ringView.setImageResource(currLoadout.ring.imageId);
                        }
                    });
                }
            });
        }
        return listItem;
    }
}


package com.waynebloom.rotmgdpscalculator;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class Loadout {
    private Context context;

    CharClass charClass;
    Item wep;
    Item abil;
    Item arm;
    Item ring;
    int baseAtt;
    int baseDex;
    int totalAtt;
    int totalDex;
    boolean[] activeEffects;    //Tracks active status effects

    private int loadoutId;
    int classId;
    int wepId;
    int abilId;
    int armId;
    int ringId;

    private ImageView classView;
    private ImageView wepView;
    private ImageView abilView;
    private ImageView armView;
    private ImageView ringView;
    private TextView attView;
    private TextView dexView;
    private ConstraintLayout statusView;
    private ImageView damagingView;
    private ImageView berserkView;
    private ImageView curseView;
    private ImageView dazedView;
    private ImageView weakView;
    private Button deleteView;

    Loadout(Context con, CharClass c, int cId, int a, int d, Item w, int wId, Item ab, int abId, Item ar, int arId, Item r, int rId, String stat, int lId) {
        context = con;
        charClass = c;
        classId = cId;
        loadoutId = lId;
        baseAtt = a;
        baseDex = d;
        totalAtt = baseAtt;
        totalDex = baseDex;
        wep = w;
        wepId = wId;
        abil = ab;
        abilId = abId;
        arm = ar;
        armId = arId;
        ring = r;
        ringId = rId;
        activeEffects = new boolean[5];

        for(int i = 0; i < activeEffects.length; i++) {
            activeEffects[i] = stat.charAt(i) != '0';
        }
    }
    /*Loadout(Context con, int cId, int wId, int abId, int arId, int rId, String stat, int lId) {
        context = con;
        loadoutId = lId;
        classId = cId;
        wepId = wId;
        abilId = abId;
        armId = arId;
        ringId = rId;
        activeEffects = new boolean[5];

        for (int i = 0; i < activeEffects.length; i++) {
            activeEffects[i] = stat.charAt(i) != '0';
        }
    }*/

    void setViews(ImageView cV, ImageView wV, ImageView aV, ImageView arV, ImageView rV, TextView atV, TextView dV, ConstraintLayout sV, Button delV) {
        classView = cV;
        wepView = wV;
        abilView = aV;
        armView = arV;
        ringView = rV;
        attView = atV;
        dexView = dV;
        statusView = sV;
            damagingView = statusView.findViewById(R.id.damaging);
            berserkView = statusView.findViewById(R.id.berserk);
            curseView = statusView.findViewById(R.id.curse);
            dazedView = statusView.findViewById(R.id.dazed);
            weakView = statusView.findViewById(R.id.weak);
        deleteView = delV;
    }

    void updateViews() {
        updateStats();

        classView.setImageResource(charClass.imageId);
        wepView.setImageResource(wep.imageId);
        abilView.setImageResource(abil.imageId);
        armView.setImageResource(arm.imageId);
        ringView.setImageResource(ring.imageId);

        String temp = baseDex + "(" + totalDex + ")";
        dexView.setText(temp);

        temp = baseAtt + "(" + totalAtt + ")";
        attView.setText(temp);

        if(baseDex < charClass.baseDex) {
            dexView.setTextColor(context.getResources().getColor(R.color.colorUnmaxedText));
        } else {
            dexView.setTextColor(context.getResources().getColor(R.color.colorMaxedText));
        }

        if(baseAtt < charClass.baseAtt) {
            attView.setTextColor(context.getResources().getColor(R.color.colorUnmaxedText));
        } else {
            attView.setTextColor(context.getResources().getColor(R.color.colorMaxedText));
        }

        if(!activeEffects[0]) {
            damagingView.setColorFilter(Color.parseColor("#777777"));
        } else {
            damagingView.setColorFilter(null);
        }

        if(!activeEffects[1]) {
            berserkView.setColorFilter(Color.parseColor("#777777"));
        } else {
            berserkView.setColorFilter(null);
        }

        if(!activeEffects[2]) {
            curseView.setColorFilter(Color.parseColor("#777777"));
        } else {
            curseView.setColorFilter(null);
        }

        if(!activeEffects[3]) {
            dazedView.setColorFilter(Color.parseColor("#777777"));
        } else {
            dazedView.setColorFilter(null);
        }

        if(!activeEffects[4]) {
            weakView.setColorFilter(Color.parseColor("#777777"));
        } else {
            weakView.setColorFilter(null);
        }
    }

    void updateStats() {
        totalAtt = baseAtt;
        totalDex = baseDex;

        totalAtt += wep.addedAtt;
        totalDex += wep.addedDex;

        totalAtt += abil.addedAtt;
        totalDex += abil.addedDex;

        totalAtt += arm.addedAtt;
        totalDex += arm.addedDex;

        totalAtt += ring.addedAtt;
        totalDex += ring.addedDex;
    }

    public ArrayList<DpsEntry> generateDps() {
        ArrayList<DpsEntry> dpsTable = new ArrayList<>();
        updateStats();

        final float DEFENSE_DMG_REDUCTION_CAP = 0.85f;
        final int MAX_ENEMY_DEFENSE = 150;

        int realAtt = totalAtt;
        int realDex = totalDex;
        double bers = 1;
        double damag = 1;
        double curse = 1;

        // Turns on status effects

        if(activeEffects[0]) {  // Damaging
            damag = 1.5;
        }
        if(activeEffects[1]) {  // Berserk
            bers = 1.5;
        }
        if(activeEffects[2]) {  // Curse
            curse = 1.2;
        }
        if(activeEffects[3]) {  // Dazed (cancels berserk and sets dex to 0)
            bers = 0;
            realDex = 0;
        }
        if(activeEffects[4]) {  // Weak (cancels damaging and sets att to 0)
            damag = 0;
            realAtt = 0;
        }

        for(int i = 0; i <= MAX_ENEMY_DEFENSE; i++) {   // Generate a table row for every defense level up to the max
            double temp = 0;

            if(wep.attribute == 0) {          // Regular equation
                temp = (((wep.avgDmg * (0.5 + realAtt / 50.0)) - i) * curse * damag * wep.noOfShots) * ((1.5 + 6.5 * (realDex / 75.0)) * bers * wep.rateOfFire);
            }
            else if (wep.attribute == 1) {    // For armor piercing (removes enemy defense)
                temp = ((wep.avgDmg * (0.5 + realAtt / 50.0)) * curse * damag * wep.noOfShots) * ((1.5 + 6.5 * (realDex / 75.0)) * bers * wep.rateOfFire);
            }

            if(temp > DEFENSE_DMG_REDUCTION_CAP * wep.avgDmg * wep.noOfShots) {      // Defense cap check (defense can only limit damage up to 85%)
                dpsTable.add(new DpsEntry(temp, loadoutId));
            }
            else {
                dpsTable.add(new DpsEntry(DEFENSE_DMG_REDUCTION_CAP * wep.avgDmg * wep.noOfShots, loadoutId));
            }
        }

        return dpsTable;
    }
}

package com.waynebloom.rotmgdpscalculator;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class Loadout {
    Context context;
    CharClass charClass;
    int classId;
    int loadoutId;
    int baseAtt;
    int baseDex;
    int totalAtt;
    int totalDex;
    ArrayList<DpsEntry> dpsTable = new ArrayList<>();
    Item wep;
    int wepId;
    int wepAttr;
    Item abil;
    int abilId;
    Item arm;
    int armId;
    Item ring;
    int ringId;
    String[] statusEffects;
    boolean[] checkedItems;

    public Loadout (Context con, CharClass c, int cId, int a, int d, Item w, int wId, Item ab, int abId, Item ar, int arId, Item r, int rId, String stat, int lId) {
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
        wepAttr = w.attribute;
        abil = ab;
        abilId = abId;
        arm = ar;
        armId = arId;
        ring = r;
        ringId = rId;
        statusEffects = context.getResources().getStringArray(R.array.stat_effects);
        checkedItems = new boolean[statusEffects.length];

        for(int i = 0; i < checkedItems.length; i++) {
            if(stat.charAt(i) == '0') {
                checkedItems[i] = false;
            }
            else {
                checkedItems[i] = true;
            }
        }
    }

    public void updateStats () {
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

        wepAttr = wep.attribute;
    }

    public ArrayList<DpsEntry> generateDps () {
        dpsTable = new ArrayList<>();
        updateStats();

        int realAtt = totalAtt;
        int realDex = totalDex;
        double bers = 1;
        double damag = 1;
        double curse = 1;

        if(checkedItems[0]) {
            damag = 1.5;
        }
        if(checkedItems[1]) {
            bers = 1.5;
        }
        if(checkedItems[2]) {
            curse = 1.2;
        }
        if(checkedItems[3]) {
            realDex = 0;
        }
        if(checkedItems[4]) {
            realAtt = 0;
        }

        for(int i = 0; i <= 150; i++) {
            double temp = 0;

            if(wepAttr == 0) {          //Regular equation
                temp = (((wep.avgDmg * (0.5 + realAtt / 50.0)) - i) * curse * damag * wep.noOfShots) * ((1.5 + 6.5 * (realDex / 75.0)) * bers * wep.rateOfFire);
            }
            else if (wepAttr == 1) {    //For AP
                temp = ((wep.avgDmg * (0.5 + realAtt / 50.0)) * curse * damag * wep.noOfShots) * ((1.5 + 6.5 * (realDex / 75.0)) * bers * wep.rateOfFire);
            }

            if(temp > 0.85 * wep.avgDmg * wep.noOfShots) {      //Defense cap check
                dpsTable.add(new DpsEntry(temp, loadoutId));
            }
            else {
                dpsTable.add(new DpsEntry(0.85 * wep.avgDmg * wep.noOfShots, loadoutId));
            }
        }

        return dpsTable;
    }
}

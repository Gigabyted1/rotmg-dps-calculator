package com.waynebloom.rotmgdpscalculator;

import android.util.Log;

import java.util.ArrayList;

public class Loadout {
    CharClass charClass;
    int classId;
    int baseAtt;
    int baseDex;
    int totalAtt;
    int totalDex;
    ArrayList<Double> dpsTable = new ArrayList<>();
    Item wep;
    int wepId;
    int wepAttr;
    Item abil;
    int abilId;
    Item arm;
    int armId;
    Item ring;
    int ringId;

    public Loadout (CharClass c, int cId, int a, int d, Item w, int wId, Item ab, int abId, Item ar, int arId, Item r, int rId) {
        charClass = c;
        classId = cId;
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

    public void generateDps () {
        dpsTable = new ArrayList<>();
        updateStats();
        for(int i = 0; i <= 150; i++) {
            double temp = 0;

            if(wepAttr == 0) {      //Regular equation
                temp = (((wep.avgDmg * (0.5 + totalAtt / 50.0)) - i) * wep.noOfShots) * ((1.5 + 6.5 * (totalDex / 75.0)) * wep.rateOfFire);
            }
            else if (wepAttr == 1) {
                temp = ((wep.avgDmg * (0.5 + totalAtt / 50.0)) * wep.noOfShots) * ((1.5 + 6.5 * (totalDex / 75.0)) * wep.rateOfFire);
            }

            if(temp > 0.85 * wep.avgDmg * wep.noOfShots) {
                dpsTable.add(temp);
            }
            else {
                dpsTable.add(0.85 * wep.avgDmg * wep.noOfShots);
            }
        }
    }
}

package com.waynebloom.rotmgdpscalculator;

import android.graphics.drawable.Drawable;

public class Item {
    String name;
    String type;
    String subType;
    int imageId;
    int addedAtt;
    int addedDex;
    double avgDmg;
    int noOfShots;
    double rateOfFire;
    double range;
    int attribute;

    public Item (String n, String t, String s, int id, int a, int d, double dmg, int shots, double rof, double r, int attr) {
        name = n;
        type = t;
        subType = s;
        imageId = id;
        addedAtt = a;
        addedDex = d;
        avgDmg = dmg;
        noOfShots = shots;
        rateOfFire = rof;
        range = r;
        attribute = attr;
    }
}

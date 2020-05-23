package com.waynebloom.rotmgdpscalculator;

import java.util.ArrayList;

public class CharClass {
    String name;
    ArrayList<Item> weps;
    ArrayList<Item> abils;
    ArrayList<Item> arms;
    ArrayList<Item> rings;
    int imageId;
    int baseAtt;
    int baseDex;

    public CharClass (String n, ArrayList<Item> w, ArrayList<Item> ab, ArrayList<Item> ar, ArrayList<Item> r, int id, int a, int d) {
        name = n;
        weps = w;
        abils = ab;
        arms = ar;
        rings = r;
        imageId = id;
        baseAtt = a;
        baseDex = d;
    }
}

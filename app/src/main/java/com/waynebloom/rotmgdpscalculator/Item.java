package com.waynebloom.rotmgdpscalculator;

import java.util.ArrayList;

class Item {
    String name;
    int imageId;
    int addedAtt;
    int addedDex;
    ArrayList<String> categories;
    double avgDmg;
    int noOfShots;
    double rateOfFire;
    double range;
    int attribute;

    Item (String n, int id, int a, int d, String c, double dmg, int shots, double rof, double r, int attr) {
        name = n;
        imageId = id;
        addedAtt = a;
        addedDex = d;
        categories = parseCategories(c);
        avgDmg = dmg;
        noOfShots = shots;
        rateOfFire = rof;
        range = r;
        attribute = attr;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public double getAvgDmg() {
        return avgDmg;
    }

    public int getNoOfShots() {
        return noOfShots;
    }

    public double getRateOfFire() {
        return rateOfFire;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    // Translates raw string data from file into an arrayList
    private ArrayList<String> parseCategories(String input) {
        ArrayList<String> output = new ArrayList<>();

        for (int i = 1; i < input.length(); i++) {
            if(input.charAt(i - 1) == '"' && input.charAt(i) != ',' && input.charAt(i) != '[' && input.charAt(i) != ']') {
                StringBuilder temp = new StringBuilder();
                while(input.charAt(i) != '"') {
                    temp.append(input.charAt(i));
                    if(i < input.length() - 1)
                        i++;
                    else
                        break;
                }
                output.add(temp.toString());
            }
        }
        return output;
    }
}

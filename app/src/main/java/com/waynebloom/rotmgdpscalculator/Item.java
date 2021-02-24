package com.waynebloom.rotmgdpscalculator;

import java.util.ArrayList;
import java.util.List;

class Item {

    // Qualitative info
    private final String name;
    private final int relItemId;
    private final int absItemId;
    private final int imageId;
    private final List<String> categories;

    // Item stats
    private final StatBonus statBonus;
    private final double avgDamage;
    private final int noOfShots;
    private final double rateOfFire;
    private final double range;
    private final int attribute;    // Such as armor piercing

    // Item set info
    private static List<ItemSet> itemSets;
    private int partOfSet;

    Item (String name, int relItemId, int absItemId, int imageId, StatBonus statBonus, String categories, double avgDamage, int noOfShots, double rateOfFire, double range, int attribute) {
        this.name = name;
        this.relItemId = relItemId;
        this.absItemId = absItemId;
        this.imageId = imageId;
        this.statBonus = statBonus;
        this.categories = parseCategories(categories);
        this.avgDamage = avgDamage;
        this.noOfShots = noOfShots;
        this.rateOfFire = rateOfFire;
        this.range = range;
        this.attribute = attribute;
    }

    public String getName() {
        return name;
    }

    public int getRelItemId() {
        return relItemId;
    }

    public int getImageId() {
        return imageId;
    }

    public StatBonus getStatBonus() {
        return statBonus;
    }

    public double getAvgDamage() {
        return avgDamage;
    }

    public int getNoOfShots() {
        return noOfShots;
    }

    public double getRateOfFire() {
        return rateOfFire;
    }

    public int getAttribute() {
        return attribute;
    }

    public List<String> getCategories() {
        return categories;
    }

    // Translates raw string data from file into an arrayList
    private List<String> parseCategories(String input) {
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

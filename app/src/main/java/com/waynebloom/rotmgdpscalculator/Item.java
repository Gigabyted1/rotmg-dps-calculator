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
    public static List<ItemSet> itemSets;
    private final int partOfSet;

    Item (String name, int imageId, String categories, int relItemId, int absItemId, int partOfSet, StatBonus statBonus, double avgDamage, int noOfShots, double rateOfFire, double range, int attribute) {
        this.name = name;
        this.imageId = imageId;
        this.categories = parseCategories(categories);
        this.relItemId = relItemId;
        this.absItemId = absItemId;
        this.partOfSet = partOfSet;
        this.statBonus = statBonus;
        this.avgDamage = avgDamage;
        this.noOfShots = noOfShots;
        this.rateOfFire = rateOfFire;
        this.range = range;
        this.attribute = attribute;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public List<String> getCategories() {
        return categories;
    }

    public int getRelItemId() {
        return relItemId;
    }

    public int getAbsItemId() {
        return absItemId;
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

    public int getPartOfSet() {
        return partOfSet;
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

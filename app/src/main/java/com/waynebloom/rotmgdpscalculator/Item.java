package com.waynebloom.rotmgdpscalculator;

import java.util.ArrayList;
import java.util.List;

class Item {
    private final String name;
    private final int itemId;
    private final int imageId;
    private final int bonusAtt;
    private final int bonusDex;
    private final List<String> categories;
    private final double avgDamage;
    private final int noOfShots;
    private final double rateOfFire;
    private final double range;
    private final int attribute;    // Such as armor piercing

    Item (String name, int itemId, int imageId, int bonusAtt, int bonusDex, String categories, double avgDamage, int noOfShots, double rateOfFire, double range, int attribute) {
        this.name = name;
        this.itemId = itemId;
        this.imageId = imageId;
        this.bonusAtt = bonusAtt;
        this.bonusDex = bonusDex;
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

    public int getItemId() {
        return itemId;
    }

    public int getImageId() {
        return imageId;
    }

    public int getBonusAtt() {
        return bonusAtt;
    }

    public int getBonusDex() {
        return bonusDex;
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

package com.waynebloom.rotmgdpscalculator;

import java.util.List;

class CharClass {
    private final String name;
    private final int classId;
    private final List<Item> weapons;
    private final List<Item> abilities;
    private final List<Item> armors;
    private final List<Item> rings;
    private final int imageId;
    private final StatBonus maxedStats;

    CharClass (String name, int classId, List<Item> weapons, List<Item> abilities, List<Item> armors, List<Item> rings, int imageId, StatBonus maxedStats) {
        this.name = name;
        this.classId = classId;
        this.weapons = weapons;
        this.abilities = abilities;
        this.armors = armors;
        this.rings = rings;
        this.imageId = imageId;
        this.maxedStats = maxedStats;
    }

    public String getName() {
        return name;
    }

    public int getClassId() {
        return classId;
    }

    public List<Item> getWeapons() {
        return weapons;
    }

    public List<Item> getAbilities() {
        return abilities;
    }

    public List<Item> getArmors() {
        return armors;
    }

    public List<Item> getRings() {
        return rings;
    }

    public int getImageId() {
        return imageId;
    }

    public StatBonus getMaxedStats() {
        return maxedStats;
    }
}

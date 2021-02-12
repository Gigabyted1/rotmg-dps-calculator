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
    private final int baseAtt;
    private final int baseDex;

    CharClass (String name, int classId, List<Item> weapons, List<Item> abilities, List<Item> armors, List<Item> rings, int imageId, int baseAtt, int baseDex) {
        this.name = name;
        this.classId = classId;
        this.weapons = weapons;
        this.abilities = abilities;
        this.armors = armors;
        this.rings = rings;
        this.imageId = imageId;
        this.baseAtt = baseAtt;
        this.baseDex = baseDex;
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

    public int getBaseAtt() {
        return baseAtt;
    }

    public int getBaseDex() {
        return baseDex;
    }
}

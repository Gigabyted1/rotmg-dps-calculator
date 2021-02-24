package com.waynebloom.rotmgdpscalculator;

class StatBonus {
    int attBonus;
    int defBonus;
    int spdBonus;
    int dexBonus;
    int vitBonus;
    int wisBonus;
    int lifeBonus;
    int manaBonus;

    StatBonus(int attBonus, int defBonus, int spdBonus, int dexBonus, int vitBonus, int wisBonus, int lifeBonus, int manaBonus) {
        this.attBonus = attBonus;
        this.defBonus = defBonus;
        this.spdBonus = spdBonus;
        this.dexBonus = dexBonus;
        this.vitBonus = vitBonus;
        this.wisBonus = wisBonus;
        this.lifeBonus = lifeBonus;
        this.manaBonus = manaBonus;
    }

    public int getAttBonus() {
        return attBonus;
    }

    public int getDefBonus() {
        return defBonus;
    }

    public int getSpdBonus() {
        return spdBonus;
    }

    public int getDexBonus() {
        return dexBonus;
    }

    public int getVitBonus() {
        return vitBonus;
    }

    public int getWisBonus() {
        return wisBonus;
    }

    public int getLifeBonus() {
        return lifeBonus;
    }

    public int getManaBonus() {
        return manaBonus;
    }
}

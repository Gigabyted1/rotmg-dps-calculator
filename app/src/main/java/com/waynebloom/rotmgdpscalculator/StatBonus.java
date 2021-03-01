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

    StatBonus() {

    }

    StatBonus(int attBonus, int defBonus, int spdBonus, int dexBonus, int wisBonus, int vitBonus, int lifeBonus, int manaBonus) {
        this.attBonus = attBonus;
        this.defBonus = defBonus;
        this.spdBonus = spdBonus;
        this.dexBonus = dexBonus;
        this.wisBonus = wisBonus;
        this.vitBonus = vitBonus;
        this.lifeBonus = lifeBonus;
        this.manaBonus = manaBonus;
    }

    public void addBonus(StatBonus statBonus) {
        attBonus = attBonus + statBonus.getAttBonus();
        defBonus = defBonus + statBonus.getDefBonus();
        spdBonus = spdBonus + statBonus.getSpdBonus();
        dexBonus = dexBonus + statBonus.getDexBonus();
        wisBonus = wisBonus + statBonus.getWisBonus();
        vitBonus = vitBonus + statBonus.getVitBonus();
        lifeBonus = lifeBonus + statBonus.getLifeBonus();
        manaBonus = manaBonus + statBonus.getManaBonus();
    }

    public void subtractBonus(StatBonus statBonus) {
        attBonus = attBonus - statBonus.getAttBonus();
        defBonus = defBonus - statBonus.getDefBonus();
        spdBonus = spdBonus - statBonus.getSpdBonus();
        dexBonus = dexBonus - statBonus.getDexBonus();
        wisBonus = wisBonus - statBonus.getWisBonus();
        vitBonus = vitBonus - statBonus.getVitBonus();
        lifeBonus = lifeBonus - statBonus.getLifeBonus();
        manaBonus = manaBonus - statBonus.getManaBonus();
    }

    public void replaceBonus(StatBonus statBonus) {
        attBonus = statBonus.getAttBonus();
        defBonus = statBonus.getDefBonus();
        spdBonus = statBonus.getSpdBonus();
        dexBonus = statBonus.getDexBonus();
        wisBonus = statBonus.getWisBonus();
        vitBonus = statBonus.getVitBonus();
        lifeBonus = statBonus.getLifeBonus();
        manaBonus = statBonus.getManaBonus();

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

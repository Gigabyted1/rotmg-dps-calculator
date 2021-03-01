package com.waynebloom.rotmgdpscalculator;

import java.util.ArrayList;
import java.util.List;

class ItemSet {
    final static int ROGUE_SKULD = 0;
    final static int ROGUE_VAMPIRE = 1;
    final static int ARCHER_PHANTOM = 2;
    final static int ARCHER_REANIMATED = 39;
    final static int ARCHER_TREASURE = 3;
    final static int ARCHER_VALENTINE = 4;
    final static int ARCHER_GOLD = 5;
    final static int WIZARD_ARCHMAGE = 6;
    final static int WIZARD_PARANORMAL = 28;
    final static int WIZARD_SLURP = 7;
    final static int PRIEST_GEB = 8;
    final static int PRIEST_MADGOD = 9;
    final static int WARRIOR_DRAGON = 10;
    final static int WARRIOR_PIRATE = 11;
    final static int KNIGHT_ORYX = 12;
    final static int KNIGHT_EASTER = 29;
    final static int KNIGHT_NORDIC = 38;
    final static int PALADIN_SWOLL = 13;
    final static int PALADIN_UNHOLY = 30;
    final static int ASSASSIN_FLESH = 14;
    final static int ASSASSIN_ACID = 15;
    final static int NECROMANCER_HOLLOW = 16;
    final static int NECROMANCER_REAPER = 31;
    final static int NECROMANCER_CUBOID = 37;
    final static int HUNTRESS_SWARM = 17;
    final static int HUNTRESS_HORTI = 18;
    final static int TRICKSTER_GOLEM = 33;
    final static int TRICKSTER_SCARECROW = 34;
    final static int MYSTIC_PHYLA = 19;
    final static int MYSTIC_TOTALIA = 32;
    final static int MYSTIC_MAGMA = 20;
    final static int SORCERER_HORRIFIC = 21;
    final static int SORCERER_MAGIC = 22;
    final static int NINJA_RAIJIN = 23;
    final static int NINJA_CRYSTAL = 24;
    final static int SAMURAI_AKUMA = 25;
    final static int SAMURAI_DISCOVER = 26;
    final static int BARD_ANGEL = 27;
    final static int CONSTRUCTION = 35;
    final static int AGENTS = 36;
    final static int ALIEN = 40;
    final static int ORYXMAS = 41;
    final static int MISTAKE = 42;

    int sWeaponId;
    int sAbilityId;
    int sArmorId;
    int sRingId;

    StatBonus[] statBonus = new StatBonus[3];

    ItemSet() {
        // blank constructor for child class to call
    }

    ItemSet(int sWeaponId, int sAbilityId, int sArmorId, int sRingId, StatBonus twoItemBonus, StatBonus threeItemBonus, StatBonus fourItemBonus) {
        this.sWeaponId = sWeaponId;
        this.sAbilityId = sAbilityId;
        this.sArmorId = sArmorId;
        this.sRingId = sRingId;
        this.statBonus[0] = twoItemBonus;
        this.statBonus[1] = threeItemBonus;
        this.statBonus[2] = fourItemBonus;
    }

    public List<Integer> checkSet(int weapon, int ability, int armor, int ring) {
        List<Integer> data = new ArrayList<>();
        int count = 0;

        if(weapon == sWeaponId) {
            count++;
        }
        if(ability == sAbilityId) {
            count++;
        }
        if(armor == sArmorId) {
            count++;
        }
        if(ring == sRingId) {
            count++;
        }

        data.add(count);
        data.add(-1);
        return data;
    }

    public StatBonus getBonus(List<Integer> data) {
        int count = data.get(0);

        if(count <= 1) {
            return new StatBonus(0,0,0,0,0,0,0,0);
        }
        else {
            StatBonus sumStatBonus = new StatBonus(0,0,0,0,0,0,0,0);
            for(int i = 0; i <= count - 2; i++) {
                sumStatBonus.addBonus(statBonus[i]);
            }
            return sumStatBonus;
        }
    }

    /*public int getsWeaponId() {
        return sWeaponId;
    }

    public int getsAbilityId() {
        return sAbilityId;
    }

    public int getsArmorId() {
        return sArmorId;
    }

    public int getsRingId() {
        return sRingId;
    }

    public StatBonus getTwoItemBonus() {
        return twoItemBonus;
    }

    public StatBonus getThreeItemBonus() {
        return threeItemBonus;
    }

    public StatBonus getFourItemBonus() {
        return fourItemBonus;
    }*/
}

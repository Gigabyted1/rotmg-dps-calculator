package com.waynebloom.rotmgdpscalculator;

class ItemSet {
    final static int ROGUE_SKULD = 0;
    final static int ROGUE_VAMPIRE = 1;
    final static int ARCHER_PHANTOM = 2;
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
    final static int PALADIN_SWOLL = 13;
    final static int PALADIN_UNOLY = 30;
    final static int ASSASSIN_FLESH = 14;
    final static int ASSASSIN_ACID = 15;
    final static int NECROMANCER_HOLLOW = 16;
    final static int NECROMANCER_REAPER = 31;
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

    int setWeaponId;
    int setAbilityId;
    int setArmorId;
    int setRingId;

    StatBonus twoItemBonus;
    StatBonus threeItemBonus;
    StatBonus fourItemBonus;

    ItemSet(StatBonus twoItemBonus, StatBonus threeItemBonus, StatBonus fourItemBonus) {
        this.twoItemBonus = twoItemBonus;
        this.threeItemBonus = threeItemBonus;
        this.fourItemBonus = fourItemBonus;
    }
}

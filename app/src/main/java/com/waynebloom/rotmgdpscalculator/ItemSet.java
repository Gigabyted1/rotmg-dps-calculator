package com.waynebloom.rotmgdpscalculator;

import java.util.ArrayList;
import java.util.List;

class ItemSet {
    int sWeaponId;
    int sAbilityId;
    int sArmorId;
    int sRingId;

    final StatBonus[] statBonus = new StatBonus[3];

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
}

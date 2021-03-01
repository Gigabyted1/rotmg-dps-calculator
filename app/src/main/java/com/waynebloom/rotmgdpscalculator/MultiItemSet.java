package com.waynebloom.rotmgdpscalculator;

import java.util.ArrayList;
import java.util.List;

class MultiItemSet extends ItemSet {
    int[] sWeaponIds;
    int[] sAbilityIds;
    int[] sArmorIds;
    int[] sRingIds;

    StatBonus[][] statBonuses;

    MultiItemSet(int[] sWeaponIds, int[] sAbilityIds, int[] sArmorIds, int[] sRingIds, StatBonus[] twoItemBonusList, StatBonus[] threeItemBonusList, StatBonus[] fourItemBonusList) {
        super();
        this.sWeaponIds = sWeaponIds;
        this.sAbilityIds = sAbilityIds;
        this.sArmorIds = sArmorIds;
        this.sRingIds = sRingIds;
        this.statBonuses = new StatBonus[sRingIds.length][twoItemBonusList.length];
        for(int i = 0; i < sRingIds.length; i++) {
            statBonuses[i] = new StatBonus[]{twoItemBonusList[i], threeItemBonusList[i], fourItemBonusList[i]};
        }
    }

    @Override
    public List<Integer> checkSet(int weapon, int ability, int armor, int ring) {
        List<Integer> data = new ArrayList<>();
        int count = 0;
        int whichRing = -1;

        // check each item against all set items of that type
        for(int w : sWeaponIds) {
            if(weapon == w) {
                count++;
            }
        }
        for(int ab : sAbilityIds) {
            if(ability == ab) {
                count++;
            }
        }
        for(int ar : sArmorIds) {
            if(armor == ar) {
                count++;
            }
        }
        for(int i = 0; i < sRingIds.length; i++) {
            if(ring == sRingIds[i]) {
                whichRing = i;
                count++;
            }
        }

        data.add(count);
        data.add(whichRing);
        return data;
    }

    public StatBonus getBonus(List<Integer> data) {
        int count = data.get(0);
        int whichRing = data.get(1);

        // check if a bonus is applicable
        if(whichRing == -1 || count <= 1) {
            return new StatBonus(0,0,0,0,0,0,0,0);
        }
        else {
            // add together all achieved bonuses and return
            StatBonus sumStatBonus = new StatBonus(0,0,0,0,0,0,0,0);
            for(int i = 0; i <= count - 2; i++) {
                sumStatBonus.addBonus(statBonuses[whichRing][i]);
            }
            return sumStatBonus;
        }
    }

    /*public int[] getsWeaponIds() {
        return sWeaponIds;
    }

    public int[] getsAbilityIds() {
        return sAbilityIds;
    }

    public int[] getsArmorIds() {
        return sArmorIds;
    }

    public int[] getsRingIds() {
        return sRingIds;
    }

    public StatBonus[] getTwoItemBonusList() {
        return twoItemBonusList;
    }

    public StatBonus[] getThreeItemBonusList() {
        return threeItemBonusList;
    }

    @Override
    public StatBonus[] getFourItemBonus() {
        return fourItemBonus;
    }*/
}

package com.waynebloom.rotmgdpscalculator

import java.util.*

internal class MultiItemSet(
    var sWeaponIds: IntArray,
    var sAbilityIds: IntArray,
    var sArmorIds: IntArray,
    var sRingIds: IntArray,
    twoItemBonusList: Array<StatBonus?>,
    threeItemBonusList: Array<StatBonus?>,
    fourItemBonusList: Array<StatBonus?>
) : ItemSet() {
    var statBonuses: Array<Array<StatBonus?>>
    override fun checkSet(weapon: Int, ability: Int, armor: Int, ring: Int): List<Int> {
        val data: MutableList<Int> = ArrayList()
        var count = 0
        var whichRing = -1

        // check each item against all set items of that type
        for (w in sWeaponIds) {
            if (weapon == w) {
                count++
            }
        }
        for (ab in sAbilityIds) {
            if (ability == ab) {
                count++
            }
        }
        for (ar in sArmorIds) {
            if (armor == ar) {
                count++
            }
        }
        for (i in sRingIds.indices) {
            if (ring == sRingIds[i]) {
                whichRing = i
                count++
            }
        }
        data.add(count)
        data.add(whichRing)
        return data
    }

    override fun getBonus(data: List<Int>): StatBonus {
        val count = data[0]
        val whichRing = data[1]

        // check if a bonus is applicable
        return if (whichRing == -1 || count <= 1) {
            StatBonus(0, 0, 0, 0, 0, 0, 0, 0)
        } else {
            // add together all achieved bonuses and return
            val sumStatBonus = StatBonus(0, 0, 0, 0, 0, 0, 0, 0)
            for (i in 0..count - 2) {
                sumStatBonus.addBonus(statBonuses[whichRing][i])
            }
            sumStatBonus
        }
    } /*public int[] getsWeaponIds() {
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

    init {
        statBonuses = Array(sRingIds.size) { arrayOfNulls(twoItemBonusList.size) }
        for (i in sRingIds.indices) {
            statBonuses[i] =
                arrayOf(twoItemBonusList[i], threeItemBonusList[i], fourItemBonusList[i])
        }
    }
}
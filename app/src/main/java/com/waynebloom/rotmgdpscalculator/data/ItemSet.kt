package com.waynebloom.rotmgdpscalculator.data

import java.util.*

internal open class ItemSet {
    var sWeaponId = 0
    var sAbilityId = 0
    var sArmorId = 0
    var sRingId = 0
    var statBonus = arrayOfNulls<StatBonus>(3)

    constructor() {
        // blank constructor for child class to call
    }

    constructor(
        sWeaponId: Int,
        sAbilityId: Int,
        sArmorId: Int,
        sRingId: Int,
        twoItemBonus: StatBonus?,
        threeItemBonus: StatBonus?,
        fourItemBonus: StatBonus?
    ) {
        this.sWeaponId = sWeaponId
        this.sAbilityId = sAbilityId
        this.sArmorId = sArmorId
        this.sRingId = sRingId
        statBonus[0] = twoItemBonus
        statBonus[1] = threeItemBonus
        statBonus[2] = fourItemBonus
    }

    open fun checkSet(weapon: Int, ability: Int, armor: Int, ring: Int): List<Int> {
        val data: MutableList<Int> = ArrayList()
        var count = 0
        if (weapon == sWeaponId) {
            count++
        }
        if (ability == sAbilityId) {
            count++
        }
        if (armor == sArmorId) {
            count++
        }
        if (ring == sRingId) {
            count++
        }
        data.add(count)
        data.add(-1)
        return data
    }

    open fun getBonus(data: List<Int>): StatBonus {
        val count = data[0]
        return if (count <= 1) {
            StatBonus(0, 0, 0, 0, 0, 0, 0, 0)
        } else {
            val sumStatBonus = StatBonus(0, 0, 0, 0, 0, 0, 0, 0)
            for (i in 0..count - 2) {
                sumStatBonus.addBonus(statBonus[i])
            }
            sumStatBonus
        }
    } /*public int getsWeaponId() {
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

    companion object {
        const val ROGUE_SKULD = 0
        const val ROGUE_VAMPIRE = 1
        const val ARCHER_PHANTOM = 2
        const val ARCHER_REANIMATED = 39
        const val ARCHER_TREASURE = 3
        const val ARCHER_VALENTINE = 4
        const val ARCHER_GOLD = 5
        const val WIZARD_ARCHMAGE = 6
        const val WIZARD_PARANORMAL = 28
        const val WIZARD_SLURP = 7
        const val PRIEST_GEB = 8
        const val PRIEST_MADGOD = 9
        const val WARRIOR_DRAGON = 10
        const val WARRIOR_PIRATE = 11
        const val KNIGHT_ORYX = 12
        const val KNIGHT_EASTER = 29
        const val KNIGHT_NORDIC = 38
        const val PALADIN_SWOLL = 13
        const val PALADIN_UNHOLY = 30
        const val ASSASSIN_FLESH = 14
        const val ASSASSIN_ACID = 15
        const val NECROMANCER_HOLLOW = 16
        const val NECROMANCER_REAPER = 31
        const val NECROMANCER_CUBOID = 37
        const val HUNTRESS_SWARM = 17
        const val HUNTRESS_HORTI = 18
        const val TRICKSTER_GOLEM = 33
        const val TRICKSTER_SCARECROW = 34
        const val MYSTIC_PHYLA = 19
        const val MYSTIC_TOTALIA = 32
        const val MYSTIC_MAGMA = 20
        const val SORCERER_HORRIFIC = 21
        const val SORCERER_MAGIC = 22
        const val NINJA_RAIJIN = 23
        const val NINJA_CRYSTAL = 24
        const val SAMURAI_AKUMA = 25
        const val SAMURAI_DISCOVER = 26
        const val BARD_ANGEL = 27
        const val CONSTRUCTION = 35
        const val AGENTS = 36
        const val ALIEN = 40
        const val ORYXMAS = 41
        const val MISTAKE = 42
    }
}
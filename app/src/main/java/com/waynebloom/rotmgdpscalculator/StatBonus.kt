package com.waynebloom.rotmgdpscalculator

internal class StatBonus {
    var attBonus = 0
    var defBonus = 0
    var spdBonus = 0
    var dexBonus = 0
    var vitBonus = 0
    var wisBonus = 0
    var lifeBonus = 0
    var manaBonus = 0

    constructor() {}
    constructor(
        attBonus: Int,
        defBonus: Int,
        spdBonus: Int,
        dexBonus: Int,
        wisBonus: Int,
        vitBonus: Int,
        lifeBonus: Int,
        manaBonus: Int
    ) {
        this.attBonus = attBonus
        this.defBonus = defBonus
        this.spdBonus = spdBonus
        this.dexBonus = dexBonus
        this.wisBonus = wisBonus
        this.vitBonus = vitBonus
        this.lifeBonus = lifeBonus
        this.manaBonus = manaBonus
    }

    fun addBonus(statBonus: StatBonus?) {
        attBonus = attBonus + statBonus!!.attBonus
        defBonus = defBonus + statBonus.defBonus
        spdBonus = spdBonus + statBonus.spdBonus
        dexBonus = dexBonus + statBonus.dexBonus
        wisBonus = wisBonus + statBonus.wisBonus
        vitBonus = vitBonus + statBonus.vitBonus
        lifeBonus = lifeBonus + statBonus.lifeBonus
        manaBonus = manaBonus + statBonus.manaBonus
    }

    fun subtractBonus(statBonus: StatBonus) {
        attBonus = attBonus - statBonus.attBonus
        defBonus = defBonus - statBonus.defBonus
        spdBonus = spdBonus - statBonus.spdBonus
        dexBonus = dexBonus - statBonus.dexBonus
        wisBonus = wisBonus - statBonus.wisBonus
        vitBonus = vitBonus - statBonus.vitBonus
        lifeBonus = lifeBonus - statBonus.lifeBonus
        manaBonus = manaBonus - statBonus.manaBonus
    }

    fun replaceBonus(statBonus: StatBonus) {
        attBonus = statBonus.attBonus
        defBonus = statBonus.defBonus
        spdBonus = statBonus.spdBonus
        dexBonus = statBonus.dexBonus
        wisBonus = statBonus.wisBonus
        vitBonus = statBonus.vitBonus
        lifeBonus = statBonus.lifeBonus
        manaBonus = statBonus.manaBonus
    }
}
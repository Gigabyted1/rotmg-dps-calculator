package com.waynebloom.rotmgdpscalculator.data

abstract class Item {
    val name: String = ""
    val itemSet: Int = -1
    val statBonus: StatBonus? = null
    val tier: List<String>? = listOf()
    // Special ability
}
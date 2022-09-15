package com.waynebloom.rotmgdpscalculator.data

data class CharClass(
    val name: String,
    val classId: Int,
    val weapons: List<Item>,
    val abilities: List<Item>,
    val armors: List<Item>,
    val rings: List<Item>,
    val imageId: Int,
    val maxStats: StatBonus
)
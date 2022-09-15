package com.waynebloom.rotmgdpscalculator.data

data class Ability(
    val manaCost: Int,
    val maxDamage: Int,
    val minDamage: Int,
    val numberOfShots: Int,
    val range: Float,
    val trueRange: Float
) : Item()

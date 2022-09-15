package com.waynebloom.rotmgdpscalculator.data

data class Weapon(
    val maxDamage: Int,
    val minDamage: Int,
    val numberOfShots: Int,
    val range: Float,
    val rateOfFire: Float,
    val trueRange: Float,
    // shot pattern difficulty?
) : Item()

package com.waynebloom.rotmgdpscalculator.data

import java.util.*

internal class Item(// Qualitative info
    val name: String,
    val imageId: Int,
    categories: String,
    relItemId: Int,
    absItemId: Int,
    partOfSet: Int,
    statBonus: StatBonus,
    avgDamage: Double,
    noOfShots: Int,
    rateOfFire: Double,
    range: Double,
    attribute: Int
) {
    val relItemId: Int
    val absItemId: Int
    val categories: List<String>

    // Item stats
    val statBonus: StatBonus
    val avgDamage: Double
    val noOfShots: Int
    val rateOfFire: Double
    private val range: Double
    val attribute // Such as armor piercing
            : Int
    val partOfSet: Int

    // Translates raw string data from file into an arrayList
    private fun parseCategories(input: String): List<String> {
        val output = ArrayList<String>()
        var i = 1
        while (i < input.length) {
            if (input[i - 1] == '"' && input[i] != ',' && input[i] != '[' && input[i] != ']') {
                val temp = StringBuilder()
                while (input[i] != '"') {
                    temp.append(input[i])
                    if (i < input.length - 1) i++ else break
                }
                output.add(temp.toString())
            }
            i++
        }
        return output
    }

    companion object {
        // Item set info
        @JvmField
        var itemSets: List<ItemSet>? = null
    }

    init {
        this.categories = parseCategories(categories)
        this.relItemId = relItemId
        this.absItemId = absItemId
        this.partOfSet = partOfSet
        this.statBonus = statBonus
        this.avgDamage = avgDamage
        this.noOfShots = noOfShots
        this.rateOfFire = rateOfFire
        this.range = range
        this.attribute = attribute
    }
}
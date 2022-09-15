package com.waynebloom.rotmgdpscalculator.data

import android.content.res.AssetManager
import android.media.Image.Plane
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory

object Datasource {

    lateinit var abilityList: List<Item>
    lateinit var armorList: List<Item>
    lateinit var classList List<CharClass>
    lateinit var ringList: List<Item>
    lateinit var weaponList: List<Item>

    private val gson = GsonBuilder().registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(Item::class.java, "type").apply {
            registerSubtype(Ability::class.java, "ability")
            registerSubtype(Weapon::class.java, "weapon")
        }
    ).create()

    fun getData(assetManager: AssetManager) {
        val rootJsonElement = JsonParser.parseString(assetManager.open("items.json").toString())

    }
}
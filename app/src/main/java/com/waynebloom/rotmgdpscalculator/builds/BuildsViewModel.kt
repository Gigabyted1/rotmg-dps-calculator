package com.waynebloom.rotmgdpscalculator.builds

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.waynebloom.rotmgdpscalculator.data.Item

class BuildsViewModel : BuildsBlueprint, View.OnClickListener, ViewModel() {

    private val _buildList: MutableLiveData<MutableList<Build>> = MutableLiveData(mutableListOf())
    val buildList: LiveData<MutableList<Build>>
        get() = _buildList

    init {

    }

    override fun loadGameData() {

    }

    override fun showSelectorRecyclerView() {
        TODO("Not yet implemented")
    }

    override fun hideSelectorRecyclerView() {
        TODO("Not yet implemented")
    }

    override fun onClassSelected() {
        TODO("Not yet implemented")
    }

    override fun onWeaponSelected() {
        TODO("Not yet implemented")
    }

    override fun onAbilitySelected() {
        TODO("Not yet implemented")
    }

    override fun onArmorSelected() {
        TODO("Not yet implemented")
    }

    override fun onRingSelected() {
        TODO("Not yet implemented")
    }

    override fun onDexteritySelected() {
        TODO("Not yet implemented")
    }

    override fun onAttackSelected() {
        TODO("Not yet implemented")
    }

    override fun onStatusEffectSelected() {
        TODO("Not yet implemented")
    }

    override fun onItemFilterCheckboxTapped(key: String) {
        TODO("Not yet implemented")
    }

    override fun onAddBuildTapped() {
        TODO("Not yet implemented")
        // randomly assign starting class
        // max 8 builds
    }

    override fun onClassTapped() {
        selectorAdapter
    }

    override fun onWeaponTapped() {
        TODO("Not yet implemented")
    }

    override fun onAbilityTapped() {
        TODO("Not yet implemented")
    }

    override fun onArmorTapped() {
        TODO("Not yet implemented")
    }

    override fun onRingTapped() {
        TODO("Not yet implemented")
    }

    override fun onDexterityTapped() {
        TODO("Not yet implemented")
    }

    override fun onAttackTapped() {
        TODO("Not yet implemented")
    }

    override fun onStatusEffectTapped() {
        TODO("Not yet implemented")
    }

    override fun onDeleteTapped() {
        TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }
}
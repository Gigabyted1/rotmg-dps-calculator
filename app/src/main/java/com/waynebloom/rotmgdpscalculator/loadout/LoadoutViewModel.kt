package com.waynebloom.rotmgdpscalculator.loadout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoadoutViewModel : ViewModel() {

    private val _builds = MutableLiveData<List<Build>>()
    val builds: LiveData<List<Build>> = _builds

}
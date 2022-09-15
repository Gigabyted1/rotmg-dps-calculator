package com.waynebloom.rotmgdpscalculator.builds.listitem

import com.waynebloom.rotmgdpscalculator.builds.Build
import com.waynebloom.rotmgdpscalculator.builds.adapter.BuildsListAdapter

interface BuildsViewListItemMvc {
    interface Listener {
        fun onClassTapped()
        fun onWeaponTapped()
        fun onAbilityTapped()
        fun onArmorTapped()
        fun onRingTapped()
        fun onDexterityTapped()
        fun onAttackTapped()
        fun onStatusEffectTapped()
        fun onDeleteTapped()
    }

    fun bindBuildsViewListItem(build: Build)
}
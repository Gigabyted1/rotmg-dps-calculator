package com.waynebloom.rotmgdpscalculator.loadout

interface LoadoutController {
    interface Listeners {
        fun onClassTapped()
        fun onWeaponTapped()
        fun onAbilityTapped()
        fun onArmorTapped()
        fun onRingTapped()
        fun onDexterityTapped()
        fun onAttackTapped()
        fun onStatusEffectTapped()
        fun onDeleteTapped()
        fun onItemFilterCheckboxTapped(key: String)
    }


}
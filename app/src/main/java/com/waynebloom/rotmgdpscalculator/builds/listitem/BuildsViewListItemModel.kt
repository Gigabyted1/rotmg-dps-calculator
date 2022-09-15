package com.waynebloom.rotmgdpscalculator.builds.listitem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.waynebloom.rotmgdpscalculator.R
import com.waynebloom.rotmgdpscalculator.base.BaseViewModel
import com.waynebloom.rotmgdpscalculator.builds.Build
import com.waynebloom.rotmgdpscalculator.builds.adapter.BuildsListAdapter
import com.waynebloom.rotmgdpscalculator.databinding.FragmentBuildsBinding
import com.waynebloom.rotmgdpscalculator.databinding.ListItemBuildBinding

class BuildsViewListItemModel(
    override val inflater: LayoutInflater,
    val parent: ViewGroup?
) : BaseViewModel(), BuildsViewListItemMvc, View.OnClickListener {

    private val controller: BuildsViewListItemController

    private var _binding: ListItemBuildBinding? = null
    private val binding get() = _binding!!

    var build: Build? = null

    init {
        controller = BuildsViewListItemController()

        _binding = setupViewBinding()
        rootView = binding.root
    }

    private fun setupViewBinding(): ListItemBuildBinding? {
        return ListItemBuildBinding.inflate(inflater, parent, false)
    }

    override fun bindBuildsViewListItem(build: Build) {
        this.build = build

        with(binding) {
            charClass.setOnClickListener(this@BuildsViewListItemModel)
            weapon.setOnClickListener(this@BuildsViewListItemModel)
            ability.setOnClickListener(this@BuildsViewListItemModel)
            armor.setOnClickListener(this@BuildsViewListItemModel)
            ring.setOnClickListener(this@BuildsViewListItemModel)
            attack.setOnClickListener(this@BuildsViewListItemModel)
            dexterity.setOnClickListener(this@BuildsViewListItemModel)
            statusEffects.setOnClickListener(this@BuildsViewListItemModel)
            delete.setOnClickListener(this@BuildsViewListItemModel)
        }
    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                charClass -> controller.onClassTapped()
                weapon -> controller.onWeaponTapped()
                ability -> controller.onAbilityTapped()
                armor -> controller.onArmorTapped()
                ring -> controller.onRingTapped()
                attack -> controller.onAttackTapped()
                dexterity -> controller.onDexterityTapped()
                statusEffects -> controller.onStatusEffectTapped()
                delete -> controller.onDeleteTapped()
            }
        }
    }

}
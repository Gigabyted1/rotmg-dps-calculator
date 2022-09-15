package com.waynebloom.rotmgdpscalculator.builds.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.waynebloom.rotmgdpscalculator.R
import com.waynebloom.rotmgdpscalculator.builds.Build
import com.waynebloom.rotmgdpscalculator.builds.Loadout
import com.waynebloom.rotmgdpscalculator.builds.listitem.BuildsViewListItemModel
import com.waynebloom.rotmgdpscalculator.data.Datasource

class BuildsListAdapter(
    val inflater: LayoutInflater
) : RecyclerView.Adapter<BuildsListAdapter.ViewHolder>() {

    private val builds = Datasource.loadBuilds()

    class ViewHolder(
        val viewModel: BuildsViewListItemModel
    ) : RecyclerView.ViewHolder(viewModel.rootView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewModel = BuildsViewListItemModel(inflater, parent)
        return ViewHolder(viewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        
        holder.viewModel.bindBuildsViewListItem(builds[position])
        
        //
        
        // Assign different background colors
        when (position) {
            0 -> holder.background.setBackgroundResource(R.drawable.bg_loadout_red)
            1 -> holder.background.setBackgroundResource(R.drawable.bg_loadout_orange)
            2 -> holder.background.setBackgroundResource(R.drawable.bg_loadout_yellow)
            3 -> holder.background.setBackgroundResource(R.drawable.bg_loadout_green)
            4 -> holder.background.setBackgroundResource(R.drawable.bg_loadout_cyan)
            5 -> holder.background.setBackgroundResource(R.drawable.bg_loadout_blue)
            6 -> holder.background.setBackgroundResource(R.drawable.bg_loadout_purple)
            7 -> holder.background.setBackgroundResource(R.drawable.bg_loadout_black)
        }

        // Sends holder view references to the loadout class
        val currentLoadout = loadouts[position]
        currentLoadout.setViews(
            this,
            holder.classView,
            holder.weaponView,
            holder.abilityView,
            holder.armorView,
            holder.ringView,
            holder.attView,
            holder.dexView,
            holder.statusView,
            holder.deleteButton
        )
    }

    override fun getItemCount(): Int {
        return loadouts.size
    }

    fun removeAt(position: Int) {
        loadouts.removeAt(position)
        if (position != loadouts.size) {
            for (loadoutPos in position until loadouts.size) {
                loadouts[loadoutPos].setLoadoutId(loadoutPos)
            }
        }
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, loadouts.size)
    }
}
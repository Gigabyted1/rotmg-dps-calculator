package com.waynebloom.rotmgdpscalculator.loadout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.waynebloom.rotmgdpscalculator.R

class LoadoutAdapter internal constructor(
    private val mContext: Context?,
    private val loadouts: List<Loadout>
) : RecyclerView.Adapter<LoadoutAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val classView: ImageView
        val weaponView: ImageView
        val abilityView: ImageView
        val armorView: ImageView
        val ringView: ImageView
        val attView: TextView
        val dexView: TextView
        val statusView: ConstraintLayout
        val deleteButton: Button
        val background: ConstraintLayout

        init {
            classView = view.findViewById(R.id.class_view)
            weaponView = view.findViewById(R.id.weapon_view)
            abilityView = view.findViewById(R.id.ability_view)
            armorView = view.findViewById(R.id.armor_view)
            ringView = view.findViewById(R.id.ring_view)
            attView = view.findViewById(R.id.total_att)
            dexView = view.findViewById(R.id.total_dex)
            statusView = view.findViewById(R.id.status)
            deleteButton = view.findViewById(R.id.delete)
            background = view.findViewById(R.id.background)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_loadout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
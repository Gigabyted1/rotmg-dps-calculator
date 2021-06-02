package com.waynebloom.rotmgdpscalculator.dps

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.waynebloom.rotmgdpscalculator.R
import java.util.*

class DpsAdapter(private val mContext: Context?, private val data: List<MutableList<DpsEntry>>) :
    RecyclerView.Adapter<DpsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var defense: TextView
        var dpsViews: Array<TextView>

        init {
            defense = view.findViewById(R.id.defense)
            dpsViews = arrayOf(
                view.findViewById(R.id.dps1),
                view.findViewById(R.id.dps2),
                view.findViewById(R.id.dps3),
                view.findViewById(R.id.dps4),
                view.findViewById(R.id.dps5),
                view.findViewById(R.id.dps6),
                view.findViewById(R.id.dps7),
                view.findViewById(R.id.dps8)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_dps, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        populateFields(holder, position, data[position])
    }

    fun populateFields(holder: ViewHolder, position: Int, currentDefLevel: List<DpsEntry>) {
        holder.defense.text = String.format(Locale.US, "%d", position)
        for (i in currentDefLevel.indices) {
            holder.dpsViews[i].text =
                String.format(Locale.US, "%.2f", currentDefLevel[i].dps)
            when (currentDefLevel[i].loadoutId) {
                0 -> {
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_red)
                    holder.dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"))
                }
                1 -> {
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_orange)
                    holder.dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"))
                }
                2 -> {
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_yellow)
                    holder.dpsViews[i].setTextColor(Color.parseColor("#444444"))
                }
                3 -> {
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_green)
                    holder.dpsViews[i].setTextColor(Color.parseColor("#444444"))
                }
                4 -> {
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_cyan)
                    holder.dpsViews[i].setTextColor(Color.parseColor("#444444"))
                }
                5 -> {
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_blue)
                    holder.dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"))
                }
                6 -> {
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_pink)
                    holder.dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"))
                }
                7 -> {
                    holder.dpsViews[i].setBackgroundResource(R.drawable.dps_black)
                    holder.dpsViews[i].setTextColor(Color.parseColor("#FFFFFF"))
                }
            }
            holder.dpsViews[i].visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
package com.waynebloom.rotmgdpscalculator.builds.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.waynebloom.rotmgdpscalculator.R
import com.waynebloom.rotmgdpscalculator.builds.Loadout
import com.waynebloom.rotmgdpscalculator.data.CharClass

class ClassListAdapter internal constructor(
    private val mContext: Context,
    private val data: List<CharClass>,
    private val callingLoadout: Loadout
) : RecyclerView.Adapter<ClassListAdapter.ViewHolder>() {
    private val myFont: Typeface

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView
        var name: TextView
        var parent: View

        init {
            image = view.findViewById(R.id.class_picture)
            name = view.findViewById(R.id.class_name)
            parent = view
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_class, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        populateFields(holder, data[position])
        holder.parent.setOnClickListener {
            callingLoadout.informSelected(data[position])
            callingLoadout.makeSelectorViewGone(Loadout.CLASS)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun populateFields(holder: ViewHolder, mClass: CharClass) {
        holder.image.setImageResource(mClass.imageId)
        holder.name.text = mClass.name
        holder.name.typeface = myFont
        holder.name.textSize = 12f
    }

    init {
        myFont = Typeface.createFromAsset(mContext.assets, "myfont.ttf")
    }
}
// Heavy structural help from https://gist.github.com/fjfish/3024308
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
import com.waynebloom.rotmgdpscalculator.data.Item
import java.util.*

class ItemListAdapter(
    inflater: LayoutInflater
) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {
    private var filteredData: List<Item>
    private val myFont: Typeface
    private val mContext: Context
    private val callingLoadout: Loadout
    private val type: Int
    private val mFilter = ItemFilter()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView
        var name: TextView
        var parent: View

        init {
            image = view.findViewById(R.id.item_picture)
            name = view.findViewById(R.id.item_name)
            parent = view
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        populateFields(holder, filteredData[position])
        holder.parent.setOnClickListener {
            callingLoadout.informSelected(filteredData[position], type)
            callingLoadout.makeSelectorViewGone(type)
        }
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    private fun populateFields(mViewHolder: ViewHolder, currentItem: Item) {
        val MAX_NAME_LENGTH = 27
        val FONT_SIZE_NAME = 10f
        mViewHolder.image.setImageResource(currentItem.imageId)

        // Fit the name in the UI by capping its length, with ... to note that the full name isn't shown
        var temp = currentItem.name
        if (temp!!.length > MAX_NAME_LENGTH) {
            temp = temp.substring(0, MAX_NAME_LENGTH - 3) + "..."
        }
        mViewHolder.name.text = temp
        mViewHolder.name.setTypeface(myFont)
        mViewHolder.name.textSize = FONT_SIZE_NAME
    }

    fun enactCategories(mCategories: List<String>) {
        mFilter.publishResults(mFilter.performFiltering(mCategories))
    }

    private inner class ItemFilter {
        fun performFiltering(mCategories: List<String>): List<Item> {
            val results = ArrayList<Item>()
            for (i in originalData) {
                var match = true
                for (itemCat in i.categories) {               // The item's categories
                    var found = false
                    for (selectedCat in mCategories) {             // The selected categories
                        if (selectedCat == itemCat) {               // Category is found
                            found = true
                            break
                        } // If not found, 'found' remains false
                    }
                    if (!found) {
                        match =
                            false // Change match to false and immediately break if any category is not found
                        break
                    }
                }
                if (match) {                                             // If no category is not found, match remains true and the item is added to results
                    results.add(i)
                }
            }
            return results
        }

        fun publishResults(results: List<Item>) {
            filteredData = results
            notifyDataSetChanged()
        }
    }

    init {
        filteredData = originalData
        myFont = Typeface.createFromAsset(context.assets, "myfont.ttf")
        mContext = context
        this.callingLoadout = callingLoadout
        this.type = type
    }
}
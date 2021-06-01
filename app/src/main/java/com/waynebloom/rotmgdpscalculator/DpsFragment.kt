package com.waynebloom.rotmgdpscalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.*
import com.waynebloom.rotmgdpscalculator.loadout.Loadout
import com.waynebloom.rotmgdpscalculator.loadout.LoadoutFragment
import java.util.*

class DpsFragment : Fragment() {
    var mActivity: MainActivity? = null
    var loadoutFragment: LoadoutFragment? = null
    var dpsDataTable: MutableList<MutableList<DpsEntry>> = ArrayList()
    var dpsViewMenu: BottomNavigationView? = null
    var emptyText: TextView? = null
    var dpsGraphView: GraphView? = null
    var dpsTableView: RecyclerView? = null
    var detailView: LinearLayout? = null
    var dpsAdpt: DpsAdapter? = null
    var dpsAdptManager: LinearLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadoutFragment = mActivity.getLoadoutFragment()
        val view = inflater.inflate(R.layout.fragment_dps, container, false)
        emptyText = view.findViewById(R.id.empty_dps)
        dpsViewMenu = view.findViewById(R.id.dps_view_menu)
        dpsTableView = view.findViewById(R.id.dps_table_view)
        detailView = view.findViewById(R.id.details_container)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // style graphview
        dpsGraphView = view.findViewById(R.id.graph)
        dpsGraphView.getViewport().isScalable = true
        dpsGraphView.getViewport().isScrollable = true
        dpsGraphView.getViewport().setMinX(0.0)
        dpsGraphView.getViewport().setMaxX(40.0)
        val renderer = dpsGraphView.getGridLabelRenderer()
        val gridColor = ContextCompat.getColor(requireContext(), R.color.gridColor)
        val titleColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        renderer.gridColor = gridColor
        renderer.verticalAxisTitleColor = titleColor
        renderer.verticalLabelsColor = gridColor
        renderer.numVerticalLabels = 6
        renderer.verticalAxisTitle = "DPS"
        renderer.horizontalAxisTitleColor = titleColor
        renderer.horizontalLabelsColor = gridColor
        renderer.horizontalAxisTitle = "Defense"
        dpsViewMenu!!.setOnNavigationItemSelectedListener { item ->
            val itemId = item.itemId
            if (itemId == R.id.graph) {
                displayView(GRAPH)
                true
            } else if (itemId == R.id.table) {
                displayView(TABLE)
                true
            } else {
                false
            }
        }
        updateDpsViews()
        displayView(GRAPH)
    }

    fun onBackPressed() {
        mActivity!!.setViewPagerPosition(0)
    }

    private val isEmpty: Boolean
        private get() = if (loadoutFragment!!.isEmpty) {
            true
        } else {
            false
        }

    fun displayView(which: Int) {
        if (view != null) {
            if (isEmpty) {
                emptyText!!.visibility = View.VISIBLE
                dpsGraphView!!.visibility = View.INVISIBLE
                detailView!!.visibility = View.INVISIBLE
                dpsTableView!!.visibility = View.INVISIBLE
            } else {
                emptyText!!.visibility = View.GONE
                if (which == TABLE) {
                    dpsGraphView!!.visibility = View.INVISIBLE
                    detailView!!.visibility = View.INVISIBLE
                    dpsTableView!!.visibility = View.VISIBLE
                } else if (which == GRAPH) {
                    dpsGraphView!!.visibility = View.VISIBLE
                    detailView!!.visibility = View.VISIBLE
                    dpsTableView!!.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun updateDpsViews() {
        if (dpsGraphView != null) {
            dpsGraphView!!.removeAllSeries()
            detailView!!.removeAllViews()
            populateMatrix(loadoutFragment!!.loadouts)
            for (i in dpsDataTable[0].indices) {
                val series = LineGraphSeries<DataPoint>()
                for (j in dpsDataTable.indices) {
                    series.appendData(DataPoint(j, dpsDataTable[j][i].dps), false, 151, true)
                }
                var colorId: Int
                colorId = when (i) {
                    0 -> ContextCompat.getColor(requireContext(), R.color.graphLoadout1)
                    1 -> ContextCompat.getColor(requireContext(), R.color.graphLoadout2)
                    2 -> ContextCompat.getColor(requireContext(), R.color.graphLoadout3)
                    3 -> ContextCompat.getColor(requireContext(), R.color.graphLoadout4)
                    4 -> ContextCompat.getColor(requireContext(), R.color.graphLoadout5)
                    5 -> ContextCompat.getColor(requireContext(), R.color.graphLoadout6)
                    6 -> ContextCompat.getColor(requireContext(), R.color.graphLoadout7)
                    7 -> ContextCompat.getColor(requireContext(), R.color.graphLoadout8)
                    else -> -1
                }
                series.color = colorId
                series.setOnDataPointTapListener { series, dataPoint ->
                    detailView!!.removeAllViews()
                    val viewHolder = dpsAdpt!!.onCreateViewHolder(
                        detailView!!, 0
                    )
                    dpsAdpt!!.onBindViewHolder(
                        viewHolder, Math.round(dataPoint.x)
                            .toInt()
                    )
                    detailView!!.addView(viewHolder.itemView)
                }
                dpsGraphView!!.addSeries(series)
            }
        }
        if (dpsTableView != null) {
            sortMatrix(dpsDataTable)
            if (dpsAdpt == null) {
                dpsAdpt = DpsAdapter(activity, dpsDataTable)
                dpsTableView!!.adapter = dpsAdpt
                dpsAdptManager = LinearLayoutManager(context)
                dpsTableView!!.layoutManager = dpsAdptManager
            } else {
                dpsAdpt!!.notifyDataSetChanged()
            }
        }
    }

    // Generates an ascending order list of each current loadout's damage per second for each level of defense up to the maximum
    private fun populateMatrix(loadouts: List<Loadout>) {
        dpsDataTable.clear()
        for (currentDefLevel in 0..150) {
            val dpsTableAtCurrentDefLevel = ArrayList<DpsEntry>()
            for (currentLoadout in loadouts) {
                dpsTableAtCurrentDefLevel.add(currentLoadout.dps[currentDefLevel])
            }
            dpsDataTable.add(dpsTableAtCurrentDefLevel)
        }
    }

    private fun sortMatrix(data: List<MutableList<DpsEntry>>): List<MutableList<DpsEntry>> {
        // Insert sort
        for (i in 0..150) {
            for (j in 1 until data[i].size) {
                val currentEntry = data[i][j]
                var k = j
                while (k > 0 && data[i][k - 1].dps < currentEntry.dps) {
                    data[i][k] = data[i][k - 1]
                    k--
                }
                data[i][k] = currentEntry
            }
        }
        return data
    }

    fun notifyLoadoutRemoved() {
        if (dpsAdpt != null) {
            dpsAdpt = DpsAdapter(context, dpsDataTable)
            dpsTableView!!.adapter = dpsAdpt
        }
    }

    companion object {
        const val TABLE = 0
        const val GRAPH = 1
        const val UPDATE = 2
    }
}
package com.waynebloom.rotmgdpscalculator.loadout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waynebloom.rotmgdpscalculator.CharClass
import com.waynebloom.rotmgdpscalculator.MainActivity
import com.waynebloom.rotmgdpscalculator.R
import com.waynebloom.rotmgdpscalculator.loadout.Loadout
import com.waynebloom.rotmgdpscalculator.loadout.LoadoutFragment
import java.io.*
import java.util.*

class LoadoutFragment : Fragment() {
    var mActivity: MainActivity? = null

    // loadouts
    private var loadouts: MutableList<Loadout> = ArrayList(8)
    private var emptyText: TextView? = null
    private var loadoutView: RecyclerView? = null
    private var loadAdpt: LoadoutAdapter? = null
    private var btnAddLoadout: Button? = null
    private var saveFile: File? = null

    // selector
    private var selectorView: RecyclerView? = null
    private var filterView: View? = null
    private var fade: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity?
        saveFile = File(requireContext().filesDir, "loadouts.txt")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loadout, container, false)
        emptyText = view.findViewById(R.id.empty_lo)
        loadoutView = view.findViewById(R.id.loadout_view)
        btnAddLoadout = view.findViewById(R.id.exalt_button)
        selectorView = view.findViewById(R.id.item_selection_view)
        filterView = view.findViewById(R.id.filter)
        fade = view.findViewById(R.id.fade)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAddLoadout!!.setOnClickListener { // randomly select the initial class
            val randomClass: CharClass = Loadout.Companion.classData!!.get(Random().nextInt(16))
            if (loadouts.size < 8) {
                val newLoadout = Loadout(
                    this@LoadoutFragment,
                    loadouts.size,
                    randomClass
                )
                loadouts.add(newLoadout)
                loadAdpt!!.notifyDataSetChanged()
                saveLoadouts()
                emptyLayout()
            } else {
                Toast.makeText(
                    context,
                    "You can only have 8 loadouts at a time.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // load saved loadouts and first time set up of adapter
        loadouts = readSavedLoadouts()
        loadAdpt = LoadoutAdapter(context, loadouts)
        loadoutView!!.adapter = loadAdpt
        loadoutView!!.layoutManager = LinearLayoutManager(context)
        emptyLayout()
    }

    fun getLoadouts(): List<Loadout> {
        return loadouts
    }

    val isEmpty: Boolean
        get() = loadouts.isEmpty()

    // show empty layout elements if there are no loadouts
    private fun emptyLayout() {
        if (isEmpty) {
            emptyText!!.visibility = View.VISIBLE
        } else {
            emptyText!!.visibility = View.GONE
        }
    }

    fun onBackPressed() {
        if (selectorView != null && filterView != null && fade != null) {
            if (selectorView!!.visibility == View.VISIBLE) {
                selectorView!!.visibility = View.INVISIBLE
                filterView!!.visibility = View.INVISIBLE
                fade!!.visibility = View.INVISIBLE
            }
        }
    }

    fun notifyLoadoutRemoved() {
        emptyLayout()
        mActivity!!.dpsFragment!!.notifyLoadoutRemoved()
        saveLoadouts()
    }

    fun saveLoadouts() {
        try {
            val writer = BufferedWriter(FileWriter(saveFile, false))
            val saveStr = StringBuilder()
            for (i in loadouts) {
                saveStr.append(i.charClass.classId).append('/')
                saveStr.append(i.weapon.relItemId).append('/')
                saveStr.append(i.ability.relItemId).append('/')
                saveStr.append(i.armor.relItemId).append('/')
                saveStr.append(i.ring.relItemId).append('/')
                val activeEffects = i.activeEffects
                for (activeEffect in activeEffects!!) {
                    if (activeEffect) {
                        saveStr.append('1')
                    } else {
                        saveStr.append('0')
                    }
                }
                saveStr.append("/\n")
            }
            writer.write(saveStr.toString())
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // TODO: Clean this shit up, my EYES
    private fun readSavedLoadouts(): MutableList<Loadout> {
        val loadouts: MutableList<Loadout> = ArrayList()
        try {
            val loadReader = BufferedReader(FileReader(saveFile))
            var lineData: ArrayList<StringBuilder>
            var fileChar: Char
            var lineLoc: Int
            while (loadReader.ready()) {
                lineData = ArrayList(6)
                lineLoc = 0
                for (i in 0..5) {
                    lineData.add(StringBuilder())
                }
                while (loadReader.ready() && lineLoc < 6) {
                    fileChar = loadReader.read().toChar()
                    if (fileChar == '/') {
                        lineLoc++
                    } else {
                        lineData[lineLoc].append(fileChar)
                    }
                    if (lineLoc == 6) {
                        loadReader.read()
                        val temps = intArrayOf(
                            lineData[0].toString().toInt(),
                            lineData[1].toString().toInt(),
                            lineData[2].toString().toInt(),
                            lineData[3].toString().toInt(),
                            lineData[4].toString().toInt()
                        )

                        // Translate the nonsense above
                        val loadoutId = loadouts.size
                        val loadedClass: CharClass = Loadout.Companion.classData!!.get(temps[0])
                        val loadedWeapon = loadedClass.weapons[temps[1]]
                        val loadedAbility = loadedClass.abilities[temps[2]]
                        val loadedArmor = loadedClass.armors[temps[3]]
                        val loadedRing = loadedClass.rings[temps[4]]
                        val loadedEffects = lineData[5].toString()
                        val newLoadout = Loadout(
                            this@LoadoutFragment,
                            loadoutId,
                            loadedClass,
                            loadedWeapon,
                            loadedAbility,
                            loadedArmor,
                            loadedRing,
                            loadedEffects
                        )
                        loadouts.add(newLoadout)
                    }
                }
            }
            loadReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return loadouts
    }
}
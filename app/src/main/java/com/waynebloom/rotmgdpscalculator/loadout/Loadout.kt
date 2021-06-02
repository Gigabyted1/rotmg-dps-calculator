package com.waynebloom.rotmgdpscalculator.loadout

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waynebloom.rotmgdpscalculator.*
import com.waynebloom.rotmgdpscalculator.data.CharClass
import com.waynebloom.rotmgdpscalculator.data.Item
import com.waynebloom.rotmgdpscalculator.data.StatBonus
import com.waynebloom.rotmgdpscalculator.dps.DpsEntry
import java.util.*

enum class Status(val modifier: Double) {
    BERSERK(1.25),
    DAMAGING(1.25),
    CURSE(1.25),
    WEAK(0.0),
    DAZED(0.0)
}

data class Build(
    private val id: Int,
    private val charClass: CharClass,
    private val weapon: Weapon,
    private val ability: Ability,
    private val armor: Armor,
    private val ring: Ring,
    private val setBonus: StatBonus,
    private val statusEffects: MutableSet<Status>
)

class Loadout {
    private val mFragment: LoadoutFragment
    private val mContext: Context?
    private var loadoutId: Int
    private var itemAdpt: ItemAdapter? = null

    // constant views
    private val selectorView: RecyclerView
    private val filterView: View
    private val checkUt: CheckBox
    private val checkSt: CheckBox
    private val checkT: CheckBox
    private val checkReskin: CheckBox
    private val checkWeak: CheckBox
    private val backgroundFade: View

    // dps factors
    var charClass: CharClass? = null
        private set
    var weapon: Item? = null
        private set
    var ability: Item? = null
        private set
    var armor: Item? = null
        private set
    var ring: Item? = null
        private set
    private var baseStats = StatBonus(0, 0, 0, 0, 0, 0, 0, 0)
    private val setBonus = StatBonus(0, 0, 0, 0, 0, 0, 0, 0)
    private val statTotals = StatBonus(0, 0, 0, 0, 0, 0, 0, 0)
    var activeEffects //Tracks active status effects
            : BooleanArray
        private set
    private var attModifier = 0.0
    private var dexModifier = 0.0
    private var dmgModifier = 0.0
    private var loadoutDps: MutableList<DpsEntry> = ArrayList()
    private var loadoutChanged: Boolean

    // view references
    private var classView: ImageView? = null
    private var weaponView: ImageView? = null
    private var abilityView: ImageView? = null
    private var armorView: ImageView? = null
    private var ringView: ImageView? = null
    private var attView: TextView? = null
    private var dexView: TextView? = null
    private var statusView: ConstraintLayout? = null
    private var damagingView: ImageView? = null
    private var berserkView: ImageView? = null
    private var curseView: ImageView? = null
    private var dazedView: ImageView? = null
    private var weakView: ImageView? = null
    private var deleteButton: Button? = null

    // Empty loadout
    constructor(mFragment: LoadoutFragment, loadoutId: Int) {
        val fragmentView = mFragment.view
        this.mFragment = mFragment
        mContext = mFragment.context
        selectorView = fragmentView!!.findViewById(R.id.item_selection_view)
        filterView = fragmentView.findViewById(R.id.filter)
        checkUt = filterView.findViewById(R.id.check_ut)
        checkSt = filterView.findViewById(R.id.check_st)
        checkT = filterView.findViewById(R.id.check_t)
        checkReskin = filterView.findViewById(R.id.check_reskin)
        checkWeak = filterView.findViewById(R.id.check_weak)
        backgroundFade = fragmentView.findViewById(R.id.fade)
        this.loadoutId = loadoutId
        loadoutChanged = true
    }

    // Loadout with Class included (for 'createNewLoadout' in MainActivity)
    constructor(mFragment: LoadoutFragment, loadoutId: Int, charClass: CharClass) {
        val fragmentView = mFragment.view
        this.mFragment = mFragment
        mContext = mFragment.context
        selectorView = fragmentView!!.findViewById(R.id.item_selection_view)
        filterView = fragmentView.findViewById(R.id.filter)
        checkUt = filterView.findViewById(R.id.check_ut)
        checkSt = filterView.findViewById(R.id.check_st)
        checkT = filterView.findViewById(R.id.check_t)
        checkReskin = filterView.findViewById(R.id.check_reskin)
        checkWeak = filterView.findViewById(R.id.check_weak)
        backgroundFade = fragmentView.findViewById(R.id.fade)
        this.loadoutId = loadoutId
        loadoutChanged = true

        // load initial stats
        this.charClass = charClass
        baseStats.addBonus(charClass.maxedStats)
        statTotals.addBonus(baseStats)
        weapon = charClass.weapons[0]
        ability = charClass.abilities[0]
        armor = charClass.armors[0]
        ring = charClass.rings[0]
        setActiveEffects("00000")

        // update stat views
        updateAtt()
        updateDex()
    }

    // Loadout with all gear included (for 'loadBuilds' in MainActivity)
    constructor(
        mFragment: LoadoutFragment,
        loadoutId: Int,
        charClass: CharClass,
        weapon: Item,
        ability: Item,
        armor: Item,
        ring: Item,
        activeEffects: String
    ) {
        val fragmentView = mFragment.view
        this.mFragment = mFragment
        mContext = mFragment.context
        selectorView = fragmentView!!.findViewById(R.id.item_selection_view)
        filterView = fragmentView.findViewById(R.id.filter)
        checkUt = filterView.findViewById(R.id.check_ut)
        checkSt = filterView.findViewById(R.id.check_st)
        checkT = filterView.findViewById(R.id.check_t)
        checkReskin = filterView.findViewById(R.id.check_reskin)
        checkWeak = filterView.findViewById(R.id.check_weak)
        backgroundFade = fragmentView.findViewById(R.id.fade)
        this.loadoutId = loadoutId
        loadoutChanged = true

        // load initial stats
        this.charClass = charClass
        baseStats.addBonus(charClass.maxedStats)
        statTotals.addBonus(baseStats)
        this.weapon = weapon
        statTotals.addBonus(weapon.statBonus)
        this.ability = ability
        statTotals.addBonus(ability.statBonus)
        this.armor = armor
        statTotals.addBonus(armor.statBonus)
        this.ring = ring
        statTotals.addBonus(ring.statBonus)
        checkAndUpdateSetBonus()
        setActiveEffects(activeEffects)

        // update stat views
        updateAtt()
        updateDex()
    }

    // assign view references and then set all click listeners
    fun setViews(
        caller: LoadoutAdapter,
        classView: ImageView?,
        weaponView: ImageView?,
        abilityView: ImageView?,
        armorView: ImageView?,
        ringView: ImageView?,
        attView: TextView?,
        dexView: TextView?,
        statusView: ConstraintLayout?,
        deleteView: Button?
    ) {
        this.classView = classView
        this.weaponView = weaponView
        this.abilityView = abilityView
        this.armorView = armorView
        this.ringView = ringView
        this.attView = attView
        this.dexView = dexView
        this.statusView = statusView
        damagingView = this.statusView!!.findViewById(R.id.damaging)
        berserkView = this.statusView!!.findViewById(R.id.berserk)
        curseView = this.statusView!!.findViewById(R.id.curse)
        dazedView = this.statusView!!.findViewById(R.id.dazed)
        weakView = this.statusView!!.findViewById(R.id.weak)
        deleteButton = deleteView
        setClassViewListener()
        setWeaponViewListener()
        setAbilityViewListener()
        setArmorViewListener()
        setRingViewListener()
        setAttViewListener()
        setDexViewListener()
        setStatusViewListener()
        setDeleteViewListener(caller)
        updateAllViews()
    }

    fun setClassViewListener() {
        classView!!.setOnClickListener {
            val adpt = ClassAdapter(mContext!!, classData!!, this@Loadout)
            selectorView.adapter = adpt
            selectorView.layoutManager = LinearLayoutManager(mContext)
            makeSelectorViewVisible(CLASS)
        }
    }

    fun setWeaponViewListener() {
        weaponView!!.setOnClickListener {
            setFilterViewListeners()
            itemAdpt = ItemAdapter(mContext!!, charClass!!.weapons, this@Loadout, WEAPON)
            itemAdpt!!.enactCategories(selectedCategories)
            selectorView.adapter = itemAdpt
            selectorView.layoutManager = LinearLayoutManager(mContext)
            makeSelectorViewVisible(WEAPON)
        }
    }

    fun setAbilityViewListener() {
        abilityView!!.setOnClickListener {
            setFilterViewListeners()
            itemAdpt = ItemAdapter(mContext!!, charClass!!.abilities, this@Loadout, ABILITY)
            itemAdpt!!.enactCategories(selectedCategories)
            selectorView.adapter = itemAdpt
            selectorView.layoutManager = LinearLayoutManager(mContext)
            makeSelectorViewVisible(ABILITY)
        }
    }

    fun setArmorViewListener() {
        armorView!!.setOnClickListener {
            setFilterViewListeners()
            itemAdpt = ItemAdapter(mContext!!, charClass!!.armors, this@Loadout, ARMOR)
            itemAdpt!!.enactCategories(selectedCategories)
            selectorView.adapter = itemAdpt
            selectorView.layoutManager = LinearLayoutManager(mContext)
            makeSelectorViewVisible(ARMOR)
        }
    }

    fun setRingViewListener() {
        ringView!!.setOnClickListener {
            setFilterViewListeners()
            itemAdpt = ItemAdapter(mContext!!, charClass!!.rings, this@Loadout, RING)
            itemAdpt!!.enactCategories(selectedCategories)
            selectorView.adapter = itemAdpt
            selectorView.layoutManager = LinearLayoutManager(mContext)
            makeSelectorViewVisible(RING)
        }
    }

    fun setAttViewListener() {
        attView!!.setOnClickListener { // Load a string array with values between 0 and the current class's maximum att
            val MIN_ATT = 0
            val MAX_ATT = charClass!!.maxedStats.attBonus
            val baseStatRange = arrayOfNulls<String>(MAX_ATT + 1) // '+1' is to include 0
            for (i in MIN_ATT..MAX_ATT) {
                baseStatRange[i] = Integer.toString(i)
            }

            // Produce a dialog box for selection of new baseAtt value
            val mBuilder = AlertDialog.Builder(
                mContext!!
            )
            mBuilder.setTitle("Change Base Attack")
                .setItems(baseStatRange) { dialog, which -> informSelected(which, ATT) }
                .create()
                .listView.setSelectionFromTop(baseStats.attBonus, 0)
            mBuilder.show()
        }
    }

    fun setDexViewListener() {
        dexView!!.setOnClickListener { // Load a string array with values between 0 and the current class's maximum dex
            val MIN_DEX = 0
            val MAX_DEX = charClass!!.maxedStats.dexBonus
            val baseStatRange = arrayOfNulls<String>(MAX_DEX + 1) // '+1' is to include 0
            for (i in MIN_DEX..MAX_DEX) {
                baseStatRange[i] = Integer.toString(i)
            }

            // Produce a dialog box for selection of new baseDex value
            val mBuilder = AlertDialog.Builder(
                mContext!!
            )
            mBuilder.setTitle("Change Base Dexterity")
                .setItems(baseStatRange) { dialog, which -> informSelected(which, DEX) }
                .create()
                .listView.setSelectionFromTop(baseStats.dexBonus, 0)
            mBuilder.show()
        }
    }

    fun setStatusViewListener() {
        statusView!!.setOnClickListener { // Retrieve names of status effects
            val statusEffectNames = mContext!!.resources.getStringArray(R.array.stat_effects)

            // Produce a dialog box for selection of status effects
            val mBuilder = AlertDialog.Builder(
                mContext
            )
            mBuilder.setTitle("Status Effects")
                .setMultiChoiceItems(
                    statusEffectNames,
                    activeEffects
                ) { dialog, which, isChecked -> }
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, which ->
                    updateStatus()
                    mFragment.saveLoadouts()
                    loadoutChanged = true
                }.create().show()
        }
    }

    fun setDeleteViewListener(caller: LoadoutAdapter) {
        deleteButton!!.setOnClickListener {
            val mBuilder = AlertDialog.Builder(
                mContext!!
            )
            mBuilder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this loadout?")
                .setPositiveButton("Yes") { dialog, which ->
                    caller.removeAt(loadoutId)
                    mFragment.saveLoadouts()
                    mFragment.notifyLoadoutRemoved()
                }
                .setNegativeButton("Cancel", null)
                .create().show()
        }
    }

    fun setFilterViewListeners() {
        checkUt.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedCategories.add("untiered")
            } else {
                selectedCategories.remove("untiered")
            }
            itemAdpt!!.enactCategories(selectedCategories)
        }
        checkSt.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedCategories.add("set_tiered")
            } else {
                selectedCategories.remove("set_tiered")
            }
            itemAdpt!!.enactCategories(selectedCategories)
        }
        checkT.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedCategories.add("tiered")
            } else {
                selectedCategories.remove("tiered")
            }
            itemAdpt!!.enactCategories(selectedCategories)
        }
        checkWeak.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedCategories.add("weak")
            } else {
                selectedCategories.remove("weak")
            }
            itemAdpt!!.enactCategories(selectedCategories)
        }
        checkReskin.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedCategories.add("reskin")
            } else {
                selectedCategories.remove("reskin")
            }
            itemAdpt!!.enactCategories(selectedCategories)
        }
    }

    // various necessary setters and getters
    fun setLoadoutId(loadoutId: Int) {
        this.loadoutId = loadoutId
    }

    fun setActiveEffects(activeEffects: String) {
        this.activeEffects = BooleanArray(5)
        for (i in 0 until activeEffects.length) {
            this.activeEffects[i] = activeEffects[i] != '0'
        }
    }

    // Called by ItemAdapter and ClassAdapter. Assigns the selected object to the corresponding load out variable
    fun informSelected(item: Item?, type: Int) {
        val prevItem: Item?
        when (type) {
            WEAPON -> {
                prevItem = weapon
                weapon = item
                updateWeapon(prevItem)
            }
            ABILITY -> {
                prevItem = ability
                ability = item
                updateAbility(prevItem)
            }
            ARMOR -> {
                prevItem = armor
                armor = item
                updateArmor(prevItem)
            }
            RING -> {
                prevItem = ring
                ring = item
                updateRing(prevItem)
            }
        }
        mFragment.saveLoadouts()
        loadoutChanged = true
    }

    fun informSelected(mClass: CharClass?) {
        val prevClass = charClass
        charClass = mClass
        updateClass(mClass)
        if (charClass!!.weapons[0].absItemId != prevClass!!.weapons[0].absItemId) {
            val prevWeapon = weapon
            weapon = charClass!!.weapons[0]
            updateWeapon(prevWeapon)
        }
        if (charClass!!.abilities[0].absItemId != prevClass.abilities[0].absItemId) {
            val prevAbility = ability
            ability = charClass!!.abilities[0]
            updateAbility(prevAbility)
        }
        if (charClass!!.armors[0].absItemId != prevClass.armors[0].absItemId) {
            val prevArmor = armor
            armor = charClass!!.armors[0]
            updateArmor(prevArmor)
        }
        mFragment.saveLoadouts()
        loadoutChanged = true
    }

    fun informSelected(newStat: Int, type: Int) {
        val prevStat: Int
        val statDiff: StatBonus
        when (type) {
            ATT -> {
                prevStat = baseStats.attBonus
                statDiff = StatBonus(prevStat - newStat, 0, 0, 0, 0, 0, 0, 0)
                baseStats.subtractBonus(statDiff)
                statTotals.subtractBonus(statDiff)
                updateAtt()
            }
            DEX -> {
                prevStat = baseStats.dexBonus
                statDiff = StatBonus(0, 0, 0, prevStat - newStat, 0, 0, 0, 0)
                baseStats.subtractBonus(statDiff)
                statTotals.subtractBonus(statDiff)
                updateDex()
            }
        }
        mFragment.saveLoadouts()
        loadoutChanged = true
    }

    fun makeSelectorViewVisible(type: Int) {
        if (type == CLASS) {
            selectorView.visibility = View.VISIBLE
            backgroundFade.visibility = View.VISIBLE
            selectorView.x = -1000f
            selectorView.animate().translationXBy(1000f).duration = 200
            filterView.visibility = View.GONE
        } else {
            selectorView.visibility = View.VISIBLE
            filterView.visibility = View.VISIBLE
            backgroundFade.visibility = View.VISIBLE
            selectorView.x = -1000f
            filterView.x = -1000f
            selectorView.animate().translationXBy(1000f).duration = 200
            filterView.animate().translationXBy(1000f).duration = 200
        }
    }

    fun makeSelectorViewGone(type: Int) {
        if (type == CLASS) {
            selectorView.visibility = View.GONE
            backgroundFade.visibility = View.GONE
            filterView.visibility = View.GONE
        } else {
            selectorView.visibility = View.GONE
            backgroundFade.visibility = View.GONE
            filterView.visibility = View.GONE
        }
    }

    fun updateClass(prevClass: CharClass?) {

        // change image
        if (classView != null) {
            classView!!.setImageResource(charClass!!.imageId)
        }

        // subtract old base stats and add new ones
        if (prevClass != null) {
            statTotals.subtractBonus(baseStats)
            baseStats = charClass!!.maxedStats
            statTotals.addBonus(baseStats)
        }

        // update stat views
        updateAtt()
        updateDex()
    }

    private fun checkAndUpdateSetBonus() {
        val temp: List<Int> = ArrayList(
            Arrays.asList(
                weapon!!.partOfSet, ability!!.partOfSet, armor!!.partOfSet, ring!!.partOfSet
            )
        )
        val sets: MutableList<Int> = ArrayList()

        // makes a list of item sets with 2 or more equipped pieces
        for (i in temp.indices) {
            for (j in i + 1 until temp.size) {
                if (temp[i] == temp[j] && temp[i] != -1 && !sets.contains(temp[i])) {
                    sets.add(temp[i])
                    break
                }
            }
        }

        // removes old bonus, builds a new bonus from any current set bonuses, adds to total
        if (sets.size > 0) {
            val newBonus = StatBonus(0, 0, 0, 0, 0, 0, 0, 0)
            statTotals.subtractBonus(setBonus)
            for (i in sets.indices) {
                val currentSet = Item.itemSets!![sets[i]]
                val currentBonus = currentSet.getBonus(
                    currentSet.checkSet(
                        weapon!!.absItemId,
                        ability!!.absItemId,
                        armor!!.absItemId,
                        ring!!.absItemId
                    )
                )
                newBonus.addBonus(currentBonus)
            }
            setBonus.replaceBonus(newBonus)
            statTotals.addBonus(setBonus)
        }
    }

    fun updateWeapon(prevWeapon: Item?) {

        // change image
        if (weaponView != null) {
            weaponView!!.setImageResource(weapon!!.imageId)
        }
        if (prevWeapon != null) {
            // subtract old stats and add new ones
            statTotals.subtractBonus(prevWeapon.statBonus)
            statTotals.addBonus(weapon!!.statBonus)

            // check for set bonus
            checkAndUpdateSetBonus()

            // update stat views
            updateAtt()
            updateDex()
        }
    }

    fun updateAbility(prevAbility: Item?) {

        // change image
        if (abilityView != null) {
            abilityView!!.setImageResource(ability!!.imageId)
        }
        if (prevAbility != null) {
            // subtract old stats and add new ones
            statTotals.subtractBonus(prevAbility.statBonus)
            statTotals.addBonus(ability!!.statBonus)

            // check for set bonus
            checkAndUpdateSetBonus()

            // update stat views
            updateAtt()
            updateDex()
        }
    }

    fun updateArmor(prevArmor: Item?) {

        // change image
        if (armorView != null) {
            armorView!!.setImageResource(armor!!.imageId)
        }
        if (prevArmor != null) {
            // subtract old stats and add new ones
            statTotals.subtractBonus(prevArmor.statBonus)
            statTotals.addBonus(armor!!.statBonus)

            // check for set bonus
            checkAndUpdateSetBonus()

            // update stat views
            updateAtt()
            updateDex()
        }
    }

    fun updateRing(prevRing: Item?) {

        // change image
        if (ringView != null) {
            ringView!!.setImageResource(ring!!.imageId)
        }
        if (prevRing != null) {
            // subtract old stats and add new ones
            statTotals.subtractBonus(prevRing.statBonus)
            statTotals.addBonus(ring!!.statBonus)

            // check for set bonus
            checkAndUpdateSetBonus()

            // update stat views
            updateAtt()
            updateDex()
        }
    }

    private fun updateAtt() {
        if (attView != null) {
            var attStr = java.lang.String.valueOf(statTotals.attBonus)
            val bonusAtt = statTotals.attBonus - baseStats.attBonus
            if (bonusAtt > 0) {
                attStr = "$attStr(+$bonusAtt)"
            } else if (bonusAtt < 0) {
                attStr = "$attStr($bonusAtt)"
            }
            if (baseStats.attBonus < charClass!!.maxedStats.attBonus) {
                attView!!.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorUnmaxedText))
            } else {
                attView!!.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorMaxedText))
            }
            attView!!.text = attStr
        }
    }

    private fun updateDex() {
        if (dexView != null) {
            var dexStr = java.lang.String.valueOf(statTotals.dexBonus)
            val bonusDex = statTotals.dexBonus - baseStats.dexBonus
            if (bonusDex > 0) {
                dexStr = "$dexStr(+$bonusDex)"
            } else if (bonusDex < 0) {
                dexStr = "$dexStr($bonusDex)"
            }

            // change color if unmaxed
            if (baseStats.dexBonus < charClass!!.maxedStats.dexBonus) {
                dexView!!.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorUnmaxedText))
            } else {
                dexView!!.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorMaxedText))
            }
            dexView!!.text = dexStr
        }
    }

    fun updateStatus() {
        if (statusView != null) {
            if (!activeEffects[0]) {
                damagingView!!.setColorFilter(Color.parseColor("#777777"))
            } else {
                damagingView.setColorFilter(null)
            }
            if (!activeEffects[1]) {
                berserkView!!.setColorFilter(Color.parseColor("#777777"))
            } else {
                berserkView.setColorFilter(null)
            }
            if (!activeEffects[2]) {
                curseView!!.setColorFilter(Color.parseColor("#777777"))
            } else {
                curseView.setColorFilter(null)
            }
            if (!activeEffects[3]) {
                dazedView!!.setColorFilter(Color.parseColor("#777777"))
            } else {
                dazedView.setColorFilter(null)
            }
            if (!activeEffects[4]) {
                weakView!!.setColorFilter(Color.parseColor("#777777"))
            } else {
                weakView.setColorFilter(null)
            }
        }
    }

    fun updateAllViews() {
        updateClass(null)
        updateWeapon(null)
        updateAbility(null)
        updateArmor(null)
        updateRing(null)
        updateStatus()
    }

    val dps: List<DpsEntry>
        get() {
            if (loadoutChanged) {
                generateDps()
                loadoutChanged = false
            }
            return loadoutDps
        }

    private fun generateDps() {
        val DEFENSE_DMG_REDUCTION_CAP = 0.9f
        val MAX_ENEMY_DEFENSE = 150
        var realAtt = statTotals.attBonus
        var realDex = statTotals.dexBonus
        dexModifier = 1.0
        attModifier = 1.0
        dmgModifier = 1.0

        // turns on status effects
        if (activeEffects[0]) {  // Damaging
            attModifier = 1.25
        }
        if (activeEffects[1]) {  // Berserk
            dexModifier = 1.25
        }
        if (activeEffects[2]) {  // Curse
            dmgModifier = 1.25
        }
        if (activeEffects[3]) {  // Dazed (cancels berserk and sets dex to 0)
            dexModifier = 1.0
            realDex = 0
        }
        if (activeEffects[4]) {  // Weak (cancels damaging and sets att to 0)
            attModifier = 1.0
            realAtt = 0
        }
        loadoutDps = ArrayList()
        val baseDmg = (weapon!!.avgDamage - 0.5) * (0.5 + realAtt / 50.0)
        val minimumDmg =
            Math.round(baseDmg * (1 - DEFENSE_DMG_REDUCTION_CAP)) * dmgModifier * attModifier
        val finalRof = (1.5 + 6.5 * (realDex / 75.0)) * dexModifier * weapon!!.rateOfFire
        val noOfShots = weapon!!.noOfShots
        for (currentDefense in 0..MAX_ENEMY_DEFENSE) {   // Generate a table row for every defense level up to the max
            var finalDps: Double
            finalDps = if (weapon!!.attribute == 0) {          // defense is a factor
                val currentDmg = Math.round(baseDmg - currentDefense) * dmgModifier * attModifier
                val finalDamage = Math.max(
                    currentDmg,  // shot damage minus enemy defense rounded, multiplied by curse and damaging/weak modifiers
                    minimumDmg // minimum shot damage due to defense reduction cap
                )
                finalDamage * noOfShots * finalRof
            } else {    // defense factor removed (armor piercing)
                Math.round(baseDmg) * dmgModifier * attModifier * noOfShots * finalRof
            }
            loadoutDps.add(DpsEntry(finalDps, loadoutId))
        }
    }

    companion object {
        // misc
        var classData: List<CharClass>? = null
        const val WEAPON = 0
        const val ABILITY = 1
        const val ARMOR = 2
        const val RING = 3
        const val ATT = 4
        const val DEX = 5
        const val CLASS = 6
        private val selectedCategories =
            ArrayList(Arrays.asList("untiered", "set_tiered", "tiered"))
    }
}
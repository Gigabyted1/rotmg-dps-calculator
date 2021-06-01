package com.waynebloom.rotmgdpscalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.waynebloom.rotmgdpscalculator.MainActivity
import com.waynebloom.rotmgdpscalculator.loadout.Loadout
import com.waynebloom.rotmgdpscalculator.loadout.LoadoutFragment
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class MainActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null
    private var mViewPager: ViewPager2? = null
    private var pagerAdapter: SectionsStatePagerAdapter? = null
    var navigationView: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.my_toolbar)
        mViewPager = findViewById(R.id.container)
        setupViewPager(mViewPager)
        navigationView = findViewById(R.id.dps_view_menu)
        navigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val itemId = item.itemId
            if (itemId == R.id.page_1) {
                setViewPagerPosition(0)
                true
            } else if (itemId == R.id.page_2) {
                setViewPagerPosition(1)
                true
            } else {
                false
            }
        })

        // Reads item and class data from file
        readData()

        // Toolbar stuff
        setSupportActionBar(toolbar)
        title = "RotMG DPS Calculator"
        toolbar.setTitleTextAppearance(this, R.style.MyFontAppearance)
    }

    override fun onBackPressed() {
        if (mViewPager!!.currentItem == 0) {
            loadoutFragment!!.onBackPressed()
        } else {
            navigationView!!.selectedItemId = R.id.page_1
            dpsFragment!!.onBackPressed()
        }
    }

    private fun setupViewPager(viewPager: ViewPager2?) {
        pagerAdapter = SectionsStatePagerAdapter(this@MainActivity)
        pagerAdapter!!.addFragment(LoadoutFragment(), "Loadout fragment")
        pagerAdapter!!.addFragment(DpsFragment(), "DPS fragment")
        viewPager!!.isUserInputEnabled = false
        viewPager.adapter = pagerAdapter
    }

    fun setViewPagerPosition(position: Int) {
        // if switching to dps
        mViewPager!!.currentItem = position
        if (position == 1) {
            val dpsFragment = pagerAdapter!!.getFragment(1) as DpsFragment
            dpsFragment.updateDpsViews()
            dpsFragment.displayView(DpsFragment.Companion.UPDATE)
        }
    }

    val loadoutFragment: LoadoutFragment?
        get() = pagerAdapter!!.getFragment(0) as LoadoutFragment
    val dpsFragment: DpsFragment?
        get() = pagerAdapter!!.getFragment(1) as DpsFragment

    // read game data from file
    private fun readData() {
        val fileNames = arrayOf("items.json", "classes.json", "item_sets.json")
        val data = arrayOfNulls<JSONObject>(3)
        try {
            for (i in 0..2) {
                val reader = BufferedReader(
                    InputStreamReader(
                        assets.open(
                            fileNames[i]
                        )
                    )
                )
                val read = StringBuilder()
                while (reader.ready()) {
                    read.append(reader.readLine())
                }
                data[i] = JSONObject(read.toString())
            }
            parseData(data)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    @Throws(JSONException::class)
    private fun parseData(data: Array<JSONObject?>) {
        val STANDARD_SET_MAXIMUM_INDEX = 37
        val itemOutsideSize = data[0]!!.getJSONArray("item").length()
        val classArraySize = data[1]!!.getJSONArray("classes").length()
        val setArraySize = data[2]!!.getJSONArray("set").length()
        val items: List<ArrayList<Item>> =
            ArrayList() // Create outside arraylist for sub-types of items
        val classes: List<CharClass> = ArrayList() // Create arraylist for character class objects
        val itemSets: MutableList<ItemSet> =
            ArrayList(setArraySize) // Create arraylist for item set objects

        // preallocate all indices, as the loader for this list jumps around
        for (i in 0 until setArraySize) {
            itemSets.add(ItemSet())
        }

        // load items
        for (i in 0 until itemOutsideSize) {
            val itemInsideSize = data[0]!!.getJSONArray("item").getJSONArray(i).length()
            items.add(i, ArrayList<Item>())
            for (j in 0 until itemInsideSize) {
                val currentItem = data[0]!!.getJSONArray("item").getJSONArray(i).getJSONObject(j)
                items[i].add(
                    j, Item(
                        currentItem.getString("name"),
                        resources.getIdentifier(
                            currentItem.getString("image"),
                            "drawable",
                            packageName
                        ),
                        currentItem.getString("categories"),
                        j,  // relative id
                        currentItem.getInt("absItemId"),
                        currentItem.getInt("partOfSet"),
                        StatBonus(
                            currentItem.getInt("att"),
                            0,
                            0,
                            currentItem.getInt("dex"),
                            0,
                            0,
                            0,
                            0
                        ),
                        currentItem.getDouble("shot_dmg"),
                        currentItem.getInt("no_shots"),
                        currentItem.getDouble("rate_of_fire"),
                        currentItem.getDouble("range"),
                        currentItem.getInt("ap")
                    )
                )
            }
        }

        // load classes
        for (i in 0 until classArraySize) {
            val currentClass = data[1]!!.getJSONArray("classes").getJSONObject(i)
            classes.add(
                i, CharClass(
                    currentClass.getString("name"),
                    i,
                    items[currentClass.getInt("weapon")],
                    items[currentClass.getInt("ability")],
                    items[currentClass.getInt("armor")],
                    items[16],  // Rings
                    resources.getIdentifier(
                        currentClass.getString("name").toLowerCase(),
                        "drawable",
                        packageName
                    ),  // Turn image name into id
                    StatBonus(
                        currentClass.getInt("maxAtt"),
                        0,
                        0,
                        currentClass.getInt("maxDex"),
                        0,
                        0,
                        0,
                        0
                    )
                )
            )
        }
        Loadout.classData = classes

        // load item sets
        for (i in 0 until setArraySize) {
            val currentSet = data[2]!!.getJSONArray("set").getJSONObject(i)
            if (i <= STANDARD_SET_MAXIMUM_INDEX) {       // standard item sets (one of each item type)
                itemSets.add(
                    currentSet.getInt("id"),
                    ItemSet(
                        currentSet.getInt("weaponId"),
                        currentSet.getInt("abilityId"),
                        currentSet.getInt("armorId"),
                        currentSet.getInt("ringId"),
                        StatBonus(
                            currentSet.getJSONObject("two_item_bonus").getInt("att"),
                            currentSet.getJSONObject("two_item_bonus").getInt("def"),
                            currentSet.getJSONObject("two_item_bonus").getInt("spd"),
                            currentSet.getJSONObject("two_item_bonus").getInt("dex"),
                            currentSet.getJSONObject("two_item_bonus").getInt("wis"),
                            currentSet.getJSONObject("two_item_bonus").getInt("vit"),
                            currentSet.getJSONObject("two_item_bonus").getInt("life"),
                            currentSet.getJSONObject("two_item_bonus").getInt("mana")
                        ),
                        StatBonus(
                            currentSet.getJSONObject("three_item_bonus").getInt("att"),
                            currentSet.getJSONObject("three_item_bonus").getInt("def"),
                            currentSet.getJSONObject("three_item_bonus").getInt("spd"),
                            currentSet.getJSONObject("three_item_bonus").getInt("dex"),
                            currentSet.getJSONObject("three_item_bonus").getInt("wis"),
                            currentSet.getJSONObject("three_item_bonus").getInt("vit"),
                            currentSet.getJSONObject("three_item_bonus").getInt("life"),
                            currentSet.getJSONObject("three_item_bonus").getInt("mana")
                        ),
                        StatBonus(
                            currentSet.getJSONObject("four_item_bonus").getInt("att"),
                            currentSet.getJSONObject("four_item_bonus").getInt("def"),
                            currentSet.getJSONObject("four_item_bonus").getInt("spd"),
                            currentSet.getJSONObject("four_item_bonus").getInt("dex"),
                            currentSet.getJSONObject("four_item_bonus").getInt("wis"),
                            currentSet.getJSONObject("four_item_bonus").getInt("vit"),
                            currentSet.getJSONObject("four_item_bonus").getInt("life"),
                            currentSet.getJSONObject("four_item_bonus").getInt("mana")
                        )
                    )
                )
            } else {          // nonstandard item sets (multiple items per type)

                // translating from JSONArray to required types
                val jWeaponIds = currentSet.getJSONArray("weaponId")
                val weaponIds = IntArray(jWeaponIds.length())
                for (k in 0 until jWeaponIds.length()) {
                    weaponIds[k] = jWeaponIds.getInt(k)
                }
                val jAbilityIds = currentSet.getJSONArray("abilityId")
                val abilityIds = IntArray(jAbilityIds.length())
                for (k in 0 until jAbilityIds.length()) {
                    abilityIds[k] = jAbilityIds.getInt(k)
                }
                val jArmorIds = currentSet.getJSONArray("armorId")
                val armorIds = IntArray(jArmorIds.length())
                for (k in 0 until jArmorIds.length()) {
                    armorIds[k] = jArmorIds.getInt(k)
                }
                val jRingIds = currentSet.getJSONArray("ringId")
                val ringIds = IntArray(jRingIds.length())
                for (k in 0 until jRingIds.length()) {
                    ringIds[k] = jRingIds.getInt(k)
                }
                val jTwoItem = currentSet.getJSONArray("two_item_bonus")
                val twoItem = arrayOfNulls<StatBonus>(jTwoItem.length())
                for (k in 0 until jTwoItem.length()) {
                    val currentBonus = jTwoItem.getJSONObject(k)
                    twoItem[k] = StatBonus(
                        currentBonus.getInt("att"),
                        currentBonus.getInt("def"),
                        currentBonus.getInt("spd"),
                        currentBonus.getInt("dex"),
                        currentBonus.getInt("wis"),
                        currentBonus.getInt("vit"),
                        currentBonus.getInt("life"),
                        currentBonus.getInt("mana")
                    )
                }
                val jThreeItem = currentSet.getJSONArray("three_item_bonus")
                val threeItem = arrayOfNulls<StatBonus>(jThreeItem.length())
                for (k in 0 until jThreeItem.length()) {
                    val currentBonus = jThreeItem.getJSONObject(k)
                    threeItem[k] = StatBonus(
                        currentBonus.getInt("att"),
                        currentBonus.getInt("def"),
                        currentBonus.getInt("spd"),
                        currentBonus.getInt("dex"),
                        currentBonus.getInt("wis"),
                        currentBonus.getInt("vit"),
                        currentBonus.getInt("life"),
                        currentBonus.getInt("mana")
                    )
                }
                val jFourItem = currentSet.getJSONArray("four_item_bonus")
                val fourItem = arrayOfNulls<StatBonus>(jFourItem.length())
                for (k in 0 until jFourItem.length()) {
                    val currentBonus = jFourItem.getJSONObject(k)
                    fourItem[k] = StatBonus(
                        currentBonus.getInt("att"),
                        currentBonus.getInt("def"),
                        currentBonus.getInt("spd"),
                        currentBonus.getInt("dex"),
                        currentBonus.getInt("wis"),
                        currentBonus.getInt("vit"),
                        currentBonus.getInt("life"),
                        currentBonus.getInt("mana")
                    )
                }
                itemSets.add(
                    currentSet.getInt("id"),
                    MultiItemSet(
                        weaponIds,
                        abilityIds,
                        armorIds,
                        ringIds,
                        twoItem,
                        threeItem,
                        fourItem
                    )
                )
            }
        }
        Item.Companion.itemSets = ArrayList(itemSets)
    }
}
package com.waynebloom.rotmgdpscalculator;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    private ViewPager2 mViewPager;
    private SectionsStatePagerAdapter pagerAdapter;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.my_toolbar);
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);
        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.page_1:
                        setViewPagerPosition(0);
                        return true;
                    case R.id.page_2:
                        setViewPagerPosition(1);
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Reads item and class data from file
        readData();

        // Toolbar stuff
        setSupportActionBar(toolbar);
        setTitle("RotMG DPS Calculator");
        toolbar.setTitleTextAppearance(this, R.style.MyFontAppearance);

    }

    private void setupViewPager(ViewPager2 viewPager) {
        pagerAdapter = new SectionsStatePagerAdapter(MainActivity.this);
        pagerAdapter.addFragment(new LoadoutFragment(), "Loadout fragment");
        pagerAdapter.addFragment(new DpsFragment(), "DPS fragment");
        viewPager.setUserInputEnabled(false);
        viewPager.setAdapter(pagerAdapter);
    }

    public void setViewPagerPosition(int position) {
        // if switching to dps
        mViewPager.setCurrentItem(position);
        if(position == 1) {
            DpsFragment dpsFragment = (DpsFragment) pagerAdapter.getFragment(1);
            dpsFragment.displayDpsTable();
        }
    }

    public SectionsStatePagerAdapter getFragmentHolder() {
        return pagerAdapter;
    }

    // read game data from file
    private void readData() {
        String[] fileNames = { "items.json", "classes.json", "item_sets.json" };
        JSONObject[] data = new JSONObject[3];
        try {
            for(int i = 0; i < 3; i++) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(fileNames[i])));
                StringBuilder read = new StringBuilder();
                while(reader.ready()) {
                    read.append(reader.readLine());
                }
                data[i] = new JSONObject(read.toString());
            }
            parseData(data);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    private void parseData(JSONObject[] data) throws JSONException {
        final int STANDARD_SET_MAXIMUM_INDEX = 37;
        int itemOutsideSize = data[0].getJSONArray("item").length();
        int classArraySize = data[1].getJSONArray("classes").length();
        int setArraySize = data[2].getJSONArray("set").length();
        List<ArrayList<Item>> items = new ArrayList<>();            // Create outside arraylist for sub-types of items
        List<CharClass> classes = new ArrayList<>();                // Create arraylist for character class objects
        List<ItemSet> itemSets = new ArrayList<>(setArraySize);     // Create arraylist for item set objects

        // preallocate all indices, as the loader for this list jumps around
        for(int i = 0; i < setArraySize; i++) {
            itemSets.add(new ItemSet());
        }

        // load items
        for (int i = 0; i < itemOutsideSize; i++) {
            int itemInsideSize = data[0].getJSONArray("item").getJSONArray(i).length();
            items.add(i, new ArrayList<Item>());

            for(int j = 0; j < itemInsideSize; j++) {
                JSONObject currentItem = data[0].getJSONArray("item").getJSONArray(i).getJSONObject(j);
                items.get(i).add(j, new Item(
                        currentItem.getString("name"),
                        getResources().getIdentifier(currentItem.getString("image"), "drawable", getPackageName()),
                        currentItem.getString("categories"),
                        j, // relative id
                        currentItem.getInt("absItemId"),
                        currentItem.getInt("partOfSet"),
                        new StatBonus(currentItem.getInt("att"), 0, 0, currentItem.getInt("dex"), 0, 0, 0, 0),
                        currentItem.getDouble("shot_dmg"),
                        currentItem.getInt("no_shots"),
                        currentItem.getDouble("rate_of_fire"),
                        currentItem.getDouble("range"),
                        currentItem.getInt("ap"))
                );
            }
        }

        // load classes
        for (int i = 0; i < classArraySize; i++) {
            JSONObject currentClass = data[1].getJSONArray("classes").getJSONObject(i);
            classes.add(i, new CharClass(
                    currentClass.getString("name"),
                    i,
                    items.get(currentClass.getInt("weapon")),
                    items.get(currentClass.getInt("ability")),
                    items.get(currentClass.getInt("armor")),
                    items.get(16),  // Rings
                    getResources().getIdentifier(currentClass.getString("name").toLowerCase(), "drawable", getPackageName()), // Turn image name into id
                    new StatBonus(currentClass.getInt("maxAtt"), 0, 0, currentClass.getInt("maxDex"), 0, 0, 0, 0))
            );
        }
        Loadout.classData = classes;

        // load item sets
        for (int i = 0; i < setArraySize; i++) {
            JSONObject currentSet = data[2].getJSONArray("set").getJSONObject(i);

            if(i <= STANDARD_SET_MAXIMUM_INDEX) {       // standard item sets (one of each item type)
                itemSets.add(
                    currentSet.getInt("id"),
                    new ItemSet(
                        currentSet.getInt("weaponId"),
                        currentSet.getInt("abilityId"),
                        currentSet.getInt("armorId"),
                        currentSet.getInt("ringId"),
                        new StatBonus(
                                currentSet.getJSONObject("two_item_bonus").getInt("att"),
                                currentSet.getJSONObject("two_item_bonus").getInt("def"),
                                currentSet.getJSONObject("two_item_bonus").getInt("spd"),
                                currentSet.getJSONObject("two_item_bonus").getInt("dex"),
                                currentSet.getJSONObject("two_item_bonus").getInt("wis"),
                                currentSet.getJSONObject("two_item_bonus").getInt("vit"),
                                currentSet.getJSONObject("two_item_bonus").getInt("life"),
                                currentSet.getJSONObject("two_item_bonus").getInt("mana")
                        ),
                        new StatBonus(
                                currentSet.getJSONObject("three_item_bonus").getInt("att"),
                                currentSet.getJSONObject("three_item_bonus").getInt("def"),
                                currentSet.getJSONObject("three_item_bonus").getInt("spd"),
                                currentSet.getJSONObject("three_item_bonus").getInt("dex"),
                                currentSet.getJSONObject("three_item_bonus").getInt("wis"),
                                currentSet.getJSONObject("three_item_bonus").getInt("vit"),
                                currentSet.getJSONObject("three_item_bonus").getInt("life"),
                                currentSet.getJSONObject("three_item_bonus").getInt("mana")
                        ),
                        new StatBonus(
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
                );
            }
            else {          // nonstandard item sets (multiple items per type)

                // translating from JSONArray to required types
                JSONArray jWeaponIds = currentSet.getJSONArray("weaponId");
                int[] weaponIds = new int[0];
                if (jWeaponIds != null) {
                    weaponIds = new int[jWeaponIds.length()];
                    for(int k = 0; k < jWeaponIds.length(); k++) {
                        weaponIds[k] = jWeaponIds.getInt(k);
                    }
                }

                JSONArray jAbilityIds = currentSet.getJSONArray("abilityId");
                int[] abilityIds = new int[0];
                if (jAbilityIds != null) {
                    abilityIds = new int[jAbilityIds.length()];
                    for(int k = 0; k < jAbilityIds.length(); k++) {
                        abilityIds[k] = jAbilityIds.getInt(k);
                    }
                }

                JSONArray jArmorIds = currentSet.getJSONArray("armorId");
                int[] armorIds = new int[0];
                if (jArmorIds != null) {
                    armorIds = new int[jArmorIds.length()];
                    for(int k = 0; k < jArmorIds.length(); k++) {
                        armorIds[k] = jArmorIds.getInt(k);
                    }
                }

                JSONArray jRingIds = currentSet.getJSONArray("ringId");
                int[] ringIds = new int[0];
                if (jRingIds != null) {
                    ringIds = new int[jRingIds.length()];
                    for(int k = 0; k < jRingIds.length(); k++) {
                        ringIds[k] = jRingIds.getInt(k);
                    }
                }

                JSONArray jTwoItem = currentSet.getJSONArray("two_item_bonus");
                StatBonus[] twoItem = new StatBonus[0];
                if (jTwoItem != null) {
                    twoItem = new StatBonus[jTwoItem.length()];
                    for(int k = 0; k < jTwoItem.length(); k++) {
                        JSONObject currentBonus = jTwoItem.getJSONObject(k);
                        twoItem[k] = new StatBonus(
                                currentBonus.getInt("att"),
                                currentBonus.getInt("def"),
                                currentBonus.getInt("spd"),
                                currentBonus.getInt("dex"),
                                currentBonus.getInt("wis"),
                                currentBonus.getInt("vit"),
                                currentBonus.getInt("life"),
                                currentBonus.getInt("mana")
                        );
                    }
                }

                JSONArray jThreeItem = currentSet.getJSONArray("three_item_bonus");
                StatBonus[] threeItem = new StatBonus[0];
                if (jThreeItem != null) {
                    threeItem = new StatBonus[jThreeItem.length()];
                    for(int k = 0; k < jThreeItem.length(); k++) {
                        JSONObject currentBonus = jThreeItem.getJSONObject(k);
                        threeItem[k] = new StatBonus(
                                currentBonus.getInt("att"),
                                currentBonus.getInt("def"),
                                currentBonus.getInt("spd"),
                                currentBonus.getInt("dex"),
                                currentBonus.getInt("wis"),
                                currentBonus.getInt("vit"),
                                currentBonus.getInt("life"),
                                currentBonus.getInt("mana")
                        );
                    }
                }

                JSONArray jFourItem = currentSet.getJSONArray("four_item_bonus");
                StatBonus[] fourItem = new StatBonus[0];
                if (jFourItem != null) {
                    fourItem = new StatBonus[jFourItem.length()];
                    for(int k = 0; k < jFourItem.length(); k++) {
                        JSONObject currentBonus = jFourItem.getJSONObject(k);
                        fourItem[k] = new StatBonus(
                                currentBonus.getInt("att"),
                                currentBonus.getInt("def"),
                                currentBonus.getInt("spd"),
                                currentBonus.getInt("dex"),
                                currentBonus.getInt("wis"),
                                currentBonus.getInt("vit"),
                                currentBonus.getInt("life"),
                                currentBonus.getInt("mana")
                        );
                    }
                }

                itemSets.add(currentSet.getInt("id"), new MultiItemSet(weaponIds, abilityIds, armorIds, ringIds, twoItem, threeItem, fourItem));
            }

        }
        Item.itemSets = new ArrayList<>(itemSets);
    }
}

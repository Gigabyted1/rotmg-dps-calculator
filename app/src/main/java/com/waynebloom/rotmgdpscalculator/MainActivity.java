package com.waynebloom.rotmgdpscalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ArrayList<Loadout> loadouts = new ArrayList<>(8);
    String[] statusEffectNames;
    DpsAdapter dpsAdpt;
    LoadoutAdapter loadAdpt;
    boolean onLoadouts = true;
    File saveFile;

    List<List<DpsEntry>> dpsDataTable = new ArrayList<>();

    Toolbar toolbar;
    View fade;
    View header;
    RecyclerView dpsTableView;
    RecyclerView itemSelView;
    RecyclerView loadoutView;
    ConstraintLayout statEditView;
    SeekBar statSeekView;
    TextView titleView;
    TextView baseStatView;
    TextView wepStatView;
    TextView abilStatView;
    TextView armStatView;
    TextView ringStatView;
    Button statConfirm;
    Button addBuild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveFile = new File(getApplicationContext().getFilesDir(), "loadouts.txt");
        statusEffectNames = getResources().getStringArray(R.array.stat_effects);

        toolbar = findViewById(R.id.my_toolbar);
        fade = findViewById(R.id.fade);
        header = findViewById(R.id.footer);
        dpsTableView = findViewById(R.id.dps_table_view);
        itemSelView = findViewById(R.id.item_selection_view);
        loadoutView = findViewById(R.id.loadout_view);
        statEditView = findViewById(R.id.stat_edit);
        statSeekView = findViewById(R.id.stat_seekbar);
        titleView = findViewById(R.id.title);
        baseStatView = findViewById(R.id.base_stat);
        wepStatView = findViewById(R.id.wep_stat);
        abilStatView = findViewById(R.id.abil_stat);
        armStatView = findViewById(R.id.arm_stat);
        ringStatView = findViewById(R.id.ring_stat);
        statConfirm = findViewById(R.id.confirm);
        addBuild = findViewById(R.id.add_button);

        // Reads item and class data from file
        readData();

        // Load previous loadouts
        try {
            loadBuilds();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadAdpt = new LoadoutAdapter(this, loadouts);
        loadoutView.setAdapter(loadAdpt);
        loadoutView.setLayoutManager(new LinearLayoutManager(this));

        // Toolbar stuff
        setSupportActionBar(toolbar);
        setTitle("Builds");
        toolbar.setTitleTextAppearance(this, R.style.MyFontAppearance);
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            saveBuilds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //More action bar setup
        getMenuInflater().inflate(R.menu.menubar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_switch:
                if(onLoadouts) {
                    loadoutView.setVisibility(View.GONE);
                    dpsTableView.setVisibility(View.VISIBLE);
                    addBuild.setVisibility(View.GONE);
                    setTitle("DPS Table");
                    populateDpsTable();
                    onLoadouts = false;
                }
                else {
                    loadoutView.setVisibility(View.VISIBLE);
                    dpsTableView.setVisibility(View.GONE);
                    addBuild.setVisibility(View.VISIBLE);
                    setTitle("Loadouts");
                    onLoadouts = true;
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(!onLoadouts) {
            loadoutView.setVisibility(View.VISIBLE);
            dpsTableView.setVisibility(View.GONE);
            addBuild.setVisibility(View.VISIBLE);
            setTitle("Loadouts");
            onLoadouts = true;
        }

        if(itemSelView.getVisibility() == View.VISIBLE) {
            itemSelView.setVisibility(View.GONE);
            fade.setVisibility(View.GONE);
            header.setVisibility(View.GONE);
        }
    }

    public void createNewLoadout(View view) {

        // Randomly select the default class
        CharClass randomClass = Loadout.classData.get(new Random().nextInt(16));

        if(loadouts.size() < 8) {
            Loadout newLoadout = new Loadout(getApplicationContext(),
                    this,
                    loadouts.size(),
                    randomClass
            );
            loadouts.add(newLoadout);
            loadAdpt.notifyDataSetChanged();
        }
        else {
            Toast.makeText(this, "You can only have 8 loadouts at a time.", Toast.LENGTH_LONG).show();
        }

    }

    // Generates an ascending order list of each current loadout's damage per second for each level of defense up to the maximum
    public void populateDpsTable() {
        dpsDataTable.clear();

        for(int currentDefLevel = 0; currentDefLevel <= 150; currentDefLevel++) {
            ArrayList<DpsEntry> dpsTableAtCurrentDefLevel = new ArrayList<>();
            for(Loadout currentLoadout : loadouts) {
                dpsTableAtCurrentDefLevel.add(currentLoadout.getDps().get(currentDefLevel));
            }
            dpsDataTable.add(dpsTableAtCurrentDefLevel);
        }

        dpsDataTable = sortDpsTable(dpsDataTable);

        if(dpsAdpt == null) {
            dpsAdpt = new DpsAdapter(MainActivity.this, dpsDataTable);
            dpsTableView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            dpsTableView.setAdapter(dpsAdpt);
        }
        else {
            dpsAdpt.notifyDataSetChanged();
        }
    }

    public void notifyLoadoutRemoved() {
        if(dpsAdpt != null) {
            dpsAdpt = new DpsAdapter(MainActivity.this, dpsDataTable);
            dpsTableView.setAdapter(dpsAdpt);
        }
    }

    private List<List<DpsEntry>> sortDpsTable(List<List<DpsEntry>> data) {        // Insert sort
        for(int i = 0; i <= 150; i++) {                                 // Insert sort on the data
            for(int j = 1; j < loadouts.size(); j++) {
                DpsEntry currentEntry = data.get(i).get(j);
                int k = j;
                while(k > 0 && data.get(i).get(k - 1).getDps() < currentEntry.getDps()) {
                    data.get(i).set(k, data.get(i).get(k - 1));
                    k--;
                }
                data.get(i).set(k, currentEntry);
            }
        }

        return data;
    }
    private void saveBuilds() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile, false));
        StringBuilder saveStr = new StringBuilder();

        for(Loadout i : loadouts) {
            saveStr.append(i.getCharClass().getClassId()).append('/');
            saveStr.append(i.getWeapon().getRelItemId()).append('/');
            saveStr.append(i.getAbility().getRelItemId()).append('/');
            saveStr.append(i.getArmor().getRelItemId()).append('/');
            saveStr.append(i.getRing().getRelItemId()).append('/');

            boolean[] activeEffects = i.getActiveEffects();
            for (boolean activeEffect : activeEffects) {
                if (activeEffect) {
                    saveStr.append('1');
                } else {
                    saveStr.append('0');
                }
            }
            saveStr.append("/\n");
        }
        writer.write(saveStr.toString());
        writer.close();
    }     // | TODO: Clean this shit up, my EYES
    private void loadBuilds() throws IOException {
        BufferedReader loadReader = new BufferedReader(new FileReader(saveFile));
        ArrayList<StringBuilder> lineData;
        char fileChar;
        int lineLoc;

        while(loadReader.ready()) {
            lineData = new ArrayList<>(6);
            lineLoc = 0;
            for(int i = 0; i < 6; i++) {
                lineData.add(new StringBuilder());
            }
            while(loadReader.ready() && lineLoc < 6) {
                fileChar = (char)loadReader.read();
                if(fileChar == '/') {
                    lineLoc++;
                }
                else {
                    lineData.get(lineLoc).append(fileChar);
                }

                if(lineLoc == 6) {
                    loadReader.read();
                    int[] temps = { Integer.parseInt(lineData.get(0).toString()), Integer.parseInt(lineData.get(1).toString()),
                            Integer.parseInt(lineData.get(2).toString()), Integer.parseInt(lineData.get(3).toString()),
                            Integer.parseInt(lineData.get(4).toString()) };

                    // Translate the nonsense above
                    int loadoutId = loadouts.size();
                    CharClass loadedClass = Loadout.classData.get(temps[0]);
                    Item loadedWeapon = loadedClass.getWeapons().get(temps[1]);
                    Item loadedAbility = loadedClass.getAbilities().get(temps[2]);
                    Item loadedArmor = loadedClass.getArmors().get(temps[3]);
                    Item loadedRing = loadedClass.getRings().get(temps[4]);
                    String loadedEffects = lineData.get(5).toString();

                    Loadout newLoadout = new Loadout(
                            getApplicationContext(),
                            MainActivity.this,
                            loadoutId,
                            loadedClass,
                            loadedWeapon,
                            loadedAbility,
                            loadedArmor,
                            loadedRing,
                            loadedEffects
                    );
                    loadouts.add(newLoadout);
                }
            }
        }

        loadReader.close();
    }     // |

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
                    getResources().getIdentifier(currentClass.getString("name"), "drawable", getPackageName()), // Turn image name into id
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

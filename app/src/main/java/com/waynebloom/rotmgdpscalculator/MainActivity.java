package com.waynebloom.rotmgdpscalculator;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ClickListeners {
    ArrayList<Loadout> loadouts = new ArrayList<>(8);
    JSONObject[] data = new JSONObject[2];
    ArrayList<CharClass> classes;
    ArrayList<ArrayList<Item>> items;
    String[] statusEffectNames;
    DpsAdapter dpsTableAdpt;
    LoadoutAdapter loadAdpt;
    ItemAdapter itemAdpt;
    boolean onLoadouts = true;
    File saveFile;
    ArrayList<String> selectedCategories;

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

        AppCenter.start(getApplication(), "5b1fb4f9-62fe-4eb6-a98e-581fb668b5f8",
                Analytics.class, Crashes.class);

        saveFile = new File(getApplicationContext().getFilesDir(), "loadouts.txt");
        statusEffectNames = getResources().getStringArray(R.array.stat_effects);

        toolbar = findViewById(R.id.my_toolbar);
        fade = findViewById(R.id.fade);
        header = findViewById(R.id.footer);
        dpsTableView = findViewById(R.id.dps_table_view);
        itemSelView = findViewById(R.id.item_selection_view);
        loadoutView = findViewById(R.id.loadout_view);
            View footer = new View(MainActivity.this);
            footer.setLayoutParams( new AbsListView.LayoutParams( Toolbar.LayoutParams.FILL_PARENT, 350));
            loadoutView.addFooterView(footer, null, false);
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

        // Reads item data from file
        readData(data);

        selectedCategories = new ArrayList<>();
        selectedCategories.add("untiered");
        selectedCategories.add("set_tiered");
        selectedCategories.add("tiered");
        setupCategorySelectors();

        // Load previous builds
        try {
            loadBuilds();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Adding a footer
        //ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.footer, itemSelView ,false);
        //itemSelView.addHeaderView(headerView);

        // Toolbar stuff
        setSupportActionBar(toolbar);
        setTitle("Builds");
        toolbar.setTitleTextAppearance(this, R.style.MyFontAppearance);
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
                    generateDpsTable();
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

    // Links with the LoadoutAdapter class through the ClickListeners interface to assign onClickListeners to each element in a loadout
    public void setupLoadoutViews(final ImageView charClass,
                                  final ImageView weapon,
                                  final ImageView ability,
                                  final ImageView armor,
                                  final ImageView ring,
                                  final TextView att,
                                  final TextView dex,
                                  final ConstraintLayout status,
                                  final Button del,
                                  final int position) {
        final Loadout currLoadout = loadouts.get(position);

        // Class
        charClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassAdapter classSelAdpt = new ClassAdapter(getApplicationContext(), classes);
                itemSelView.setAdapter(classSelAdpt);
                itemSelView.setX(-1000f);
                itemSelView.setVisibility(View.VISIBLE);
                itemSelView.animate().translationXBy(1000f).setDuration(200);
                header.setX(-1000f);
                header.setVisibility(View.VISIBLE);
                header.animate().translationXBy(1000f).setDuration(200);
                fade.setVisibility(View.VISIBLE);

                itemSelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String temp;
                        currLoadout.charClass = classes.get(position);
                        currLoadout.classId = position;
                        currLoadout.baseAtt = currLoadout.charClass.baseAtt;
                        currLoadout.baseDex = currLoadout.charClass.baseDex;

                        currLoadout.updateStats();
                        temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                        att.setText(temp);
                        temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                        dex.setText(temp);

                        //TODO: Fix item saving between classes that share an item type
                        /*if(!currLoadout.wep.subType.equals(currLoadout.charClass.weps.get(0).subType)) {
                            w.setImageResource(currLoadout.charClass.weps.get(0).imageId);
                            currLoadout.wep = currLoadout.charClass.weps.get(0);
                            currLoadout.wepId = 0;
                        }
                        if(!currLoadout.abil.subType.equals(currLoadout.charClass.abils.get(0).subType)) {
                            ab.setImageResource(currLoadout.charClass.abils.get(0).imageId);
                            currLoadout.abil = currLoadout.charClass.abils.get(0);
                            currLoadout.abilId = 0;
                        }
                        if(!currLoadout.arm.subType.equals(currLoadout.charClass.arms.get(0).subType)) {
                            ar.setImageResource(currLoadout.charClass.arms.get(0).imageId);
                            currLoadout.arm = currLoadout.charClass.arms.get(0);
                            currLoadout.armId = 0;
                        }*/

                        itemSelView.setVisibility(View.GONE);
                        fade.setVisibility(View.GONE);
                        header.setVisibility(View.GONE);
                        charClass.setImageResource(currLoadout.charClass.imageId);
                    }
                });
            }
        });

        // Weapon
        weapon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemListOffset = currLoadout.wepId;
                itemAdpt = new ItemAdapter(getApplicationContext(), currLoadout.charClass.weps);
                itemAdpt.enactCategories(selectedCategories);
                itemSelView.setAdapter(itemAdpt);
                itemSelView.setX(-1000f);
                itemSelView.setVisibility(View.VISIBLE);
                itemSelView.animate().translationXBy(1000f).setDuration(200);
                header.setX(-1000f);
                header.setVisibility(View.VISIBLE);
                header.animate().translationXBy(1000f).setDuration(200);
                fade.setVisibility(View.VISIBLE);
                itemSelView.animate().translationXBy(1000f).setDuration(200);
                //itemSelView.setSelectionFromTop(itemListOffset, 0);

                itemSelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String temp;
                        currLoadout.wep = currLoadout.charClass.weps.get(position);
                        currLoadout.wepId = position;

                        currLoadout.updateStats();
                        temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                        att.setText(temp);
                        temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                        dex.setText(temp);

                        itemSelView.setVisibility(View.GONE);
                        fade.setVisibility(View.GONE);
                        header.setVisibility(View.GONE);
                        weapon.setImageResource(currLoadout.wep.imageId);
                    }
                });
            }
        });

        // Ability
        ability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemListOffset = currLoadout.abilId;
                itemAdpt = new ItemAdapter(getApplicationContext(), currLoadout.charClass.abils);
                itemAdpt.enactCategories(selectedCategories);
                itemSelView.setAdapter(itemAdpt);
                itemSelView.setX(-1000f);
                itemSelView.setVisibility(View.VISIBLE);
                itemSelView.animate().translationXBy(1000f).setDuration(200);
                header.setX(-1000f);
                header.setVisibility(View.VISIBLE);
                header.animate().translationXBy(1000f).setDuration(200);
                fade.setVisibility(View.VISIBLE);
                itemSelView.animate().translationXBy(1000f).setDuration(200);
                itemSelView.setSelectionFromTop(itemListOffset, 0);

                itemSelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String temp;
                        currLoadout.abil = currLoadout.charClass.abils.get(position);
                        currLoadout.abilId = position;

                        currLoadout.updateStats();
                        temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                        att.setText(temp);
                        temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                        dex.setText(temp);

                        itemSelView.setVisibility(View.GONE);
                        fade.setVisibility(View.GONE);
                        header.setVisibility(View.GONE);
                        ability.setImageResource(currLoadout.abil.imageId);
                    }
                });
            }
        });

        // Armor
        armor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemListOffset = currLoadout.armId;
                itemAdpt = new ItemAdapter(getApplicationContext(), currLoadout.charClass.arms);
                itemAdpt.enactCategories(selectedCategories);
                itemSelView.setAdapter(itemAdpt);
                itemSelView.setX(-1000f);
                itemSelView.setVisibility(View.VISIBLE);
                itemSelView.animate().translationXBy(1000f).setDuration(200);
                header.setX(-1000f);
                header.setVisibility(View.VISIBLE);
                header.animate().translationXBy(1000f).setDuration(200);
                fade.setVisibility(View.VISIBLE);
                itemSelView.animate().translationXBy(1000f).setDuration(200);
                itemSelView.setSelectionFromTop(itemListOffset, 0);

                itemSelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String temp;
                        currLoadout.arm = currLoadout.charClass.arms.get(position);
                        currLoadout.armId = position;

                        currLoadout.updateStats();
                        temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                        att.setText(temp);
                        temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                        dex.setText(temp);

                        itemSelView.setVisibility(View.GONE);
                        fade.setVisibility(View.GONE);
                        header.setVisibility(View.GONE);
                        armor.setImageResource(currLoadout.arm.imageId);
                    }
                });
            }
        });

        // Ring
        ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemListOffset = currLoadout.ringId;
                itemAdpt = new ItemAdapter(getApplicationContext(), currLoadout.charClass.rings);
                itemAdpt.enactCategories(selectedCategories);
                itemSelView.setAdapter(itemAdpt);
                itemSelView.setX(-1000f);
                itemSelView.setVisibility(View.VISIBLE);
                itemSelView.animate().translationXBy(1000f).setDuration(200);
                header.setX(-1000f);
                header.setVisibility(View.VISIBLE);
                header.animate().translationXBy(1000f).setDuration(200);
                fade.setVisibility(View.VISIBLE);
                itemSelView.animate().translationXBy(1000f).setDuration(200);
                itemSelView.setSelectionFromTop(itemListOffset, 0);

                itemSelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String temp;
                        currLoadout.ring = currLoadout.charClass.rings.get(position);
                        currLoadout.ringId = position;

                        currLoadout.updateStats();
                        temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                        att.setText(temp);
                        temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                        dex.setText(temp);

                        itemSelView.setVisibility(View.GONE);
                        fade.setVisibility(View.GONE);
                        header.setVisibility(View.GONE);
                        ring.setImageResource(currLoadout.ring.imageId);
                    }
                });
            }
        });

        // Attack stat
        att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] baseStatRange = new String[currLoadout.charClass.baseAtt + 1];

                for(int i = 0; i < baseStatRange.length; i++) {
                    baseStatRange[i] = Integer.toString(i);
                }

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Change Base Attack");
                mBuilder.setItems(baseStatRange, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currLoadout.baseAtt = which;

                        currLoadout.updateStats();
                        String temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                        att.setText(temp);

                        if(currLoadout.baseAtt < currLoadout.charClass.baseAtt) {
                            att.setTextColor(getResources().getColor(R.color.colorUnmaxedText));
                        } else {
                            att.setTextColor(getResources().getColor(R.color.colorMaxedText));
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        // Dexterity stat
        dex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] baseStatRange = new String[currLoadout.charClass.baseDex + 1];

                for(int i = 0; i < baseStatRange.length; i++) {
                    baseStatRange[i] = Integer.toString(i);
                }

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Change Base Dexterity");
                mBuilder.setItems(baseStatRange, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currLoadout.baseDex = which;

                        currLoadout.updateStats();
                        String temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                        dex.setText(temp);

                        if(currLoadout.baseDex < currLoadout.charClass.baseDex) {
                            dex.setTextColor(getResources().getColor(R.color.colorUnmaxedText));
                        } else {
                            dex.setTextColor(getResources().getColor(R.color.colorMaxedText));
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        // Delete
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Confirm Delete");
                mBuilder.setMessage("Are you sure you want to delete this loadout?");
                mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadAdpt.remove(currLoadout);
                        loadAdpt.notifyDataSetChanged();
                    }
                });
                mBuilder.setNegativeButton("Cancel", null);

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        // Status effects
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageView d = status.findViewById(R.id.damaging);
                final ImageView b = status.findViewById(R.id.berserk);
                final ImageView c = status.findViewById(R.id.curse);
                final ImageView z = status.findViewById(R.id.dazed);
                final ImageView w = status.findViewById(R.id.weak);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Status Effects");
                mBuilder.setMultiChoiceItems(statusEffectNames, currLoadout.activeEffects, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!currLoadout.activeEffects[0]) {
                            d.setColorFilter(Color.parseColor("#777777"));
                        } else {
                            d.setColorFilter(null);
                        }

                        if(!currLoadout.activeEffects[1]) {
                            b.setColorFilter(Color.parseColor("#777777"));
                        } else {
                            b.setColorFilter(null);
                        }

                        if(!currLoadout.activeEffects[2]) {
                            c.setColorFilter(Color.parseColor("#777777"));
                        } else {
                            c.setColorFilter(null);
                        }

                        if(!currLoadout.activeEffects[3]) {
                            z.setColorFilter(Color.parseColor("#777777"));
                        } else {
                            z.setColorFilter(null);
                        }

                        if(!currLoadout.activeEffects[4]) {
                            w.setColorFilter(Color.parseColor("#777777"));
                        } else {
                            w.setColorFilter(null);
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        // Stores the references to the elements in each listView listing inside the corresponding loadout object
        currLoadout.setViews(charClass, weapon, ability, armor, ring, att, dex, status, del);
    }



    private void setupCategorySelectors() {
        CheckBox checkUt = findViewById(R.id.check_ut);
        CheckBox checkSt = findViewById(R.id.check_st);
        CheckBox checkT = findViewById(R.id.check_t);
        CheckBox checkWeak = findViewById(R.id.check_weak);
        CheckBox checkReskin = findViewById(R.id.check_reskin);

        checkUt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    selectedCategories.add("untiered");
                }
                else {
                    selectedCategories.remove("untiered");
                }
                itemAdpt.enactCategories(selectedCategories);
            }
        });

        checkSt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    selectedCategories.add("set_tiered");
                }
                else {
                    selectedCategories.remove("set_tiered");
                }
                itemAdpt.enactCategories(selectedCategories);
            }
        });

        checkT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    selectedCategories.add("tiered");
                }
                else {
                    selectedCategories.remove("tiered");
                }
                itemAdpt.enactCategories(selectedCategories);
            }
        });

        checkWeak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    selectedCategories.add("weak");
                }
                else {
                    selectedCategories.remove("weak");
                }
                itemAdpt.enactCategories(selectedCategories);
            }
        });

        checkReskin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    selectedCategories.add("reskin");
                }
                else {
                    selectedCategories.remove("reskin");
                }
                itemAdpt.enactCategories(selectedCategories);
            }
        });
    }

    // Adds a new empty loadout to the loadouts listView
    public void addLoadout(View view) {
        Random ran1 = new Random();
        int temp = ran1.nextInt(16);

        if(loadouts.size() < 8) {
            loadouts.add(new Loadout(getApplicationContext(), classes.get(temp), temp,
                    classes.get(temp).baseAtt,
                    classes.get(temp).baseDex,
                    classes.get(temp).weps.get(0), 0,
                    classes.get(temp).abils.get(0), 0,
                    classes.get(temp).arms.get(0), 0,
                    items.get(16).get(0), 0, "00000",
                    loadouts.size()));

            loadAdpt = new LoadoutAdapter(this, loadouts, this);
            loadoutView.setAdapter(loadAdpt);
        }
        else {
            Toast.makeText(this, "You can only have 8 sets at a time.", Toast.LENGTH_LONG).show();
        }

    }

    // Generates an ascending order list of each current loadout's damage per second for each level of defense up to the maximum
    public void generateDpsTable() {
        ArrayList<ArrayList<DpsEntry>> dpsTables = new ArrayList<>();   // loadouts.size() arrays of 150
        ArrayList<ArrayList<DpsEntry>> tempTable;
        DpsEntry temp;

        for(int i = 0; i < loadouts.size(); i++) {                      // Load the unsorted data
            dpsTables.add(loadouts.get(i).generateDps());
        }

        for(int i = 0; i <= 150; i++) {                                 // Insert sort on the data
            for(int j = 1; j < loadouts.size(); j++) {
                temp = dpsTables.get(j).get(i);
                int k = j;
                while(k > 0 && dpsTables.get(k - 1).get(i).dps > temp.dps) {
                    dpsTables.get(k).set(i, dpsTables.get(k - 1).get(i));
                    k--;
                }
                dpsTables.get(k).set(i, temp);
            }
        }

        tempTable = dpsTables;
        dpsTables = new ArrayList<>();

        for(int i = 0; i <= 150; i++) {                                 //Inverts the array to 150 arrays of loadouts.size() since the adapter needs it that way
            dpsTables.add(new ArrayList<DpsEntry>());
            for(int j = loadouts.size() - 1; j >= 0; j--) {
                dpsTables.get(i).add(tempTable.get(j).get(i));
            }
        }

        dpsTableAdpt = new DpsAdapter(this, dpsTables);
        dpsTableView.setAdapter(dpsTableAdpt);
    }

    private void saveBuilds() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile, false));
        StringBuilder saveStr = new StringBuilder();

        for(Loadout i : loadouts) {
            saveStr.append(i.classId);
            saveStr.append("/");
            saveStr.append(i.wepId);
            saveStr.append("/");
            saveStr.append(i.abilId);
            saveStr.append("/");
            saveStr.append(i.armId);
            saveStr.append("/");
            saveStr.append(i.ringId);
            saveStr.append("/");
            for(int j = 0; j < i.activeEffects.length; j++) {
                if(i.activeEffects[j]) {
                    saveStr.append("1");
                }
                else {
                    saveStr.append("0");
                }
            }
            saveStr.append("/\n");
        }
        writer.write(saveStr.toString());
        writer.close();
    }

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

                    loadouts.add(new Loadout(getApplicationContext(), classes.get(temps[0]), temps[0],
                            classes.get(temps[0]).baseAtt, classes.get(temps[0]).baseDex,
                            classes.get(temps[0]).weps.get(temps[1]), temps[1],
                            classes.get(temps[0]).abils.get(temps[2]), temps[2],
                            classes.get(temps[0]).arms.get(temps[3]), temps[3],
                            classes.get(temps[0]).rings.get(temps[4]), temps[4],
                            lineData.get(5).toString(), loadouts.size()));
                }
            }
        }

        loadAdpt = new LoadoutAdapter(this, loadouts, this);
        loadoutView.setAdapter(loadAdpt);

        loadReader.close();
    }

    private void readData(JSONObject[] data) {
        Log.i("Notification", "Reading data");

        String[] fileNames = { "items.json", "classes.json" };
        try {
            for(int i = 0; i < 2; i++) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(fileNames[i])));
                StringBuilder read = new StringBuilder();
                while(reader.ready()) {
                    read.append(reader.readLine());
                }
                Log.i("JSON data", read.toString());
                data[i] = new JSONObject(read.toString());
            }
            parseData(data);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseData(JSONObject[] data) throws JSONException {
        int classArraySize = data[1].getJSONArray("classes").length();
        int itemOutsideSize = data[0].getJSONArray("item").length();
        classes = new ArrayList<>();  // Create arraylist for character class objects
        items = new ArrayList<>();     // Create arraylist for sub-types of items

        for (int i = 0; i < itemOutsideSize; i++) {
            int itemInsideSize = data[0].getJSONArray("item").getJSONArray(i).length();
            items.add(i, new ArrayList<Item>());

            for(int j = 0; j < itemInsideSize; j++) {
                JSONObject currentItem = data[0].getJSONArray("item").getJSONArray(i).getJSONObject(j);
                items.get(i).add(j, new Item(
                        currentItem.getString("name"),
                        getResources().getIdentifier(currentItem.getString("image"), "drawable", getPackageName()),
                        currentItem.getInt("att"),
                        currentItem.getInt("dex"),
                        currentItem.getString("categories"),
                        currentItem.getInt("shot_dmg"),
                        currentItem.getInt("no_shots"),
                        currentItem.getInt("rate_of_fire"),
                        currentItem.getInt("range"),
                        currentItem.getInt("ap"))
                );
            }
        }

        for (int i = 0; i < classArraySize; i++) {
            JSONObject currentClass = data[1].getJSONArray("classes").getJSONObject(i);
            classes.add(i, new CharClass(
                    currentClass.getString("name"),
                    items.get(currentClass.getInt("weapon")),
                    items.get(currentClass.getInt("ability")),
                    items.get(currentClass.getInt("armor")),
                    items.get(16),  // Rings
                    getResources().getIdentifier(currentClass.getString("name"), "drawable", getPackageName()), // Turn image name into id
                    currentClass.getInt("att"),
                    currentClass.getInt("dex"))
            );
        }
    }
}

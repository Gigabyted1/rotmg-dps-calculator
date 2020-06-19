package com.waynebloom.rotmgdpscalculator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ClickListeners {
    ArrayList<Loadout> loadouts = new ArrayList<>(8);
    ArrayList<CharClass> classes = new ArrayList<>();
    ArrayList<Item> daggers = new ArrayList<>();
    ArrayList<Item> bows = new ArrayList<>();
    ArrayList<Item> staves = new ArrayList<>();
    ArrayList<Item> wands = new ArrayList<>();
    ArrayList<Item> swords = new ArrayList<>();
    ArrayList<Item> katanas = new ArrayList<>();
    ArrayList<Item> cloaks = new ArrayList<>();
    ArrayList<Item> quivers = new ArrayList<>();
    ArrayList<Item> spells = new ArrayList<>();
    ArrayList<Item> tomes = new ArrayList<>();
    ArrayList<Item> helms = new ArrayList<>();
    ArrayList<Item> shields = new ArrayList<>();
    ArrayList<Item> seals = new ArrayList<>();
    ArrayList<Item> poisons = new ArrayList<>();
    ArrayList<Item> skulls = new ArrayList<>();
    ArrayList<Item> traps = new ArrayList<>();
    ArrayList<Item> orbs = new ArrayList<>();
    ArrayList<Item> prisms = new ArrayList<>();
    ArrayList<Item> scepters = new ArrayList<>();
    ArrayList<Item> stars = new ArrayList<>();
    ArrayList<Item> wakis = new ArrayList<>();
    ArrayList<Item> robes = new ArrayList<>();
    ArrayList<Item> larms = new ArrayList<>();
    ArrayList<Item> harms = new ArrayList<>();
    ArrayList<Item> rings = new ArrayList<>();
    DpsAdapter dpsTableAdpt;
    LoadoutAdapter loadAdpt;
    boolean onLoadouts = true;
    File saveFile;

    Toolbar toolbar;
    ListView dpsTableView;
    ListView itemSelView;
    ListView loadoutView;
    ConstraintLayout statEditView;
    ConstraintLayout descView;
    SeekBar statSeekView;
    TextView titleView;
    TextView baseStatView;
    TextView wepStatView;
    TextView abilStatView;
    TextView armStatView;
    TextView ringStatView;
    Button statConfirm;
    Button addBuild;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveFile = new File(getApplicationContext().getFilesDir(), "loadouts.txt");

        toolbar = findViewById(R.id.my_toolbar);
        dpsTableView = findViewById(R.id.dps_table_view);
        itemSelView = findViewById(R.id.item_selection_view);
        loadoutView = findViewById(R.id.loadout_view);
        statEditView = findViewById(R.id.stat_edit);
        descView = findViewById(R.id.desc_view);
        statSeekView = findViewById(R.id.stat_seekbar);
        titleView = findViewById(R.id.title);
        baseStatView = findViewById(R.id.base_stat);
        wepStatView = findViewById(R.id.wep_stat);
        abilStatView = findViewById(R.id.abil_stat);
        armStatView = findViewById(R.id.arm_stat);
        ringStatView = findViewById(R.id.ring_stat);
        statConfirm = findViewById(R.id.confirm);
        addBuild = findViewById(R.id.add_button);

        readData(daggers, "daggers.txt");
        readData(bows, "bows.txt");
        readData(staves, "staves.txt");
        readData(wands, "wands.txt");
        readData(swords, "swords.txt");
        readData(katanas, "katanas.txt");
        readData(cloaks, "cloaks.txt");
        readData(quivers, "quivers.txt");
        readData(spells, "spells.txt");
        readData(tomes, "tomes.txt");
        readData(helms, "helms.txt");
        readData(shields, "shields.txt");
        readData(seals, "seals.txt");
        readData(poisons, "poisons.txt");
        readData(skulls, "skulls.txt");
        readData(traps, "traps.txt");
        readData(orbs, "orbs.txt");
        readData(prisms, "prisms.txt");
        readData(scepters, "scepters.txt");
        readData(stars, "stars.txt");
        readData(wakis, "wakis.txt");
        readData(larms, "larms.txt");
        readData(harms, "harms.txt");
        readData(robes, "robes.txt");
        readData(rings, "rings.txt");

        classes.add(new CharClass("Rogue", daggers, cloaks, larms, rings, R.drawable.rogue, 50, 75));
        classes.add(new CharClass("Archer", bows, quivers, larms,rings,  R.drawable.archer,  75, 50));
        classes.add(new CharClass("Wizard", staves, spells, robes, rings, R.drawable.wizard, 75, 75));
        classes.add(new CharClass("Priest", wands, tomes, robes, rings, R.drawable.priest, 50, 55));
        classes.add(new CharClass("Warrior", swords, helms, harms, rings, R.drawable.warrior, 75, 75));
        classes.add(new CharClass("Knight", swords, shields, harms, rings, R.drawable.knight, 50, 50));
        classes.add(new CharClass("Paladin", swords, seals, harms, rings, R.drawable.paladin, 50, 45));
        classes.add(new CharClass("Assassin", daggers, poisons, larms, rings, R.drawable.assassin, 50, 75));
        classes.add(new CharClass("Necromancer", staves, skulls, robes, rings, R.drawable.necromancer, 75, 60));
        classes.add(new CharClass("Huntress", bows, traps, larms, rings, R.drawable.huntress, 75, 50));
        classes.add(new CharClass("Mystic", staves, orbs, robes, rings, R.drawable.mystic, 60, 55));
        classes.add(new CharClass("Trickster", daggers, prisms, larms, rings, R.drawable.trickster, 65, 75));
        classes.add(new CharClass("Sorcerer", wands, scepters, robes, rings, R.drawable.sorcerer, 70, 60));
        classes.add(new CharClass("Ninja", katanas, stars, larms, rings, R.drawable.ninja, 70, 70));
        classes.add(new CharClass("Samurai", katanas, wakis, harms, rings, R.drawable.samurai, 75, 50));

        try {
            loadBuilds();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSupportActionBar(toolbar);
        setTitle("Builds");
        toolbar.setTitleTextAppearance(this, R.style.MyFontAppearance);
    }

    @Override
    public void onAttClicked (final TextView attView, final Loadout currLoadout) {
        attView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp;
                final int bonusAtt = currLoadout.totalAtt - currLoadout.baseAtt;

                statEditView.setVisibility(View.VISIBLE);
                statSeekView.setMax(currLoadout.charClass.baseAtt);
                statSeekView.setProgress(currLoadout.baseAtt);
                titleView.setText("Attack");
                temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                baseStatView.setText(temp);
                temp = "+" + currLoadout.wep.addedAtt;
                wepStatView.setText(temp);
                temp = "+" + currLoadout.abil.addedAtt;
                abilStatView.setText(temp);
                temp = "+" + currLoadout.arm.addedAtt;
                armStatView.setText(temp);
                temp = "+" + currLoadout.ring.addedAtt;
                ringStatView.setText(temp);

                statSeekView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    String temp;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        int tempAtt = progress + bonusAtt;
                        temp = progress + "(" + tempAtt + ")";
                        baseStatView.setText(temp);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                statConfirm.setOnClickListener(new View.OnClickListener() {
                    String temp;

                    @Override
                    public void onClick(View v) {
                        currLoadout.baseAtt = statSeekView.getProgress();
                        statEditView.setVisibility(View.GONE);
                        currLoadout.updateStats();
                        temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                        attView.setText(temp);
                    }
                });
            }
        });
    }

    @Override
    public void onDexClicked (final TextView dexView, final Loadout currLoadout) {
        dexView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp;
                final int bonusDex = currLoadout.totalDex - currLoadout.baseDex;

                statEditView.setVisibility(View.VISIBLE);
                statSeekView.setMax(currLoadout.charClass.baseDex);
                statSeekView.setProgress(currLoadout.baseDex);
                titleView.setText("Dexterity");
                temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                baseStatView.setText(temp);
                temp = "+" + currLoadout.wep.addedDex;
                wepStatView.setText(temp);
                temp = "+" + currLoadout.abil.addedDex;
                abilStatView.setText(temp);
                temp = "+" + currLoadout.arm.addedDex;
                armStatView.setText(temp);
                temp = "+" + currLoadout.ring.addedDex;
                ringStatView.setText(temp);

                statSeekView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    String temp;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        int tempDex = progress + bonusDex;
                        temp = progress + "(" + tempDex + ")";
                        baseStatView.setText(temp);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                statConfirm.setOnClickListener(new View.OnClickListener() {
                    String temp;

                    @Override
                    public void onClick(View v) {
                        currLoadout.baseDex = statSeekView.getProgress();
                        statEditView.setVisibility(View.VISIBLE);
                        currLoadout.updateStats();
                        temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                        dexView.setText(temp);
                    }
                });
            }
        });
    }

    @Override
    public void onDelClicked(Button view, final Loadout currLoadout) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAdpt.remove(currLoadout);
                loadAdpt.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStatusClicked(ConstraintLayout view, final Loadout currLoadout) {
        final ImageView damagingView = view.findViewById(R.id.damaging);
        final ImageView berserkView = view.findViewById(R.id.berserk);
        final ImageView curseView = view.findViewById(R.id.curse);
        final ImageView dazedView = view.findViewById(R.id.dazed);
        final ImageView weakView = view.findViewById(R.id.weak);

        if(!currLoadout.checkedItems[0]) {
            damagingView.setColorFilter(Color.parseColor("#777777"));
        }
        else {
            damagingView.setColorFilter(null);
        }

        if(!currLoadout.checkedItems[1]) {
            berserkView.setColorFilter(Color.parseColor("#777777"));
        }
        else {
            berserkView.setColorFilter(null);
        }

        if(!currLoadout.checkedItems[2]) {
            curseView.setColorFilter(Color.parseColor("#777777"));
        }
        else {
            curseView.setColorFilter(null);
        }

        if(!currLoadout.checkedItems[3]) {
            dazedView.setColorFilter(Color.parseColor("#777777"));
        }
        else {
            dazedView.setColorFilter(null);
        }

        if(!currLoadout.checkedItems[4]) {
            weakView.setColorFilter(Color.parseColor("#777777"));
        }
        else {
            weakView.setColorFilter(null);
        }

        view.setOnClickListener(new View.OnClickListener() {

            boolean[] temp = currLoadout.checkedItems;

            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Status Effects");
                mBuilder.setMultiChoiceItems(currLoadout.statusEffects, currLoadout.checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //currLoadout.checkedItems = temp;

                        if(!currLoadout.checkedItems[0]) {
                            damagingView.setColorFilter(Color.parseColor("#777777"));
                        }
                        else {
                            damagingView.setColorFilter(null);
                        }

                        if(!currLoadout.checkedItems[1]) {
                            berserkView.setColorFilter(Color.parseColor("#777777"));
                        }
                        else {
                            berserkView.setColorFilter(null);
                        }

                        if(!currLoadout.checkedItems[2]) {
                            curseView.setColorFilter(Color.parseColor("#777777"));
                        }
                        else {
                            curseView.setColorFilter(null);
                        }

                        if(!currLoadout.checkedItems[3]) {
                            dazedView.setColorFilter(Color.parseColor("#777777"));
                        }
                        else {
                            dazedView.setColorFilter(null);
                        }

                        if(!currLoadout.checkedItems[4]) {
                            weakView.setColorFilter(Color.parseColor("#777777"));
                        }
                        else {
                            weakView.setColorFilter(null);
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!onLoadouts) {
            loadoutView.setVisibility(View.VISIBLE);
            dpsTableView.setVisibility(View.GONE);
            addBuild.setVisibility(View.VISIBLE);
            descView.setVisibility(View.GONE);
            setTitle("Loadouts");
            onLoadouts = true;
        }
        else {

        }
    }

    protected void onStop () {
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_switch:
                if(onLoadouts) {
                    loadoutView.setVisibility(View.GONE);
                    dpsTableView.setVisibility(View.VISIBLE);
                    addBuild.setVisibility(View.GONE);
                    descView.setVisibility(View.VISIBLE);
                    setTitle("DPS Table");
                    generateDpsTable();
                    onLoadouts = false;
                }
                else {
                    loadoutView.setVisibility(View.VISIBLE);
                    dpsTableView.setVisibility(View.GONE);
                    addBuild.setVisibility(View.VISIBLE);
                    descView.setVisibility(View.GONE);
                    setTitle("Loadouts");
                    onLoadouts = true;
                }
                return true;

            case R.id.action_delete:


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addLoadout (View view) {
        Random ran1 = new Random();
        int temp = ran1.nextInt(15);
        String[] seTemp = getResources().getStringArray(R.array.stat_effects);

        if(loadouts.size() < 8) {
            loadouts.add(new Loadout(getApplicationContext(), classes.get(temp), temp,
                    classes.get(temp).baseAtt,
                    classes.get(temp).baseDex,
                    classes.get(temp).weps.get(0), 0,
                    classes.get(temp).abils.get(0), 0,
                    classes.get(temp).arms.get(0), 0,
                    rings.get(0), 0, "00000",
                    loadouts.size()));

            loadAdpt = new LoadoutAdapter(this, itemSelView, loadouts, classes,this);
            loadoutView.setAdapter(loadAdpt);
        }
        else {
            Toast.makeText(this, "You can only have 8 sets at a time.", Toast.LENGTH_LONG).show();
        }

    }

    public void generateDpsTable () {
        ArrayList<ArrayList<DpsEntry>> dpsTables = new ArrayList<>();   //loadouts.size() arrays of 150
        ArrayList<ArrayList<DpsEntry>> tempTable;
        DpsEntry temp;

        for(int i = 0; i < loadouts.size(); i++) {                      //Load the unsorted data
            dpsTables.add(loadouts.get(i).generateDps());
        }

        for(int i = 0; i <= 150; i++) {                                 //Insert sort on the data
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
            for(int j = 0; j < i.checkedItems.length; j++) {
                if(i.checkedItems[j]) {
                    saveStr.append("1");
                }
                else {
                    saveStr.append("0");
                }
            }
            saveStr.append("/\n");

            Log.i("Save debug", saveStr.toString());
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

        loadAdpt = new LoadoutAdapter(this, itemSelView, loadouts, classes, this);
        loadoutView.setAdapter(loadAdpt);

        loadReader.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void readData (ArrayList<Item> storage, String fileName) {
        final int NO_OF_INFO = 11;
        ArrayList<StringBuilder> lineData;
        char fileChar;
        int lineLoc;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getApplicationContext().getAssets().open(fileName), StandardCharsets.US_ASCII));

            while(reader.ready()) {
                lineData = new ArrayList<>(NO_OF_INFO);
                lineLoc = 0;
                for(int i = 0; i < NO_OF_INFO; i++) {
                    lineData.add(new StringBuilder());
                }
                while(reader.ready() && lineLoc < NO_OF_INFO) {
                    fileChar = (char)reader.read();
                    if(fileChar == '/') {
                        lineLoc++;
                    }
                    else {
                        lineData.get(lineLoc).append(fileChar);
                    }

                    if(lineLoc == NO_OF_INFO) {
                        reader.read();
                        storage.add(new Item(lineData.get(0).toString(), lineData.get(1).toString(), lineData.get(2).toString(),
                                getResources().getIdentifier(lineData.get(3).toString() , "drawable", getPackageName()),
                                Integer.parseInt(lineData.get(4).toString()), Integer.parseInt(lineData.get(5).toString()),
                                Double.parseDouble(lineData.get(6).toString()), Integer.parseInt(lineData.get(7).toString()),
                                Double.parseDouble(lineData.get(8).toString()), Double.parseDouble(lineData.get(9).toString()),
                                Integer.parseInt(lineData.get(10).toString())));
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

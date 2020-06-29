package com.waynebloom.rotmgdpscalculator;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
    ArrayList<Item> lutes = new ArrayList<>();
    ArrayList<Item> robes = new ArrayList<>();
    ArrayList<Item> larms = new ArrayList<>();
    ArrayList<Item> harms = new ArrayList<>();
    ArrayList<Item> rings = new ArrayList<>();
    String[] statusEffectNames;
    DpsAdapter dpsTableAdpt;
    LoadoutAdapter loadAdpt;
    boolean onLoadouts = true;
    File saveFile;

    Toolbar toolbar;
    ListView dpsTableView;
    ListView itemSelView;
    ListView loadoutView;
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
    AdView mAdView;

    ConsentForm form;
    AdRequest request;
    Bundle extras;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConsentInformation consentInformation = ConsentInformation.getInstance(MainActivity.this);
        ConsentInformation.getInstance(MainActivity.this).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);

        String[] publisherIds = {"pub-1507290914426110"};

        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
                if(ConsentInformation.getInstance(MainActivity.this).isRequestLocationInEeaOrUnknown()) {
                    if(consentStatus == ConsentStatus.PERSONALIZED) {
                        request = new AdRequest.Builder().build();
                        MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
                            @Override
                            public void onInitializationComplete(InitializationStatus initializationStatus) {
                            }
                        });
                        mAdView.loadAd(request);
                    } else
                    if(consentStatus == ConsentStatus.NON_PERSONALIZED) {
                        extras.putString("npa", "1");
                        request = new AdRequest.Builder()
                                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                                .build();
                        MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
                            @Override
                            public void onInitializationComplete(InitializationStatus initializationStatus) {
                            }
                        });
                        mAdView.loadAd(request);
                    } else {
                        URL privacyUrl = null;
                        try {
                            // TODO: Replace with your app's privacy policy URL.
                            privacyUrl = new URL("https://www.google.com/");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            // Handle error.
                        }
                        form = new ConsentForm.Builder(MainActivity.this, privacyUrl)
                                .withListener(new ConsentFormListener() {
                                    @Override
                                    public void onConsentFormLoaded() {
                                        // Consent form loaded successfully.
                                        form.show();
                                    }

                                    @Override
                                    public void onConsentFormOpened() {
                                        // Consent form was displayed.
                                    }

                                    @Override
                                    public void onConsentFormClosed(
                                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                                        extras = new Bundle();

                                        if (consentStatus == ConsentStatus.NON_PERSONALIZED) {
                                            extras.putString("npa", "1");
                                            MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
                                                @Override
                                                public void onInitializationComplete(InitializationStatus initializationStatus) {
                                                }
                                            });
                                            request = new AdRequest.Builder()
                                                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                                                    .build();
                                            mAdView.loadAd(request);
                                        } else if (consentStatus == ConsentStatus.PERSONALIZED) {
                                            MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
                                                @Override
                                                public void onInitializationComplete(InitializationStatus initializationStatus) {
                                                }
                                            });
                                            request = new AdRequest.Builder().build();
                                            mAdView.loadAd(request);
                                        } else {
                                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                                            mBuilder.setMessage("Sorry! An ad-free version is an upcoming feature, but for now is unavailable. " +
                                                    "Without consent for ads, we can't display them to you. Ad revenue is how we are able to " +
                                                    "provide this app free of charge.");
                                            mBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            });

                                            AlertDialog mDialog = mBuilder.create();
                                            mDialog.show();
                                        }
                                    }

                                    @Override
                                    public void onConsentFormError(String errorDescription) {
                                        // Consent form error.
                                    }
                                })
                                .withPersonalizedAdsOption()
                                .withNonPersonalizedAdsOption()
                                .withAdFreeOption()
                                .build();

                        form.load();
                    }
                } else {
                    MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
                        @Override
                        public void onInitializationComplete(InitializationStatus initializationStatus) {
                        }
                    });
                    request = new AdRequest.Builder().build();
                    mAdView.loadAd(request);
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Consent status update failed.");
                mBuilder.setMessage("\"" + errorDescription + "\"" + "\n\nPlease ensure you are connected to the internet or a mobile network and try again.");
                mBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        AppCenter.start(getApplication(), "5b1fb4f9-62fe-4eb6-a98e-581fb668b5f8",
                Analytics.class, Crashes.class);

        saveFile = new File(getApplicationContext().getFilesDir(), "loadouts.txt");
        statusEffectNames = getResources().getStringArray(R.array.stat_effects);

        toolbar = findViewById(R.id.my_toolbar);
        dpsTableView = findViewById(R.id.dps_table_view);
        itemSelView = findViewById(R.id.item_selection_view);
        loadoutView = findViewById(R.id.loadout_view);
            View footer = new View(MainActivity.this);
            footer.setLayoutParams( new AbsListView.LayoutParams( Toolbar.LayoutParams.FILL_PARENT, 250 ));
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
        mAdView = findViewById(R.id.adView);

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
        readData(lutes, "lutes.txt");
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
        classes.add(new CharClass("Bard", bows, lutes, robes, rings, R.drawable.bard, 55, 70));

        try {
            loadBuilds();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSupportActionBar(toolbar);
        setTitle("Builds");
        toolbar.setTitleTextAppearance(this, R.style.MyFontAppearance);
    }

    public void setupLoadoutViews(final ImageView c, final ImageView w, final ImageView ab, final ImageView ar, final ImageView r, final TextView at, final TextView dex, final ConstraintLayout s, final Button del, final int p) {
        final Loadout currLoadout = loadouts.get(p);

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassAdapter classSelAdpt = new ClassAdapter(getApplicationContext(), classes);
                itemSelView.setAdapter(classSelAdpt);
                itemSelView.setX(-1000f);
                itemSelView.setVisibility(View.VISIBLE);
                itemSelView.animate().translationXBy(1000f).setDuration(200);

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
                        at.setText(temp);
                        temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                        dex.setText(temp);

                        if(!currLoadout.wep.subType.equals(currLoadout.charClass.weps.get(0).subType)) {
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
                        }

                        itemSelView.setVisibility(View.GONE);
                        c.setImageResource(currLoadout.charClass.imageId);
                    }
                });
            }
        });

        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemListOffset = currLoadout.wepId;
                ItemAdapter itemSelAdpt = new ItemAdapter(getApplicationContext(), currLoadout.charClass.weps);
                itemSelView.setAdapter(itemSelAdpt);
                itemSelView.setX(-1000f);
                itemSelView.setVisibility(View.VISIBLE);
                itemSelView.animate().translationXBy(1000f).setDuration(200);
                itemSelView.setSelectionFromTop(itemListOffset, 0);

                itemSelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String temp;
                        currLoadout.wep = currLoadout.charClass.weps.get(position);
                        currLoadout.wepId = position;

                        currLoadout.updateStats();
                        temp = currLoadout.baseAtt + "(" + currLoadout.totalAtt + ")";
                        at.setText(temp);
                        temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                        dex.setText(temp);

                        itemSelView.setVisibility(View.GONE);
                        w.setImageResource(currLoadout.wep.imageId);
                    }
                });
            }
        });

        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemListOffset = currLoadout.abilId;
                ItemAdapter itemSelAdpt = new ItemAdapter(getApplicationContext(), currLoadout.charClass.abils);
                itemSelView.setAdapter(itemSelAdpt);
                itemSelView.setX(-1000f);
                itemSelView.setVisibility(View.VISIBLE);
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
                        at.setText(temp);
                        temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                        dex.setText(temp);

                        itemSelView.setVisibility(View.GONE);
                        ab.setImageResource(currLoadout.abil.imageId);
                    }
                });
            }
        });

        ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemListOffset = currLoadout.armId;
                ItemAdapter itemSelAdpt = new ItemAdapter(getApplicationContext(), currLoadout.charClass.arms);
                itemSelView.setAdapter(itemSelAdpt);
                itemSelView.setX(-1000f);
                itemSelView.setVisibility(View.VISIBLE);
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
                        at.setText(temp);
                        temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                        dex.setText(temp);

                        itemSelView.setVisibility(View.GONE);
                        ar.setImageResource(currLoadout.arm.imageId);
                    }
                });
            }
        });

        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemListOffset = currLoadout.ringId;
                ItemAdapter itemSelAdpt = new ItemAdapter(getApplicationContext(), currLoadout.charClass.rings);
                itemSelView.setAdapter(itemSelAdpt);
                itemSelView.setX(-1000f);
                itemSelView.setVisibility(View.VISIBLE);
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
                        at.setText(temp);
                        temp = currLoadout.baseDex + "(" + currLoadout.totalDex + ")";
                        dex.setText(temp);

                        itemSelView.setVisibility(View.GONE);
                        r.setImageResource(currLoadout.ring.imageId);
                    }
                });
            }
        });

        at.setOnClickListener(new View.OnClickListener() {
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
                        at.setText(temp);

                        if(currLoadout.baseAtt < currLoadout.charClass.baseAtt) {
                            at.setTextColor(getResources().getColor(R.color.colorUnmaxedText));
                        } else {
                            at.setTextColor(getResources().getColor(R.color.colorMaxedText));
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

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

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageView d = s.findViewById(R.id.damaging);
                final ImageView b = s.findViewById(R.id.berserk);
                final ImageView c = s.findViewById(R.id.curse);
                final ImageView z = s.findViewById(R.id.dazed);
                final ImageView w = s.findViewById(R.id.weak);

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

        currLoadout.setViews(c, w, ab, ar, r, at, dex, s, del);
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

            case R.id.action_delete:


            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
                    rings.get(0), 0, "00000",
                    loadouts.size()));

            loadAdpt = new LoadoutAdapter(this, loadouts, this);
            loadoutView.setAdapter(loadAdpt);
        }
        else {
            Toast.makeText(this, "You can only have 8 sets at a time.", Toast.LENGTH_LONG).show();
        }

    }

    public void generateDpsTable() {
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void readData(ArrayList<Item> storage, String fileName) {
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

package com.waynebloom.rotmgdpscalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Loadout {

    // misc
    public static List<CharClass> classData;
    private final LoadoutFragment mFragment;
    private final Context mContext;
    private int loadoutId;
    private ItemAdapter itemAdpt;
    static final int WEAPON = 0;
    static final int ABILITY = 1;
    static final int ARMOR = 2;
    static final int RING = 3;
    static final int ATT = 4;
    static final int DEX = 5;

    // constant views
    private final RecyclerView selectorView;
    private final View filterView;
        private final CheckBox checkUt;
        private final CheckBox checkSt;
        private final CheckBox checkT;
        private final CheckBox checkReskin;
        private final CheckBox checkWeak;
    private final View backgroundFade;

    // dps factors
    private CharClass charClass;
    private Item weapon;
    private Item ability;
    private Item armor;
    private Item ring;
    private StatBonus baseStats = new StatBonus(0, 0, 0, 0, 0, 0, 0, 0);
    private final StatBonus setBonus = new StatBonus(0, 0, 0, 0, 0, 0, 0, 0);
    private final StatBonus statTotals = new StatBonus(0,0,0,0,0,0,0,0);
    private boolean[] activeEffects;    //Tracks active status effects
    private static final ArrayList<String> selectedCategories = new ArrayList<>(Arrays.asList("untiered", "set_tiered", "tiered"));
    private List<DpsEntry> loadoutDps = new ArrayList<>();
    private boolean loadoutChanged;

    // view references
    private ImageView classView;
    private ImageView weaponView;
    private ImageView abilityView;
    private ImageView armorView;
    private ImageView ringView;
    private TextView attView;
    private TextView dexView;
    private ConstraintLayout statusView;
    private ImageView damagingView;
    private ImageView berserkView;
    private ImageView curseView;
    private ImageView dazedView;
    private ImageView weakView;
    private Button deleteButton;

    // Empty loadout
    Loadout(LoadoutFragment mFragment, int loadoutId) {
        View fragmentView = mFragment.getView();

        this.mFragment = mFragment;
        this.mContext = mFragment.getContext();
        selectorView = fragmentView.findViewById(R.id.item_selection_view);
        filterView = fragmentView.findViewById(R.id.filter);
            checkUt = filterView.findViewById(R.id.check_ut);
            checkSt = filterView.findViewById(R.id.check_st);
            checkT = filterView.findViewById(R.id.check_t);
            checkReskin = filterView.findViewById(R.id.check_reskin);
            checkWeak = filterView.findViewById(R.id.check_weak);
        backgroundFade = fragmentView.findViewById(R.id.fade);
        this.loadoutId = loadoutId;
        this.loadoutChanged = true;
    }

    // Loadout with Class included (for 'createNewLoadout' in MainActivity)
    Loadout(LoadoutFragment mFragment, int loadoutId, CharClass charClass) {
        View fragmentView = mFragment.getView();

        this.mFragment = mFragment;
        this.mContext = mFragment.getContext();
        this.selectorView = fragmentView.findViewById(R.id.item_selection_view);
        this.filterView = fragmentView.findViewById(R.id.filter);
            checkUt = filterView.findViewById(R.id.check_ut);
            checkSt = filterView.findViewById(R.id.check_st);
            checkT = filterView.findViewById(R.id.check_t);
            checkReskin = filterView.findViewById(R.id.check_reskin);
            checkWeak = filterView.findViewById(R.id.check_weak);
        this.backgroundFade = fragmentView.findViewById(R.id.fade);
        this.loadoutId = loadoutId;
        this.loadoutChanged = true;

        // load initial stats
        this.charClass = charClass;
        this.baseStats.addBonus(charClass.getMaxedStats());
        this.statTotals.addBonus(baseStats);
        this.weapon = charClass.getWeapons().get(0);
        this.ability = charClass.getAbilities().get(0);
        this.armor = charClass.getArmors().get(0);
        this.ring = charClass.getRings().get(0);
        setActiveEffects("00000");

        // update stat views
        updateAtt();
        updateDex();
    }

    // Loadout with all gear included (for 'loadBuilds' in MainActivity)
    Loadout(LoadoutFragment mFragment, int loadoutId, CharClass charClass, Item weapon, Item ability, Item armor, Item ring, String activeEffects) {
        View fragmentView = mFragment.getView();

        this.mFragment = (LoadoutFragment) mFragment;
        this.mContext = mFragment.getContext();
        this.selectorView = fragmentView.findViewById(R.id.item_selection_view);
        this.filterView = fragmentView.findViewById(R.id.filter);
            checkUt = filterView.findViewById(R.id.check_ut);
            checkSt = filterView.findViewById(R.id.check_st);
            checkT = filterView.findViewById(R.id.check_t);
            checkReskin = filterView.findViewById(R.id.check_reskin);
            checkWeak = filterView.findViewById(R.id.check_weak);
        this.backgroundFade = fragmentView.findViewById(R.id.fade);
        this.loadoutId = loadoutId;
        this.loadoutChanged = true;

        // load initial stats
        this.charClass = charClass;
        this.baseStats.addBonus(charClass.getMaxedStats());
        this.statTotals.addBonus(baseStats);
        this.weapon = weapon;
        this.statTotals.addBonus(weapon.getStatBonus());
        this.ability = ability;
        this.statTotals.addBonus(ability.getStatBonus());
        this.armor = armor;
        this.statTotals.addBonus(armor.getStatBonus());
        this.ring = ring;
        this.statTotals.addBonus(ring.getStatBonus());
        checkAndUpdateSetBonus();
        setActiveEffects(activeEffects);

        // update stat views
        updateAtt();
        updateDex();
    }

    // assign view references and then set all click listeners
    public void setViews(LoadoutAdapter caller, ImageView classView, ImageView weaponView, ImageView abilityView, ImageView armorView, ImageView ringView, TextView attView, TextView dexView, ConstraintLayout statusView, Button deleteView) {
        this.classView = classView;
        this.weaponView = weaponView;
        this.abilityView = abilityView;
        this.armorView = armorView;
        this.ringView = ringView;
        this.attView = attView;
        this.dexView = dexView;
        this.statusView = statusView;
        this.damagingView = this.statusView.findViewById(R.id.damaging);
        this.berserkView = this.statusView.findViewById(R.id.berserk);
        this.curseView = this.statusView.findViewById(R.id.curse);
        this.dazedView = this.statusView.findViewById(R.id.dazed);
        this.weakView = this.statusView.findViewById(R.id.weak);
        this.deleteButton = deleteView;

        setClassViewListener();
        setWeaponViewListener();
        setAbilityViewListener();
        setArmorViewListener();
        setRingViewListener();
        setAttViewListener();
        setDexViewListener();
        setStatusViewListener();
        setDeleteViewListener(caller);

        updateAllViews();
    }

    public void setClassViewListener() {
        classView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassAdapter adpt = new ClassAdapter(mContext, classData, Loadout.this);
                selectorView.setAdapter(adpt);
                selectorView.setLayoutManager(new LinearLayoutManager(mContext));
                makeSelectorViewVisible();
            }
        });
    }

    public void setWeaponViewListener() {
        weaponView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilterViewListeners();
                itemAdpt = new ItemAdapter(mContext, charClass.getWeapons(), Loadout.this, WEAPON);
                itemAdpt.enactCategories(selectedCategories);
                selectorView.setAdapter(itemAdpt);
                selectorView.setLayoutManager(new LinearLayoutManager(mContext));
                makeSelectorViewVisible();
            }
        });
    }

    public void setAbilityViewListener() {
        abilityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilterViewListeners();
                itemAdpt = new ItemAdapter(mContext, charClass.getAbilities(), Loadout.this, ABILITY);
                itemAdpt.enactCategories(selectedCategories);
                selectorView.setAdapter(itemAdpt);
                selectorView.setLayoutManager(new LinearLayoutManager(mContext));
                makeSelectorViewVisible();
            }
        });
    }

    public void setArmorViewListener() {
        armorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilterViewListeners();
                itemAdpt = new ItemAdapter(mContext, charClass.getArmors(), Loadout.this, ARMOR);
                itemAdpt.enactCategories(selectedCategories);
                selectorView.setAdapter(itemAdpt);
                selectorView.setLayoutManager(new LinearLayoutManager(mContext));
                makeSelectorViewVisible();
            }
        });
    }

    public void setRingViewListener() {
        ringView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilterViewListeners();
                itemAdpt = new ItemAdapter(mContext, charClass.getRings(), Loadout.this, RING);
                itemAdpt.enactCategories(selectedCategories);
                selectorView.setAdapter(itemAdpt);
                selectorView.setLayoutManager(new LinearLayoutManager(mContext));
                makeSelectorViewVisible();
            }
        });
    }

    public void setAttViewListener() {
        attView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load a string array with values between 0 and the current class's maximum att
                final int MIN_ATT = 0;
                final int MAX_ATT = charClass.getMaxedStats().getAttBonus();
                String[] baseStatRange = new String[MAX_ATT + 1];   // '+1' is to include 0
                for(int i = MIN_ATT; i <= MAX_ATT; i++) {
                    baseStatRange[i] = Integer.toString(i);
                }

                // Produce a dialog box for selection of new baseAtt value
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setTitle("Change Base Attack")
                        .setItems(baseStatRange, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        informSelected(which, ATT);
                    }
                })
                        .create()
                        .getListView().setSelectionFromTop(baseStats.getAttBonus(), 0);
                mBuilder.show();
            }
        });
    }

    public void setDexViewListener() {
        dexView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load a string array with values between 0 and the current class's maximum dex
                final int MIN_DEX = 0;
                final int MAX_DEX = charClass.getMaxedStats().getDexBonus();
                String[] baseStatRange = new String[MAX_DEX + 1];   // '+1' is to include 0
                for(int i = MIN_DEX; i <= MAX_DEX; i++) {
                    baseStatRange[i] = Integer.toString(i);
                }

                // Produce a dialog box for selection of new baseDex value
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setTitle("Change Base Dexterity")
                        .setItems(baseStatRange, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        informSelected(which, DEX);
                    }
                })
                        .create()
                        .getListView().setSelectionFromTop(baseStats.getDexBonus(), 0);
                mBuilder.show();
            }
        });
    }

    public void setStatusViewListener() {
        statusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve names of status effects
                String[] statusEffectNames = mContext.getResources().getStringArray(R.array.stat_effects);

                // Produce a dialog box for selection of status effects
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setTitle("Status Effects")
                        .setMultiChoiceItems(statusEffectNames, activeEffects, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                            }
                        })
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateStatus();
                                mFragment.saveLoadouts();
                                loadoutChanged = true;
                            }
                }).create().show();
            }
        });
    }

    public void setDeleteViewListener(final LoadoutAdapter caller) {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this loadout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        caller.removeAt(loadoutId);
                        mFragment.saveLoadouts();
                        mFragment.notifyLoadoutRemoved();
                    }
                })
                        .setNegativeButton("Cancel", null)
                        .create().show();
            }
        });
    }

    public void setFilterViewListeners() {

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

    // various necessary setters and getters
    public void setLoadoutId(int loadoutId) {
        this.loadoutId = loadoutId;
    }

    public void setActiveEffects(String activeEffects) {
        this.activeEffects = new boolean[5];
        for(int i = 0; i < activeEffects.length(); i++) {
            this.activeEffects[i] = activeEffects.charAt(i) != '0';
        }
    }

    public CharClass getCharClass() {
        return charClass;
    }

    public Item getWeapon() {
        return weapon;
    }

    public Item getAbility() {
        return ability;
    }

    public Item getArmor() {
        return armor;
    }

    public Item getRing() {
        return ring;
    }

    public boolean[] getActiveEffects() {
        return activeEffects;
    }

    // Called by ItemAdapter and ClassAdapter. Assigns the selected object to the corresponding load out variable
    public void informSelected(Item item, int type) {
        Item prevItem;
        switch(type) {
            case WEAPON:
                prevItem = weapon;
                weapon = item;
                updateWeapon(prevItem);
                break;
            case ABILITY:
                prevItem = ability;
                ability = item;
                updateAbility(prevItem);
                break;
            case ARMOR:
                prevItem = armor;
                armor = item;
                updateArmor(prevItem);
                break;
            case RING:
                prevItem = ring;
                ring = item;
                updateRing(prevItem);
                break;
        }
        mFragment.saveLoadouts();

        loadoutChanged = true;
    }

    public void informSelected(CharClass mClass) {
        CharClass prevClass = charClass;
        charClass = mClass;
        updateClass(mClass);
        if (charClass.getWeapons().get(0).getAbsItemId() != prevClass.getWeapons().get(0).getAbsItemId()) {
            Item prevWeapon = weapon;
            weapon = charClass.getWeapons().get(0);
            updateWeapon(prevWeapon);
        }
        if (charClass.getAbilities().get(0).getAbsItemId() != prevClass.getAbilities().get(0).getAbsItemId()) {
            Item prevAbility = ability;
            ability = charClass.getAbilities().get(0);
            updateAbility(prevAbility);
        }
        if (charClass.getArmors().get(0).getAbsItemId() != prevClass.getArmors().get(0).getAbsItemId()) {
            Item prevArmor = armor;
            armor = charClass.getArmors().get(0);
            updateArmor(prevArmor);
        }
        mFragment.saveLoadouts();

        loadoutChanged = true;
    }

    public void informSelected(int newStat, int type) {
        int prevStat;
        StatBonus statDiff;
        switch (type) {
            case ATT:
                prevStat = baseStats.getAttBonus();
                statDiff = new StatBonus(prevStat - newStat, 0, 0, 0, 0, 0, 0, 0);
                baseStats.subtractBonus(statDiff);
                statTotals.subtractBonus(statDiff);
                updateAtt();
                break;
            case DEX:
                prevStat = baseStats.getDexBonus();
                statDiff = new StatBonus(0, 0, 0, prevStat - newStat, 0, 0, 0, 0);
                baseStats.subtractBonus(statDiff);
                statTotals.subtractBonus(statDiff);
                updateDex();
                break;
        }
        mFragment.saveLoadouts();

        loadoutChanged = true;
    }

    public void makeSelectorViewVisible() {
        selectorView.setVisibility(View.VISIBLE);
        filterView.setVisibility(View.VISIBLE);
        backgroundFade.setVisibility(View.VISIBLE);
        selectorView.setX(-1000f);
        filterView.setX(-1000f);
        selectorView.animate().translationXBy(1000f).setDuration(200);
        filterView.animate().translationXBy(1000f).setDuration(200);
    }

    public void makeSelectorViewGone() {
        selectorView.setVisibility(View.GONE);
        backgroundFade.setVisibility(View.GONE);
        filterView.setVisibility(View.GONE);
    }

    void updateClass(CharClass prevClass) {

        // change image
        if(classView != null) {
            classView.setImageResource(charClass.getImageId());
        }

        // subtract old base stats and add new ones
        if(prevClass != null) {
            statTotals.subtractBonus(baseStats);
            baseStats = charClass.getMaxedStats();
            statTotals.addBonus(baseStats);
        }

        // update stat views
        updateAtt();
        updateDex();
    }

    private void checkAndUpdateSetBonus() {
        List<Integer> temp = new ArrayList<>(Arrays.asList(weapon.getPartOfSet(), ability.getPartOfSet(), armor.getPartOfSet(), ring.getPartOfSet()));
        List<Integer> sets = new ArrayList<>();

        // makes a list of item sets with 2 or more equipped pieces
        for(int i = 0; i < temp.size(); i++) {
            for(int j = i + 1; j < temp.size(); j++) {
                if(temp.get(i).equals(temp.get(j)) && !temp.get(i).equals(-1) && !sets.contains(temp.get(i))) {
                    sets.add(temp.get(i));
                    break;
                }
            }
        }

        // removes old bonus, builds a new bonus from any current set bonuses, adds to total
        if (sets.size() > 0) {
            StatBonus newBonus = new StatBonus(0,0,0,0,0,0,0,0);
            statTotals.subtractBonus(setBonus);
            for(int i = 0; i < sets.size(); i++) {
                ItemSet currentSet = Item.itemSets.get(sets.get(i));
                StatBonus currentBonus = currentSet.getBonus(currentSet.checkSet(weapon.getAbsItemId(), ability.getAbsItemId(), armor.getAbsItemId(), ring.getAbsItemId()));
                newBonus.addBonus(currentBonus);
            }
            setBonus.replaceBonus(newBonus);
            statTotals.addBonus(setBonus);
        }
    }

    void updateWeapon(Item prevWeapon) {

        // change image
        if(weaponView != null) {
            weaponView.setImageResource(weapon.getImageId());
        }

        if(prevWeapon != null) {
            // subtract old stats and add new ones
            statTotals.subtractBonus(prevWeapon.getStatBonus());
            statTotals.addBonus(weapon.getStatBonus());

            // check for set bonus
            checkAndUpdateSetBonus();

            // update stat views
            updateAtt();
            updateDex();
        }
    }

    void updateAbility(Item prevAbility) {

        // change image
        if(abilityView != null) {
            abilityView.setImageResource(ability.getImageId());
        }

        if(prevAbility != null) {
            // subtract old stats and add new ones
            statTotals.subtractBonus(prevAbility.getStatBonus());
            statTotals.addBonus(ability.getStatBonus());

            // check for set bonus
            checkAndUpdateSetBonus();

            // update stat views
            updateAtt();
            updateDex();
        }
    }

    void updateArmor(Item prevArmor) {

        // change image
        if(armorView != null) {
            armorView.setImageResource(armor.getImageId());
        }

        if(prevArmor != null) {
            // subtract old stats and add new ones
            statTotals.subtractBonus(prevArmor.getStatBonus());
            statTotals.addBonus(armor.getStatBonus());

            // check for set bonus
            checkAndUpdateSetBonus();

            // update stat views
            updateAtt();
            updateDex();
        }
    }

    void updateRing(Item prevRing) {

        // change image
        if(ringView != null) {
            ringView.setImageResource(ring.getImageId());
        }

        if(prevRing != null) {
            // subtract old stats and add new ones
            statTotals.subtractBonus(prevRing.getStatBonus());
            statTotals.addBonus(ring.getStatBonus());

            // check for set bonus
            checkAndUpdateSetBonus();

            // update stat views
            updateAtt();
            updateDex();
        }
    }

    private void updateAtt() {
        if (attView != null) {
            String attStr = String.valueOf(statTotals.getAttBonus());

            int bonusAtt = statTotals.getAttBonus() - baseStats.getAttBonus();
            if(bonusAtt > 0) {
                attStr = attStr + "(+" + bonusAtt + ')';
            }
            else if(bonusAtt < 0) {
                attStr = attStr + '(' + bonusAtt + ')';
            }

            if(baseStats.getAttBonus() < charClass.getMaxedStats().getAttBonus()) {
                attView.setTextColor(ContextCompat.getColor(mContext, R.color.colorUnmaxedText));
            }
            else {
                attView.setTextColor(ContextCompat.getColor(mContext, R.color.colorMaxedText));
            }
            attView.setText(attStr);
        }
    }

    private void updateDex() {
        if (dexView != null) {
            String dexStr = String.valueOf(statTotals.getDexBonus());

            int bonusDex = statTotals.getDexBonus() - baseStats.getDexBonus();
            if(bonusDex > 0) {
                dexStr = dexStr + "(+" + bonusDex + ')';
            }
            else if(bonusDex < 0) {
                dexStr = dexStr + '(' + bonusDex + ')';
            }
            
            // change color if unmaxed
            if(baseStats.getDexBonus() < charClass.getMaxedStats().getDexBonus()) {
                dexView.setTextColor(ContextCompat.getColor(mContext, R.color.colorUnmaxedText));
            }
            else {
                dexView.setTextColor(ContextCompat.getColor(mContext, R.color.colorMaxedText));
            }
            dexView.setText(dexStr);
        }
    }

    void updateStatus() {
        if(statusView != null) {
            if(!activeEffects[0]) {
                damagingView.setColorFilter(Color.parseColor("#777777"));
            } else {
                damagingView.setColorFilter(null);
            }

            if(!activeEffects[1]) {
                berserkView.setColorFilter(Color.parseColor("#777777"));
            } else {
                berserkView.setColorFilter(null);
            }

            if(!activeEffects[2]) {
                curseView.setColorFilter(Color.parseColor("#777777"));
            } else {
                curseView.setColorFilter(null);
            }

            if(!activeEffects[3]) {
                dazedView.setColorFilter(Color.parseColor("#777777"));
            } else {
                dazedView.setColorFilter(null);
            }

            if(!activeEffects[4]) {
                weakView.setColorFilter(Color.parseColor("#777777"));
            } else {
                weakView.setColorFilter(null);
            }
        }
    }

    public void updateAllViews() {
        updateClass(null);
        updateWeapon(null);
        updateAbility(null);
        updateArmor(null);
        updateRing(null);
        updateStatus();
    }

    public List<DpsEntry> getDps() {
        if(loadoutChanged) {
            generateDps();
            loadoutChanged = false;
        }

        return loadoutDps;
    }

    private void generateDps() {
        final float DEFENSE_DMG_REDUCTION_CAP = 0.9f;
        final int MAX_ENEMY_DEFENSE = 150;

        int realAtt = statTotals.getAttBonus();
        int realDex = statTotals.getDexBonus();
        double dexModifier;
        double attModifier;
        double dmgModifier;
        dexModifier = 1.0;
        attModifier = 1.0;
        dmgModifier = 1.0;

        // turns on status effects
        if(activeEffects[0]) {  // Damaging
            attModifier = 1.25;
        }
        if(activeEffects[1]) {  // Berserk
            dexModifier = 1.25;
        }
        if(activeEffects[2]) {  // Curse
            dmgModifier = 1.25;
        }
        if(activeEffects[3]) {  // Dazed (cancels berserk and sets dex to 0)
            dexModifier = 1.0;
            realDex = 0;
        }
        if(activeEffects[4]) {  // Weak (cancels damaging and sets att to 0)
            attModifier = 1.0;
            realAtt = 0;
        }

        loadoutDps = new ArrayList<>();
        double baseDmg = ((weapon.getAvgDamage() - 0.5) * (0.5 + realAtt / 50.0));
        double minimumDmg = Math.round(baseDmg * (1 - DEFENSE_DMG_REDUCTION_CAP)) * dmgModifier * attModifier;
        double finalRof = (1.5 + 6.5 * (realDex / 75.0)) * dexModifier * weapon.getRateOfFire();
        Log.i("DpsCalc", "Rof: " + finalRof);
        int noOfShots = weapon.getNoOfShots();
        Log.i("DpsCalc", "Shots: " + noOfShots);
        for(int currentDefense = 0; currentDefense <= MAX_ENEMY_DEFENSE; currentDefense++) {   // Generate a table row for every defense level up to the max
            double finalDps;
            Log.i("DpsCalc", "Current defense: " + currentDefense);

            if(weapon.getAttribute() == 0) {          // defense is a factor
                double currentDmg = (Math.round(baseDmg - currentDefense) * dmgModifier * attModifier);
                Log.i("DpsCalc", "[Base shot damage: " + baseDmg + "] [Damage modifier: " + dmgModifier + "] [Attack modifier: " + attModifier + "] [Calculated shot damage: " + currentDmg + ']');

                double finalDamage = Math.max(
                        currentDmg,     // shot damage minus enemy defense rounded, multiplied by curse and damaging/weak modifiers
                        minimumDmg      // minimum shot damage due to defense reduction cap
                );
                Log.i("DpsCalc", "Final shot damage: " + finalDamage);
                finalDps = finalDamage * noOfShots * finalRof;
                Log.i("DpsCalc", "Final dps: " + finalDps);
            }
            else {    // defense factor removed (armor piercing)
                finalDps = Math.round(baseDmg) * dmgModifier * attModifier * noOfShots * finalRof;
            }
            loadoutDps.add(new DpsEntry(finalDps, loadoutId));
        }
    }
}

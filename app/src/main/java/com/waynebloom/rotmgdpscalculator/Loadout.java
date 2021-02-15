package com.waynebloom.rotmgdpscalculator;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Loadout {

    // Cached references
    private final Activity mActivity;
    private final Context mContext;

    // Necessary data
    private static List<CharClass> classData;
    private static final ArrayList<String> selectedCategories = new ArrayList<>(Arrays.asList("untiered", "set_tiered", "tiered"));
    private List<DpsEntry> loadoutDps = new ArrayList<>();
    private int loadoutId;

    // DPS calculation stats
    private CharClass charClass;
    private Item weapon;
    private Item ability;
    private Item armor;
    private Item ring;
    private int baseAtt;
    private int totalAtt;
    private int baseDex;
    private int totalDex;
    private boolean[] activeEffects;    //Tracks active status effects
    private boolean loadoutChanged;

    // Each-row views
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

    // Single instance
    private final RecyclerView selectorView; private ItemAdapter itemAdpt;
    private final View filterView;
    private final CheckBox checkUt;
    private final CheckBox checkSt;
    private final CheckBox checkT;
    private final CheckBox checkReskin;
    private final CheckBox checkWeak;
    private final View backgroundFade;

    // For communication with ItemAdapter and ClassAdapter
    static final int CLASS = 0;
    static final int WEAPON = 1;
    static final int ABILITY = 2;
    static final int ARMOR = 3;
    static final int RING = 4;

    // Empty loadout
    Loadout(Context mContext, Activity activity, int loadoutId) {
        this.mContext = mContext;
        this.mActivity = activity;
        selectorView = activity.findViewById(R.id.selector);
        filterView = activity.findViewById(R.id.filter);
            checkUt = filterView.findViewById(R.id.check_ut);
            checkSt = filterView.findViewById(R.id.check_st);
            checkT = filterView.findViewById(R.id.check_t);
            checkReskin = filterView.findViewById(R.id.check_reskin);
            checkWeak = filterView.findViewById(R.id.check_weak);
        backgroundFade = activity.findViewById(R.id.fade);
        this.loadoutId = loadoutId;
        this.loadoutChanged = true;
    }

    // Loadout with Class included (for 'createNewLoadout' in MainActivity)
    Loadout(Context mContext, Activity activity, int loadoutId, CharClass charClass) {
        this.mContext = mContext;
        this.mActivity = activity;
        selectorView = activity.findViewById(R.id.selector);
        filterView = activity.findViewById(R.id.filter);
            checkUt = filterView.findViewById(R.id.check_ut);
            checkSt = filterView.findViewById(R.id.check_st);
            checkT = filterView.findViewById(R.id.check_t);
            checkReskin = filterView.findViewById(R.id.check_reskin);
            checkWeak = filterView.findViewById(R.id.check_weak);
        backgroundFade = activity.findViewById(R.id.fade);
        this.loadoutId = loadoutId;
        this.loadoutChanged = true;

        setCharClass(charClass);
        setWeapon(charClass.getWeapons().get(0));
        setAbility(charClass.getAbilities().get(0));
        setArmor(charClass.getArmors().get(0));
        setRing(charClass.getRings().get(0));
        setActiveEffects("00000");
    }

    // Loadout with all gear included (for 'loadBuilds' in MainActivity)
    Loadout(Context mContext, Activity activity, int loadoutId, CharClass charClass, Item weapon, Item ability, Item armor, Item ring, String activeEffects) {
        this.mContext = mContext;
        this.mActivity = activity;
        selectorView = activity.findViewById(R.id.selector);
        filterView = activity.findViewById(R.id.filter);
            checkUt = filterView.findViewById(R.id.check_ut);
            checkSt = filterView.findViewById(R.id.check_st);
            checkT = filterView.findViewById(R.id.check_t);
            checkReskin = filterView.findViewById(R.id.check_reskin);
            checkWeak = filterView.findViewById(R.id.check_weak);
        backgroundFade = activity.findViewById(R.id.fade);
        this.loadoutId = loadoutId;
        this.loadoutChanged = true;

        this.charClass = charClass;
        this.weapon = weapon;
        this.ability = ability;
        this.armor = armor;
        this.ring = ring;

        setCharClass(charClass);
        setWeapon(weapon);
        setAbility(ability);
        setArmor(armor);
        setRing(ring);
        setActiveEffects(activeEffects);
    }

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

    // Called once from onCreate
    public static void setStatics(List<CharClass> classData) {
        Loadout.classData = classData;
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
                final int MAX_ATT = charClass.getBaseAtt();
                String[] baseStatRange = new String[MAX_ATT + 1];   // '+1' is to include 0
                for(int i = MIN_ATT; i <= MAX_ATT; i++) {
                    baseStatRange[i] = Integer.toString(i);
                }

                // Produce a dialog box for selection of new baseAtt value
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mActivity);
                mBuilder.setTitle("Change Base Attack")
                        .setItems(baseStatRange, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        baseAtt = which;
                        updateAtt();
                    }
                })
                        .create()
                        .getListView().setSelectionFromTop(baseAtt, 0);
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
                final int MAX_DEX = charClass.getBaseDex();
                String[] baseStatRange = new String[MAX_DEX + 1];   // '+1' is to include 0
                for(int i = MIN_DEX; i <= MAX_DEX; i++) {
                    baseStatRange[i] = Integer.toString(i);
                }

                // Produce a dialog box for selection of new baseDex value
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mActivity);
                mBuilder.setTitle("Change Base Dexterity")
                        .setItems(baseStatRange, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        baseDex = which;
                        updateDex();
                    }
                })
                        .create()
                        .getListView().setSelectionFromTop(baseDex, 0);
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mActivity);
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mActivity);
                mBuilder.setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this loadout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        caller.removeAt(loadoutId);
                        ((MainActivity) mActivity).notifyLoadoutRemoved();
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

    public void setLoadoutId(int loadoutId) {
        this.loadoutId = loadoutId;
    }

    public void setCharClass(CharClass charClass) {
        this.charClass = charClass;
        baseAtt = charClass.getBaseAtt();
        totalAtt = baseAtt;
        baseDex = charClass.getBaseDex();
        totalDex = baseDex;
        updateClass();
    }

    public void setWeapon(Item weapon) {
        this.weapon = weapon;
        updateWeapon();
    }

    public void setAbility(Item ability) {
        this.ability = ability;
        updateAbility();
    }

    public void setArmor(Item armor) {
        this.armor = armor;
        updateArmor();
    }

    public void setRing(Item ring) {
        this.ring = ring;
        updateRing();
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
    public void informSelected(Item item, CharClass mClass, int type) {
        switch(type) {
            case CLASS:
                charClass = mClass;
                updateClass();
                if(!charClass.getWeapons().get(weapon.getItemId()).equals(weapon)) {
                    weapon = charClass.getWeapons().get(0);
                    updateWeapon();
                }
                if(!charClass.getAbilities().get(ability.getItemId()).equals(ability)) {
                    ability = charClass.getAbilities().get(0);
                    updateAbility();
                }
                if(!charClass.getArmors().get(armor.getItemId()).equals(armor)) {
                    armor = charClass.getArmors().get(0);
                    updateArmor();
                }
                break;
            case WEAPON:
                weapon = item;
                updateWeapon();
                break;
            case ABILITY:
                ability = item;
                updateAbility();
                break;
            case ARMOR:
                armor = item;
                updateArmor();
                break;
            case RING:
                ring = item;
                updateRing();
                break;
        }

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

    void updateClass() {
        if(classView != null) {
            classView.setImageResource(charClass.getImageId());
        }
        baseAtt = charClass.getBaseAtt();
        baseDex = charClass.getBaseDex();
        updateAtt();
        updateDex();
    }

    void updateWeapon() {
        if(weaponView != null) {
            weaponView.setImageResource(weapon.getImageId());
        }
        updateAtt();
        updateDex();
    }

    void updateAbility() {
        if(abilityView != null) {
            abilityView.setImageResource(ability.getImageId());
        }
        updateAtt();
        updateDex();
    }

    void updateArmor() {
        if(armorView != null) {
            armorView.setImageResource(armor.getImageId());
        }
        updateAtt();
        updateDex();
    }

    void updateRing() {
        if(ringView != null) {
            ringView.setImageResource(ring.getImageId());
        }
        updateAtt();
        updateDex();
    }

    void updateAtt() {
        if(attView != null) {
            String attString;

            totalAtt = baseAtt + weapon.getBonusAtt() + ability.getBonusAtt() + armor.getBonusAtt() + ring.getBonusAtt();
            attString = baseAtt + "(" + totalAtt + ")";
            attView.setText(attString);

            if(baseAtt < charClass.getBaseAtt()) {
                attView.setTextColor(mContext.getResources().getColor(R.color.colorUnmaxedText));
            }
            else {
                attView.setTextColor(mContext.getResources().getColor(R.color.colorMaxedText));
            }
        }
    }

    void updateDex() {
        if(dexView != null) {
            String dexString;

            totalDex = baseDex + weapon.getBonusDex() + ability.getBonusDex() + armor.getBonusDex() + ring.getBonusDex();
            dexString = baseDex + "(" + totalDex + ")";
            dexView.setText(dexString);

            if(baseDex < charClass.getBaseDex()) {
                dexView.setTextColor(mContext.getResources().getColor(R.color.colorUnmaxedText));
            }
            else {
                dexView.setTextColor(mContext.getResources().getColor(R.color.colorMaxedText));
            }
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
        updateClass();
        updateWeapon();
        updateAbility();
        updateArmor();
        updateRing();
        updateAtt();
        updateDex();
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
        final float DEFENSE_DMG_REDUCTION_CAP = 0.85f;
        final int MAX_ENEMY_DEFENSE = 150;

        int realAtt = totalAtt;
        int realDex = totalDex;
        double dexModifier = 1;
        double attModifier = 1;
        double dmgModifier = 1;

        // Turns on status effects

        if(activeEffects[0]) {  // Damaging
            attModifier = 1.5;
        }
        if(activeEffects[1]) {  // Berserk
            dexModifier = 1.5;
        }
        if(activeEffects[2]) {  // Curse
            dmgModifier = 1.2;
        }
        if(activeEffects[3]) {  // Dazed (cancels berserk and sets dex to 0)
            dexModifier = 1;
            realDex = 0;
        }
        if(activeEffects[4]) {  // Weak (cancels damaging and sets att to 0)
            attModifier = 1;
            realAtt = 0;
        }

        loadoutDps = new ArrayList<>();
        for(int currentDefense = 0; currentDefense <= MAX_ENEMY_DEFENSE; currentDefense++) {   // Generate a table row for every defense level up to the max
            double dpsAtCurrentDefense = 0;

            if(weapon.getAttribute() == 0) {          // Regular equation
                dpsAtCurrentDefense = (((weapon.getAvgDamage() * (0.5 + realAtt / 50.0)) - currentDefense) * dmgModifier * attModifier * weapon.getNoOfShots()) * ((1.5 + 6.5 * (realDex / 75.0)) * dexModifier * weapon.getRateOfFire());
            }
            else if (weapon.getAttribute() == 1) {    // For armor piercing (removes enemy defense)
                dpsAtCurrentDefense = ((weapon.getAvgDamage() * (0.5 + realAtt / 50.0)) * dmgModifier * attModifier * weapon.getNoOfShots()) * ((1.5 + 6.5 * (realDex / 75.0)) * dexModifier * weapon.getRateOfFire());
            }

            if(dpsAtCurrentDefense > DEFENSE_DMG_REDUCTION_CAP * weapon.getAvgDamage() * weapon.getNoOfShots()) {      // Defense cap check (defense can only limit damage up to 85%)
                loadoutDps.add(new DpsEntry(dpsAtCurrentDefense, loadoutId));
            }
            else {
                loadoutDps.add(new DpsEntry(DEFENSE_DMG_REDUCTION_CAP * weapon.getAvgDamage() * weapon.getNoOfShots(), loadoutId));
            }
        }
    }
}

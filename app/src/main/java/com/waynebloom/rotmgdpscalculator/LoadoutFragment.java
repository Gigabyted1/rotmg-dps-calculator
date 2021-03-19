package com.waynebloom.rotmgdpscalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadoutFragment extends Fragment {
    MainActivity mActivity;

    // loadouts
    private List<Loadout> loadouts = new ArrayList<>(8);
    private RecyclerView loadoutView;
    private LoadoutAdapter loadAdpt;
    private Button btnAddLoadout;
    private File saveFile;

    // selector
    private RecyclerView selectorView;
    private View filterView;
    private View fade;

    public LoadoutFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        saveFile = new File(requireContext().getFilesDir(), "loadouts.txt");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loadout, container, false);
        loadoutView = view.findViewById(R.id.loadout_view);
        btnAddLoadout = view.findViewById(R.id.add_button);
        selectorView = view.findViewById(R.id.item_selection_view);
        filterView = view.findViewById(R.id.filter);
        fade = view.findViewById(R.id.fade);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAddLoadout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // randomly select the initial class
                CharClass randomClass = Loadout.classData.get(new Random().nextInt(16));

                if(loadouts.size() < 8) {
                    Loadout newLoadout = new Loadout(
                            LoadoutFragment.this,
                            loadouts.size(),
                            randomClass
                    );
                    loadouts.add(newLoadout);
                    loadAdpt.notifyDataSetChanged();
                    saveLoadouts();
                }
                else {
                    Toast.makeText(getContext(), "You can only have 8 loadouts at a time.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // load saved loadouts and first time set up of adapter
        loadouts = readSavedLoadouts();
        loadAdpt = new LoadoutAdapter(getContext(), loadouts);
        loadoutView.setAdapter(loadAdpt);
        loadoutView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public List<Loadout> getLoadouts() {
        return loadouts;
    }

    public void onBackPressed() {
        if(selectorView.getVisibility() == View.VISIBLE) {
            selectorView.setVisibility(View.INVISIBLE);
            filterView.setVisibility(View.INVISIBLE);
            fade.setVisibility(View.INVISIBLE);
        }
    }

    public void notifyLoadoutRemoved() {
        DpsFragment dpsFragment = (DpsFragment) mActivity.getFragmentHolder().getFragment(1);
        dpsFragment.notifyLoadoutRemoved();
        saveLoadouts();
    }

    public void saveLoadouts() {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: Clean this shit up, my EYES
    private List<Loadout> readSavedLoadouts() {
        List<Loadout> loadouts = new ArrayList<>();
        try {
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
                                LoadoutFragment.this,
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadouts;
    }
}
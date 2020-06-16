package com.waynebloom.rotmgdpscalculator;

import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public interface ClickListeners {
    void onDelClicked(Button view, Loadout loadout);
    void onAttClicked(TextView a, Loadout l);
    void onDexClicked(TextView d, Loadout l);
    void onStatusClicked(ConstraintLayout b, Loadout l);
}

package com.waynebloom.rotmgdpscalculator;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public interface ClickListeners {
    void setupLoadoutViews(ImageView c, ImageView w, ImageView ab, ImageView ar, ImageView r, TextView at, TextView dex, ConstraintLayout s, Button del, int p);
}

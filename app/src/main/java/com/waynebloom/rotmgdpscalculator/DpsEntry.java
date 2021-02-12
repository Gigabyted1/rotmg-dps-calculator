package com.waynebloom.rotmgdpscalculator;

public class DpsEntry {
    private final double dps;
    private final int loadoutId;

    DpsEntry(double dps, int loadoutId) {
        this.dps = dps;
        this.loadoutId = loadoutId;
    }

    public double getDps() {
        return dps;
    }

    public int getLoadoutId() {
        return loadoutId;
    }
}

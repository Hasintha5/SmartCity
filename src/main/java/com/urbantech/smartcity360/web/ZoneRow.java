package com.urbantech.smartcity360.web;

import java.util.Locale;

public class ZoneRow {

    private final String name;
    private final String level;
    private final int depth;
    private final double usage;
    private final boolean powerOn;
    private final int buildings;
    private final boolean leaf;

    public ZoneRow(String name, String level, int depth, double usage,
                   boolean powerOn, int buildings, boolean leaf) {
        this.name = name;
        this.level = level;
        this.depth = depth;
        this.usage = usage;
        this.powerOn = powerOn;
        this.buildings = buildings;
        this.leaf = leaf;
    }

    public String getName() { return name; }
    public String getLevel() { return level; }
    public int getDepth() { return depth; }
    public boolean isPowerOn() { return powerOn; }
    public int getBuildings() { return buildings; }
    public boolean isLeaf() { return leaf; }

    public String getUsageText() {
        return String.format(Locale.ROOT, "%.1f", usage);
    }
}
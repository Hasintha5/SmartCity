package com.urbantech.smartcity360.composite;

import java.util.List;

public class Building implements CityComponent {

    private final String name;
    private final double usageKwh;
    private boolean powerOn = true;

    public Building(String name, double usageKwh) {
        this.name = name;
        this.usageKwh = usageKwh;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLevel() {
        return "Building";
    }

    @Override
    public double reportUsage() {
        return powerOn ? usageKwh : 0.0;
    }

    @Override
    public boolean isPowerOn() {
        return powerOn;
    }

    @Override
    public int shutdownPower() {
        if (powerOn) {
            powerOn = false;
            return 1;
        }
        return 0;
    }

    @Override
    public int restorePower() {
        if (!powerOn) {
            powerOn = true;
            return 1;
        }
        return 0;
    }

    @Override
    public int countBuildings() {
        return 1;
    }

    @Override
    public List<CityComponent> getChildren() {
        return List.of();
    }

    @Override
    public CityComponent find(String name) {
        return this.name.equals(name) ? this : null;
    }
}
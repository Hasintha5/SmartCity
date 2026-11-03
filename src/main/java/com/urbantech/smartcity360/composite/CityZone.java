package com.urbantech.smartcity360.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CityZone implements CityComponent {

    private final String name;
    private final String level;
    private final List<CityComponent> children = new ArrayList<>();

    public CityZone(String name, String level) {
        this.name = name;
        this.level = level;
    }

    public CityZone add(CityComponent child) {
        children.add(child);
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLevel() {
        return level;
    }

    @Override
    public double reportUsage() {
        double total = 0;
        for (CityComponent child : children) {
            total += child.reportUsage();
        }
        return total;
    }

    @Override
    public boolean isPowerOn() {
        for (CityComponent child : children) {
            if (child.isPowerOn()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int shutdownPower() {
        int switchedOff = 0;
        for (CityComponent child : children) {
            switchedOff += child.shutdownPower();
        }
        return switchedOff;
    }

    @Override
    public int restorePower() {
        int switchedOn = 0;
        for (CityComponent child : children) {
            switchedOn += child.restorePower();
        }
        return switchedOn;
    }

    @Override
    public int countBuildings() {
        int count = 0;
        for (CityComponent child : children) {
            count += child.countBuildings();
        }
        return count;
    }

    @Override
    public List<CityComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public CityComponent find(String name) {
        if (this.name.equals(name)) {
            return this;
        }
        for (CityComponent child : children) {
            CityComponent found = child.find(name);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
}
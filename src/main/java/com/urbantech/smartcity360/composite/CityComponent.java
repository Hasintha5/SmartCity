package com.urbantech.smartcity360.composite;

import java.util.List;

public interface CityComponent {

    String getName();

    String getLevel();

    double reportUsage();

    boolean isPowerOn();

    int shutdownPower();

    int restorePower();

    int countBuildings();

    List<CityComponent> getChildren();

    CityComponent find(String name);
}
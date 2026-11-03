package com.urbantech.smartcity360.flyweight;

public class NaiveStreetLight {

    private final int id;
    private final double latitude;
    private final double longitude;
    private boolean on = true;

    private final String model;
    private final int wattage;
    private final int lumens;
    private final int colourTemperature;
    private final int[] hourlyBrightness;
    private final byte[] firmwareConfig;

    public NaiveStreetLight(int id, double latitude, double longitude, StreetLightType template) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.model = new String(template.getModel());
        this.wattage = template.getWattage();
        this.lumens = template.getLumens();
        this.colourTemperature = template.getColourTemperature();
        this.hourlyBrightness = template.copySchedule();    // own copy
        this.firmwareConfig = template.copyFirmware();      // own copy
    }

    public int getId() { return id; }
    public int getWattage() { return wattage; }
}
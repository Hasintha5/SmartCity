package com.urbantech.smartcity360.flyweight;

public class StreetLight {

    private final int id;
    private final double latitude;
    private final double longitude;
    private boolean on = true;
    private final StreetLightType type;

    public StreetLight(int id, double latitude, double longitude, StreetLightType type) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public String status(int hour) {
        return type.operate(id, latitude, longitude, on, hour);
    }

    public int getId() { return id; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public boolean isOn() { return on; }
    public void setOn(boolean on) { this.on = on; }
    public StreetLightType getType() { return type; }
}
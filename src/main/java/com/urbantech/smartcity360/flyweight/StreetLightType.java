package com.urbantech.smartcity360.flyweight;

import java.util.Locale;

public final class StreetLightType {

    private static final int OBJECT_BYTES = 40;

    private final String code;
    private final String model;
    private final int wattage;
    private final int lumens;
    private final int colourTemperature;
    private final int[] hourlyBrightness;
    private final byte[] firmwareConfig;

    public StreetLightType(String code, String model, int wattage, int lumens,
                           int colourTemperature, int[] hourlyBrightness, byte[] firmwareConfig) {
        this.code = code;
        this.model = model;
        this.wattage = wattage;
        this.lumens = lumens;
        this.colourTemperature = colourTemperature;
        this.hourlyBrightness = hourlyBrightness.clone();
        this.firmwareConfig = firmwareConfig.clone();
    }

    public String getCode() { return code; }
    public String getModel() { return model; }
    public int getWattage() { return wattage; }
    public int getLumens() { return lumens; }
    public int getColourTemperature() { return colourTemperature; }

    public int brightnessAt(int hour) {
        return hourlyBrightness[Math.floorMod(hour, 24)];
    }

    public String operate(int lightId, double latitude, double longitude, boolean on, int hour) {
        int brightness = brightnessAt(hour);
        String state;
        if (!on) {
            state = "switched off";
        } else if (brightness == 0) {
            state = "idle (daylight)";
        } else {
            state = brightness + "% brightness";
        }
        return String.format(Locale.ROOT, "Light #%d at (%.4f, %.4f): %s, %s",
                lightId, latitude, longitude, model, state);
    }

    public int estimatedBytes() {
        int modelBytes = 24 + 16 + ((model.length() + 7) / 8) * 8;
        int scheduleBytes = 16 + 4 * hourlyBrightness.length;
        int firmwareBytes = 16 + firmwareConfig.length;
        return OBJECT_BYTES + modelBytes + scheduleBytes + firmwareBytes;
    }

    int[] copySchedule() { return hourlyBrightness.clone(); }
    byte[] copyFirmware() { return firmwareConfig.clone(); }
}
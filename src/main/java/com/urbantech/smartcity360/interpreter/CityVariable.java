package com.urbantech.smartcity360.interpreter;

public enum CityVariable {

    HOUR("hour", "Hour of the day", "0 to 23", 12, 0, 23),
    NOISE("noise", "Noise level", "dB", 55, 0, 150),
    PARKING_MINUTES("parking_minutes", "Time parked", "minutes", 90, 0, 1440),
    AIR_QUALITY("air_quality", "Air quality index", "AQI", 60, 0, 500),
    VEHICLES("vehicles", "Vehicles on the road", "vehicles", 120, 0, 5000);

    private final String key;
    private final String label;
    private final String unit;
    private final double defaultValue;
    private final double min;
    private final double max;

    CityVariable(String key, String label, String unit,
                 double defaultValue, double min, double max) {
        this.key = key;
        this.label = label;
        this.unit = unit;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
    }

    public String getKey() { return key; }
    public String getLabel() { return label; }
    public String getUnit() { return unit; }
    public double getDefaultValue() { return defaultValue; }
    public double getMin() { return min; }
    public double getMax() { return max; }

    public static CityVariable fromKey(String key) {
        for (CityVariable variable : values()) {
            if (variable.key.equals(key)) {
                return variable;
            }
        }
        return null;
    }

    public static String allKeys() {
        StringBuilder text = new StringBuilder();
        for (CityVariable variable : values()) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(variable.key);
        }
        return text.toString();
    }
}
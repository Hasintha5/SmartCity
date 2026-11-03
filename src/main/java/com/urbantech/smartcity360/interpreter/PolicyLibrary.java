package com.urbantech.smartcity360.interpreter;

import java.util.List;
import java.util.Map;

public final class PolicyLibrary {

    private static final List<Policy> ALL = List.of(
            new Policy("noise", "Night noise limit",
                    "((hour >= 22 OR hour < 6) AND noise <= 55) OR (hour >= 6 AND hour < 22 AND noise <= 70)",
                    "55 dB at night (22:00 to 06:00), 70 dB in the daytime.",
                    Map.of(CityVariable.HOUR, 23.0, CityVariable.NOISE, 62.0,
                            CityVariable.PARKING_MINUTES, 90.0, CityVariable.AIR_QUALITY, 60.0,
                            CityVariable.VEHICLES, 120.0)),

            new Policy("parking", "Parking limit",
                    "parking_minutes <= 120 OR hour >= 18 OR hour < 6",
                    "Parking is limited to 2 hours, except in the evening and at night.",
                    Map.of(CityVariable.HOUR, 14.0, CityVariable.NOISE, 55.0,
                            CityVariable.PARKING_MINUTES, 90.0, CityVariable.AIR_QUALITY, 60.0,
                            CityVariable.VEHICLES, 120.0)),

            new Policy("air", "Air quality alert",
                    "air_quality <= 100 AND NOT (vehicles > 500 AND air_quality > 80)",
                    "Air quality must stay under 100, and under 80 when traffic is heavy.",
                    Map.of(CityVariable.HOUR, 9.0, CityVariable.NOISE, 60.0,
                            CityVariable.PARKING_MINUTES, 30.0, CityVariable.AIR_QUALITY, 90.0,
                            CityVariable.VEHICLES, 650.0))
    );

    private PolicyLibrary() {
    }

    public static List<Policy> all() {
        return ALL;
    }

    public static Policy find(String key) {
        for (Policy policy : ALL) {
            if (policy.getKey().equals(key)) {
                return policy;
            }
        }
        return null;
    }
}
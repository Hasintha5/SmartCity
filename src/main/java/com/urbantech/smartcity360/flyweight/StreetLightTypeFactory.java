package com.urbantech.smartcity360.flyweight;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class StreetLightTypeFactory {

    private static final Map<String, StreetLightType> CACHE = new ConcurrentHashMap<>();
    private static final AtomicInteger REQUESTS = new AtomicInteger();
    private static final AtomicInteger CREATED = new AtomicInteger();

    private StreetLightTypeFactory() {
    }

    public static StreetLightType getType(String code) {
        REQUESTS.incrementAndGet();
        return CACHE.computeIfAbsent(code, c -> {
            CREATED.incrementAndGet();
            return create(c);
        });
    }

    public static List<String> codes() {
        return List.of("LED-60", "LED-100", "SOLAR-40");
    }

    public static int typeCount() {
        return CACHE.size();
    }

    public static int requests() {
        return REQUESTS.get();
    }

    public static int created() {
        return CREATED.get();
    }

    public static long totalTypeBytes() {
        long sum = 0;
        for (StreetLightType type : CACHE.values()) {
            sum += type.estimatedBytes();
        }
        return sum;
    }

    public static void reset() {
        CACHE.clear();
        REQUESTS.set(0);
        CREATED.set(0);
    }

    private static StreetLightType create(String code) {
        switch (code) {
            case "LED-60":
                return build(code, "LED street light 60W", 60, 7200, 4000, 90, 50, 70);
            case "LED-100":
                return build(code, "LED street light 100W", 100, 12000, 4000, 100, 60, 80);
            case "SOLAR-40":
                return build(code, "Solar street light 40W", 40, 4800, 3000, 80, 40, 50);
            default:
                throw new IllegalArgumentException("Unknown street light type: " + code);
        }
    }

    private static StreetLightType build(String code, String model, int wattage, int lumens,
                                         int kelvin, int evening, int night, int dawn) {
        int[] schedule = new int[24];
        for (int hour = 0; hour < 24; hour++) {
            if (hour >= 23 || hour < 5) {
                schedule[hour] = night;
            } else if (hour >= 18) {
                schedule[hour] = evening;
            } else if (hour == 5) {
                schedule[hour] = dawn;
            } else {
                schedule[hour] = 0;
            }
        }
        byte[] firmware = new byte[512];
        Arrays.fill(firmware, (byte) wattage);
        return new StreetLightType(code, model, wattage, lumens, kelvin, schedule, firmware);
    }
}
package com.urbantech.smartcity360.flyweight;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public final class FleetComparison {

    public static final int LIGHT_BYTES = 40;

    private static final double CENTRE_LAT = 6.9271;
    private static final double CENTRE_LNG = 79.8612;
    private static final int SAMPLE_SIZE = 5;

    private FleetComparison() {
    }

    private static String codeFor(int id) {
        int slot = id % 10;
        if (slot < 6) {
            return "LED-60";
        }
        if (slot < 9) {
            return "LED-100";
        }
        return "SOLAR-40";
    }

    private static double randomOffset(Random random) {
        return (random.nextDouble() - 0.5) * 0.1;
    }

    public static List<StreetLight> buildFlyweight(int count) {
        Random random = new Random(42);
        List<StreetLight> lights = new ArrayList<>(count);
        for (int id = 1; id <= count; id++) {
            StreetLightType type = StreetLightTypeFactory.getType(codeFor(id));
            lights.add(new StreetLight(id,
                    CENTRE_LAT + randomOffset(random), CENTRE_LNG + randomOffset(random), type));
        }
        return lights;
    }

    public static List<NaiveStreetLight> buildNaive(int count) {
        Random random = new Random(42);
        Map<String, StreetLightType> templates = Map.of(
                "LED-60", StreetLightTypeFactory.getType("LED-60"),
                "LED-100", StreetLightTypeFactory.getType("LED-100"),
                "SOLAR-40", StreetLightTypeFactory.getType("SOLAR-40"));
        List<NaiveStreetLight> lights = new ArrayList<>(count);
        for (int id = 1; id <= count; id++) {
            lights.add(new NaiveStreetLight(id,
                    CENTRE_LAT + randomOffset(random), CENTRE_LNG + randomOffset(random),
                    templates.get(codeFor(id))));
        }
        return lights;
    }

    private static long timeNaive(int count) {
        long start = System.nanoTime();
        List<NaiveStreetLight> lights = buildNaive(count);
        long elapsed = System.nanoTime() - start;
        if (lights.size() != count) {
            throw new IllegalStateException("Wrong number of lights");
        }
        return elapsed;
    }

    public static synchronized Result run(int count) {

        timeNaive(Math.min(count, 2000));
        buildFlyweight(Math.min(count, 2000));

        long naiveNanos = timeNaive(count);

        StreetLightTypeFactory.reset();
        long start = System.nanoTime();
        List<StreetLight> lights = buildFlyweight(count);
        long flyweightNanos = System.nanoTime() - start;

        int requests = StreetLightTypeFactory.requests();
        int created = StreetLightTypeFactory.created();
        int typeCount = StreetLightTypeFactory.typeCount();

        long naiveBytes = 0;
        for (StreetLight light : lights) {
            naiveBytes += LIGHT_BYTES + light.getType().estimatedBytes();
        }

        long flyweightBytes = (long) count * LIGHT_BYTES + StreetLightTypeFactory.totalTypeBytes();

        List<StreetLight> sample = new ArrayList<>(lights.subList(0, Math.min(SAMPLE_SIZE, count)));
        return new Result(count, typeCount, requests, created,
                flyweightBytes, naiveBytes, flyweightNanos, naiveNanos, sample);
    }

    public static synchronized List<ScalingRow> scalingTable() {
        long led60 = StreetLightTypeFactory.getType("LED-60").estimatedBytes();
        long led100 = StreetLightTypeFactory.getType("LED-100").estimatedBytes();
        long solar = StreetLightTypeFactory.getType("SOLAR-40").estimatedBytes();
        long allTypes = led60 + led100 + solar;
        long typeBytesTimes10 = 6 * led60 + 3 * led100 + solar;

        int[] sizes = {1_000, 10_000, 50_000, 100_000, 1_000_000};
        List<ScalingRow> rows = new ArrayList<>();
        for (int size : sizes) {
            long naive = (long) size * LIGHT_BYTES + (long) size * typeBytesTimes10 / 10;
            long flyweight = (long) size * LIGHT_BYTES + allTypes;
            rows.add(new ScalingRow(size, naive, flyweight));
        }
        return rows;
    }


    public static String format(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        if (bytes < 1024 * 1024) {
            return String.format(Locale.ROOT, "%.1f KB", bytes / 1024.0);
        }
        return String.format(Locale.ROOT, "%.2f MB", bytes / (1024.0 * 1024.0));
    }

    private static String percent(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }

    private static String millis(long nanos) {
        return String.format(Locale.ROOT, "%.1f ms", nanos / 1_000_000.0);
    }

    public static final class Result {
        private final int count;
        private final int typeCount;
        private final int requests;
        private final int created;
        private final long flyweightBytes;
        private final long naiveBytes;
        private final long flyweightNanos;
        private final long naiveNanos;
        private final List<StreetLight> sample;

        Result(int count, int typeCount, int requests, int created, long flyweightBytes,
               long naiveBytes, long flyweightNanos, long naiveNanos, List<StreetLight> sample) {
            this.count = count;
            this.typeCount = typeCount;
            this.requests = requests;
            this.created = created;
            this.flyweightBytes = flyweightBytes;
            this.naiveBytes = naiveBytes;
            this.flyweightNanos = flyweightNanos;
            this.naiveNanos = naiveNanos;
            this.sample = sample;
        }

        public int getCount() { return count; }
        public int getTypeCount() { return typeCount; }
        public int getRequests() { return requests; }
        public int getCreated() { return created; }
        public List<StreetLight> getSample() { return sample; }

        public String getFlyweightText() { return format(flyweightBytes); }
        public String getNaiveText() { return format(naiveBytes); }
        public String getSavedText() { return format(naiveBytes - flyweightBytes); }
        public String getSavedPercentText() {
            return percent(100.0 * (naiveBytes - flyweightBytes) / naiveBytes);
        }
        public String getFlyweightPercentText() {
            return percent(100.0 * flyweightBytes / naiveBytes);
        }
        public String getFlyweightMsText() { return millis(flyweightNanos); }
        public String getNaiveMsText() { return millis(naiveNanos); }
    }

    public static final class ScalingRow {
        private final int count;
        private final long naiveBytes;
        private final long flyweightBytes;

        ScalingRow(int count, long naiveBytes, long flyweightBytes) {
            this.count = count;
            this.naiveBytes = naiveBytes;
            this.flyweightBytes = flyweightBytes;
        }

        public String getCountText() { return String.format(Locale.ROOT, "%,d", count); }
        public String getNaiveText() { return format(naiveBytes); }
        public String getFlyweightText() { return format(flyweightBytes); }
        public String getSavedPercentText() {
            return percent(100.0 * (naiveBytes - flyweightBytes) / naiveBytes);
        }
    }
}
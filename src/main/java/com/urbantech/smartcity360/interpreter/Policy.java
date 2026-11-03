package com.urbantech.smartcity360.interpreter;

import java.util.Map;

public class Policy {

    private final String key;
    private final String title;
    private final String rule;
    private final String description;
    private final Map<CityVariable, Double> sample;

    public Policy(String key, String title, String rule, String description,
                  Map<CityVariable, Double> sample) {
        this.key = key;
        this.title = title;
        this.rule = rule;
        this.description = description;
        this.sample = sample;
    }

    public String getKey() { return key; }
    public String getTitle() { return title; }
    public String getRule() { return rule; }
    public String getDescription() { return description; }
    public Map<CityVariable, Double> getSample() { return sample; }
}
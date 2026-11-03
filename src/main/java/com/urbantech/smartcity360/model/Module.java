package com.urbantech.smartcity360.model;

public class Module {

    private final String part;
    private final String title;
    private final String pattern;
    private final String description;
    private final String path;
    private final String colorClass;
    private final boolean ready;

    public Module(String part, String title, String pattern, String description,
                  String path, String colorClass, boolean ready) {
        this.part = part;
        this.title = title;
        this.pattern = pattern;
        this.description = description;
        this.path = path;
        this.colorClass = colorClass;
        this.ready = ready;
    }

    public String getPart() { return part; }
    public String getTitle() { return title; }
    public String getPattern() { return pattern; }
    public String getDescription() { return description; }
    public String getPath() { return path; }
    public String getColorClass() { return colorClass; }
    public boolean isReady() { return ready; }
}

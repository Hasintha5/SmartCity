package com.urbantech.smartcity360.model;

import java.util.List;

public final class ModuleCatalog {

    private ModuleCatalog() {}

    public static List<Module> all() {
        return List.of(
            new Module("A", "Energy streams", "Decorator",
                "Add encryption, authentication and compression to meter data.",
                "/streams", "line-a", true),
            new Module("B", "Smart zones", "Composite",
                "Run one command on a city, district, street or building.",
                "/zones", "line-b", true),
            new Module("C", "Citizen complaints", "Chain of Responsibility",
                "Escalate a complaint from staff to supervisor to city manager.",
                "/complaints", "line-c", true),
            new Module("D", "Street lights", "Flyweight",
                "Run thousands of street lights with very little memory.",
                "/streetlights", "line-d", true),
            new Module("E", "City policies", "Interpreter",
                "Write a rule such as noise < 70 and check it live.",
                "/policies", "line-e", true)
        );
    }
}

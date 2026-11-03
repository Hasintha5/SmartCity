package com.urbantech.smartcity360.composite;

public final class CityData {

    private static CityZone city = build();

    private CityData() {
    }

    public static synchronized CityZone getCity() {
        return city;
    }

    public static synchronized void reset() {
        city = build();
    }

    private static CityZone build() {
        CityZone root = new CityZone("Colombo", "City");

        // District 1
        CityZone fort = new CityZone("Fort", "District");
        CityZone chatham = new CityZone("Chatham Street", "Street");
        chatham.add(new Building("Town Hall", 420.5));
        chatham.add(new Building("Public Library", 180.0));
        CityZone york = new CityZone("York Street", "Street");
        york.add(new Building("Police Station", 260.0));
        york.add(new Building("Post Office", 150.5));
        fort.add(chatham).add(york);

        // District 2
        CityZone cinnamon = new CityZone("Cinnamon Gardens", "District");
        CityZone independence = new CityZone("Independence Avenue", "Street");
        independence.add(new Building("National Museum", 210.0));
        independence.add(new Building("Art Gallery", 95.5));
        CityZone horton = new CityZone("Horton Place", "Street");
        horton.add(new Building("Apartment Block 1", 320.0));
        horton.add(new Building("Apartment Block 2", 305.5));
        cinnamon.add(independence).add(horton);

        // District 3
        CityZone dehiwala = new CityZone("Dehiwala", "District");
        CityZone galle = new CityZone("Galle Road", "Street");
        galle.add(new Building("Shopping Mall", 900.0));
        galle.add(new Building("Health Clinic", 130.0));
        dehiwala.add(galle);

        root.add(fort).add(cinnamon).add(dehiwala);
        return root;
    }
}
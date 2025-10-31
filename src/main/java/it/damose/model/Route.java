package it.damose.model;

public class Route {
    private final String id;
    private final String shortName;
    private final String longName;

    public Route(String id, String shortName, String longName) {
        this.id = id;
        this.shortName = shortName;
        this.longName = longName;
    }

    public String getId() { return id; }
    public String getShortName() { return shortName; }
    public String getLongName() { return longName; }

    @Override
    public String toString() {
        return shortName + " - " + longName;
    }
}

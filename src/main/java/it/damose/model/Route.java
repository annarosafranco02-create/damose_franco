package it.damose.model;

public class Route {
    private String id;
    private String shortName;
    private String type;

    public Route(String id, String shortName, String type) {
        this.id = id;
        this.shortName = shortName;
        this.type = type;
    }

    public String getId() { return id; }
    public String getShortName() { return shortName; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return shortName + " (" + type + ")";
    }
}

package it.damose.model;

import java.util.HashSet;
import java.util.Set;

public class Stop {
    private final String id;
    private final String name;
    private final double lat;
    private final double lon;
    private final Set<String> routeIds = new HashSet<>();

    public Stop(String id, String name, double lat, double lon) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }

    public void addRoute(String routeId) { routeIds.add(routeId); }
    public Set<String> getRouteIds() { return routeIds; }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}

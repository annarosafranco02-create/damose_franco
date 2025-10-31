package it.damose.model;

import java.util.ArrayList;
import java.util.List;

public class Stop {

    private String id;
    private String name;
    private double lat;
    private double lon;
    private List<Route> routes = new ArrayList<>();

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
    public List<Route> getRoutes() { return routes; }

    public void addRoute(Route route) {
        if (!routes.contains(route)) routes.add(route);
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}

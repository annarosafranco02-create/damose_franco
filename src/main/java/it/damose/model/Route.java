package it.damose.model;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private String routeId;
    private String shortName;
    private String longName;
    private List<Trip> trips = new ArrayList<>();

    public Route(String routeId, String shortName, String longName) {
        this.routeId = routeId;
        this.shortName = shortName;
        this.longName = longName;
    }

    public String getRouteId() { return routeId; }
    public String getShortName() { return shortName; }
    public String getLongName() { return longName; }
    public List<Trip> getTrips() { return trips; }

    public void addTrip(Trip trip) {
        if (!trips.contains(trip)) trips.add(trip);
    }

    @Override
    public String toString() {
        return shortName + " - " + longName;
    }
}

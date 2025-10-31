package it.damose.model;

import java.util.List;

public class Trip {
    private final String routeId;
    private final String tripId;
    private final List<String> stopIds;

    public Trip(String routeId, String tripId, List<String> stopIds) {
        this.routeId = routeId;
        this.tripId = tripId;
        this.stopIds = stopIds;
    }

    public String getRouteId() { return routeId; }
    public String getTripId() { return tripId; }
    public List<String> getStopIds() { return stopIds; }
}

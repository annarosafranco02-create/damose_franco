package it.damose.model;

import java.util.ArrayList;
import java.util.List;

public class Trip {

    private String tripId;
    private String routeId;
    private String serviceId;
    private List<String> stopIds = new ArrayList<>();

    public Trip(String tripId, String routeId, String serviceId) {
        this.tripId = tripId;
        this.routeId = routeId;
        this.serviceId = serviceId;
    }

    public String getTripId() { return tripId; }
    public String getRouteId() { return routeId; }
    public String getServiceId() { return serviceId; }
    public List<String> getStopIds() { return stopIds; }

    public void addStopId(String stopId) { stopIds.add(stopId); }
}

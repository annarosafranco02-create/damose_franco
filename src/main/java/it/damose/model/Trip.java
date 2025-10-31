package it.damose.model;

public class Trip {
    private String id;
    private String routeId;
    private String serviceId;

    public Trip(String id, String routeId, String serviceId) {
        this.id = id;
        this.routeId = routeId;
        this.serviceId = serviceId;
    }

    public String getId() { return id; }
    public String getRouteId() { return routeId; }
    public String getServiceId() { return serviceId; }

    public String getTripId() {
        return id + "-" + routeId + "-" + serviceId;
    }
}

package it.damose.model;

public class StopTime {
    private String tripId;
    private String stopId;
    private String arrivalTime;

    public StopTime(String tripId, String stopId, String arrivalTime) {
        this.tripId = tripId;
        this.stopId = stopId;
        this.arrivalTime = arrivalTime;
    }

    public String getTripId() { return tripId; }
    public String getStopId() { return stopId; }
    public String getArrivalTime() { return arrivalTime; }

    @Override
    public String toString() {
        return arrivalTime + " - Trip: " + tripId;
    }
}

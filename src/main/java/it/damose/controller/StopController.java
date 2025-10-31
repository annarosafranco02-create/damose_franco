package it.damose.controller;

import it.damose.data.*;
import it.damose.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class StopController {
    private List<Stop> stops;
    private List<Route> routes;
    private List<Trip> trips;
    private List<StopTime> stopTimes;

    public StopController() {
        stops = StopsLoader.loadStops();
        RouteLoader RoutesLoader;
        routes = RouteLoader.loadRoutes();
        trips = TripLoader.loadTrips();
        stopTimes = StopTimesLoader.loadStopTimes();
    }

    public List<Stop> getAllStops() {
        return stops;
    }

    /** Trova le linee che passano da una fermata **/
    public List<Route> getRoutesByStop(String stopId) {
        // Trova i trip_id che passano da quella fermata
        Set<String> tripIds = stopTimes.stream()
                .filter(st -> st.getStopId().equals(stopId))
                .map(StopTime::getTripId)
                .collect(Collectors.toSet());

        // Trova i route_id corrispondenti a quei trip_id
        Set<String> routeIds = trips.stream()
                .filter(t -> tripIds.contains(t.getTripId()))
                .map(Trip::getRouteId)
                .collect(Collectors.toSet());

        // Restituisce le Route corrispondenti
        return routes.stream()
                .filter(r -> routeIds.contains(r.getId()))
                .collect(Collectors.toList());
    }

    /** Trova gli orari di arrivo in una fermata **/
    public List<String> getArrivalTimesByStop(String stopId) {
        return stopTimes.stream()
                .filter(st -> st.getStopId().equals(stopId))
                .map(StopTime::getArrivalTime)
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Stop> searchStops(String query) {
        return stops.stream().filter(stop -> stop.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
    }
}

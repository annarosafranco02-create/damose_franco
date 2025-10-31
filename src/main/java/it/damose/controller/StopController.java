package it.damose.controller;

import it.damose.data.StopsLoader;
import it.damose.data.RouteLoader;
import it.damose.data.TripLoader;
import it.damose.model.Stop;
import it.damose.model.Route;
import it.damose.model.Trip;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class StopController {

    private List<Stop> stops;
    private List<Route> routes;
    private List<Trip> trips;
    private Map<String, Route> routeMap = new HashMap<>();

    public StopController() {
        loadData();
    }

    private void loadData() {
        // Carica fermate
        stops = StopsLoader.loadStops();
        System.out.println("Totale fermate caricate: " + stops.size());

        // Carica linee
        routes = RouteLoader.loadRoutes();
        System.out.println("Totale linee caricate: " + routes.size());
        for (Route r : routes) routeMap.put(r.getId(), r);

        // Carica corse
        trips = TripLoader.loadTrips();
        System.out.println("Totale corse caricate: " + trips.size());

        // Associa routeId alle fermate
        Map<String, Stop> stopMap = new HashMap<>();
        for (Stop s : stops) stopMap.put(s.getId(), s);

        for (Trip t : trips) {
            for (String stopId : t.getStopIds()) {
                Stop s = stopMap.get(stopId);
                if (s != null) s.addRoute(t.getRouteId());
            }
        }
    }

    public List<Stop> getStops() {
        return stops;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public Route getRouteById(String routeId) {
        return routeMap.get(routeId);
    }
}

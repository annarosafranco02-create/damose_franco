package it.damose.controller;

import it.damose.data.RouteLoader;
import it.damose.data.StopsLoader;
import it.damose.data.TripLoader;
import it.damose.model.Route;
import it.damose.model.Stop;
import it.damose.model.Trip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StopController {

    private Map<String, Stop> stops;
    private Map<String, Route> routes;
    private Map<String, Trip> trips;

    private final String ZIP_PATH = "src/main/resources/data/static_gtfs.zip";

    public StopController() {
        // Carica fermate
        stops = StopsLoader.loadStopsFromZip(ZIP_PATH);
        if (stops.isEmpty()) {
            System.err.println("ERRORE: nessuna fermata caricata!");
        } else {
            System.out.println("Totale fermate caricate: " + stops.size());
        }

        // Carica linee
        routes = RouteLoader.loadRoutesFromZip(ZIP_PATH);
        System.out.println("Totale linee caricate: " + routes.size());

        // Carica corse e collega alle linee
        trips = TripLoader.loadTripToRouteFromZip(ZIP_PATH, routes);
        System.out.println("Totale corse caricate: " + trips.size());

        // Collega fermate alle linee
        linkStopsToRoutes();
    }

    /**
     * Collega ogni fermata alle linee che la servono
     */
    private void linkStopsToRoutes() {
        for (Route route : routes.values()) {
            for (Trip trip : route.getTrips()) {
                for (String stopId : trip.getStopIds()) {
                    Stop stop = stops.get(stopId);
                    if (stop != null) {
                        stop.addRoute(route); // ogni Stop deve avere metodo addRoute(Route)
                    }
                }
            }
        }
    }

    /**
     * Ritorna tutte le fermate
     */
    public List<Stop> getAllStops() {
        return List.copyOf(stops.values());
    }

    /**
     * Ritorna tutte le linee
     */
    public List<Route> getAllRoutes() {
        return List.copyOf(routes.values());
    }

    /**
     * Cerca fermata per nome o ID
     */
    public Stop searchStop(String query) {
        query = query.trim().toLowerCase();
        for (Stop stop : stops.values()) {
            if (stop.getId().equals(query) || stop.getName().toLowerCase().contains(query)) {
                return stop;
            }
        }
        return null;
    }

    /**
     * Cerca linea per nome o codice
     */
    public Route searchRoute(String query) {
        query = query.trim().toLowerCase();
        for (Route route : routes.values()) {
            if (route.getRouteId().equals(query) || route.getShortName().toLowerCase().contains(query)) {
                return route;
            }
        }
        return null;
    }
}

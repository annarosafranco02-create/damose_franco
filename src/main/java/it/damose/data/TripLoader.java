package it.damose.data;

import it.damose.model.Route;
import it.damose.model.Trip;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TripLoader {

    public static Map<String, Trip> loadTripToRouteFromZip(String zipPath, Map<String, Route> routes) {
        Map<String, Trip> trips = new HashMap<>();

        try (ZipFile zip = new ZipFile(zipPath)) {
            // leggi trips.txt
            ZipEntry tripsEntry = zip.getEntry("rome_static_gtfs/trips.txt");
            if (tripsEntry == null) return trips;

            try (InputStream is = zip.getInputStream(tripsEntry);
                 BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

                String line;
                br.readLine(); // header
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;
                    String routeId = parts[0];
                    String serviceId = parts[1];
                    String tripId = parts[2];

                    Trip trip = new Trip(tripId, routeId, serviceId);
                    trips.put(tripId, trip);

                    // collega al Route
                    Route route = routes.get(routeId);
                    if (route != null) {
                        route.addTrip(trip);
                    }
                }
            }

            // leggi stop_times.txt per ogni trip
            ZipEntry stopTimesEntry = zip.getEntry("rome_static_gtfs/stop_times.txt");
            if (stopTimesEntry != null) {
                try (InputStream is = zip.getInputStream(stopTimesEntry);
                     BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

                    String line = br.readLine(); // header
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length < 4) continue;
                        String tripId = parts[2];
                        String stopId = parts[3];
                        Trip trip = trips.get(tripId);
                        if (trip != null) {
                            trip.addStopId(stopId);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return trips;
    }
}

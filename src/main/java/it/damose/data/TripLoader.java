package it.damose.data;

import it.damose.model.Trip;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TripLoader {

    private static final String ZIP_PATH = "src/main/resources/data/static_gtfs.zip";

    public static List<Trip> loadTrips() {
        List<Trip> trips = new ArrayList<>();

        try (ZipFile zip = new ZipFile(ZIP_PATH)) {

            // Cerca trips.txt dentro lo zip, indipendentemente dalla cartella interna
            ZipEntry tripsEntry = null;
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                if (e.getName().endsWith("trips.txt")) {
                    tripsEntry = e;
                    break;
                }
            }

            if (tripsEntry == null) {
                System.err.println("ERRORE: trips.txt non trovato nello zip!");
                return trips;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(zip.getInputStream(tripsEntry)))) {
                String line = br.readLine(); // salta intestazione

                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");

                    if (parts.length < 3) continue; // riga malformata

                    String routeId = parts[0].trim();
                    String tripId = parts[2].trim();

                    // Crea trip con liste vuote per stopIds e orari
                    Trip trip = new Trip(routeId, tripId, new ArrayList<>());
                    trips.add(trip);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("TripLoader: totale corse caricate = " + trips.size());
        return trips;
    }
}

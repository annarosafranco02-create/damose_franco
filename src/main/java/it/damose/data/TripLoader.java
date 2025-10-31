package it.damose.data;

import it.damose.model.Trip;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class TripLoader {

    public static List<Trip> loadTrips() {
        List<Trip> trips = new ArrayList<>();
        try {
            InputStream is = TripLoader.class.getResourceAsStream("/data/static_gtfs.zip");
            if (is == null) return trips;

            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("trips.txt")) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(zis));
                    String line = br.readLine(); // header
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 3) {
                            trips.add(new Trip(parts[2], parts[0], parts[1]));
                        }
                    }
                    break;
                }
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trips;
    }
}

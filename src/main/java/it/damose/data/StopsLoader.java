package it.damose.data;

import it.damose.model.Stop;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class StopsLoader {

    private static final String ZIP_PATH = "src/main/resources/data/static_gtfs.zip";

    public static List<Stop> loadStops() {
        List<Stop> stops = new ArrayList<>();
        try (ZipFile zip = new ZipFile(ZIP_PATH)) {

            // Cerca stops.txt in qualsiasi cartella interna
            ZipEntry stopsEntry = null;
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                if (e.getName().endsWith("stops.txt")) {
                    stopsEntry = e;
                    break;
                }
            }

            if (stopsEntry == null) {
                System.err.println("ERRORE: stops.txt non trovato nello zip!");
                return stops;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(zip.getInputStream(stopsEntry)))) {
                String line = br.readLine(); // salta intestazione
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;
                    String id = parts[0].trim();
                    String name = parts[2].replace("\"", "").trim();
                    double lat = parts.length > 4 && !parts[4].isEmpty() ? Double.parseDouble(parts[4]) : 0;
                    double lon = parts.length > 5 && !parts[5].isEmpty() ? Double.parseDouble(parts[5]) : 0;
                    stops.add(new Stop(id, name, lat, lon));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stops;
    }
}

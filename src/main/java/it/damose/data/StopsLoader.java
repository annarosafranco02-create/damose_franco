package it.damose.data;

import it.damose.model.Stop;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class StopsLoader {

    public static List<Stop> loadStops() {
        List<Stop> stops = new ArrayList<>();

        try (InputStream zipStream = StopsLoader.class.getResourceAsStream("/data/static_gtfs.zip")) {
            if (zipStream == null) {
                System.err.println("❌ Impossibile trovare static_gtfs.zip nelle risorse.");
                return stops;
            }

            ZipInputStream zis = new ZipInputStream(zipStream);
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equalsIgnoreCase("stops.txt")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zis));
                    reader.readLine(); // salta header
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 4) {
                            String id = parts[0];
                            String name = parts[2];
                            double lat = Double.parseDouble(parts[3]);
                            double lon = Double.parseDouble(parts[4]);
                            stops.add(new Stop(id, name, lat, lon));
                        }
                    }
                    break;
                }
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stops;
    }
}

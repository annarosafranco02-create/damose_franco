package it.damose.data;

import it.damose.model.Stop;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class StopsLoader {

    public static Map<String, Stop> loadStopsFromZip(String zipPath) {
        Map<String, Stop> stops = new HashMap<>();

        try (ZipFile zip = new ZipFile(zipPath)) {
            ZipEntry entry = zip.getEntry("rome_static_gtfs/stops.txt");
            if (entry == null) return stops;

            try (InputStream is = zip.getInputStream(entry);
                 BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

                String line;
                br.readLine(); // salta header
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 5) continue;
                    String id = parts[0];
                    String name = parts[2].replace("\"", "");
                    double lat = Double.parseDouble(parts[4]);
                    double lon = Double.parseDouble(parts[5]);
                    stops.put(id, new Stop(id, name, lat, lon));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stops;
    }
}

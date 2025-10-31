package it.damose.data;

import it.damose.model.Route;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class RouteLoader {

    public static Map<String, Route> loadRoutesFromZip(String zipPath) {
        Map<String, Route> routes = new HashMap<>();

        try (ZipFile zip = new ZipFile(zipPath)) {
            ZipEntry entry = zip.getEntry("rome_static_gtfs/routes.txt");
            if (entry == null) return routes;

            try (InputStream is = zip.getInputStream(entry);
                 BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

                String line;
                br.readLine(); // salta header
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 4) continue;
                    String id = parts[0];
                    String shortName = parts[2].replace("\"", "");
                    String longName = parts[3].replace("\"", "");
                    routes.put(id, new Route(id, shortName, longName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return routes;
    }
}

package it.damose.data;

import it.damose.model.Route;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class RouteLoader {

    public static List<Route> loadRoutes() {
        List<Route> routes = new ArrayList<>();
        try {
            InputStream is = RouteLoader.class.getResourceAsStream("/data/static_gtfs.zip");
            if (is == null) return routes;

            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("routes.txt")) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(zis));
                    String line = br.readLine(); // header
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 4) {
                            routes.add(new Route(parts[0], parts[2], parts[3]));
                        }
                    }
                    break;
                }
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }
}


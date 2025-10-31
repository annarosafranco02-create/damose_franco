package it.damose.data;

import it.damose.model.Route;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class RouteLoader {

    private static final String ZIP_PATH = "src/main/resources/data/static_gtfs.zip";

    public static List<Route> loadRoutes() {
        List<Route> routes = new ArrayList<>();
        try (ZipFile zip = new ZipFile(ZIP_PATH)) {

            // Cerca routes.txt in qualsiasi cartella interna
            ZipEntry routesEntry = null;
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                if (e.getName().endsWith("routes.txt")) {
                    routesEntry = e;
                    break;
                }
            }

            if (routesEntry == null) {
                System.err.println("ERRORE: routes.txt non trovato nello zip!");
                return routes;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(zip.getInputStream(routesEntry)))) {
                String line = br.readLine(); // salta intestazione
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;
                    String id = parts[0].trim();
                    String shortName = parts[2].replace("\"", "").trim();
                    String longName = parts.length > 3 ? parts[3].replace("\"", "").trim() : "";
                    routes.add(new Route(id, shortName, longName));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return routes;
    }
}

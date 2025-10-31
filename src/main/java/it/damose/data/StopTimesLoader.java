package it.damose.data;

import it.damose.model.StopTime;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipFile;

public class StopTimesLoader {
    public static List<StopTime> loadStopTimes() {
        List<StopTime> list = new ArrayList<>();
        try (ZipFile zip = new ZipFile(
                new File(StopTimesLoader.class.getResource("/data/static_gtfs.zip").toURI()))) {

            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    zip.getInputStream(zip.getEntry("stop_times.txt")), StandardCharsets.UTF_8))) {

                br.readLine(); // salta l’intestazione
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length < 4) continue;
                    String tripId = parts[0];
                    String arrival = parts[1];
                    String stopId = parts[3];
                    list.add(new StopTime(tripId, stopId, arrival));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

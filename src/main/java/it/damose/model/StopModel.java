package it.damose.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StopModel {
    private final List<Stop> stops;

    public StopModel(List<Stop> stops) {
        this.stops = new ArrayList<>(stops); // copia della lista
    }

    public List<Stop> getAllStops() {
        return new ArrayList<>(stops);
    }

    // Metodo di ricerca per nome o ID
    public List<Stop> searchStops(String query) {
        String q = query.trim().toLowerCase();
        boolean isNumeric = q.matches("\\d+");
        return stops.stream()
                .filter(s -> (isNumeric && s.getId().contains(q)) ||
                        (!isNumeric && s.getName().toLowerCase().contains(q)))
                .collect(Collectors.toList());
    }
}
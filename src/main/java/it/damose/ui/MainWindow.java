package it.damose.ui;

import it.damose.data.RouteLoader;
import it.damose.data.StopsLoader;
import it.damose.data.TripLoader;
import it.damose.model.Route;
import it.damose.model.Stop;
import it.damose.model.Trip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class MainWindow extends JFrame {

    private final DefaultListModel<Object> listModel = new DefaultListModel<>();
    private final JList<Object> resultsList = new JList<>(listModel);
    private final JTextArea detailArea = new JTextArea();
    private final JTextField searchField = new JTextField();

    private Map<String, Stop> stops;
    private Map<String, Route> routes;
    private Map<String, Trip> trips;

    public MainWindow() {
        super("Rome Transit Tracker - Damose");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        initLayout();
        loadData();
    }

    private void initLayout() {
        JPanel top = new JPanel(new BorderLayout(5,5));
        top.add(searchField, BorderLayout.CENTER);
        JButton searchBtn = new JButton("Cerca");
        top.add(searchBtn, BorderLayout.EAST);
        top.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listPane = new JScrollPane(resultsList);

        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setBorder(BorderFactory.createTitledBorder("Dettaglio"));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listPane, detailArea);
        split.setDividerLocation(400);

        add(top, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);

        ActionListener doSearch = e -> doSearch();
        searchBtn.addActionListener(doSearch);
        searchField.addActionListener(doSearch);

        resultsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Object obj = resultsList.getSelectedValue();
                if (obj != null) {
                    if (obj instanceof Stop s) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Fermata: ").append(s.getName()).append(" (").append(s.getId()).append(")\n");
                        sb.append("Coordinate: ").append(s.getLat()).append(", ").append(s.getLon()).append("\n");
                        sb.append("Linee:\n");
                        for (Route r : s.getRoutes()) {
                            sb.append(" - ").append(r.getShortName()).append(" ").append(r.getLongName()).append("\n");
                        }
                        detailArea.setText(sb.toString());
                    } else if (obj instanceof Route r) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Linea: ").append(r.getShortName()).append(" - ").append(r.getLongName()).append("\n");
                        sb.append("Numero corse: ").append(r.getTrips().size()).append("\n");
                        detailArea.setText(sb.toString());
                    }
                }
            }
        });
    }

    private void loadData() {
        String zipPath = "src/main/resources/data/static_gtfs.zip";

        stops = StopsLoader.loadStopsFromZip(zipPath);
        System.out.println("Totale fermate caricate: " + stops.size());

        routes = RouteLoader.loadRoutesFromZip(zipPath);
        System.out.println("Totale linee caricate: " + routes.size());

        trips = TripLoader.loadTripToRouteFromZip(zipPath, routes);
        System.out.println("Totale corse caricate: " + trips.size());

        // collega le linee alle fermate usando stop_times.txt
        for (Trip t : trips.values()) {
            for (String stopId : t.getStopIds()) {
                Stop stop = stops.get(stopId);
                Route route = routes.get(t.getRouteId());
                if (stop != null && route != null) {
                    stop.addRoute(route);
                }
            }
        }

        // carica tutto nella lista di default
        listModel.clear();
        stops.values().forEach(listModel::addElement);
        routes.values().forEach(listModel::addElement);
    }

    private void doSearch() {
        String q = searchField.getText().trim().toLowerCase();
        listModel.clear();
        if (q.isEmpty()) {
            stops.values().forEach(listModel::addElement);
            routes.values().forEach(listModel::addElement);
            return;
        }

        // Prima cerco per numero linea
        List<Route> matchedRoutes = routes.values().stream()
                .filter(r -> r.getShortName().toLowerCase().contains(q))
                .collect(Collectors.toList());
        matchedRoutes.forEach(listModel::addElement);

        // Poi cerco per id fermata o nome
        List<Stop> matchedStops = stops.values().stream()
                .filter(s -> s.getId().toLowerCase().contains(q) || s.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());
        matchedStops.forEach(listModel::addElement);

        System.out.println("Risultati trovati: " + (matchedRoutes.size() + matchedStops.size()));
    }
}

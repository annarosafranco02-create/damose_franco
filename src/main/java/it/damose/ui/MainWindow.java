package it.damose.ui;

import it.damose.data.StopsLoader;
import it.damose.data.RouteLoader;
import it.damose.data.TripLoader;
import it.damose.model.Stop;
import it.damose.model.Route;
import it.damose.model.Trip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainWindow extends JFrame {

    private final DefaultListModel<Stop> listModel = new DefaultListModel<>();
    private final JList<Stop> resultsList = new JList<>(listModel);
    private final JTextArea detailArea = new JTextArea();
    private final JTextField searchField = new JTextField();

    private List<Stop> stops;
    private List<Route> routes;
    private List<Trip> trips;
    private Map<String, Route> routeMap = new HashMap<>();

    public MainWindow() {
        super("Rome Transit Tracker - Damose");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        initLayout();
        loadData();
    }

    private void initLayout() {
        // Top panel con barra di ricerca
        JPanel top = new JPanel(new BorderLayout(5,5));
        top.add(searchField, BorderLayout.CENTER);
        JButton searchBtn = new JButton("Cerca");
        top.add(searchBtn, BorderLayout.EAST);
        top.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        add(top, BorderLayout.NORTH);

        // Lista e dettaglio fermata
        JScrollPane listPane = new JScrollPane(resultsList);
        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setBorder(BorderFactory.createTitledBorder("Dettaglio fermata"));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listPane, detailArea);
        split.setDividerLocation(400);
        add(split, BorderLayout.CENTER);

        // Azioni ricerca
        ActionListener doSearch = e -> doSearch();
        searchBtn.addActionListener(doSearch);
        searchField.addActionListener(doSearch);

        // Selezione fermata
        resultsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Stop s = resultsList.getSelectedValue();
                if (s != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(s.toString()).append("\nLinee:");
                    for (String routeId : s.getRouteIds()) {
                        Route r = routeMap.get(routeId);
                        if (r != null) sb.append("\n- ").append(r.toString());
                    }
                    detailArea.setText(sb.toString());
                }
            }
        });
    }

    private void loadData() {
        // Ferme
        stops = StopsLoader.loadStops();
        if (stops.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ERRORE: nessuna fermata caricata!");
        } else {
            System.out.println("Totale fermate caricate: " + stops.size());
            for (Stop s : stops) listModel.addElement(s);
        }

        // Linee
        routes = RouteLoader.loadRoutes();
        for (Route r : routes) routeMap.put(r.getId(), r);
        System.out.println("Totale linee caricate: " + routes.size());

        // Corse
        trips = TripLoader.loadTrips();
        System.out.println("Totale corse caricate: " + trips.size());

        // Associa routeId alle fermate
        Map<String, Stop> stopMap = new HashMap<>();
        for (Stop s : stops) stopMap.put(s.getId(), s);

        for (Trip t : trips) {
            for (String stopId : t.getStopIds()) {
                Stop s = stopMap.get(stopId);
                if (s != null) s.addRoute(t.getRouteId());
            }
        }
    }

    private void doSearch() {
        String q = searchField.getText().trim().toLowerCase();
        listModel.clear();
        if (q.isEmpty()) {
            for (Stop s : stops) listModel.addElement(s);
            return;
        }
        boolean isNumeric = q.matches("\\d+");
        for (Stop s : stops) {
            if (isNumeric && s.getId().contains(q)) listModel.addElement(s);
            else if (!isNumeric && s.getName().toLowerCase().contains(q)) listModel.addElement(s);
        }
        System.out.println("Risultati trovati: " + listModel.getSize());
    }
}

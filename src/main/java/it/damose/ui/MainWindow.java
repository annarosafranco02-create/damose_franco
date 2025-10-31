package it.damose.ui;

import it.damose.controller.StopController;
import it.damose.model.Stop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class MainWindow extends JFrame {
    private final StopController stopController = new StopController(); // ✅ nessun parametro
    private final DefaultListModel<Stop> listModel = new DefaultListModel<>();
    private final JList<Stop> resultsList = new JList<>(listModel);
    private final JTextArea detailArea = new JTextArea();
    private final JTextField searchField = new JTextField();

    public MainWindow() {
        super("Rome Transit Tracker - Damose");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        initLayout();
        loadStops();
    }

    private void initLayout() {
        JPanel top = new JPanel(new BorderLayout(5, 5));
        JButton searchBtn = new JButton("Cerca");
        top.add(searchField, BorderLayout.CENTER);
        top.add(searchBtn, BorderLayout.EAST);
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setBorder(BorderFactory.createTitledBorder("Dettaglio fermata"));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(resultsList), new JScrollPane(detailArea));
        split.setDividerLocation(400);

        add(top, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);

        ActionListener searchAction = e -> doSearch();
        searchBtn.addActionListener(searchAction);
        searchField.addActionListener(searchAction);

        resultsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Stop s = resultsList.getSelectedValue();
                if (s != null) {
                    detailArea.setText(s.toString());
                }
            }
        });
    }

    private void loadStops() {
        List<Stop> stops = stopController.getAllStops();
        listModel.clear();
        for (Stop s : stops) listModel.addElement(s);
    }

    private void doSearch() {
        String query = searchField.getText().trim();
        List<Stop> results = stopController.searchStops(query);
        listModel.clear();
        for (Stop s : results) listModel.addElement(s);
    }
}

package controller;

import io.FileParser;
import model.Graph;
import view.MainFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public class MenuController {
    private final MainFrame frame;
    private final FileParser parser = new FileParser();

    public MenuController(MainFrame frame) {
        this.frame = frame;
    }

    public void onLoadGraph() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load Graph File");
        chooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));

        int result = chooser.showOpenDialog(frame);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File selected = chooser.getSelectedFile();
        try {
            Graph graph = parser.loadFromFile(selected);
            System.out.printf("Graph loaded successfully: %d vertices, %d edges%n",
                    graph.getVertices().size(), graph.getEdges().size());
            // TODO: hand the graph to the view for rendering
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(frame,
                    "Failed to load graph:\n" + e.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

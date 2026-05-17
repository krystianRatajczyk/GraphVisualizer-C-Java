package view;

import controller.AlgorithmController;
import controller.LoadController;
import model.Config;
import model.Graph;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MenuBar {
    private final JFrame parent;

    public MenuBar(JFrame parent) {
        this.parent = parent;
    }

    public JMenuBar buildMenuBar(Config config, LoadController loadController, AlgorithmController algorithmController) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton generateButton = getGenerateButton(config, algorithmController);
        JButton loadButton = getLoadButton(config, loadController);

        menuBar.add(loadButton);
        menuBar.add(generateButton);

        return menuBar;
    }

    private JButton getLoadButton(Config config, LoadController loadController) {
        JButton loadButton = new JButton("Load Graph");
        loadButton.addActionListener(e -> {
            if (config.getInputFile() != null) {
                config.setInputFile(null);
                loadButton.setText("Load Graph");
                return;
            }
            loadController.onLoadInputFile(config);
            if (config.getInputFile() != null) {
                loadButton.setText(config.getInputFile().getName());
            }
        });

        return loadButton;
    }

    private JButton getGenerateButton(Config config, AlgorithmController algorithmController) {
        JButton generateButton = new JButton("Generate");

        generateButton.addActionListener(e -> {
            if (config.getInputFile() == null) {
                JOptionPane.showMessageDialog(parent, "Choose file first", "No file", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            if (config.getAlgorithm() == null || config.getAlgorithm().isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Choose algorithm variant in the Control Panel", "No algorithm", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            try {
                Graph graph = algorithmController.runAlgorithm(config);

                if (graph != null) {
                    JOptionPane.showMessageDialog(parent,
                            "Graph loaded successfully !\n" + "Edges: " + graph.getEdges().size() +
                                    " Vertices: " + graph.getVertices().size(), "Info", JOptionPane.PLAIN_MESSAGE);
                }
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(parent, exception.getMessage(), "Runtime error", JOptionPane.PLAIN_MESSAGE);
            }
        });
        return generateButton;
    }
}

package view;

import controller.AlgorithmController;
import model.Config;
import model.Graph;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

public class Sidebar extends JPanel {
    private final JFrame parent;

    public Sidebar(JFrame parent) {
        setPreferredSize(new Dimension(350, 0));
        setBackground(Color.WHITE);
        this.parent = parent;
    }

    private JButton getGenerateButton(JComboBox<String> algorithm, Config config, AlgorithmController algorithmController, Canvas canvas) {
        JButton generateButton = new JButton("Generate");

        generateButton.addActionListener(e -> {
            if (config.getInputFile() == null) {
                JOptionPane.showMessageDialog(parent, "Choose file first", "No file", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            config.setAlgorithm((String) algorithm.getSelectedItem());

            try {
                Graph graph = algorithmController.runAlgorithm(config);

                if (graph != null) {
                    JOptionPane.showMessageDialog(parent,
                            "Graph loaded successfully !\n" + "Edges: " + graph.getEdges().size() +
                                    " Vertices: " + graph.getVertices().size(), "Info", JOptionPane.PLAIN_MESSAGE);
                    canvas.setGraph(graph);
                }
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(parent, exception.getMessage(), "Runtime error", JOptionPane.PLAIN_MESSAGE);
            }
        });
        return generateButton;
    }

    private JPanel buildAlgorithmSection(Config config, AlgorithmController algorithmController, Canvas canvas) {
        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);
        section.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] algorithms = {"Fruchterman-Reingold", "Tutte"};
        JComboBox<String> algorithm = new JComboBox<>(algorithms);

        JButton generateButton = getGenerateButton(algorithm, config, algorithmController, canvas);

        JLabel algorithmLabel = new JLabel("Choose algorithm");
        algorithmLabel.setFont(algorithmLabel.getFont().deriveFont(16f));
        algorithmLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // spacing

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        controls.setOpaque(false);
        controls.add(algorithm);
        controls.add(generateButton);

        section.add(algorithmLabel, BorderLayout.NORTH);
        section.add(controls, BorderLayout.CENTER);
        return section;
    }

    private JPanel buildControlsSection(Config config, Canvas canvas) {
        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);
        section.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel controlsLabel = new JLabel("Options");
        controlsLabel.setFont(controlsLabel.getFont().deriveFont(16f));
        controlsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel checkboxes = new JPanel();
        checkboxes.setLayout(new BoxLayout(checkboxes, BoxLayout.Y_AXIS));
        checkboxes.setOpaque(false);

        JCheckBox vertexIds = new JCheckBox("Show vertex IDs");
        JCheckBox edgeNames = new JCheckBox("Show edge names");
        JCheckBox edgeWeights = new JCheckBox("Show edge weights");

        checkboxes.add(vertexIds);
        checkboxes.add(edgeNames);
        checkboxes.add(edgeWeights);

        vertexIds.addActionListener(e -> {
            config.setShowIds(!config.getShowIds());
            canvas.repaint();
        });

        edgeNames.addActionListener(e -> {
            config.setShowEdgeNames(!config.getShowEdgeNames());
            canvas.repaint();
        });

        edgeWeights.addActionListener(e -> {
            config.setShowEdgeWeights(!config.getShowEdgeWeights());
            canvas.repaint();
        });

        section.add(controlsLabel, BorderLayout.NORTH);
        section.add(checkboxes, BorderLayout.CENTER);

        return section;
    }

    public JPanel buildSidebar(Config config, AlgorithmController algorithmController, Canvas canvas) {
        setLayout(new BorderLayout());
        JPanel stackWrapper = new JPanel();
        stackWrapper.setLayout(new BoxLayout(stackWrapper, BoxLayout.Y_AXIS));

        stackWrapper.add(buildAlgorithmSection(config, algorithmController, canvas));
        stackWrapper.add(buildControlsSection(config, canvas));

        stackWrapper.setOpaque(false);

        add(stackWrapper, BorderLayout.NORTH);
        return this;
    }
}

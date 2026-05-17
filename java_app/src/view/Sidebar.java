package view;

import controller.AlgorithmController;
import controller.LoadController;
import model.Config;
import model.Graph;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class Sidebar extends JPanel {
    private final Canvas canvas;
    private final JFrame parent;
    private final Config config;
    private final LoadController loadController;
    private final AlgorithmController algorithmController;

    public Sidebar(Canvas canvas, JFrame parent, Config config, LoadController loadController, AlgorithmController algorithmController) {
        this.canvas = canvas;
        this.parent = parent;
        this.config = config;
        this.loadController = loadController;
        this.algorithmController = algorithmController;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(220, 0));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        JLabel title = new JLabel("Control Panel");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.add(title);

        top.add(Box.createRigidArea(new Dimension(0,8)));

        JLabel algoLabel = new JLabel("Algorithm variant:");
        algoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.add(algoLabel);

        String[] algorithms = {"Fruchterman-Reingold", "Tutte"};
        JComboBox<String> algorithmCombo = new JComboBox<>(algorithms);
        algorithmCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        algorithmCombo.addActionListener(e -> config.setAlgorithm((String) algorithmCombo.getSelectedItem()));
        top.add(algorithmCombo);

        top.add(Box.createRigidArea(new Dimension(0,8)));

        JButton generateButton = new JButton("Generate");
        generateButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        generateButton.addActionListener(e -> {
            if (config.getInputFile() == null) {
                JOptionPane.showMessageDialog(parent, "Choose file first", "No file", JOptionPane.PLAIN_MESSAGE);
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
        top.add(generateButton);

        top.add(Box.createRigidArea(new Dimension(0,8)));

        JCheckBox idsCheckbox = new JCheckBox("Show vertex IDs");
        idsCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        idsCheckbox.addActionListener(e -> canvas.setShowIds(idsCheckbox.isSelected()));
        top.add(idsCheckbox);

        JCheckBox thicknessCheckbox = new JCheckBox("Show edge thickness");
        thicknessCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        thicknessCheckbox.addActionListener(e -> canvas.setShowEdgeThickness(thicknessCheckbox.isSelected()));
        top.add(thicknessCheckbox);

        top.add(Box.createRigidArea(new Dimension(0,8)));

        JLabel modeLabel = new JLabel("Render mode:");
        modeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.add(modeLabel);

        JRadioButton normalMode = new JRadioButton("Normal", true);
        normalMode.setAlignmentX(Component.LEFT_ALIGNMENT);
        JRadioButton contrastMode = new JRadioButton("High contrast");
        contrastMode.setAlignmentX(Component.LEFT_ALIGNMENT);
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(normalMode);
        modeGroup.add(contrastMode);
        top.add(normalMode);
        top.add(contrastMode);

        top.add(Box.createRigidArea(new Dimension(0,8)));

        top.add(Box.createVerticalGlue());

        JButton clearButton = new JButton("Clear");
        clearButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        clearButton.addActionListener(e -> canvas.clear());
        top.add(clearButton);

        add(top, BorderLayout.NORTH);
    }
}

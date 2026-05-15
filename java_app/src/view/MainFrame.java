package view;

import controller.AlgorithmController;
import controller.LoadController;
import model.Config;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainFrame extends JFrame {
    private final LoadController loadController;
    private final AlgorithmController algorithmController;
    private final Config config;

    public MainFrame() {
        super("Graph Visualizer");
        loadController = new LoadController(this);
        algorithmController = new AlgorithmController();
        config = new Config();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setJMenuBar(buildMenuBar());

    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));

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

        String[] algorithms = {"Fruchterman-Reingold", "Tutte"};
        JComboBox algorithm = new JComboBox(algorithms);

        JButton generate = getJButton(algorithm);

        menuBar.add(loadButton);
        menuBar.add(algorithm);
        menuBar.add(generate);

        return menuBar;
    }

    private JButton getJButton(JComboBox algorithm) {
        JButton generate = new JButton("Generate");
        generate.addActionListener(e -> {
            if (config.getInputFile() == null) {
                JOptionPane.showMessageDialog(this, "Choose file first", "No file", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            config.setAlgorithm((String) algorithm.getSelectedItem());

            try {
                algorithmController.runAlgorithm(config);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(this, "Cannot run algorithm", "Runtime error", JOptionPane.PLAIN_MESSAGE);
            }
        });
        return generate;
    }
}

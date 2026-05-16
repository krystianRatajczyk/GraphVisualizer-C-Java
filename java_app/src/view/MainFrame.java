package view;

import controller.AlgorithmController;
import controller.LoadController;
import model.Config;
import model.Graph;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainFrame extends JFrame {
    public final LoadController loadController;
    public final AlgorithmController algorithmController;
    public final Config config;

    public MainFrame() {
        super("Graph Visualizer");
        loadController = new LoadController(this);
        algorithmController = new AlgorithmController();
        config = new Config();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Canvas canvas = new Canvas();
        add(canvas);

        MenuBar menuBar = new MenuBar(this);
        setJMenuBar(menuBar.buildMenuBar(config, loadController, algorithmController, canvas));
    }
}

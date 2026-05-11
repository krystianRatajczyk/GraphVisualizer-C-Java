package view;

import controller.MenuController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final MenuController controller;

    public MainFrame() {
        super("Graph Visualizer");
        controller = new MenuController(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setJMenuBar(buildMenuBar());

    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        JMenuItem loadItem = new JMenuItem("Load Graph...");
        loadItem.setMnemonic('L');
        loadItem.addActionListener(e -> controller.onLoadGraph());

        fileMenu.add(loadItem);
        menuBar.add(fileMenu);
        return menuBar;
    }
}

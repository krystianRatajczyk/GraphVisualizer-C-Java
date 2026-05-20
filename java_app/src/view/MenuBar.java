package view;

import controller.AlgorithmController;
import controller.LoadController;
import model.Config;
import model.Graph;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MenuBar {
    public JMenuBar buildMenuBar(Config config, LoadController loadController) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton loadButton = getLoadButton(config, loadController);

        menuBar.add(loadButton);

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
}

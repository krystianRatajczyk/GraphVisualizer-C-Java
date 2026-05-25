package view;

import controller.LoadController;
import controller.SaveController;
import model.Config;
import view.Canvas;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuBar {
    private JFrame parent;

    public MenuBar(JFrame parent) {
        this.parent = parent;
    }

    public JMenuBar buildMenuBar(Config config, LoadController loadController, SaveController saveController, Canvas canvas) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton loadButton = getLoadButton(config, loadController, canvas);
        JButton saveButton = getSaveButton(canvas, config, saveController);

        menuBar.add(loadButton);
        menuBar.add(saveButton);

        return menuBar;
    }

    private JButton getSaveButton(Canvas canvas, Config config, SaveController saveController) {
        JButton saveButton = new JButton("Save");

        saveButton.setEnabled(false);
        canvas.setOnGraphSet(hasGraph -> saveButton.setEnabled(hasGraph));

        saveButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (config.getInputFile() != null) {
                String baseName = config.getInputFile().getName().replaceAll("\\.[^.]+$", "");
                chooser.setSelectedFile(new File(baseName + ".png"));
            }

            if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

            try {
                saveController.save(canvas, chooser.getSelectedFile());
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(parent, "Cannot save graph", "Runtime error", JOptionPane.PLAIN_MESSAGE);
            }
        });

        return saveButton;
    }

    private JButton getLoadButton(Config config, LoadController loadController, Canvas canvas) {
        JButton loadButton = new JButton("Load Graph");
        loadButton.addActionListener(e -> {
            if (config.getInputFile() != null) {
                config.setInputFile(null);
                canvas.clear();
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

package controller;

import io.FileParser;
import model.Config;
import model.Graph;
import view.MainFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public class LoadController {
    private final MainFrame frame;
    private final FileParser parser = new FileParser();

    public LoadController(MainFrame frame) {
        this.frame = frame;
    }

    public void onLoadInputFile(Config config) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load Graph File");
        chooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));

        int result = chooser.showOpenDialog(frame);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File selected = chooser.getSelectedFile();
        config.setInputFile(selected);
    }
}

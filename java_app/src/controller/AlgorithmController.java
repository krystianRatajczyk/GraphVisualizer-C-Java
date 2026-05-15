package controller;

import io.FileParser;
import model.Config;
import model.Graph;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.Map;

public class AlgorithmController {
    public void runAlgorithm(Config config) throws IOException {
        String fullName = config.getInputFile().getName();
        int dotIndex = fullName.lastIndexOf('.');
        if (dotIndex >= 0) {
            fullName = fullName.substring(0, dotIndex);
        }


        File cAppDir = new File("c_app").getCanonicalFile();
        File resDir = new File("java_app/res");
        resDir.mkdirs();

        File outputFile = new File(resDir, fullName + "_res.txt").getCanonicalFile();
        config.setOutputFile(outputFile);

        Map<String, String> commands = Map.of("Fruchterman-Reingold", "fr", "Tutte", "tutte");
        try {
            run(cAppDir, "make", "clean");
            run(cAppDir, "make");
            run(cAppDir, "./graph_layout",
                    "-a", commands.get(config.getAlgorithm()),
                    "-i", config.getInputFile().getAbsolutePath(),
                    "-o", outputFile.getAbsolutePath());
            FileParser parser = new FileParser();
            Graph graph = parser.loadGraph(config.getInputFile(), config.getOutputFile());
            System.out.println("Graph loaded succesfully!" + " edges: " + graph.getEdges().size() + " vertices: " + graph.getEdges().size());
        } catch (IOException e) {
            throw new IOException(e);
        }


    }

    private void run(File dir, String... command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(dir);
        pb.inheritIO();

        try {
            int exitCode = pb.start().waitFor();
            if (exitCode != 0) {
                System.out.println("Command failed (exit " + exitCode + "): " + String.join(" ", command));
            }

        } catch (IOException e) {
            throw new IOException(e);
        } catch (InterruptedException e) {
            System.out.println("Running interrupted");
        }

    }
}

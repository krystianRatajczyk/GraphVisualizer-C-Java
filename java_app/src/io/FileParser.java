package io;

import model.Edge;
import model.Graph;
import model.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileParser {

    public Graph loadGraph(File inputFile, File outputFile) throws IOException {
        Graph graph = new Graph();

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.strip();
                parseVertex(line, graph);
            }
        } catch (IllegalArgumentException e){
            throw new IOException(e);
        }


        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                line = line.strip();
                parseEdge(line, graph, lineNumber);
                lineNumber++;
            }
        } catch (IllegalArgumentException e){
            throw new IOException(e);
        }

        return graph;
    }

    private void parseVertex(String line, Graph graph) {
        String[] parts = line.split("\\s+");
        if (parts.length < 3) throw new IllegalArgumentException("Bad vertex line: " + line);

        int id = Integer.parseInt(parts[0]);
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);

        graph.addVertex(new Vertex(id, x, y));
    }

    private void parseEdge(String line, Graph graph, int lineNumber) {
        String[] parts = line.split("\\s+");
        if (parts.length < 4) throw new IllegalArgumentException("Bad edge line: " + line);

        String name = parts[0];
        int srcId = Integer.parseInt(parts[1]);
        int dstId = Integer.parseInt(parts[2]);
        double weight = Double.parseDouble(parts[3]);

        Vertex src = graph.findById(srcId);
        Vertex dst = graph.findById(dstId);

        if (src == null || dst == null) {
            throw new IllegalArgumentException(
                    "Edge references unknown vertex id(s): " + srcId + ", " + dstId + ", line number: " + lineNumber);
        }

        graph.addEdge(new Edge(name, src, dst, weight));
    }

}

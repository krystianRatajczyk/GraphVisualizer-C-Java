package io;

import model.Edge;
import model.Graph;
import model.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Parses a plain-text graph file with the following format:
 *
 *   VERTICES
 *   <id> <x> <y>
 *   ...
 *   EDGES
 *   <sourceId> <targetId>
 *   ...
 *
 * Lines starting with '#' are treated as comments and ignored.
 */
public class FileParser {

    public Graph loadFromFile(File file) throws IOException {
        Graph graph = new Graph();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Section current = Section.NONE;

            while ((line = reader.readLine()) != null) {
                line = line.strip();

                if (line.isEmpty() || line.startsWith("#")) continue;

                if (line.equalsIgnoreCase("VERTICES")) {
                    current = Section.VERTICES;
                    continue;
                }
                if (line.equalsIgnoreCase("EDGES")) {
                    current = Section.EDGES;
                    continue;
                }

                switch (current) {
                    case VERTICES -> parseVertex(line, graph);
                    case EDGES    -> parseEdge(line, graph);
                    default       -> { /* TODO: handle header/metadata lines if needed */ }
                }
            }
        }

        return graph;
    }

    // TODO: extend if vertex lines carry additional attributes (e.g. label, color)
    private void parseVertex(String line, Graph graph) {
        String[] parts = line.split("\\s+");
        if (parts.length < 3) throw new IllegalArgumentException("Bad vertex line: " + line);

        int id    = Integer.parseInt(parts[0]);
        double x  = Double.parseDouble(parts[1]);
        double y  = Double.parseDouble(parts[2]);

        graph.addVertex(new Vertex(id, x, y));
    }

    private void parseEdge(String line, Graph graph) {
        String[] parts = line.split("\\s+");
        if (parts.length < 4) throw new IllegalArgumentException("Bad edge line: " + line);

        String name   = parts[0];
        int srcId     = Integer.parseInt(parts[1]);
        int dstId     = Integer.parseInt(parts[2]);
        double weight = Double.parseDouble(parts[3]);

        Vertex src = graph.findById(srcId);
        Vertex dst = graph.findById(dstId);

        if (src == null || dst == null) {
            throw new IllegalArgumentException(
                "Edge references unknown vertex id(s): " + srcId + ", " + dstId);
        }

        graph.addEdge(new Edge(name, src, dst, weight));
    }

    private enum Section { NONE, VERTICES, EDGES }
}

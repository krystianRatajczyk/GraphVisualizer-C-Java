package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graph {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public void addVertex(Vertex v) { vertices.add(v); }
    public void addEdge(Edge e) { edges.add(e); }

    public List<Vertex> getVertices() { return Collections.unmodifiableList(vertices); }
    public List<Edge> getEdges() { return Collections.unmodifiableList(edges); }

    public Vertex findById(int id) {
        return vertices.stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElse(null);
    }
}

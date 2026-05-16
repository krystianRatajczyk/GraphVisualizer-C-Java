package model;

import java.util.*;

public class Graph {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();
    private final Map<Integer, List<Integer>> adj = new HashMap<>();
    private int maxDegree = 0;

    public void addVertex(Vertex v) { vertices.add(v); }
    public void addEdge(Edge e) { edges.add(e); }
    public void addVertex(int id, int v) {
        if (!adj.containsKey(id)) {
            adj.put(id, new ArrayList<>());
        }
        adj.get(id).add(v);
        maxDegree = Math.max(maxDegree, adj.get(id).size());
    }

    public Map<Integer, List<Integer>> getAdj () { return adj;}
    public List<Vertex> getVertices() { return Collections.unmodifiableList(vertices); }
    public List<Edge> getEdges() { return Collections.unmodifiableList(edges); }
    public int getMaxDegree() { return maxDegree; }
    public Vertex findById(int id) {
        return vertices.stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElse(null);
    }

}

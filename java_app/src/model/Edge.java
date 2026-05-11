package model;

public class Edge {
    private final String name;
    private final Vertex source;
    private final Vertex target;
    private final double weight;

    public Edge(String name, Vertex source, Vertex target, double weight) {
        this.name = name;
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public String getName()   { return name; }
    public Vertex getSource() { return source; }
    public Vertex getTarget() { return target; }
    public double getWeight() { return weight; }

    @Override
    public String toString() {
        return "Edge{" + name + ": " + source.getId() + " -> " + target.getId() + ", w=" + weight + "}";
    }
}

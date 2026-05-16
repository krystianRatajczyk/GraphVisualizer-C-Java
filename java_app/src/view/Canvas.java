package view;

import model.Edge;
import model.Graph;
import model.Vertex;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {
    private Graph graph;
    private final int padding = 30;
    private double offsetX, offsetY, scale, yMin, xMin;


    public void setGraph(Graph graph) {
        this.graph = graph;
        double xMax = Double.MIN_VALUE, xMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE, yMin = Double.MAX_VALUE;

        for (Vertex v : graph.getVertices()) {
            xMax = Math.max(xMax, v.getX());
            xMin = Math.min(xMin, v.getX());
            yMax = Math.max(yMax, v.getY());
            yMin = Math.min(yMin, v.getY());
        }

        double deltaX = xMax - xMin;
        double deltaY = yMax - yMin;

        if (deltaX == 0) deltaX = 1;
        if (deltaY == 0) deltaY = 1;

        int usableWidth = getWidth() - (2 * padding);
        int usableHeight = getHeight() - (2 * padding);

        double scale = Math.min(usableWidth / deltaX, usableHeight / deltaY);

        double offsetX = padding + (usableWidth - (deltaX * scale)) / 2;
        double offsetY = padding + (usableHeight - (deltaY * scale)) / 2;

        this.scale = scale;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.yMin = yMin;
        this.xMin = xMin;

        repaint();
    }

    private int scaleX(double x) {
        return (int) (offsetX + (x - xMin) * scale);
    }

    private int scaleY(double y) {
        return (int) (getHeight() - offsetY - (y - yMin) * scale);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (graph == null) return;

        for (Edge e : graph.getEdges()) {
            int x1 = scaleX(e.getSource().getX()), y1 = scaleY(e.getSource().getY());
            int x2 = scaleX(e.getTarget().getX()), y2 = scaleY(e.getTarget().getY());
            g.drawLine(x1, y1, x2, y2);
        }

        for (Vertex v : graph.getVertices()) {
            int x = scaleX(v.getX()), y = scaleY(v.getY());
            g.fillOval(x - 5, y - 5, 10, 10);
        }
    }
}

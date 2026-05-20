package view;

import model.Config;
import model.Edge;
import model.Graph;
import model.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Canvas extends JPanel {
    private Graph graph;
    private double offsetX, offsetY, scale, yMin, xMin;
    private double panX, panY;
    private double zoomFactor = 1.0;
    private Point dragStart;
    private final int radius = 20;
    private Vertex hoveredVertex = null;
    private Vertex draggingVertex = null;
    private final Config config;

    public Canvas(Config config) {
        this.config = config;
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (graph == null) {
                    return;
                }

                int mouseX = e.getX();
                int mouseY = e.getY();

                hoveredVertex = null;

                for (Vertex v : graph.getVertices()) {
                    int scaledX = scaleX(v.getX());
                    int scaledY = scaleY(v.getY());
                    int dx = mouseX - scaledX;
                    int dy = mouseY - scaledY;
                    if (dx * dx + dy * dy <= radius * radius) {
                        hoveredVertex = v;
                        break;
                    }
                }

                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
                draggingVertex = hoveredVertex;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggingVertex = null;
                dragStart = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart == null) return;
                int dx = e.getX() - dragStart.x;
                int dy = e.getY() - dragStart.y;
                dragStart = e.getPoint();
                System.out.println(dx + " : " + dy);
                if (draggingVertex != null) {
                    draggingVertex.setX(draggingVertex.getX() + dx / (scale * zoomFactor));
                    draggingVertex.setY(draggingVertex.getY() - dy / (scale * zoomFactor));
                } else {
                    panX += dx;
                    panY += dy;
                }
                repaint();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (graph == null) return;
                double ratio = e.getWheelRotation() < 0 ? 1.1 : 0.9;
                double mx = e.getX();
                double my = e.getY();
                panX = (mx - offsetX) * (1 - ratio) + panX * ratio;
                panY = (my - getHeight() + offsetY) * (1 - ratio) + panY * ratio;
                zoomFactor *= ratio;
                repaint();
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        panX = 0;
        panY = 0;
        zoomFactor = 1.0;
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

        int padding = 30;
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
        return (int) (offsetX + (x - xMin) * scale * zoomFactor + panX);
    }

    private int scaleY(double y) {
        return (int) (getHeight() - offsetY - (y - yMin) * scale * zoomFactor + panY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (graph == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g.setFont(new Font("Arial", Font.BOLD, 13));

        for (Edge e : graph.getEdges()) {
            int x1 = scaleX(e.getSource().getX()), y1 = scaleY(e.getSource().getY());
            int x2 = scaleX(e.getTarget().getX()), y2 = scaleY(e.getTarget().getY());

            Vertex source = e.getSource();
            Vertex target = e.getTarget();

            if (source == hoveredVertex || target == hoveredVertex) {
                g2.setColor(Color.BLACK);
            } else {
                g2.setColor(Color.LIGHT_GRAY);
            }

            if (config.getShowEdgeNames()) {
                int edgeNameX = scaleX((source.getX() + target.getX()) / 2);
                int edgeNameY = scaleY((source.getY() + target.getY()) / 2);

                String text = String.valueOf(e.getName());
                g.drawString(text, edgeNameX, edgeNameY);
            }

            if (config.getShowEdgeWeights()) {
                int edgeWeightX = scaleX((source.getX() + target.getX()) / 2);
                int edgeWeightY = scaleY((source.getY() + target.getY()) / 2);

                String text = String.valueOf(e.getWeight());
                g.drawString(text, edgeWeightX, edgeWeightY);
            }

            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(x1, y1, x2, y2);
        }

        for (Vertex v : graph.getVertices()) {
            int x = scaleX(v.getX()), y = scaleY(v.getY());

            float hue = 0.66f - (graph.getAdj().get(v.getId()).size() / (float) graph.getMaxDegree()) * 0.66f;
            g.setColor(Color.getHSBColor(hue, 1f, 0.85f));
            g.fillOval(x - radius / 2, y - radius / 2, radius, radius);

            if (config.getShowIds()) {
                g.setColor(Color.WHITE);
                String text = String.valueOf(v.getId());
                g.drawString(text, x + (text.length() > 1 ? -7 : -3), y + 5);
            }

        }
    }
}

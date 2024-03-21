import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphPaperApp extends JFrame {
    public GraphPaperApp() {
        setTitle("Graph Paper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        
        GraphPaperPanel panel = new GraphPaperPanel();
        getContentPane().add(panel);
        
        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GraphPaperApp());
    }
}

class GraphPaperPanel extends JPanel {
    private Point startPoint;
    private Point endPoint;
    private GraphPoint centerPoint;

    public GraphPaperPanel() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                endPoint = e.getPoint();
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point mousePoint = e.getPoint();
                centerPoint = convertToGraphCoordinates(mousePoint);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGraphPaper(g);
        if (startPoint != null && endPoint != null) {
            g.setColor(Color.BLUE);
            g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        }
        if (centerPoint != null) {
            g.setColor(Color.RED);
            g.drawString("(" + centerPoint.getX() + ", " + centerPoint.getY() + ")", getWidth() / 2 + 5, getHeight() / 2 - 5);
        }
    }

    private GraphPoint convertToGraphCoordinates(Point point) {
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        
        double scaleX = (double) (point.x - centerX) / centerX;
        double scaleY = (double) (centerY - point.y) / centerY;
        
        return new GraphPoint(scaleX, scaleY);
    }
    
    private void drawGraphPaper(Graphics g) {
        // Draw grid lines
        int width = getWidth();
        int height = getHeight();
        int cellSize = Math.min(width, height) / 20; // Adjust grid size here
        int centerX = width / 2;
        int centerY = height / 2;
        
        g.setColor(Color.LIGHT_GRAY);
        
        // Draw horizontal grid lines
        for (int y = centerY - cellSize; y >= 0; y -= cellSize) {
            g.drawLine(0, y, width, y);
        }
        for (int y = centerY + cellSize; y <= height; y += cellSize) {
            g.drawLine(0, y, width, y);
        }
        
        // Draw vertical grid lines
        for (int x = centerX - cellSize; x >= 0; x -= cellSize) {
            g.drawLine(x, 0, x, height);
        }
        for (int x = centerX + cellSize; x <= width; x += cellSize) {
            g.drawLine(x, 0, x, height);
        }
        
        // Draw axes
        g.setColor(Color.BLACK);
        g.drawLine(0, centerY, width, centerY);
        g.drawLine(centerX, 0, centerX, height);
    }
}

class GraphPoint {
    private double x;
    private double y;

    public GraphPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

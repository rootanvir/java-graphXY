import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class graph extends JFrame {
    private GraphPaperPanel panel;
    private boolean fullscreen = false; // Initialize to false initially
    private boolean windowOpened = false; // Flag to track whether the window has been opened

    public graph() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new GraphPaperPanel();
        getContentPane().add(panel);

        // Register keyboard shortcut for toggling fullscreen
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_F) {
                    toggleFullscreen();
                } else if (e.getKeyCode() == KeyEvent.VK_P && !windowOpened) {
                    openNewWindow();
                    windowOpened = true;
                }
                return false;
            }
        });
    }

    private void toggleFullscreen() {
        if (!fullscreen) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            if (gd.isFullScreenSupported()) {
                gd.setFullScreenWindow(this);
                fullscreen = true;
            }
        } else {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            gd.setFullScreenWindow(null);
            fullscreen = false;
        }
        panel.repaint(); // Repaint panel to update graphics
    }

    private void openNewWindow() {
        JFrame newFrame = new JFrame("Developer Windows");
        String name="Tanvir Ahmed Tuhin ";
        JLabel label = new JLabel(name);
        newFrame.getContentPane().add(label);
        newFrame.setSize(300, 200);
        newFrame.setVisible(true);

        newFrame.addWindowListener(new WindowAdapter() {
        // 
           @Override
            public void windowClosed(WindowEvent e) {
                windowOpened = false;
            }
        });
        // Action listener for closing the new window
        newFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                windowOpened = false;
            }
        });
        windowOpened = true; // Set the flag to true after opening the window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            graph frame = new graph();
            frame.setVisible(true);
        });
    }
}

class GraphPaperPanel extends JPanel {
    private GraphPoint centerPoint;

    public GraphPaperPanel() {
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
        if (centerPoint != null) {
            g.setColor(Color.RED);
            g.drawString("(" + centerPoint.getX() + ", " + centerPoint.getY() + ")", getWidth() / 2 + 5,
                    getHeight() / 2 - 5);
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
        int cellSize = Math.min(width, height) / 40; // Adjust grid size here
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

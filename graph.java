import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class graph extends JFrame {

    private GraphPaperPanel panel;
    private boolean fullscreen = false; // Initialize to false initially
    private boolean windowOpened = false; // Flag to track whether the window has been opened
    private DotPositionWindow dotPositionWindow;

    public graph() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new GraphPaperPanel();
        getContentPane().add(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new FileHandle().deleteFile("pointer.txt");
            }
        });
        

        // Register keyboard shortcut for toggling fullscreen
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_F) {
                    toggleFullscreen();
                } else if (e.getKeyCode() == KeyEvent.VK_P && !windowOpened) {
                    openNewWindow();
                    windowOpened = true;
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    deleteLastDot();
                } else if (e.getKeyCode() == KeyEvent.VK_T) {
                    openDotPositionWindow();
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
        JFrame newFrame = new JFrame("Developer ");
        String name = "Tanvir Ahmed Tuhin ";
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

    private void openDotPositionWindow() {
        if (dotPositionWindow == null) {
            dotPositionWindow = new DotPositionWindow();
        }
        dotPositionWindow.updateDotPositions(panel.getDotPoints());
        dotPositionWindow.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            graph frame = new graph();
            frame.setVisible(true);
        });
    }

    private void deleteLastDot() {
        panel.removeLastDot();
    }
}

class GraphPaperPanel extends JPanel {
    private GraphPoint centerPoint;
    private List<Point> dotPoints = new ArrayList<>(); // List to store multiple dots

    public GraphPaperPanel() {
        setFocusable(true); // Set panel focusable to capture key events

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point mousePoint = e.getPoint();
                centerPoint = convertToGraphCoordinates(mousePoint);
                repaint();
            }
        });

        // Add mouse listener to handle double-click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addDot(e.getPoint());
                    new FileHandle().saveDoubleValues(centerPoint.getX(), centerPoint.getY());

                }
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
        for (Point dotPoint : dotPoints) {
            int dotSize = 5; // Size of the dot
            g.setColor(Color.RED);
            g.fillOval(dotPoint.x - dotSize / 2, dotPoint.y - dotSize / 2, dotSize, dotSize); // Draw the dot at the
                                                                                              // specified point
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

    public void addDot(Point point) {
        dotPoints.add(point);
        repaint(); // Repaint panel to update graphics
    }

    public void removeLastDot() {
        if (!dotPoints.isEmpty()) {
            //dotPoints.remove(dotPoints.size() - 1);
            dotPoints.clear();
            repaint(); // Repaint panel to update graphics
            new FileHandle().deleteFile("pointer.txt");
            new FileHandle().saveDoubleValues();;

        }
    }

    public List<Point> getDotPoints() {
        return dotPoints;
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

class DotPositionWindow extends JFrame {
    private JTextArea textArea;

    public DotPositionWindow() {
        setTitle("Dot Positions");
        setSize(500, 800);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setAlwaysOnTop(true);
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane);

        // Center the window on screen
        setLocationRelativeTo(null);
    }

    public void updateDotPositions(List<Point> dotPoints) {
        textArea.setText(""); // Clear the text area
        textArea.setEditable(false);
        String TextFromFile=new FileHandle().readWholeFile("pointer.txt");

        if (!dotPoints.isEmpty()) {
            // for (Point dotPoint : dotPoints) {
            //     textArea.append("(" + dotPoint.x + ", " + dotPoint.y + ")\n");
            // }
            textArea.append(TextFromFile);
            

        } else {
            textArea.append("No dot positions available.");
        }
    }
}
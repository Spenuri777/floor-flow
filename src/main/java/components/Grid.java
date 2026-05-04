package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

public class Grid extends JPanel implements ViewportListener {

    private ViewportState model;
    private Point dragStartPoint = null;
    private Point currentPoint = null;

    private Transformer transformer;

    private double leftStart;
    private double topStart;

    public Grid(ViewportState model) {
        this.model = model;
        model.addListener(this);

        transformer = new Transformer();

        init();
    }

    private void init() {
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int mx = e.getX();
                int my = e.getY();
                int w = getWidth();
                int h = getHeight();

                if(e.getWheelRotation() < 0) {
                    model.zoomIn(mx, my, w, h);
                } else {
                    model.zoomOut(mx, my, w, h);
                }
            }
        });

        MouseAdapter dragAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    dragStartPoint = e.getPoint();
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    double primaryOffset = model.getBASE_PIXELS_PER_METER() * model.getZoom();
                    transformer.screenToWorld(e.getPoint(), leftStart, topStart, primaryOffset);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStartPoint != null) {
                    double deltaX = e.getX() - dragStartPoint.getX();
                    double deltaY = e.getY() - dragStartPoint.getY();

                    model.addOffset(deltaX, deltaY);

                    dragStartPoint = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    dragStartPoint = null;
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };

        addMouseListener(dragAdapter);
        addMouseMotionListener(dragAdapter);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        double primaryOffset = model.getBASE_PIXELS_PER_METER() * model.getZoom();
        double secondaryOffset = primaryOffset * 0.1;
        double step = model.getPrimaryStep();

        if(primaryOffset < 1) return;

        double size = 100.0 * primaryOffset;
        leftStart = ((width - size) / 2.0) + model.getOffsetX();
        topStart = ((height - size) / 2.0) + model.getOffsetY();

//        System.out.println("Zoom from GRID: " + model.getZoom() + "x");
//        System.out.println("LeftStart: " + (width - size) / 2.0);

        // SECONDARY LINES
        g2d.setColor(new Color(230, 230, 230));
        for(double x = leftStart; x <= leftStart + size; x += secondaryOffset * step) {
            g2d.draw(new Line2D.Double(x, topStart, x, topStart + size));
        }
        for(double y = topStart; y <= topStart + size; y += secondaryOffset * step) {
            g2d.draw(new Line2D.Double(leftStart, y, leftStart + size, y));
        }

        // PRIMARY LINES
        g2d.setColor(new Color(200, 200, 200));
        for(double x = leftStart; x <= leftStart + size; x += primaryOffset * step) {
            g2d.draw(new Line2D.Double(x, topStart, x, topStart + size));
        }
        for(double y = topStart; y <= topStart + size; y += primaryOffset * step) {
            g2d.draw(new Line2D.Double(leftStart, y, leftStart + size, y));
        }

        // WORKSPACE BORDER
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(new Rectangle2D.Double(leftStart, topStart, size, size));
    }

    @Override
    public void onViewportChanged() {
        repaint();
    }
}
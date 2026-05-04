package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Grid extends JPanel implements ViewportListener {

    private ViewportState model;

    public Grid(ViewportState model) {
        this.model = model;
        model.addListener(this);
        init();
    }

    private void init() {
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getWheelRotation() < 0) {
                    model.zoomIn();
                } else {
                    model.zoomOut();
                }
            }
        });
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
        double leftStart = (width - size) / 2.0;
        double topStart = (height - size) / 2.0;

        // Rysowanie siatki pomocniczej (jaśniejszej)
        g2d.setColor(new Color(230, 230, 230));
        for(double x = leftStart; x <= leftStart + size; x += secondaryOffset * step) {
            g2d.draw(new Line2D.Double(x, topStart, x, topStart + size));
        }
        for(double y = topStart; y <= topStart + size; y += secondaryOffset * step) {
            g2d.draw(new Line2D.Double(leftStart, y, leftStart + size, y));
        }

        // Rysowanie siatki głównej
        g2d.setColor(new Color(200, 200, 200));
        for(double x = leftStart; x <= leftStart + size; x += primaryOffset * step) {
            g2d.draw(new Line2D.Double(x, topStart, x, topStart + size));
        }
        for(double y = topStart; y <= topStart + size; y += primaryOffset * step) {
            g2d.draw(new Line2D.Double(leftStart, y, leftStart + size, y));
        }

        // Czerwona ramka obszaru roboczego
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(new Rectangle2D.Double(leftStart, topStart, size, size));
    }

    @Override
    public void onViewportChanged() {
        repaint();
    }
}
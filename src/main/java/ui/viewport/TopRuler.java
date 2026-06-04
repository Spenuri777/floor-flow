package ui.viewport;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class TopRuler extends JPanel implements ViewportListener {
    private ViewportState model;

    public TopRuler(ViewportState model) {
        this.model = model;
        model.addListener(this);
        setBackground(new Color(243, 243, 243));
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

        if (primaryOffset < 1) return;

        double size = 100.0 * primaryOffset;
        double leftStart = (width - size) / 2.0 + model.getOffsetX();

        g2d.setColor(Color.GRAY);
        for (double x = leftStart; x <= leftStart + size; x += secondaryOffset * step) {
            g2d.draw(new Line2D.Double(x, 0, x, height - 25));
        }

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));

        for (double x = leftStart; x <= leftStart + size; x += primaryOffset * step) {
            g2d.draw(new Line2D.Double(x, 0, x, height - 15));

            String text = Math.round((x - leftStart) / primaryOffset) + "m";
            g2d.drawString(text, (float) x - 5, height - 4);
        }

        g2d.draw(new Line2D.Double(0, 0, width, 0));
    }

    @Override
    public void onViewportChanged() {
        repaint();
    }
}
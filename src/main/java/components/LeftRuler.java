package components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class LeftRuler extends JPanel implements ViewportListener {
    private ViewportState model;

    public LeftRuler (ViewportState model) {
        this.model = model;
        model.addListener(this);
        setBackground(new Color(245, 245, 245));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth(); // Wynosi 30px dzięki MigLayout
        int height = getHeight();

        double primaryOffset = model.getBASE_PIXELS_PER_METER() * model.getZoom();
        double secondaryOffset = primaryOffset * 0.1;
        double step = model.getPrimaryStep();

        if(primaryOffset < 1) return;

        double size = 100.0 * primaryOffset;
        double topStart = (height - size) / 2.0;

        // Kreski pomocnicze (krótkie)
        g2d.setColor(Color.GRAY);
        for(double y = topStart; y <= topStart + size; y += secondaryOffset * step) {
            g2d.draw(new Line2D.Double(width - 5, y, width, y));
        }

        // Kreski główne (dłuższe) i tekst
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));

        for(double y = topStart; y <= topStart + size; y += primaryOffset * step) {
            g2d.draw(new Line2D.Double(width - 12, y, width, y));

            String text = Math.round((y - topStart) / primaryOffset) + "m";
            // Obracamy tekst i dostosowujemy pozycję, by pasował po lewej
            g2d.drawString(text, 2, (float) y - 3);
        }

        // Prawa linia odcinająca miarkę od siatki
        g2d.draw(new Line2D.Double(width - 1, 0, width - 1, height));
    }

    @Override
    public void onViewportChanged() {
        repaint();
    }
}
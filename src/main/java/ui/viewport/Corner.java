package ui.viewport;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class Corner extends JPanel {

    public Corner() {
        init();
    }

    private void init() {
        setBackground(new Color(243, 243, 243));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        g2d.draw(new Line2D.Double(0, 0, width, 0));
        g2d.draw(new Line2D.Double(0, 0, 0, height));
    }

}

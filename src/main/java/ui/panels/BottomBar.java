package ui.panels;

import net.miginfocom.swing.MigLayout;
import ui.viewport.ViewportListener;
import ui.viewport.ViewportState;

import javax.swing.*;
import java.awt.*;

public class BottomBar extends JPanel implements ViewportListener {

    private ViewportState model;

    private JLabel statusText;
    private JLabel lengthText;
    private JLabel coordText;
    private JLabel zoomText;

    public BottomBar(ViewportState model) {
        this.model = model;
        model.addListener(this);

        init();
    }

    private void init() {
        setLayout(new MigLayout("insets 6", "[grow][][][]", "[]"));

        statusText = new JLabel(model.getContextHelp());
        lengthText = new JLabel(model.getDrawingLength());
        coordText = new JLabel(model.getCurrentPoint());
        zoomText = new JLabel(String.format("%.0f%%", model.getZoom() * 100));

        add(statusText, "growx");
        add(lengthText, "gap 10");
        add(coordText, "gap 10");
        add(zoomText, "gap 10");

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth(), h = getHeight();
        Color color1 = new Color(209, 223, 237);
        Color color2 = new Color(235, 245, 252);
        GradientPaint gp = new GradientPaint((float) getWidth() / 2, 0, color1, (float) getWidth() / 2, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);

        g2d.setColor(Color.BLACK);
        g2d.drawLine(0, 0, getWidth(), 0);
    }

    @Override
    public void onViewportChanged() {
        if (statusText != null) {
            statusText.setText(model.getContextHelp());
        }

        if (zoomText != null) {
            zoomText.setText(String.format("%.0f%%", model.getZoom() * 100));
        }

        if (coordText != null) {
            coordText.setText(model.getCurrentPoint());
        }

        if (lengthText != null) {
            lengthText.setText(model.getDrawingLength());
        }

        repaint();
    }
}

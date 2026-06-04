package ui.panels;

import net.miginfocom.swing.MigLayout;
import ui.viewport.ViewportListener;
import ui.viewport.ViewportState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class Furnitures extends JPanel implements ViewportListener {

    private ViewportState model;

    public Furnitures(ViewportState model) {
        this.model = model;
        model.addListener(this);

        init();
    }

    private void init() {
        setLayout(new MigLayout("filly, insets 7, gap 7"));

        JButton tablesButton = generateButton("table", "Stół");
        JButton chairButton = generateButton("chair", "Krzesło");
        JButton bedButton = generateButton("bed", "Łóżko");

        add(tablesButton, "w 90!, grow");
        add(chairButton, "w 90!, grow");
        add(bedButton, "w 90!, grow");

        tablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setMouseDrawing(false, "");
                model.setMouseDrawing(true, "TABLE");
            }
        });

        chairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setMouseDrawing(false, "");
                model.setMouseDrawing(true, "CHAIR");
            }
        });

        bedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setMouseDrawing(false, "");
                model.setMouseDrawing(true, "BED");
            }
        });


    }

    private JButton generateButton(String imageName, String text) {
        URL imageUrl = getClass().getResource("/" + imageName + ".png");
        ImageIcon icon = new ImageIcon(imageUrl);
        JButton button = new JButton(text.substring(0, 1).toUpperCase() + text.substring(1), icon);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

        return button;
    }

    @Override
    public void onViewportChanged() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth(), h = getHeight();
        Color color1 = new Color(235, 245, 252);
        Color color2 = new Color(209, 223, 237);
        GradientPaint gp = new GradientPaint((float) getWidth() / 2, 0, color1, (float) getWidth() / 2, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }

}

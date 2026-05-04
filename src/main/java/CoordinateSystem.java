import com.formdev.flatlaf.themes.FlatMacLightLaf;
import components.Grid;
import components.LeftRuler;
import components.TopRuler;
import components.ViewportState;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class CoordinateSystem extends JPanel {
    ViewportState model = new ViewportState();

    public CoordinateSystem () {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill, insets 0, gap 0",
                "[30!]0[grow]",
                "[30!]0[grow]"
        ));

        JPanel corner = new JPanel();
        corner.setBackground(new Color(245, 245, 245));
        add(corner, "width 30!, height 30!");

        TopRuler topRuler = new TopRuler(model);
        add(topRuler, "growx, wrap, height 30!");

        LeftRuler leftRuler = new LeftRuler(model);
        add(leftRuler, "growy, width 30!");

        Grid gridPanel = new Grid(model);
        add(gridPanel, "grow");
    }
}

package engine;

import net.miginfocom.swing.MigLayout;
import ui.viewport.*;

import javax.swing.*;

public class CoordinateSystem extends JPanel {
    private final ViewportState model;

    public CoordinateSystem(ViewportState model) {
        this.model = model;
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill, insets 0, gap 0",
                "[30!]0[grow]",
                "[30!]0[grow]"
        ));

        Corner corner = new Corner();
        add(corner, "width 30!, height 30!");

        TopRuler topRuler = new TopRuler(this.model);
        add(topRuler, "growx, wrap, height 30!");

        LeftRuler leftRuler = new LeftRuler(this.model);
        add(leftRuler, "growy, width 30!");

        Grid gridPanel = new Grid(this.model);
        add(gridPanel, "grow");
    }
}

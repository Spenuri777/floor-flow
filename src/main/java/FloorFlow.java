import components.Navigation;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class FloorFlow extends JFrame {

    public FloorFlow() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill, insets 0, gap 0", "[grow]0[350!]", "[150!]0[grow]0[30!]"));

        JPanel rightSidebar = new JPanel();
        rightSidebar.setBackground(new Color(45, 45, 45));
        add(rightSidebar, "cell 1 0 1 3, grow");

        Navigation navigation = new Navigation();
        add(navigation, "cell 0 0, grow");

        CoordinateSystem coordinateSystem = new CoordinateSystem();
        add(coordinateSystem, "cell 0 1, grow");

        JPanel bottomBar = new JPanel();
        bottomBar.setBackground(new Color(240, 240, 240));
        add(bottomBar, "cell 0 2, grow");
    }
}

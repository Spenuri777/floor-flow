package components;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class Navigation extends JTabbedPane {

    public Navigation() {
        init();
    }

    private void init() {
        JPanel page1 = new JPanel();
        page1.add(new JLabel("This is Tab 1"));

        // Create the second tab (page2) and add a JLabel to it
        Components components = new Components();

        addTab("Plik", page1);
        addTab("Komponenty", components);

        setBackground(new Color(230, 230, 230));
        putClientProperty(FlatClientProperties.STYLE, "" +
                "border: 0,0,1,0,$Component.borderColor");
    }

}

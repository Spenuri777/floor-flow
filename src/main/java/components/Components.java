package components;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class Components extends JPanel {

    public Components() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("filly, insets 5, gap 5"));

        JButton wallButton = new JButton("Ściana");
        JButton windowButton = new JButton("Okno");
        JButton doorButton = new JButton("Drzwi");
        JButton stairsButton = new JButton("Schody");

        add(wallButton, "w 90!, grow");
        add(windowButton, "w 90!, grow");
        add(doorButton, "w 90!, grow");
        add(stairsButton, "w 90!, grow");
    }

}

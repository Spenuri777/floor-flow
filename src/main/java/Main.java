import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        CoordinateSystem cs = new CoordinateSystem();
        cs.setVisible(true);
        cs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cs.setSize(1920, 1080);
        cs.setLocationRelativeTo(null);
    }
}

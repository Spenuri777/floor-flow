import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        FloorFlow floorFlow = new FloorFlow();
        floorFlow.setVisible(true);
        floorFlow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        floorFlow.setSize(1920, 1080);
        floorFlow.setLocationRelativeTo(null);
    }
}

import api.ApiClient;
import ui.LoginWindow;   
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        FlatLaf.registerCustomDefaultsSource("themes");

        ApiClient apiClient = new ApiClient();

        SwingUtilities.invokeLater(() -> {
            LoginWindow loginWindow = new LoginWindow(apiClient);
            loginWindow.setVisible(true);
        });
    }
}
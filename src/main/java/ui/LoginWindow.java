package ui;

import api.ApiClient;
import api.dto.UserDTO;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import ui.viewport.ViewportState;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoginWindow extends JFrame {

    private final ViewportState model = new ViewportState();
    private final ApiClient apiClient;

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton loginButton;

    public LoginWindow(ApiClient apiClient) {
        this.apiClient = apiClient;

        setTitle("FloorFlow | Logowanie");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        init();
    }

    private void init() {
        setSize(1280, 720);
        
        setLayout(new MigLayout("fill, insets 20", "[center]", "[][100!]20[]"));

        
        JLabel appTitle = new JLabel("FloorFlow");
        appTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font: bold +25;");
        add(appTitle, "wrap");

        
        URL imageUrl = getClass().getResource("/logo.png");
        if (imageUrl != null) {
            ImageIcon tempIcon = new ImageIcon(imageUrl);
            Image scaledImage = tempIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon finalIcon = new ImageIcon(scaledImage);
            add(new JLabel(finalIcon), "wrap, w 100!, h 100!");
        }

        
        txtEmail = new JTextField("jankowalski@mail.com");
        txtPassword = new JPasswordField("Haslo1234");
        loginButton = new JButton("Zaloguj się");

        
        String fieldStyle = "arc:10; margin: 4,6,4,6;";
        txtEmail.putClientProperty(FlatClientProperties.STYLE, fieldStyle);
        txtPassword.putClientProperty(FlatClientProperties.STYLE, fieldStyle);
        loginButton.putClientProperty(FlatClientProperties.STYLE, "arc:10; margin: 4,6,4,6;");

        
        JPanel panel = new JPanel(new MigLayout("wrap, fillx, insets 35 45 30 45", "fill, 280:320"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "background: darken(@background,3%)"); 

        JLabel lbTitle = new JLabel("Witaj ponownie!");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font: bold +10");

        JLabel description = new JLabel("Zaloguj się, aby zarządzać swoimi projektami.");
        description.putClientProperty(FlatClientProperties.STYLE, "font: -2; foreground: #888888");

        panel.add(lbTitle);
        panel.add(description, "gapbottom 15");

        panel.add(new JLabel("Adres email"), "gapy 8");
        panel.add(txtEmail);

        panel.add(new JLabel("Hasło"), "gapy 8");
        panel.add(txtPassword);

        panel.add(loginButton, "gapy 20");

        add(panel);

        loginButton.addActionListener(e -> {
            loginButton.setEnabled(false);
            loginButton.setText("Logowanie...");

            String email = txtEmail.getText();
            String password = new String(txtPassword.getPassword());

            SwingWorker<UserDTO, Void> worker = new SwingWorker<>() {
                @Override
                protected UserDTO doInBackground() throws Exception {
                    return apiClient.login(email, password);
                }

                @Override
                protected void done() {
                    try {
                        UserDTO user = get();
                        JOptionPane.showMessageDialog(LoginWindow.this, "Zalogowano pomyślnie!");
                        model.setUserId(user.id());

                        new ProjectListWindow(model, apiClient, user.id()).setVisible(true);
                        dispose();

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(LoginWindow.this, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                        loginButton.setEnabled(true);
                        loginButton.setText("Zaloguj się");
                    }
                }
            };
            worker.execute();
        });
    }


}
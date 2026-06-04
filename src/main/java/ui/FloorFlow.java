package ui;

import api.ApiClient;
import com.formdev.flatlaf.FlatClientProperties;
import ui.panels.BottomBar;
import ui.panels.Navigation;
import ui.panels.Sidebar;
import ui.viewport.ViewportState;
import engine.CoordinateSystem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FloorFlow extends JFrame {

    private final ViewportState model;
    private ApiClient apiClient;

    public FloorFlow(ViewportState model, ApiClient apiClient) {
        this.model = model;
        this.apiClient = apiClient;
        init();
        setupSaveShortcut();
    }

    private void init() {
        setTitle("FloorFlow");
        setLayout(new MigLayout("fill, insets 0, gap 0", "[grow]0[350!]", "[150!]0[grow]0[30!]"));

        Sidebar sidebar = new Sidebar(this.model);
        add(sidebar, "cell 1 0 1 3, grow");

        Navigation navigation = new Navigation(this.model);
        add(navigation, "cell 0 0, grow");

        CoordinateSystem coordinateSystem = new CoordinateSystem(this.model);
        add(coordinateSystem, "cell 0 1, grow");

        BottomBar bottomBar = new BottomBar(this.model);
        add(bottomBar, "cell 0 2, grow");

        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
    }

    private void setupSaveShortcut() {
        KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(saveKeyStroke, "SAVE_PROJECT");
        getRootPane().getActionMap().put("SAVE_PROJECT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProjectAsync();
            }
        });
    }

    private void saveProjectAsync() {
        String jsonData = model.exportToJson();

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                apiClient.updateProject(model.getProjectId(), model.getProjectName(), jsonData);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(FloorFlow.this,
                            "Błąd zapisu projektu: " + ex.getMessage(),
                            "Błąd Zapisu",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
}
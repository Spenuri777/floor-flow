package ui.panels;

import api.ApiClient;
import api.dto.ProjectFullDTO;
import api.dto.ProjectSummaryDTO;
import net.miginfocom.swing.MigLayout;
import ui.viewport.ViewportListener;
import ui.viewport.ViewportState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

public class File extends JPanel implements ViewportListener {
    private ViewportState model;
    private ApiClient apiClient = new ApiClient();

    public File(ViewportState model) {
        this.model = model;
        model.addListener(this);

        init();
    }

    private void init() {
        setLayout(new MigLayout("filly, insets 7, gap 7"));

        JButton saveButton = generateButton("save", "Zapisz");
        JButton openButton = generateButton("open", "Otwórz");

        add(saveButton, "w 90!, grow");
        add(openButton, "w 90!, grow");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProjectAsync();
            }
        });

        
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchProjectsAndShowDialog(openButton);
            }
        });
    }

    private JButton generateButton(String imageName, String text) {
        URL imageUrl = getClass().getResource("/" + imageName + ".png");
        ImageIcon icon = new ImageIcon(imageUrl);
        JButton button = new JButton(text.substring(0, 1).toUpperCase() + text.substring(1), icon);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

        return button;
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
                    JOptionPane.showMessageDialog(File.this,
                            "Błąd zapisu projektu: " + ex.getMessage(),
                            "Błąd Zapisu",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    

    private void fetchProjectsAndShowDialog(JButton openBtn) {
        String userId = model.getUserId();
        if (userId == null || userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Brak ID użytkownika. Zaloguj się ponownie.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        openBtn.setEnabled(false);
        openBtn.setText("Ładowanie...");

        SwingWorker<List<ProjectSummaryDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<ProjectSummaryDTO> doInBackground() throws Exception {
                return apiClient.getUserProjects(userId);
            }

            @Override
            protected void done() {
                openBtn.setEnabled(true);
                openBtn.setText("Otwórz");
                try {
                    List<ProjectSummaryDTO> projects = get();
                    showSelectionDialog(projects);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(File.this, "Nie udało się pobrać listy projektów: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void showSelectionDialog(List<ProjectSummaryDTO> projects) {
        if (projects.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nie masz jeszcze żadnych zapisanych projektów.", "Brak projektów", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        
        JList<ProjectSummaryDTO> list = new JList<>(new java.util.Vector<>(projects));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        
        int result = JOptionPane.showConfirmDialog(
                SwingUtilities.getWindowAncestor(this),
                scrollPane,
                "Wybierz projekt do otwarcia",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        
        if (result == JOptionPane.OK_OPTION) {
            ProjectSummaryDTO selected = list.getSelectedValue();
            if (selected != null) {
                loadFullProject(selected.id());
            }
        }
    }

    private void loadFullProject(String projectId) {
        SwingWorker<ProjectFullDTO, Void> worker = new SwingWorker<>() {
            @Override
            protected ProjectFullDTO doInBackground() throws Exception {
                return apiClient.getProject(projectId);
            }

            @Override
            protected void done() {
                try {
                    ProjectFullDTO fullProject = get();

                    
                    model.setProjectId(fullProject.id());
                    model.setProjectName(fullProject.name());

                    if (fullProject.data() != null && !fullProject.data().isEmpty()) {
                        model.importFromJson(fullProject.data());
                    }

                    
                    Window window = SwingUtilities.getWindowAncestor(File.this);
                    if (window instanceof JFrame) {
                        ((JFrame) window).setTitle("FloorFlow - " + fullProject.name());
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(File.this, "Błąd podczas wczytywania projektu: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    @Override
    public void onViewportChanged() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth(), h = getHeight();
        Color color1 = new Color(235, 245, 252);
        Color color2 = new Color(209, 223, 237);
        GradientPaint gp = new GradientPaint((float) getWidth() / 2, 0, color1, (float) getWidth() / 2, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}
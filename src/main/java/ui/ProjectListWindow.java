package ui;

import api.ApiClient;
import api.dto.ProjectFullDTO;
import api.dto.ProjectSummaryDTO;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import ui.viewport.ViewportState;

import javax.swing.*;
import java.util.List;

public class ProjectListWindow extends JFrame {

    private ViewportState model;
    private final ApiClient apiClient;
    private final String userId;

    private DefaultListModel<ProjectSummaryDTO> listModel;
    private JList<ProjectSummaryDTO> projectList;

    public ProjectListWindow(ViewportState model, ApiClient apiClient, String userId) {
        this.model = model;
        this.apiClient = apiClient;
        this.userId = userId;

        setTitle("FloorFlow | Twoje Projekty");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadProjectsAsync();
    }

    private void initUI() {
        setLayout(new MigLayout("wrap 1, insets 20", "[grow, fill]", "[][grow, fill][]"));

        JLabel lbTitle = new JLabel("Wybierz projekt z listy:");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font: bold +10");

        add(lbTitle);

        listModel = new DefaultListModel<>();
        projectList = new JList<>(listModel);
        add(new JScrollPane(projectList), "grow");

        
        JButton newProjectBtn = new JButton("Nowy projekt");
        JButton openButton = new JButton("Otwórz wybrany");
        JButton deleteButton = new JButton("Usuń wybrany");

        
        add(newProjectBtn, "split 3, center, gaptop 10");
        add(openButton, "gaptop 10");
        add(deleteButton, "gaptop 10");

        
        openButton.addActionListener(e -> {
            ProjectSummaryDTO selected = projectList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Wybierz projekt z listy!");
                return;
            }
            openProjectAsync(selected.id());
        });

        
        newProjectBtn.addActionListener(e -> {
            String projectName = JOptionPane.showInputDialog(this, "Podaj nazwę nowego projektu:", "Nowy projekt", JOptionPane.PLAIN_MESSAGE);

            
            if (projectName != null && !projectName.trim().isEmpty()) {
                createProjectAsync(projectName.trim());
            }
        });

        deleteButton.addActionListener(e -> {
            ProjectSummaryDTO selected = projectList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Wybierz projekt do usunięcia!");
                return;
            }
            deleteProjectAsync(selected.id());
        });
    }

    private void loadProjectsAsync() {
        SwingWorker<List<ProjectSummaryDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<ProjectSummaryDTO> doInBackground() throws Exception {
                return apiClient.getUserProjects(userId);
            }

            @Override
            protected void done() {
                try {
                    List<ProjectSummaryDTO> projects = get();
                    listModel.clear();
                    for (ProjectSummaryDTO p : projects) {
                        listModel.addElement(p);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ProjectListWindow.this, "Nie udało się pobrać projektów.");
                }
            }
        };
        worker.execute();
    }

    private void openProjectAsync(String projectId) {
        
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

                    FloorFlow floorFlow = new FloorFlow(model, apiClient);
                    floorFlow.setTitle("FloorFlow - " + fullProject.name());

                    floorFlow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    floorFlow.setSize(1920, 1080);
                    floorFlow.setLocationRelativeTo(null);

                    floorFlow.setVisible(true);
                    dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ProjectListWindow.this, "Błąd podczas otwierania: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    
    private void createProjectAsync(String projectName) {
        SwingWorker<ProjectFullDTO, Void> worker = new SwingWorker<>() {
            @Override
            protected ProjectFullDTO doInBackground() throws Exception {
                return apiClient.createProject(userId, projectName);
            }

            @Override
            protected void done() {
                try {
                    ProjectFullDTO newProject = get();

                    
                    model.setProjectId(newProject.id());
                    model.setProjectName(newProject.name());

                    
                    FloorFlow floorFlow = new FloorFlow(model, apiClient);
                    floorFlow.setTitle("FloorFlow - " + newProject.name());

                    floorFlow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    floorFlow.setSize(1920, 1080);
                    floorFlow.setLocationRelativeTo(null);

                    floorFlow.setVisible(true);
                    dispose(); 

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ProjectListWindow.this, "Błąd podczas tworzenia projektu: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void deleteProjectAsync(String projectId) {
        
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Czy na pewno chcesz trwale usunąć ten projekt?",
                "Potwierdź usunięcie",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                apiClient.deleteProject(projectId);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); 
                    JOptionPane.showMessageDialog(ProjectListWindow.this, "Projekt został usunięty.");

                    
                    loadProjectsAsync();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ProjectListWindow.this,
                            "Błąd podczas usuwania: " + ex.getMessage(),
                            "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
}
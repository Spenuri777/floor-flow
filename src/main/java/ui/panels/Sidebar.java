package ui.panels;

import model.annotations.Ruler;
import model.annotations.Text;
import model.components.Stairs;
import model.components.Window;
import model.components.Door;
import model.components.Wall;
import model.furniture.Bed;
import model.furniture.Chair;
import model.furniture.Table;
import net.miginfocom.swing.MigLayout;
import ui.viewport.ViewportListener;
import ui.viewport.ViewportState;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Sidebar extends JPanel implements ViewportListener {

    private final ViewportState model;
    private DefaultListModel<Object> listModel;
    private JList<Object> layerList;

    private PropertiesPanel propertiesPanel;
    private JButton deleteButton;

    public Sidebar(ViewportState model) {
        this.model = model;
        this.model.addListener(this);

        init();
    }

    private void init() {
        setLayout(new MigLayout("insets 20, wrap 1", "[grow, fill]", ""));

        JLabel propertiesLabel = new JLabel("Właściwości obiektu");
        add(propertiesLabel);

        propertiesPanel = new PropertiesPanel(model);
        propertiesPanel.setOpaque(false);
        add(propertiesPanel, "grow, pushy");

        JLabel objectsLabel = new JLabel("Obiekty");
        add(objectsLabel, "gaptop 15");

        listModel = new DefaultListModel<>();
        layerList = new JList<>(listModel);
        layerList.setCellRenderer(new LayerCellRenderer());

        layerList.putClientProperty("List.cellFocusColor", new Color(0, 0, 0, 0));
        layerList.setSelectionBackground(new Color(209, 223, 237));
        layerList.setSelectionForeground(Color.BLACK);

        deleteButton = new JButton();
        deleteButton.setToolTipText("Usuń zaznaczony obiekt");

        URL iconUrl = getClass().getResource("/x.png");
        if (iconUrl != null) {
            Image img = new ImageIcon(iconUrl).getImage();
            deleteButton.setIcon(new ImageIcon(img.getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        }

        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> {
            int selectedIndex = layerList.getSelectedIndex();
            if (selectedIndex != -1) {
                Object selectedObj = model.getAllElements().get(selectedIndex);
                model.removeElement(selectedObj);
                propertiesPanel.setElement(null);
            }
        });

        layerList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = layerList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Object selectedObj = model.getAllElements().get(selectedIndex);
                    propertiesPanel.setElement(selectedObj);

                    deleteButton.setEnabled(true);
                } else {
                    propertiesPanel.setElement(null);
                    deleteButton.setEnabled(false);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(layerList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        add(scrollPane, "grow, pushy");

        add(deleteButton, "gaptop 5, right, width 30!, height 30!");

        updateList();
    }

    private void updateList() {
        int selected = layerList.getSelectedIndex();

        listModel.clear();
        for (Object obj : model.getAllElements()) {
            listModel.addElement(obj);
        }

        if (selected >= 0 && selected < listModel.size()) {
            layerList.setSelectedIndex(selected);
        }

        if (selected >= 0 && selected < listModel.size()) {
            layerList.setSelectedIndex(selected);
        } else if (listModel.size() == 0) {
            propertiesPanel.setElement(null);
            deleteButton.setEnabled(false);
        }
    }

    @Override
    public void onViewportChanged() {
        updateList();
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

        g2d.setColor(Color.BLACK);
        g2d.drawLine(0, 0, 0, getHeight());
    }

    private class LayerCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            if (value instanceof Wall) {
                label.setText("Ściana [" + index + "]");
                label.setIcon(loadIcon("/wall.png"));
            } else if (value instanceof Window) {
                label.setText("Okno [" + index + "]");
                label.setIcon(loadIcon("/window.png"));
            } else if (value instanceof Door) {
                label.setText("Drzwi [" + index + "]");
                label.setIcon(loadIcon("/door.png"));
            } else if (value instanceof Stairs) {
                label.setText("Schody [" + index + "]");
                label.setIcon(loadIcon("/stairs.png"));
            } else if (value instanceof Ruler) {
                label.setText("Miarka [" + index + "]");
                label.setIcon(loadIcon("/ruler.png"));
            } else if (value instanceof Text) {
                label.setText(((Text) value).getText());
                label.setIcon(loadIcon("/text.png"));
            } else if (value instanceof Table) {
                label.setText("Stół [" + index + "]");
                label.setIcon(loadIcon("/table.png"));
            } else if (value instanceof Chair) {
                label.setText("Krzesło [" + index + "]");
                label.setIcon(loadIcon("/chair.png"));
            } else if (value instanceof Bed) {
                label.setText("Łóżko [" + index + "]");
                label.setIcon(loadIcon("/bed.png"));
            } else {
                label.setText("Obiekt [" + index + "]");
            }

            return label;
        }

        private ImageIcon loadIcon(String path) {
            URL imgUrl = getClass().getResource(path);
            if (imgUrl != null) {
                Image img = new ImageIcon(imgUrl).getImage();
                return new ImageIcon(img.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            }
            return null;
        }
    }
}
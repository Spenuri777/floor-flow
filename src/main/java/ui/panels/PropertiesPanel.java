package ui.panels;

import model.annotations.Text;
import model.components.Door;
import model.components.Stairs;
import model.core.LinearElement;
import model.core.PlacedElement;
import model.core.PlanElement;
import net.miginfocom.swing.MigLayout;
import ui.viewport.ViewportState;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PropertiesPanel extends JPanel {

    private final ViewportState model;

    public PropertiesPanel(ViewportState model) {
        this.model = model;
        setOpaque(false);
        setLayout(new MigLayout("wrap 2, insets 0", "[][grow, fill]"));
        showEmpty();
    }

    public void showEmpty() {
        removeAll();
        add(new JLabel(""), "span 2, align center");
        revalidate();
        repaint();
    }

    public void setElement(Object obj) {
        removeAll();

        if (!(obj instanceof PlanElement)) {
            showEmpty();
            return;
        }

        PlanElement element = (PlanElement) obj;

        add(new JLabel("Widoczność:"));
        JCheckBox visibleCheck = new JCheckBox("", element.isVisible());
        visibleCheck.setOpaque(false);
        visibleCheck.addActionListener(e -> {
            element.setVisible(visibleCheck.isSelected());
            model.notifyListeners();
        });
        add(visibleCheck);

        if (element instanceof LinearElement) {
            LinearElement linear = (LinearElement) element;

            add(new JLabel("Start:"));
            add(new JLabel(String.format("X: %.2f m, Y: %.2f m", linear.getStart().x, linear.getStart().y)));

            add(new JLabel("Koniec:"));
            add(new JLabel(String.format("X: %.2f m, Y: %.2f m", linear.getEnd().x, linear.getEnd().y)));

            add(new JLabel("Grubość (m):"));
            JSpinner thickSpinner = new JSpinner(new SpinnerNumberModel(linear.getThickness(), 0.0, 2.0, 0.05));
            thickSpinner.addChangeListener(e -> {
                linear.setThickness((Double) thickSpinner.getValue());
                model.notifyListeners();
            });
            add(thickSpinner);
        }

        if (element instanceof PlacedElement) {
            PlacedElement placed = (PlacedElement) element;

            add(new JLabel("Pozycja:"));
            add(new JLabel(String.format("X: %.2f m, Y: %.2f m", placed.getPlace().x, placed.getPlace().y)));

            add(new JLabel("Szerokość (m):"));
            JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(placed.getWidth(), 0.1, 20.0, 0.1));
            widthSpinner.addChangeListener(e -> {
                placed.setWidth((Double) widthSpinner.getValue());
                model.notifyListeners();
            });
            add(widthSpinner);

            add(new JLabel("Długość (m):"));
            JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(placed.getHeight(), 0.1, 20.0, 0.1));
            heightSpinner.addChangeListener(e -> {
                placed.setHeight((Double) heightSpinner.getValue());
                model.notifyListeners();
            });
            add(heightSpinner);
        }

        if (element instanceof Door) {
            Door door = (Door) element;
            add(new JLabel("Kąt otw.:"));
            JSpinner angleSpinner = new JSpinner(new SpinnerNumberModel(door.getOpenAngle(), 10, 180, 5));
            angleSpinner.addChangeListener(e -> {
                door.setOpenAngle((Integer) angleSpinner.getValue());
                model.notifyListeners();
            });
            add(angleSpinner);

            add(new JLabel("Odwróć skrz.:"));
            JCheckBox sideCheck = new JCheckBox("", door.getSide());
            sideCheck.setOpaque(false);
            sideCheck.addActionListener(e -> {
                door.setSide(sideCheck.isSelected());
                model.notifyListeners();
            });
            add(sideCheck);

        } else if (element instanceof Stairs) {
            Stairs stairs = (Stairs) element;
            add(new JLabel("Stopnie:"));
            JSpinner stepsSpinner = new JSpinner(new SpinnerNumberModel(stairs.getStepsCount(), 2, 50, 1));
            stepsSpinner.addChangeListener(e -> {
                stairs.setStepsCount((Integer) stepsSpinner.getValue());
                model.notifyListeners();
            });
            add(stepsSpinner);

            add(new JLabel("Pionowe:"));
            JCheckBox dirCheck = new JCheckBox("", stairs.getDirection());
            dirCheck.setOpaque(false);
            dirCheck.addActionListener(e -> {
                stairs.setDirection(dirCheck.isSelected());
                model.notifyListeners();
            });
            add(dirCheck);

        } else if (element instanceof Text) {
            Text text = (Text) element;

            add(new JLabel("Pozycja:"));
            add(new JLabel(String.format("X: %.2f m, Y: %.2f m", text.getPlace().x, text.getPlace().y)));

            add(new JLabel("Napis:"));
            JTextField textField = new JTextField(text.getText());
            textField.getDocument().addDocumentListener(new DocumentListener() {
                private void update() {
                    text.setText(textField.getText());
                    model.notifyListeners();
                }

                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                public void changedUpdate(DocumentEvent e) {
                    update();
                }
            });
            add(textField);
        }

        revalidate();
        repaint();
    }
}
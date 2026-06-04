package ui.panels;

import com.formdev.flatlaf.FlatClientProperties;
import ui.viewport.ViewportState;

import javax.swing.*;

public class Navigation extends JTabbedPane {

    private ViewportState model;

    public Navigation(ViewportState model) {
        this.model = model;

        init();
    }

    private void init() {
        File file = new File(this.model);
        Components components = new Components(this.model);
        Furnitures furnitures = new Furnitures(this.model);
        Annotation annotation = new Annotation(this.model);

        addTab("Plik", file);
        addTab("Komponenty", components);
        addTab("Meble", furnitures);
        addTab("Adnotacje", annotation);

        putClientProperty(FlatClientProperties.STYLE,
                "border: 0,0,1,0,#EBF5FC;" +
                        "hoverColor: #EBF5FC;" +
                        "tabArc: 0;" +
                        "tabSelectionArc: 0;" +
                        "cardTabArc: 0;" +
                        "buttonArc: 0;" +
                        "background: #861B21;" +
                        "selectedBackground: #EBF5FC;" +
                        "underlineColor: #00000000;" +
                        "foreground: #FFFFFF;" +
                        "selectedForeground: #000000;" +
                        "hoverForeground: #000000;" +
                        "tabsOpaque: true;" +
                        "opaque: true;" +
                        "hasFullBorder: false;" +
                        "contentAreaColor: #EBF5FC"
        );
    }
}
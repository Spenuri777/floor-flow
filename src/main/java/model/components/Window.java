package model.components;

import model.core.LinearElement;

import java.awt.geom.Point2D;

public class Window extends LinearElement {
    public Window() {
        super();
    }

    public Window(Point2D.Double start, Point2D.Double end) {
        super(start, end, 0.25);
        setName("Okno");
    }
}
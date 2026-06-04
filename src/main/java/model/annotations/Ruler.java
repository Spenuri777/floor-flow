package model.annotations;

import model.core.LinearElement;

import java.awt.geom.Point2D;

public class Ruler extends LinearElement {

    public Ruler() {
        super();
    }

    public Ruler(Point2D.Double start, Point2D.Double end) {
        super(start, end, 0.0);
        setName("Wymiar");
    }
}
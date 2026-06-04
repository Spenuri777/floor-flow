package model.furniture;

import model.core.PlacedElement;

import java.awt.geom.Point2D;

public class Table extends PlacedElement {

    public Table() {
        super();
    }

    public Table(Point2D.Double place, boolean direction) {
        super(place, 1.5, 0.9, direction);
        setName("Stół");
    }
}
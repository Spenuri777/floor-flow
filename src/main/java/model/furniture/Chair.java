package model.furniture;

import model.core.PlacedElement;

import java.awt.geom.Point2D;

public class Chair extends PlacedElement {

    public Chair() {
        super();
    }

    public Chair(Point2D.Double place, boolean direction) {
        super(place, 0.5, 0.5, direction);
        setName("Krzesło");
    }

}
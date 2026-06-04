package model.furniture;

import model.core.PlacedElement;

import java.awt.geom.Point2D;

public class Bed extends PlacedElement {

    public Bed() {
        super();
    }

    public Bed(Point2D.Double place, boolean direction) {
        super(place, 1.6, 2.0, direction);
        setName("Łóżko");
    }

}
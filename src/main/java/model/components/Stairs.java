package model.components;

import model.core.PlacedElement;

import java.awt.geom.Point2D;

public class Stairs extends PlacedElement {
    private int stepsCount = 10;

    public Stairs() {
        super();
    }

    public Stairs(Point2D.Double place, boolean direction) {
        super(place, 1.25, 5.0, direction);
        setName("Schody");
    }

    public int getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(int stepsCount) {
        this.stepsCount = stepsCount;
    }
}
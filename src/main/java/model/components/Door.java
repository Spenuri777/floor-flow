package model.components;

import model.core.LinearElement;

import java.awt.geom.Point2D;

public class Door extends LinearElement {
    private boolean side;
    private int openAngle = 90;

    public Door() {
        super();
    }

    public Door(Point2D.Double start, Point2D.Double end, boolean side) {
        super(start, end, 0.25);
        this.side = side;
        setName("Drzwi");
    }

    public boolean getSide() {
        return side;
    }

    public void setSide(boolean side) {
        this.side = side;
    }

    public int getOpenAngle() {
        return openAngle;
    }

    public void setOpenAngle(int openAngle) {
        this.openAngle = openAngle;
    }
}
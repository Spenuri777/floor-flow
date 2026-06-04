package model.core;

import java.awt.geom.Point2D;

public abstract class PlacedElement extends PlanElement {
    protected Point2D.Double place;
    protected double width;
    protected double height;
    protected boolean direction;

    public PlacedElement() {
        super();
    }

    public PlacedElement(Point2D.Double place, double width, double height, boolean direction) {
        this.place = place;
        this.width = width;
        this.height = height;
        this.direction = direction;
    }

    public Point2D.Double getPlace() {
        return place;
    }

    public void setPlace(Point2D.Double place) {
        this.place = place;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean getDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public void rotate() {
        this.direction = !this.direction;
    }
}
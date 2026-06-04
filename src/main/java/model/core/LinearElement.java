package model.core;

import java.awt.geom.Point2D;

public abstract class LinearElement extends PlanElement {
    protected Point2D.Double start;
    protected Point2D.Double end;
    protected double thickness;

    public LinearElement() {
        super();
    }

    public LinearElement(Point2D.Double start, Point2D.Double end, double defaultThickness) {
        this.start = start;
        this.end = end;
        this.thickness = defaultThickness;
    }

    public Point2D.Double getStart() {
        return start;
    }

    public void setStart(Point2D.Double start) {
        this.start = start;
    }

    public Point2D.Double getEnd() {
        return end;
    }

    public void setEnd(Point2D.Double end) {
        this.end = end;
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public double getLength() {
        return start.distance(end);
    }
}
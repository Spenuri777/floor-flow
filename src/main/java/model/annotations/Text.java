package model.annotations;

import model.core.PlanElement;

import java.awt.geom.Point2D;

public class Text extends PlanElement {
    private Point2D.Double place;
    private String text;
    private double size;

    public Text() {
        super();
    }

    public Text(Point2D.Double place, String text, double size) {
        this.place = place;
        this.text = text;
        this.size = size;
        setName("Tekst");
    }

    public Point2D.Double getPlace() {
        return place;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getSize() {
        return size;
    }
}
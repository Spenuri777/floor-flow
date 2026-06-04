package model.components;

import model.core.LinearElement;

import java.awt.geom.Point2D;

public class Wall extends LinearElement {
    public Wall() {
        super();
    }

    public Wall(Point2D.Double start, Point2D.Double end) {
        super(start, end, 0.25);
        setName("Ściana");
    }
}
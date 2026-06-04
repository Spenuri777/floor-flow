package engine;

import java.awt.geom.Point2D;

public class Transformer {

    public Transformer() {
    }

    public Point2D.Double screenToWorld(Point2D screenPoint, double leftStart, double topStart, double primaryOffset) {
        double xInMeters = (screenPoint.getX() - leftStart) / primaryOffset;
        double yInMeters = (screenPoint.getY() - topStart) / primaryOffset;

        return new Point2D.Double(xInMeters, yInMeters);
    }

    public Point2D.Double worldToScreen(Point2D worldPoint, double leftStart, double topStart, double primaryOffset) {
        double xInPixels = leftStart + (worldPoint.getX() * primaryOffset);
        double yInPixels = topStart + (worldPoint.getY() * primaryOffset);

        return new Point2D.Double(xInPixels, yInPixels);
    }

    public boolean isPointOnSegment(Point2D.Double p, Point2D.Double a, Point2D.Double b, double tolerance) {
        double distance = java.awt.geom.Line2D.ptSegDist(a.x, a.y, b.x, b.y, p.x, p.y);
        return distance < tolerance;
    }

}
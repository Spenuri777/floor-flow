package components;

import java.awt.*;

public class Transformer {

    public Transformer() {
        init();
    }

    private void init() {
    }

    public Point screenToWorld(Point point, double leftStart, double topStart, double primaryOffset) {
//        System.out.println(point.getX() / offset + "m, " + point.getY() / offset + "m");
        double xInMeters = (point.getX() - leftStart) / primaryOffset;
        double yInMeters = (point.getY() - topStart) / primaryOffset;
        System.out.println("(" + xInMeters + "m, " + yInMeters + "m)");

        return new Point((int) Math.round(xInMeters), (int) Math.round(yInMeters));
    }

    public void worldToScreen() {

    }

}

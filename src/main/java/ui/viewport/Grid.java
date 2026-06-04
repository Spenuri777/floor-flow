package ui.viewport;

import model.annotations.Ruler;
import model.annotations.Text;
import model.components.Stairs;
import model.components.Window;
import engine.Transformer;
import model.components.Door;
import model.components.Wall;
import model.furniture.Bed;
import model.furniture.Chair;
import model.furniture.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Objects;

public class Grid extends JPanel implements ViewportListener {

    private final ViewportState model;
    private final Transformer transformer;
    private final ui.viewport.Painter painter;

    private Point dragStartPoint = null;

    public Grid(ViewportState model) {
        this.model = model;
        model.addListener(this);

        transformer = new Transformer();
        painter = new Painter(model, transformer);

        init();
    }

    private void init() {
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int mx = e.getX();
                int my = e.getY();
                int w = getWidth();
                int h = getHeight();

                if (e.getWheelRotation() < 0) {
                    model.zoomIn(mx, my, w, h);
                } else {
                    model.zoomOut(mx, my, w, h);
                }
            }
        });

        MouseAdapter dragAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    dragStartPoint = e.getPoint();
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                } else if (SwingUtilities.isLeftMouseButton(e)) {

                    if (model.isMouseDrawing() && Objects.equals(model.getCurrentComponent(), "STAIRS")) {
                        Point2D.Double clickedWorldPoint = calculatePoint(e);

                        model.addStairs(new Stairs(clickedWorldPoint, false));

                        model.setStartDrawingWorldPoint(null);
                        model.setGhostWorldPoint(null);

                        return;
                    } else if (model.isMouseDrawing() && Objects.equals(model.getCurrentComponent(), "TEXT")) {
                        Point2D.Double clickedWorldPoint = calculatePoint(e);
                        String text = JOptionPane.showInputDialog("Wprowadź tekst: ");

                        if (text == null) {
                            model.setStartDrawingWorldPoint(null);
                            model.setGhostWorldPoint(null);

                            return;
                        }

                        model.addText(new Text(clickedWorldPoint, text, 1.0));

                        model.setStartDrawingWorldPoint(null);
                        model.setGhostWorldPoint(null);

                        return;
                    } else if (model.isMouseDrawing() && Objects.equals(model.getCurrentComponent(), "TABLE")) {
                        Point2D.Double clickedWorldPoint = calculatePoint(e);

                        model.addTable(new Table(clickedWorldPoint, true));

                        model.setStartDrawingWorldPoint(null);
                        model.setGhostWorldPoint(null);

                        return;
                    } else if (model.isMouseDrawing() && Objects.equals(model.getCurrentComponent(), "CHAIR")) {
                        Point2D.Double clickedWorldPoint = calculatePoint(e);

                        model.addChair(new Chair(clickedWorldPoint, true));

                        model.setStartDrawingWorldPoint(null);
                        model.setGhostWorldPoint(null);

                        return;
                    } else if (model.isMouseDrawing() && Objects.equals(model.getCurrentComponent(), "BED")) {
                        Point2D.Double clickedWorldPoint = calculatePoint(e);

                        model.addBed(new Bed(clickedWorldPoint, true));

                        model.setStartDrawingWorldPoint(null);
                        model.setGhostWorldPoint(null);

                        return;
                    }

                    if (model.isMouseDrawing()) {
                        Point2D.Double clickedWorldPoint = calculatePoint(e);

                        if (model.getStartDrawingWorldPoint() == null) {
                            model.setStartDrawingWorldPoint(clickedWorldPoint);
                        } else {
                            if (Objects.equals(model.getCurrentComponent(), "WALL")) {
                                model.addWall(new Wall(model.getStartDrawingWorldPoint(), clickedWorldPoint));
                            } else if (Objects.equals(model.getCurrentComponent(), "WINDOW")) {
                                model.addWindow(new Window(model.getStartDrawingWorldPoint(), clickedWorldPoint));
                            } else if (Objects.equals(model.getCurrentComponent(), "DOOR")) {
                                model.addDoor(new Door(model.getStartDrawingWorldPoint(), clickedWorldPoint, true));
                            } else if (Objects.equals(model.getCurrentComponent(), "RULER")) {
                                model.addRuler(new Ruler(model.getStartDrawingWorldPoint(), clickedWorldPoint));
                            }

                            model.setStartDrawingWorldPoint(clickedWorldPoint);
                            model.setGhostWorldPoint(null);
                        }
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if (model.isMouseDrawing()) {
                        model.setStartDrawingWorldPoint(null);
                        model.setGhostWorldPoint(null);
                    }
                }

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                double primaryOffset = model.getBASE_PIXELS_PER_METER() * model.getZoom();
                double size = 100.0 * primaryOffset;
                double leftStart = ((getWidth() - size) / 2.0) + model.getOffsetX();
                double topStart = ((getHeight() - size) / 2.0) + model.getOffsetY();
                Point2D.Double currentPoint = transformer.screenToWorld(e.getPoint(), leftStart, topStart, primaryOffset);
                model.setCurrentPoint(currentPoint);

                if (model.isMouseDrawing() && model.getStartDrawingWorldPoint() != null) {
                    Point2D.Double ghostWorld = calculatePoint(e);
                    model.setGhostWorldPoint(ghostWorld);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStartPoint != null) {
                    double deltaX = e.getX() - dragStartPoint.getX();
                    double deltaY = e.getY() - dragStartPoint.getY();
                    model.addOffset(deltaX, deltaY);
                    dragStartPoint = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    dragStartPoint = null;
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };

        addMouseListener(dragAdapter);
        addMouseMotionListener(dragAdapter);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    if (e.getID() == KeyEvent.KEY_PRESSED) {
                        model.setShiftPressed(true);
                    } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                        model.setShiftPressed(false);
                    }
                }
                return false;
            }
        });
    }

    private Point2D.Double applySnapping(Point2D.Double rawPoint) {
        double snapRadius = 1.0;

        if (Objects.equals(model.getCurrentComponent(), "WALL")) {
            for (Wall wall : model.getWalls()) {
                if (rawPoint.distance(wall.getStart()) < snapRadius) {
                    return new Point2D.Double(wall.getStart().x, wall.getStart().y);
                } else if (rawPoint.distance(wall.getEnd()) < snapRadius) {
                    return new Point2D.Double(wall.getEnd().x, wall.getEnd().y);
                } else if (transformer.isPointOnSegment(rawPoint, wall.getStart(), wall.getEnd(), 0.1)) {
                    return projectPointToSegment(rawPoint, wall.getStart(), wall.getEnd());
                }
            }
        } else if (Objects.equals(model.getCurrentComponent(), "WINDOW") || Objects.equals(model.getCurrentComponent(), "DOOR") || Objects.equals(model.getCurrentComponent(), "RULER")) {
            for (Wall wall : model.getWalls()) {
                if (transformer.isPointOnSegment(rawPoint, wall.getStart(), wall.getEnd(), 0.1)) {
                    return projectPointToSegment(rawPoint, wall.getStart(), wall.getEnd());
                }
            }
        }

        return rawPoint;
    }

    private Point2D.Double projectPointToSegment(Point2D.Double p, Point2D.Double a, Point2D.Double b) {
        double l2 = a.distanceSq(b);
        if (l2 == 0) return a;
        double t = ((p.x - a.x) * (b.x - a.x) + (p.y - a.y) * (b.y - a.y)) / l2;
        t = Math.max(0, Math.min(1, t));
        return new Point2D.Double(a.x + t * (b.x - a.x), a.y + t * (b.y - a.y));
    }

    private Point2D.Double applyAngleSnapping(Point2D.Double rawPoint, Point2D.Double startPoint) {
        if (startPoint == null) return rawPoint;

        double dx = rawPoint.x - startPoint.x;
        double dy = rawPoint.y - startPoint.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        double angleRad = Math.atan2(dy, dx);
        double angleDeg = Math.toDegrees(angleRad);

        double snapAngleDeg = Math.round(angleDeg / 15.0) * 15.0;
        double snapAngleRad = Math.toRadians(snapAngleDeg);

        double newX = startPoint.x + distance * Math.cos(snapAngleRad);
        double newY = startPoint.y + distance * Math.sin(snapAngleRad);

        return new Point2D.Double(newX, newY);
    }

    private Point2D.Double calculatePoint(MouseEvent e) {
        double primaryOffset = model.getBASE_PIXELS_PER_METER() * model.getZoom();
        double size = 100.0 * primaryOffset;
        double leftStart = ((getWidth() - size) / 2.0) + model.getOffsetX();
        double topStart = ((getHeight() - size) / 2.0) + model.getOffsetY();

        Point2D.Double worldPoint = transformer.screenToWorld(e.getPoint(), leftStart, topStart, primaryOffset);

        worldPoint = applySnapping(worldPoint);

        if (model.isShiftPressed() && model.getStartDrawingWorldPoint() != null) {
            worldPoint = applyAngleSnapping(worldPoint, model.getStartDrawingWorldPoint());
        }

        return worldPoint;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        painter.paint((Graphics2D) g, getWidth(), getHeight());
    }

    @Override
    public void onViewportChanged() {
        repaint();
    }
}
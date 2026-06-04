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

import java.awt.*;
import java.awt.geom.*;

public class Painter {

    private final ViewportState model;
    private final Transformer transformer;

    private final Path2D.Double sharedPolygon = new Path2D.Double();
    private final Line2D.Double sharedLine = new Line2D.Double();
    private final Rectangle2D.Double sharedRect = new Rectangle2D.Double();
    private final Arc2D.Double sharedArc = new Arc2D.Double();

    public Painter(ViewportState model, Transformer transformer) {
        this.model = model;
        this.transformer = transformer;
    }

    public void paint(Graphics2D g2d, int width, int height) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double primaryOffset = model.getBASE_PIXELS_PER_METER() * model.getZoom();
        double secondaryOffset = primaryOffset * 0.1;
        double step = model.getPrimaryStep();

        if (primaryOffset < 1) return;

        double size = 100.0 * primaryOffset;
        double leftStart = ((width - size) / 2.0) + model.getOffsetX();
        double topStart = ((height - size) / 2.0) + model.getOffsetY();

        Stroke thinStroke = new BasicStroke(1);
        Stroke thickStroke = new BasicStroke(Math.round(0.05 * primaryOffset));
        Font rulerFont = new Font("Arial", Font.BOLD, (int) Math.round(1 * primaryOffset));

        // DRAWING GRID
        g2d.setStroke(thinStroke);

        g2d.setColor(new Color(230, 230, 230));
        for (double x = leftStart; x <= leftStart + size; x += secondaryOffset * step) {
            sharedLine.setLine(x, topStart, x, topStart + size);
            g2d.draw(sharedLine);
        }
        for (double y = topStart; y <= topStart + size; y += secondaryOffset * step) {
            sharedLine.setLine(leftStart, y, leftStart + size, y);
            g2d.draw(sharedLine);
        }

        g2d.setColor(new Color(200, 200, 200));
        for (double x = leftStart; x <= leftStart + size; x += primaryOffset * step) {
            sharedLine.setLine(x, topStart, x, topStart + size);
            g2d.draw(sharedLine);
        }
        for (double y = topStart; y <= topStart + size; y += primaryOffset * step) {
            sharedLine.setLine(leftStart, y, leftStart + size, y);
            g2d.draw(sharedLine);
        }

        g2d.setColor(Color.BLACK);
        sharedRect.setRect(leftStart, topStart, size, size);
        g2d.draw(sharedRect);

        // DRAWING WALLS
        for (Wall wall : model.getWalls()) {
            if (!wall.isVisible()) continue;

            Point2D.Double screenStart = transformer.worldToScreen(wall.getStart(), leftStart, topStart, primaryOffset);
            Point2D.Double screenEnd = transformer.worldToScreen(wall.getEnd(), leftStart, topStart, primaryOffset);

            double dx = screenEnd.x - screenStart.x;
            double dy = screenEnd.y - screenStart.y;
            double length = Math.sqrt(dx * dx + dy * dy);
            if (length == 0) continue;

            double dirX = dx / length;
            double dirY = dy / length;
            double nx = -dirY;
            double ny = dirX;

            double halfThicknessPx = (wall.getThickness() * primaryOffset) / 2.0;

            double extStartX = screenStart.x - dirX * halfThicknessPx;
            double extStartY = screenStart.y - dirY * halfThicknessPx;
            double extEndX = screenEnd.x + dirX * halfThicknessPx;
            double extEndY = screenEnd.y + dirY * halfThicknessPx;

            sharedPolygon.reset();
            sharedPolygon.moveTo(extStartX + nx * halfThicknessPx, extStartY + ny * halfThicknessPx);
            sharedPolygon.lineTo(extStartX - nx * halfThicknessPx, extStartY - ny * halfThicknessPx);
            sharedPolygon.lineTo(extEndX - nx * halfThicknessPx, extEndY - ny * halfThicknessPx);
            sharedPolygon.lineTo(extEndX + nx * halfThicknessPx, extEndY + ny * halfThicknessPx);
            sharedPolygon.closePath();

            g2d.setColor(Color.DARK_GRAY);
            g2d.fill(sharedPolygon);
            g2d.setStroke(thinStroke);
            g2d.setColor(Color.BLACK);
            g2d.draw(sharedPolygon);
        }

        //DRAWING WINDOWS
        for (Window window : model.getWindows()) {
            if (!window.isVisible()) continue;

            Point2D.Double screenStart = transformer.worldToScreen(window.getStart(), leftStart, topStart, primaryOffset);
            Point2D.Double screenEnd = transformer.worldToScreen(window.getEnd(), leftStart, topStart, primaryOffset);

            double dx = screenEnd.x - screenStart.x;
            double dy = screenEnd.y - screenStart.y;
            double length = Math.sqrt(dx * dx + dy * dy);
            if (length == 0) continue;

            double dirX = dx / length;
            double dirY = dy / length;
            double nx = -dirY;
            double ny = dirX;

            double halfThicknessPx = (window.getThickness() * primaryOffset) / 2.0;

            double extStartX = screenStart.x - dirX * halfThicknessPx;
            double extStartY = screenStart.y - dirY * halfThicknessPx;
            double extEndX = screenEnd.x + dirX * halfThicknessPx;
            double extEndY = screenEnd.y + dirY * halfThicknessPx;

            sharedPolygon.reset();
            sharedPolygon.moveTo(extStartX + nx * halfThicknessPx, extStartY + ny * halfThicknessPx);
            sharedPolygon.lineTo(extStartX - nx * halfThicknessPx, extStartY - ny * halfThicknessPx);
            sharedPolygon.lineTo(extEndX - nx * halfThicknessPx, extEndY - ny * halfThicknessPx);
            sharedPolygon.lineTo(extEndX + nx * halfThicknessPx, extEndY + ny * halfThicknessPx);
            sharedPolygon.closePath();

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fill(sharedPolygon);
            g2d.setStroke(thinStroke);
            g2d.setColor(Color.DARK_GRAY);
            g2d.draw(sharedPolygon);

            g2d.setStroke(thickStroke);
            g2d.setColor(Color.BLACK);
            sharedLine.setLine(extStartX, extStartY, extEndX, extEndY);
            g2d.draw(sharedLine);
        }

        // DRAWING DOORS
        for (Door door : model.getDoors()) {
            if (!door.isVisible()) continue;

            Point2D.Double screenStart = transformer.worldToScreen(door.getStart(), leftStart, topStart, primaryOffset);
            Point2D.Double screenEnd = transformer.worldToScreen(door.getEnd(), leftStart, topStart, primaryOffset);

            double dx = screenEnd.x - screenStart.x;
            double dy = screenEnd.y - screenStart.y;
            double length = Math.sqrt(dx * dx + dy * dy);
            if (length == 0) continue;

            double dirX = dx / length;
            double dirY = dy / length;
            double nx = -dirY;
            double ny = dirX;

            double halfThicknessPx = (door.getThickness() * primaryOffset) / 2.0;

            double extStartX = screenStart.x - dirX * halfThicknessPx;
            double extStartY = screenStart.y - dirY * halfThicknessPx;
            double extEndX = screenEnd.x + dirX * halfThicknessPx;
            double extEndY = screenEnd.y + dirY * halfThicknessPx;

            sharedPolygon.reset();
            sharedPolygon.moveTo(extStartX + nx * halfThicknessPx, extStartY + ny * halfThicknessPx);
            sharedPolygon.lineTo(extStartX - nx * halfThicknessPx, extStartY - ny * halfThicknessPx);
            sharedPolygon.lineTo(extEndX - nx * halfThicknessPx, extEndY - ny * halfThicknessPx);
            sharedPolygon.lineTo(extEndX + nx * halfThicknessPx, extEndY + ny * halfThicknessPx);
            sharedPolygon.closePath();

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fill(sharedPolygon);
            g2d.setStroke(thinStroke);
            g2d.setColor(Color.DARK_GRAY);
            g2d.draw(sharedPolygon);

            g2d.setStroke(thickStroke);
            g2d.setColor(Color.BLACK);

            double baseAngleRad = Math.atan2(dy, dx);
            double openAngleRad = Math.toRadians(door.getOpenAngle());
            double wingDirectionMultiplier = door.getSide() ? 1.0 : -1.0;
            double wingAngleRad = baseAngleRad + (openAngleRad * wingDirectionMultiplier);

            double wingTipX = screenStart.x + length * Math.cos(wingAngleRad);
            double wingTipY = screenStart.y + length * Math.sin(wingAngleRad);

            sharedLine.setLine(screenStart.x, screenStart.y, wingTipX, wingTipY);
            g2d.draw(sharedLine);

            double arcX = screenStart.x - length;
            double arcY = screenStart.y - length;
            double arcSize = length * 2;
            double startAngleDeg = -Math.toDegrees(baseAngleRad);
            double sweepAngleDeg = door.getSide() ? -door.getOpenAngle() : door.getOpenAngle();

            sharedArc.setArc(arcX, arcY, arcSize, arcSize, startAngleDeg, sweepAngleDeg, Arc2D.OPEN);
            g2d.draw(sharedArc);
        }

        // DRAWING STAIRS
        g2d.setStroke(thinStroke);
        for (Stairs stairs : model.getStairs()) {
            if (!stairs.isVisible()) continue;

            Point2D.Double stairsStart = transformer.worldToScreen(stairs.getPlace(), leftStart, topStart, primaryOffset);
            double stairsWidth = stairs.getWidth() * primaryOffset;
            double stairsHeight = stairs.getHeight() * primaryOffset;

            double actualWidth = stairs.getDirection() ? stairsWidth : stairsHeight;
            double actualHeight = stairs.getDirection() ? stairsHeight : stairsWidth;

            sharedRect.setRect(stairsStart.x, stairsStart.y, actualWidth, actualHeight);

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fill(sharedRect);
            g2d.setColor(Color.DARK_GRAY);
            g2d.draw(sharedRect);

            int stepsCount = stairs.getStepsCount();
            if (stairs.getDirection()) {
                double stepSizePxY = actualHeight / stepsCount;
                for (int i = 0; i <= stepsCount; i++) {
                    double y = stairsStart.y + (i * stepSizePxY);
                    sharedLine.setLine(stairsStart.x, y, stairsStart.x + actualWidth, y);
                    g2d.draw(sharedLine);
                }
            } else {
                double stepSizePxX = actualWidth / stepsCount;
                for (int i = 0; i <= stepsCount; i++) {
                    double x = stairsStart.x + (i * stepSizePxX);
                    sharedLine.setLine(x, stairsStart.y, x, stairsStart.y + actualHeight);
                    g2d.draw(sharedLine);
                }
            }
        }

        // DRAWING TABLES
        g2d.setStroke(thinStroke);
        for (Table table : model.getTables()) {
            if (!table.isVisible()) continue;

            Point2D.Double screenStart = transformer.worldToScreen(table.getPlace(), leftStart, topStart, primaryOffset);
            double wPx = table.getWidth() * primaryOffset;
            double hPx = table.getHeight() * primaryOffset;

            double actualWidth = table.getDirection() ? wPx : hPx;
            double actualHeight = table.getDirection() ? hPx : wPx;

            sharedRect.setRect(screenStart.x, screenStart.y, actualWidth, actualHeight);

            g2d.setColor(new Color(205, 170, 125));
            g2d.fill(sharedRect);
            g2d.setColor(Color.DARK_GRAY);
            g2d.draw(sharedRect);
        }

        // DRAWING CHAIRS
        g2d.setStroke(thinStroke);
        for (Chair chair : model.getChairs()) {
            if (!chair.isVisible()) continue;

            Point2D.Double screenStart = transformer.worldToScreen(chair.getPlace(), leftStart, topStart, primaryOffset);
            double wPx = chair.getWidth() * primaryOffset;
            double hPx = chair.getHeight() * primaryOffset;

            double actualWidth = chair.getDirection() ? wPx : hPx;
            double actualHeight = chair.getDirection() ? hPx : wPx;

            sharedRect.setRect(screenStart.x, screenStart.y, actualWidth, actualHeight);

            g2d.setColor(new Color(222, 184, 135));
            g2d.fill(sharedRect);
            g2d.setColor(Color.DARK_GRAY);
            g2d.draw(sharedRect);
        }

        // DRAWING BEDS
        g2d.setStroke(thinStroke);
        for (Bed bed : model.getBeds()) {
            if (!bed.isVisible()) continue;

            Point2D.Double screenStart = transformer.worldToScreen(bed.getPlace(), leftStart, topStart, primaryOffset);
            double wPx = bed.getWidth() * primaryOffset;
            double hPx = bed.getHeight() * primaryOffset;

            double actualWidth = bed.getDirection() ? wPx : hPx;
            double actualHeight = bed.getDirection() ? hPx : wPx;

            sharedRect.setRect(screenStart.x, screenStart.y, actualWidth, actualHeight);

            g2d.setColor(new Color(240, 248, 255));
            g2d.fill(sharedRect);
            g2d.setColor(Color.DARK_GRAY);
            g2d.draw(sharedRect);

            if (bed.getDirection()) {
                double pillowY = screenStart.y + (actualHeight * 0.2);
                sharedLine.setLine(screenStart.x, pillowY, screenStart.x + actualWidth, pillowY);
            } else {
                double pillowX = screenStart.x + (actualWidth * 0.2);
                sharedLine.setLine(pillowX, screenStart.y, pillowX, screenStart.y + actualHeight);
            }
            g2d.draw(sharedLine);
        }

        // DRAWING RULERS
        g2d.setStroke(thinStroke);
        g2d.setColor(Color.BLUE);
        g2d.setFont(rulerFont);

        for (Ruler ruler : model.getRulers()) {
            if (!ruler.isVisible()) continue;

            Point2D.Double screenStart = transformer.worldToScreen(ruler.getStart(), leftStart, topStart, primaryOffset);
            Point2D.Double screenEnd = transformer.worldToScreen(ruler.getEnd(), leftStart, topStart, primaryOffset);

            sharedLine.setLine(screenStart.x, screenStart.y, screenEnd.x, screenEnd.y);
            g2d.draw(sharedLine);

            double distanceMeters = ruler.getStart().distance(ruler.getEnd());
            double midX = (screenStart.x + screenEnd.x) / 2.0;
            double midY = (screenStart.y + screenEnd.y) / 2.0;

            g2d.drawString(String.format("%.2f m", distanceMeters), (float) midX + 5, (float) midY - 5);
        }

        // DRAWING TEXTS
        g2d.setColor(Color.BLACK);
        for (Text text : model.getTexts()) {
            if (!text.isVisible()) continue;

            Point2D.Double textStart = transformer.worldToScreen(text.getPlace(), leftStart, topStart, primaryOffset);

            g2d.setFont(new Font("Arial", Font.BOLD, (int) Math.round(text.getSize() * primaryOffset)));
            g2d.drawString(text.getText(), (int) textStart.x, (int) textStart.y);
        }

        // DRAWING RULERS
        g2d.setStroke(thinStroke);
        g2d.setColor(Color.BLUE);
        g2d.setFont(rulerFont);

        for (Ruler ruler : model.getRulers()) {
            if (!ruler.isVisible()) continue;

            Point2D.Double screenStart = transformer.worldToScreen(ruler.getStart(), leftStart, topStart, primaryOffset);
            Point2D.Double screenEnd = transformer.worldToScreen(ruler.getEnd(), leftStart, topStart, primaryOffset);

            sharedLine.setLine(screenStart.x, screenStart.y, screenEnd.x, screenEnd.y);
            g2d.draw(sharedLine);

            double distanceMeters = ruler.getStart().distance(ruler.getEnd());
            double midX = (screenStart.x + screenEnd.x) / 2.0;
            double midY = (screenStart.y + screenEnd.y) / 2.0;

            g2d.drawString(String.format("%.2f m", distanceMeters), (float) midX + 5, (float) midY - 5);
        }

        // DRAWING TEXTS
        g2d.setColor(Color.BLACK);
        for (Text text : model.getTexts()) {
            if (!text.isVisible()) continue;

            Point2D.Double textStart = transformer.worldToScreen(text.getPlace(), leftStart, topStart, primaryOffset);

            g2d.setFont(new Font("Arial", Font.BOLD, (int) Math.round(text.getSize() * primaryOffset)));
            g2d.drawString(text.getText(), (int) textStart.x, (int) textStart.y);
        }

        // DRAWING GHOST LINE
        if (model.isMouseDrawing() && model.getStartDrawingWorldPoint() != null && model.getGhostWorldPoint() != null) {
            g2d.setStroke(thinStroke);
            g2d.setColor(Color.RED);
            Point2D.Double screenStart = transformer.worldToScreen(model.getStartDrawingWorldPoint(), leftStart, topStart, primaryOffset);
            Point2D.Double screenGhost = transformer.worldToScreen(model.getGhostWorldPoint(), leftStart, topStart, primaryOffset);

            sharedLine.setLine(screenStart.x, screenStart.y, screenGhost.x, screenGhost.y);
            g2d.draw(sharedLine);

            g2d.setFont(rulerFont);
            g2d.drawString(String.format("%.2f m", model.getGhostWorldPoint().distance(model.getStartDrawingWorldPoint())), (int) screenGhost.x, (int) screenGhost.y);
        }
    }
}
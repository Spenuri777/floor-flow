package ui.viewport;

import model.annotations.Ruler;
import model.annotations.Text;
import model.components.Door;
import model.components.Stairs;
import model.components.Wall;
import model.components.Window;
import model.furniture.Bed;
import model.furniture.Chair;
import model.furniture.Table;

import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Point2D;
import java.util.Objects;

public class ViewportState {

    private String projectId;
    private String projectName;
    private String userId;

    private final List<ViewportListener> listeners = new ArrayList<>();

    private final double BASE_PIXELS_PER_METER = 50.0;
    private double zoom = 1.0;
    private final double ZOOM_FACTOR = 1.05;
    private final double MIN_ZOOM = 0.1;
    private final double MAX_ZOOM = 16.0;

    private final double[] steps = {50.0, 25.0, 20.0, 10.0, 5.0, 4.0, 2.0, 1.0};

    private double offsetX = 0.0;
    private double offsetY = 0.0;

    private String currentComponent = "";
    private boolean isMouseDrawing = false;

    private Point2D.Double startDrawingWorldPoint;
    private Point2D.Double ghostWorldPoint;

    private boolean isShiftPressed = false;

    private final List<Wall> walls = new ArrayList<>();
    private final List<Window> windows = new ArrayList<>();
    private final List<Door> doors = new ArrayList<>();
    private final List<Stairs> stairs = new ArrayList<>();
    private final List<Ruler> rulers = new ArrayList<>();
    private final List<Text> texts = new ArrayList<>();

    private final List<Table> tables = new ArrayList<>();
    private final List<Chair> chairs = new ArrayList<>();
    private final List<Bed> beds = new ArrayList<>();

    private final List<Object> allElements = new ArrayList<>();

    private Point2D.Double currentPoint;

    public String getUserId() {
        return userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public double getBASE_PIXELS_PER_METER() {
        return BASE_PIXELS_PER_METER;
    }

    public double getZoom() {
        return zoom;
    }

    public double getPrimaryStep() {
        int index = (int) ((Math.log(zoom / MIN_ZOOM) / Math.log(MAX_ZOOM / MIN_ZOOM)) * steps.length);
        if (index >= steps.length) index = steps.length - 1;
        if (index < 0) index = 0;
        return steps[index];
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void zoomIn(int mouseX, int mouseY, int screenWidth, int screenHeight) {
        double oldZoom = this.zoom;
        this.zoom *= ZOOM_FACTOR;

        if (zoom > MAX_ZOOM) zoom = MAX_ZOOM;

        applyZoomToMouseOffset(oldZoom, this.zoom, mouseX, mouseY, screenWidth, screenHeight);
        notifyListeners();
    }

    public void zoomOut(int mouseX, int mouseY, int screenWidth, int screenHeight) {
        double oldZoom = this.zoom;
        this.zoom /= ZOOM_FACTOR;

        if (zoom < MIN_ZOOM) zoom = MIN_ZOOM;

        applyZoomToMouseOffset(oldZoom, this.zoom, mouseX, mouseY, screenWidth, screenHeight);
        notifyListeners();
    }

    private void applyZoomToMouseOffset(double oldZoom, double newZoom, int mouseX, int mouseY, int width, int height) {

        double zoomRatio = newZoom / oldZoom;

        double dx = mouseX - (width / 2.0);
        double dy = mouseY - (height / 2.0);

        this.offsetX = (this.offsetX * zoomRatio) + (dx * (1.0 - zoomRatio));
        this.offsetY = (this.offsetY * zoomRatio) + (dy * (1.0 - zoomRatio));
    }

    public void addOffset(double deltaX, double deltaY) {
        this.offsetX += deltaX;
        this.offsetY += deltaY;
        notifyListeners();
    }

    public boolean isMouseDrawing() {
        return isMouseDrawing;
    }

    public boolean isShiftPressed() {
        return isShiftPressed;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setMouseDrawing(boolean mouseDrawing, String component) {
        this.isMouseDrawing = mouseDrawing;
        this.currentComponent = component;

        if (!mouseDrawing) {
            startDrawingWorldPoint = null;
            ghostWorldPoint = null;
            notifyListeners();
        }

        notifyListeners();
    }

    public void setShiftPressed(boolean shiftPressed) {
        this.isShiftPressed = shiftPressed;
        notifyListeners();
    }

    public void setCurrentPoint(Point2D.Double currentPoint) {
        this.currentPoint = currentPoint;
        notifyListeners();
    }

    public Point2D.Double getStartDrawingWorldPoint() {
        return startDrawingWorldPoint;
    }

    public void setStartDrawingWorldPoint(Point2D.Double p) {
        this.startDrawingWorldPoint = p;
    }

    public Point2D.Double getGhostWorldPoint() {
        return ghostWorldPoint;
    }

    public void setGhostWorldPoint(Point2D.Double ghostWorldPoint) {
        this.ghostWorldPoint = ghostWorldPoint;
        notifyListeners();
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Window> getWindows() {
        return windows;
    }

    public List<Door> getDoors() {
        return doors;
    }

    public List<Stairs> getStairs() {
        return stairs;
    }

    public List<Ruler> getRulers() {
        return rulers;
    }

    public List<Text> getTexts() {
        return texts;
    }

    public List<Table> getTables() {
        return tables;
    }

    public List<Chair> getChairs() {
        return chairs;
    }

    public List<Bed> getBeds() {
        return beds;
    }


    public void addWall(Wall wall) {
        walls.add(wall);
        allElements.add(wall);
        notifyListeners();
    }

    public void addWindow(Window window) {
        windows.add(window);
        allElements.add(window);
        notifyListeners();
    }

    public void addDoor(Door door) {
        doors.add(door);
        allElements.add(door);
        notifyListeners();
    }

    public void addStairs(Stairs stairs) {
        this.stairs.add(stairs);
        allElements.add(stairs);
        notifyListeners();
    }

    public void addRuler(Ruler ruler) {
        rulers.add(ruler);
        allElements.add(ruler);
        notifyListeners();
    }

    public void addText(Text text) {
        texts.add(text);
        allElements.add(text);
        notifyListeners();
    }

    public void addTable(Table table) {
        tables.add(table);
        allElements.add(table);
        notifyListeners();
    }

    public void addChair(Chair chair) {
        chairs.add(chair);
        allElements.add(chair);
        notifyListeners();
    }

    public void addBed(Bed bed) {
        beds.add(bed);
        allElements.add(bed);
        notifyListeners();
    }

    public List<Object> getAllElements() {
        return allElements;
    }

    public void removeElement(Object obj) {
        allElements.remove(obj);

        if (obj instanceof Wall) walls.remove(obj);
        else if (obj instanceof Window) windows.remove(obj);
        else if (obj instanceof Door) doors.remove(obj);
        else if (obj instanceof Stairs) stairs.remove(obj);
        else if (obj instanceof Ruler) rulers.remove(obj);
        else if (obj instanceof Text) texts.remove(obj);
        else if (obj instanceof Table) tables.remove(obj);
        else if (obj instanceof Chair) chairs.remove(obj);
        else if (obj instanceof Bed) beds.remove(obj);

        notifyListeners();
    }

    public String getCurrentComponent() {
        return currentComponent;
    }

    public String getContextHelp() {
        boolean hasStartPoint = getStartDrawingWorldPoint() != null;

        if (Objects.equals(getCurrentComponent(), "") || getCurrentComponent() == null) {
            return "Wybierz narzędzie...";
        } else if (Objects.equals(getCurrentComponent(), "WALL")) {
            if (!hasStartPoint) {
                return "Ściana: Kliknij, aby ustalić początek ściany.";
            } else {
                return "Ściana: Kliknij, aby zakończyć ścianę.";
            }
        } else if (Objects.equals(getCurrentComponent(), "WINDOW")) {
            if (!hasStartPoint) {
                return "Okno: Kliknij na narysowaną ścianę, aby ustalić początek okna.";
            } else {
                return "Okno: Wskaż koniec okna wzdłuż ściany.";
            }
        } else if (Objects.equals(getCurrentComponent(), "DOOR")) {
            if (!hasStartPoint) {
                return "Drzwi: Kliknij na narysowaną ścianę, aby ustalić pozycję drzwi.";
            } else {
                return "Drzwi: Wskaż szerokość i stronę otwarcia.";
            }
        } else if (Objects.equals(getCurrentComponent(), "STAIRS")) {
            return "Schody: Kliknij w dowolnym miejscu, aby wstawić schody.";
        } else if (Objects.equals(getCurrentComponent(), "RULER")) {
            if (!hasStartPoint) {
                return "Linijka: Kliknij, aby ustalić punkt początkowy pomiaru.";
            } else {
                return "Linijka: Kliknij w drugim punkcie, aby zakończyć pomiar.";
            }
        } else if (Objects.equals(getCurrentComponent(), "TEXT")) {
            return "Tekst: Kliknij w docelowym miejscu, aby wstawić napis.";
        }

        return "Gotowy: Wybierz narzędzie.";
    }

    public String getCurrentPoint() {
        if (currentPoint == null) {
            return "X: --m, Y: --m";
        }

        return "X: " + String.format("%.2f", this.currentPoint.x) + "m, Y: " + String.format("%.2f", this.currentPoint.y) + "m";

    }

    public String getDrawingLength() {
        if (isMouseDrawing() && getStartDrawingWorldPoint() != null && getGhostWorldPoint() != null) {
            return "Długość: " + String.format("%.2f", getGhostWorldPoint().distance(getStartDrawingWorldPoint())) + "m";
        }
        return "Długość: ---";
    }

    
    public void importFromJson(String json) {
        if (json == null || json.trim().isEmpty() || json.equals("{}")) {
            return; 
        }

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(json);

            
            walls.clear();
            windows.clear();
            doors.clear();
            stairs.clear();
            rulers.clear();
            texts.clear();
            tables.clear();
            chairs.clear();
            beds.clear();
            allElements.clear();

            
            if (root.has("walls")) {
                walls.addAll(mapper.convertValue(root.get("walls"), new com.fasterxml.jackson.core.type.TypeReference<List<Wall>>() {
                }));
            }
            if (root.has("windows")) {
                windows.addAll(mapper.convertValue(root.get("windows"), new com.fasterxml.jackson.core.type.TypeReference<List<Window>>() {
                }));
            }
            if (root.has("doors")) {
                doors.addAll(mapper.convertValue(root.get("doors"), new com.fasterxml.jackson.core.type.TypeReference<List<Door>>() {
                }));
            }
            if (root.has("stairs")) {
                stairs.addAll(mapper.convertValue(root.get("stairs"), new com.fasterxml.jackson.core.type.TypeReference<List<Stairs>>() {
                }));
            }
            if (root.has("rulers")) {
                rulers.addAll(mapper.convertValue(root.get("rulers"), new com.fasterxml.jackson.core.type.TypeReference<List<Ruler>>() {
                }));
            }
            if (root.has("texts")) {
                texts.addAll(mapper.convertValue(root.get("texts"), new com.fasterxml.jackson.core.type.TypeReference<List<Text>>() {
                }));
            }
            if (root.has("tables")) {
                tables.addAll(mapper.convertValue(root.get("tables"), new com.fasterxml.jackson.core.type.TypeReference<List<Table>>() {
                }));
            }
            if (root.has("chairs")) {
                chairs.addAll(mapper.convertValue(root.get("chairs"), new com.fasterxml.jackson.core.type.TypeReference<List<Chair>>() {
                }));
            }
            if (root.has("beds")) {
                beds.addAll(mapper.convertValue(root.get("beds"), new com.fasterxml.jackson.core.type.TypeReference<List<Bed>>() {
                }));
            }

            
            allElements.addAll(walls);
            allElements.addAll(windows);
            allElements.addAll(doors);
            allElements.addAll(stairs);
            allElements.addAll(rulers);
            allElements.addAll(texts);
            allElements.addAll(tables);
            allElements.addAll(chairs);
            allElements.addAll(beds);

            
            notifyListeners();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Wystąpił błąd podczas wczytywania pliku JSON!");
        }
    }

    public String exportToJson() {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("walls", walls);
            data.put("windows", windows);
            data.put("doors", doors);
            data.put("stairs", stairs);
            data.put("rulers", rulers);
            data.put("texts", texts);
            data.put("tables", tables);
            data.put("chairs", chairs);
            data.put("beds", beds);

            return mapper.writeValueAsString(data);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public void addListener(ViewportListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        for (ViewportListener listener : listeners) {
            listener.onViewportChanged();
        }
    }
}
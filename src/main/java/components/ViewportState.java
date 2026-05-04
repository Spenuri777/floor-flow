package components;

import java.util.ArrayList;
import java.util.List;

public class ViewportState {

    private final List<ViewportListener> listeners = new ArrayList<>();

    private final double BASE_PIXELS_PER_METER = 50.0;
    private double zoom = 1.0;
    private final double ZOOM_FACTOR = 1.05;
    private final double MIN_ZOOM = 0.1;
    private final double MAX_ZOOM = 16.0;

    private final double[] steps = {50.0, 25.0, 20.0, 10.0, 5.0, 4.0, 2.0, 1.0};

    private double offsetX = 0.0;
    private double offsetY = 0.0;

    public double getBASE_PIXELS_PER_METER() { return BASE_PIXELS_PER_METER; }

    public double getZoom() { return zoom; }

    public double getPrimaryStep() {
        int index = (int)( (Math.log(zoom / MIN_ZOOM) / Math.log(MAX_ZOOM / MIN_ZOOM)) * steps.length );
        if(index >= steps.length) index = steps.length - 1;
        if(index < 0) index = 0;
        return steps[index];
    }

    public double getOffsetX() { return offsetX; }

    public double getOffsetY() { return offsetY; }

    // Zmodyfikowane metody zoom
    public void zoomIn(int mouseX, int mouseY, int screenWidth, int screenHeight) {
        double oldZoom = this.zoom;
        this.zoom *= ZOOM_FACTOR;

        if(zoom > MAX_ZOOM) zoom = MAX_ZOOM;

        applyZoomToMouseOffset(oldZoom, this.zoom, mouseX, mouseY, screenWidth, screenHeight);
        notifyListeners();
    }

    public void zoomOut(int mouseX, int mouseY, int screenWidth, int screenHeight) {
        double oldZoom = this.zoom;
        this.zoom /= ZOOM_FACTOR;

        if(zoom < MIN_ZOOM) zoom = MIN_ZOOM;

        applyZoomToMouseOffset(oldZoom, this.zoom, mouseX, mouseY, screenWidth, screenHeight);
        notifyListeners();
    }

    // MAGIA MATEMATYKI: Przeliczanie offsetu
    private void applyZoomToMouseOffset(double oldZoom, double newZoom, int mouseX, int mouseY, int width, int height) {
        if (oldZoom == newZoom) return; // Jeśli osiągnęliśmy limit zooma, nie przesuwaj

        double zoomRatio = newZoom / oldZoom;

        // Obliczamy odległość myszki od fizycznego środka ekranu
        double dx = mouseX - (width / 2.0);
        double dy = mouseY - (height / 2.0);

        // Aplikujemy wzór na kompensację przesunięcia
        this.offsetX = (this.offsetX * zoomRatio) + (dx * (1.0 - zoomRatio));
        this.offsetY = (this.offsetY * zoomRatio) + (dy * (1.0 - zoomRatio));
    }

    public void addOffset(double deltaX, double deltaY) {
        this.offsetX += deltaX;
        this.offsetY += deltaY;
        notifyListeners();
    }

    public void addListener(ViewportListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (ViewportListener listener: listeners) {
            listener.onViewportChanged();
        }
    }
}
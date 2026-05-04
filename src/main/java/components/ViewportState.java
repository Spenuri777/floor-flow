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

    public double getBASE_PIXELS_PER_METER() { return BASE_PIXELS_PER_METER; }
    public double getZoom() { return zoom; }

    public double getPrimaryStep() {
        int index = (int)( (Math.log(zoom / MIN_ZOOM) / Math.log(MAX_ZOOM / MIN_ZOOM)) * steps.length );
        if(index >= steps.length) index = steps.length - 1;
        if(index < 0) index = 0;
        return steps[index];
    }

    public void zoomIn() {
        this.zoom *= ZOOM_FACTOR;
        if(zoom > MAX_ZOOM) zoom = MAX_ZOOM;
        notifyListeners();
    }

    public void zoomOut() {
        this.zoom /= ZOOM_FACTOR;
        if(zoom < MIN_ZOOM) zoom = MIN_ZOOM;
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
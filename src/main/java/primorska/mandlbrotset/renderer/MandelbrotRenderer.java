/*package primorska.mandlbrotset.renderer;

import javafx.scene.canvas.GraphicsContext;
import java.awt.image.BufferedImage;


public interface MandelbrotRenderer {
    void render(GraphicsContext gc, int width, int height,
                double minX, double maxX, double minY, double maxY,
                double zoomFactor) throws InterruptedException;
    default void logHardwareUsage() {
        // Default no-op, override in implementations
    }
    // In MandelbrotRenderer.java
    default BufferedImage renderToImage(int width, int height,
                                        double minX, double maxX,
                                        double minY, double maxY,
                                        int maxIter) throws Exception {
        return null; // Implement in DistributedRendererWrapper
    }
    default BufferedImage renderToImage(int width, int height,
                                        double minX, double maxX,
                                        double minY, double maxY,
                                        int maxIter) throws Exception {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return img;
    }

}
*/
package primorska.mandlbrotset.renderer;

import javafx.scene.canvas.GraphicsContext;
import java.awt.image.BufferedImage;

public interface MandelbrotRenderer {
    void render(GraphicsContext gc, int width, int height,
                double minX, double maxX, double minY, double maxY,
                double zoomFactor) throws InterruptedException;

    default void logHardwareUsage() {
        // Default no-op, override in implementations
    }

    default BufferedImage renderToImage(int width, int height,
                                        double minX, double maxX,
                                        double minY, double maxY,
                                        int maxIter) throws Exception {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
}



/*package primorska.mandlbrotset.distributed;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import primorska.mandlbrotset.renderer.MandelbrotRenderer;

import java.awt.image.BufferedImage;

public class DistributedRendererWrapper implements MandelbrotRenderer {

    @Override
    public void render(GraphicsContext gc, int width, int height,
                       double minX, double maxX, double minY, double maxY,
                       double zoomFactor) {

        // Run MPI rendering on background thread
        new Thread(() -> {
            try {
                // Call the MPI distributed render method
                BufferedImage awtImage = DistributedRenderer.renderDistributed(
                        width, height, minX, maxX, minY, maxY, 1000);

                if (awtImage != null) {
                    WritableImage fxImage = SwingFXUtils.toFXImage(awtImage, null);

                    // Update JavaFX canvas on UI thread
                    Platform.runLater(() -> {
                        gc.drawImage(fxImage, 0, 0, width, height);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}*/
package primorska.mandlbrotset.distributed;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import primorska.mandlbrotset.renderer.MandelbrotRenderer;

import java.awt.image.BufferedImage;

public class DistributedRendererWrapper implements MandelbrotRenderer {

    @Override
    public void render(GraphicsContext gc, int width, int height,
                       double minX, double maxX, double minY, double maxY,
                       double zoomFactor) throws InterruptedException {

        try {
            BufferedImage image = DistributedRenderer.renderDistributed(width, height, minX, maxX, minY, maxY, 500);

            if (image != null) {
                PixelWriter writer = gc.getPixelWriter();
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        int rgb = image.getRGB(x, y);
                        Color color = Color.rgb(
                                (rgb >> 16) & 0xFF,
                                (rgb >> 8) & 0xFF,
                                rgb & 0xFF
                        );
                        writer.setColor(x, y, color);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


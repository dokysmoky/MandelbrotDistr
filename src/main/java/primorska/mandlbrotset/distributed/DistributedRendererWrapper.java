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
}
package primorska.mandlbrotset.distributed;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import primorska.mandlbrotset.renderer.MandelbrotRenderer;
import mpi.*;

import java.awt.image.BufferedImage;

public class DistributedRendererWrapper implements MandelbrotRenderer {


    @Override
    public void logHardwareUsage() {
        int mpiNodes = 1;
        try {
            mpiNodes = MPI.COMM_WORLD.Size();
        } catch (Exception e) {
            System.out.println("MPI environment not initialized or unavailable");
        }
        System.out.println("DistributedRendererWrapper: Using " + mpiNodes + " MPI nodes");
    }


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
}*/

package primorska.mandlbrotset.distributed;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import primorska.mandlbrotset.renderer.MandelbrotRenderer;
import mpi.*;

import java.awt.image.BufferedImage;

public class DistributedRendererWrapper implements MandelbrotRenderer {

    private int rank = -1;
    private int size = -1;

    public DistributedRendererWrapper() {
        System.out.println("Constructor called on rank (before MPI.Init()): " + rank);
        System.out.flush();

        try {
            if (!MPI.Initialized()) {
                MPI.Init(new String[0]);
            }
            rank = MPI.COMM_WORLD.Rank();
            size = MPI.COMM_WORLD.Size();
            logHardwareUsage();
            System.out.println("Constructor after MPI.Init() on rank: " + rank);
            System.out.flush();

        } catch (Exception e) {
            System.out.println("MPI environment not initialized or unavailable");
            e.printStackTrace();
        }
    }

    @Override
    public void logHardwareUsage() {
        int mpiNodes = size;  // same as MPI.COMM_WORLD.Size()
        int cores = Runtime.getRuntime().availableProcessors();

        // Log MPI nodes info and memory usage for each rank
        System.out.println("Rank " + rank + "/" + size + ": DistributedRendererWrapper active.");
        System.out.flush();
        System.out.println("Rank " + rank + ": Using " + size + " MPI nodes");
        System.out.flush();

        long usedMemoryMB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        long maxMemoryMB = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        System.out.println("Rank " + rank + ": Memory used = " + usedMemoryMB + " MB / Max = " + maxMemoryMB + " MB");
        System.out.flush();

    }

    @Override
    public void render(GraphicsContext gc, int width, int height,
                       double minX, double maxX, double minY, double maxY,
                       double zoomFactor) throws InterruptedException {
        logHardwareUsage();

        try {
            // Log hardware usage at the start of rendering

            BufferedImage image = DistributedRenderer.renderDistributed(width, height, minX, maxX, minY, maxY, 500);

            if (image != null && rank == 0) {
                // Only rank 0 updates the GraphicsContext
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
            } else if (rank != 0) {
                System.out.println("Rank " + rank + ": Image is null or not responsible for drawing.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

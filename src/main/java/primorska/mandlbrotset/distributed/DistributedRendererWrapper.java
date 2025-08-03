
/*package primorska.mandlbrotset.distributed;

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

    public BufferedImage renderToImage(int width, int height,
                                       double minX, double maxX,
                                       double minY, double maxY,
                                       int maxIter) {
        try {
            // Call the static method that might throw Exception
            return DistributedRenderer.renderDistributed(width, height, minX, maxX, minY, maxY, maxIter);
        } catch (Exception e) {
            e.printStackTrace();
            // Return null or some fallback value if error occurs
            return null;
        }
    }


}
*/
/*package primorska.mandlbrotset.distributed;

import javafx.application.Platform;
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
            // Initialize MPI if not already initialized - you may want to do this outside of constructor in main app
            if (!MPI.Initialized()) {
                MPI.Init(new String[0]);
            }

            rank = MPI.COMM_WORLD.Rank();
            size = MPI.COMM_WORLD.Size();

            System.out.println("Constructor after MPI.Init() on rank: " + rank);
            System.out.flush();

            logHardwareUsage();

        } catch (Exception e) {
            System.out.println("MPI environment not initialized or unavailable");
            e.printStackTrace();
        }
    }

    @Override
    public void logHardwareUsage() {
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
            // Perform distributed rendering, max iterations set to 500 here (can be changed)
            BufferedImage image = renderToImage(width, height, minX, maxX, minY, maxY, 500);


            if (image != null && rank == 0) {
                Platform.runLater(() -> {
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
                });

        } else if (rank != 0) {
                System.out.println("Rank " + rank + ": Not responsible for drawing image.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public BufferedImage renderToImage(int width, int height,
                                       double minX, double maxX,
                                       double minY, double maxY,
                                       int maxIter) {
        try {
            // Call the static method that performs distributed rendering
            return DistributedRenderer.renderDistributed(width, height, minX, maxX, minY, maxY, maxIter);
        } catch (Exception e) {
            e.printStackTrace();
            // Return a blank image instead of null to prevent errors downstream
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
    }
}
*//*
package primorska.mandlbrotset.distributed;

import javafx.scene.canvas.GraphicsContext;
import primorska.mandlbrotset.renderer.MandelbrotRenderer;
import mpi.*;
import primorska.mandlbrotset.MandelbrotApp;

import java.awt.image.BufferedImage;

public class DistributedRendererWrapper implements MandelbrotRenderer {

    private final int rank;
    private final int size;

    public DistributedRendererWrapper() {
        rank = MPI.COMM_WORLD.Rank();
        size = MPI.COMM_WORLD.Size();
        System.out.printf("Rank %d/%d: DistributedRendererWrapper active.%n", rank, size);
    }


    @Override
    public BufferedImage renderToImage(int width, int height, double minX, double maxX, double minY, double maxY, int maxIter) {
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int rowsPerRank = height / size;
        int startRow = rank * rowsPerRank;
        int endRow = (rank == size - 1) ? height : startRow + rowsPerRank;

        int[] pixels = new int[(endRow - startRow) * width];

        // Each rank computes its part
        for (int y = startRow; y < endRow; y++) {
            for (int x = 0; x < width; x++) {
                double zx = minX + x * (maxX - minX) / width;
                double zy = minY + y * (maxY - minY) / height;
                int iter = mandelbrot(zx, zy, maxIter);
                int color = getColor(iter, maxIter);
                pixels[(y - startRow) * width + x] = color;
            }
        }

        if (rank == 0) {
            // Rank 0: prepare full image buffer
            int[][] allPixels = new int[size][];
            allPixels[0] = pixels;

            // Receive from others
            for (int i = 1; i < size; i++) {
                int rows = (i == size - 1) ? (height - i * rowsPerRank) : rowsPerRank;
                int[] recvPixels = new int[rows * width];
                MPI.COMM_WORLD.Recv(recvPixels, 0, recvPixels.length, MPI.INT, i, 0);
                allPixels[i] = recvPixels;
            }

            // Combine into final image
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < size; i++) {
                int rowStart = i * rowsPerRank;
                int[] chunk = allPixels[i];
                for (int y = 0; y < chunk.length / width; y++) {
                    for (int x = 0; x < width; x++) {
                        image.setRGB(x, rowStart + y, chunk[y * width + x]);
                    }
                }
            }

            MandelbrotApp.setRenderedImage(image);
            return image;

        } else {
            // Worker ranks send their results to rank 0
            MPI.COMM_WORLD.Send(pixels, 0, pixels.length, MPI.INT, 0, 0);
            return null; // Workers do not return the image
        }
    }
    private int mandelbrot(double zx, double zy, int maxIter) {
        double x = 0, y = 0;
        int iter = 0;
        while (x * x + y * y < 4 && iter < maxIter) {
            double xtemp = x * x - y * y + zx;
            y = 2 * x * y + zy;
            x = xtemp;
            iter++;
        }
        return iter;
    }

    private int getColor(int iter, int maxIter) {
        if (iter == maxIter) return 0x000000; // black
        return java.awt.Color.HSBtoRGB((float) iter / maxIter, 1, 1);
    }

    private void runWorker(int width, int height,
                           double minX, double maxX, double minY, double maxY,
                           int maxIter) {
        try {
            int[] meta = new int[4];
            double[] bounds = new double[4];

            MPI.COMM_WORLD.Recv(meta, 0, 4, MPI.INT, 0, 0);
            MPI.COMM_WORLD.Recv(bounds, 0, 4, MPI.DOUBLE, 0, 3);

            int startRow = meta[0];
            int endRow = meta[1];
            width = meta[2];
            height = meta[3];

            minX = bounds[0];
            maxX = bounds[1];
            minY = bounds[2];
            maxY = bounds[3];

            int rows = endRow - startRow;
            int[] rgb = new int[rows * width];

            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < width; x++) {
                    double zx = minX + x * (maxX - minX) / width;
                    double zy = minY + (y + startRow) * (maxY - minY) / height;

                    int iter = 0;
                    double zx2 = 0, zy2 = 0, cx = zx, cy = zy;

                    while (zx2 + zy2 < 4 && iter < maxIter) {
                        double temp = zx2 - zy2 + cx;
                        zy = 2 * zx * zy + cy;
                        zx = temp;
                        zx2 = zx * zx;
                        zy2 = zy * zy;
                        iter++;
                    }

                    int color = iter == maxIter ? 0 : java.awt.Color.HSBtoRGB(iter / 256f, 1, iter / (iter + 8f));
                    rgb[y * width + x] = color;
                }
            }

            MPI.COMM_WORLD.Send(new int[]{startRow, rows}, 0, 2, MPI.INT, 0, 1);
            MPI.COMM_WORLD.Send(rgb, 0, rgb.length, MPI.INT, 0, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logHardwareUsage() {
        long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.printf("Rank %d: Memory used = %d MB / Max = %d MB%n", rank, usedMem / 1024 / 1024,
                Runtime.getRuntime().maxMemory() / 1024 / 1024);
    }

    @Override
    public void render(GraphicsContext gc, int width, int height,
                       double minX, double maxX,
                       double minY, double maxY,
                       double zoomFactor) {
        BufferedImage image = renderToImage(width, height, minX, maxX, minY, maxY, 1000);
        if (image != null) {
            javafx.scene.image.Image fxImage = convertToFxImage(image);
            javafx.application.Platform.runLater(() -> gc.drawImage(fxImage, 0, 0));
        }
    }

    private javafx.scene.image.Image convertToFxImage(BufferedImage bufferedImage) {
        java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
        try {
            javax.imageio.ImageIO.write(bufferedImage, "png", os);
            return new javafx.scene.image.Image(new java.io.ByteArrayInputStream(os.toByteArray()));
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}*/

package primorska.mandlbrotset.distributed;

import javafx.scene.canvas.GraphicsContext;
import primorska.mandlbrotset.renderer.MandelbrotRenderer;
import mpi.*;
import primorska.mandlbrotset.MandelbrotApp;

import java.awt.image.BufferedImage;

public class DistributedRendererWrapper implements MandelbrotRenderer {

    private final int rank;
    private final int size;

    public DistributedRendererWrapper() {
        rank = MPI.COMM_WORLD.Rank();
        size = MPI.COMM_WORLD.Size();
        System.out.printf("Rank %d/%d: DistributedRendererWrapper active.%n", rank, size);
    }

    @Override
    public BufferedImage renderToImage(int width, int height,
                                       double minX, double maxX,
                                       double minY, double maxY,
                                       int maxIter) {

        System.out.printf("Rank %d/%d: Starting renderToImage%n", rank, size);

        int rowsPerRank = height / size;
        int startRow = rank * rowsPerRank;
        int endRow = (rank == size - 1) ? height : startRow + rowsPerRank;

        System.out.printf("Rank %d: computing rows %d to %d%n", rank, startRow, endRow - 1);

        int[] pixels = new int[(endRow - startRow) * width];

        for (int py = startRow; py < endRow; py++) {
            for (int px = 0; px < width; px++) {
                double x0 = minX + px * (maxX - minX) / (width - 1);
                double y0 = minY + py * (maxY - minY) / (height - 1);

                int iter = mandelbrotIterations(x0, y0, maxIter);
                int color = getColor(iter, maxIter);

                pixels[(py - startRow) * width + px] = color;
            }
        }

        if (rank == 0) {
            System.out.printf("Rank 0: receiving pixel data from workers%n");

            int[] fullPixels = new int[width * height];
            System.arraycopy(pixels, 0, fullPixels, 0, pixels.length);

            for (int r = 1; r < size; r++) {
                int rStart = r * rowsPerRank;
                int rRows = (r == size - 1) ? (height - rStart) : rowsPerRank;
                int[] recvBuf = new int[rRows * width];

                MPI.COMM_WORLD.Recv(recvBuf, 0, recvBuf.length, MPI.INT, r, 0);
                System.arraycopy(recvBuf, 0, fullPixels, rStart * width, recvBuf.length);

                System.out.printf("Rank 0: received pixel data from rank %d, rows %d to %d%n",
                        r, rStart, rStart + rRows - 1);
            }

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image.setRGB(0, 0, width, height, fullPixels, 0, width);

            MandelbrotApp.setRenderedImage(image);
            System.out.println("Rank 0: renderToImage finished, returning image.");
            return image;

        } else {
            System.out.printf("Rank %d: computed pixels, ready to send %d elements%n", rank, pixels.length);
            MPI.COMM_WORLD.Send(pixels, 0, pixels.length, MPI.INT, 0, 0);
            System.out.printf("Rank %d: sent pixel data of length %d to rank 0%n", rank, pixels.length);
            return null;
        }

    }


    private int mandelbrotIterations(double x0, double y0, int maxIter) {
        double x = 0, y = 0;
        int iter = 0;
        while (x * x + y * y <= 4 && iter < maxIter) {
            double xtemp = x * x - y * y + x0;
            y = 2 * x * y + y0;
            x = xtemp;
            iter++;
        }
        return iter;
    }

    private int getColor(int iter, int maxIter) {
        if (iter == maxIter) return 0xFF000000; // black (ARGB)
        float hue = (float) iter / maxIter;
        int rgb = java.awt.Color.HSBtoRGB(hue, 1f, 1f);
        return 0xFF000000 | (rgb & 0x00FFFFFF); // set alpha to fully opaque
    }

    @Override
    public void logHardwareUsage() {
        long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.printf("Rank %d: Memory used = %d MB / Max = %d MB%n", rank, usedMem / 1024 / 1024,
                Runtime.getRuntime().maxMemory() / 1024 / 1024);
    }

    @Override
    public void render(GraphicsContext gc, int width, int height,
                       double minX, double maxX,
                       double minY, double maxY,
                       double zoomFactor) {
        BufferedImage image = renderToImage(width, height, minX, maxX, minY, maxY, 1000);
        if (image != null) {
            javafx.scene.image.Image fxImage = convertToFxImage(image);
            javafx.application.Platform.runLater(() -> gc.drawImage(fxImage, 0, 0));
        }
    }

    private javafx.scene.image.Image convertToFxImage(BufferedImage bufferedImage) {
        try (java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream()) {
            javax.imageio.ImageIO.write(bufferedImage, "png", os);
            return new javafx.scene.image.Image(new java.io.ByteArrayInputStream(os.toByteArray()));
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

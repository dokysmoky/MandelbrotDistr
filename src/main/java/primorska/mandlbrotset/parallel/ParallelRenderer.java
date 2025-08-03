
/*package primorska.mandlbrotset.parallel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import primorska.mandlbrotset.renderer.MandelbrotRenderer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelRenderer implements MandelbrotRenderer {

    @Override
    public void logHardwareUsage() {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("ParallelRenderer: Using " + cores + " CPU cores");
    }

    @Override
    public void render(GraphicsContext gc, int width, int height,
                       double minX, double maxX, double minY, double maxY,
                       double zoomFactor) throws InterruptedException {

        long startTime = System.nanoTime();

        double rangeX = (maxX - minX) / zoomFactor;
        double rangeY = (maxY - minY) / zoomFactor;

        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);

        int rowsPerThread = height / cores;

        for (int i = 0; i < cores; i++) {
            final int startY = i * rowsPerThread;
            final int endY = (i == cores - 1) ? height : startY + rowsPerThread;

            executor.submit(() -> {
                for (int py = startY; py < endY; py++) {
                    for (int px = 0; px < width; px++) {
                        double x0 = minX + px * rangeX / width;
                        double y0 = minY + py * rangeY / height;
                        double x = 0.0, y = 0.0;
                        int iteration = 0;
                        int maxIter = 1000;

                        while (x * x + y * y <= 4 && iteration < maxIter) {
                            double xtemp = x * x - y * y + x0;
                            y = 2 * x * y + y0;
                            x = xtemp;
                            iteration++;
                        }

                        Color color = (iteration < maxIter)
                                ? Color.hsb(280 - ((double) iteration / maxIter) * 280, 0.8, 1.0 - ((double) iteration / maxIter) * 0.8)
                                : Color.BLACK;

                        synchronized (gc) {
                            gc.getPixelWriter().setColor(px, py, color);
                        }
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.nanoTime();
        System.out.printf("Parallel Mandelbrot drawn in %.2f ms%n", (endTime - startTime) / 1_000_000.0);
    }
}*/
/*
package primorska.mandlbrotset.parallel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import primorska.mandlbrotset.renderer.MandelbrotRenderer;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelRenderer implements MandelbrotRenderer {

    @Override
    public void logHardwareUsage() {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("ParallelRenderer: Using " + cores + " CPU cores");
    }

    @Override
    public void render(GraphicsContext gc, int width, int height,
                       double minX, double maxX, double minY, double maxY,
                       double zoomFactor) throws InterruptedException {

        long startTime = System.nanoTime();

        gc.clearRect(0, 0, width, height);

        double rangeX = (maxX - minX) / zoomFactor;
        double rangeY = (maxY - minY) / zoomFactor;

        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);

        int rowsPerThread = height / cores;

        for (int i = 0; i < cores; i++) {
            final int startY = i * rowsPerThread;
            final int endY = (i == cores - 1) ? height : startY + rowsPerThread;

            executor.submit(() -> {
                for (int py = startY; py < endY; py++) {
                    for (int px = 0; px < width; px++) {
                        double x0 = minX + px * rangeX / width;
                        double y0 = minY + py * rangeY / height;
                        double x = 0.0, y = 0.0;
                        int iteration = 0;
                        int maxIter = 1000;

                        while (x * x + y * y <= 4 && iteration < maxIter) {
                            double xtemp = x * x - y * y + x0;
                            y = 2 * x * y + y0;
                            x = xtemp;
                            iteration++;
                        }

                        Color color = (iteration < maxIter)
                                ? Color.hsb(280 - ((double) iteration / maxIter) * 280, 0.8, 1.0 - ((double) iteration / maxIter) * 0.8)
                                : Color.BLACK;

                        synchronized (gc) {
                            gc.getPixelWriter().setColor(px, py, color);
                        }
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.nanoTime();
        System.out.printf("Parallel Mandelbrot drawn in %.2f ms%n", (endTime - startTime) / 1_000_000.0);
    }

    // --- New method for benchmarking (no GUI) ---
    public BufferedImage renderToImage(int width, int height,
                                       double minX, double maxX,
                                       double minY, double maxY,
                                       int maxIter) throws InterruptedException {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        double rangeX = (maxX - minX);
        double rangeY = (maxY - minY);

        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);

        int rowsPerThread = height / cores;

        for (int i = 0; i < cores; i++) {
            final int startY = i * rowsPerThread;
            final int endY = (i == cores - 1) ? height : startY + rowsPerThread;

            executor.submit(() -> {
                for (int py = startY; py < endY; py++) {
                    for (int px = 0; px < width; px++) {
                        double x0 = minX + px * rangeX / width;
                        double y0 = minY + py * rangeY / height;
                        double x = 0.0, y = 0.0;
                        int iteration = 0;

                        while (x * x + y * y <= 4 && iteration < maxIter) {
                            double xtemp = x * x - y * y + x0;
                            y = 2 * x * y + y0;
                            x = xtemp;
                            iteration++;
                        }

                        int rgb;
                        if (iteration == maxIter) {
                            rgb = 0x000000; // black
                        } else {
                            float hue = 280f - (280f * iteration / maxIter);
                            java.awt.Color c = java.awt.Color.getHSBColor(hue / 360f, 0.8f, 1.0f - 0.8f * iteration / maxIter);
                            rgb = (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue();
                        }

                        synchronized (image) {
                            image.setRGB(px, py, rgb);
                        }
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        return image;
    }
}*/

package primorska.mandlbrotset.parallel;

import primorska.mandlbrotset.renderer.MandelbrotRenderer;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelRenderer implements MandelbrotRenderer {

    @Override
    public void logHardwareUsage() {
        int cores = Runtime.getRuntime().availableProcessors();
        long usedMemoryMB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        long maxMemoryMB = Runtime.getRuntime().maxMemory() / (1024 * 1024);

        System.out.println("ParallelRenderer using " + cores + " threads.");
        System.out.println("Memory used: " + usedMemoryMB + " MB / Max: " + maxMemoryMB + " MB");
    }

    @Override
    public void render(javafx.scene.canvas.GraphicsContext gc, int width, int height,
                       double minX, double maxX, double minY, double maxY,
                       double zoomFactor) throws InterruptedException {
        BufferedImage img = renderToImage(width, height, minX, maxX, minY, maxY, 500);
        javafx.application.Platform.runLater(() -> {
            javafx.scene.image.WritableImage fxImage = javafx.embed.swing.SwingFXUtils.toFXImage(img, null);
            gc.clearRect(0, 0, width, height);
            gc.drawImage(fxImage, 0, 0);
        });
    }

    public BufferedImage renderToImage(int width, int height,
                                       double minX, double maxX,
                                       double minY, double maxY,
                                       int maxIter) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int y = 0; y < height; y++) {
            final int row = y;
            executor.submit(() -> {
                for (int x = 0; x < width; x++) {
                    double zx = minX + x * (maxX - minX) / width;
                    double zy = minY + row * (maxY - minY) / height;

                    int iter = mandelbrot(zx, zy, maxIter);
                    int color = getColor(iter, maxIter);
                    image.setRGB(x, row, color);
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return image;
    }

    private int mandelbrot(double zx, double zy, int maxIter) {
        double x = 0, y = 0;
        int iter = 0;
        while (x * x + y * y <= 4 && iter < maxIter) {
            double xtemp = x * x - y * y + zx;
            y = 2 * x * y + zy;
            x = xtemp;
            iter++;
        }
        return iter;
    }

    private int getColor(int iter, int maxIter) {
        if (iter == maxIter) return 0xFF000000;  // black
        float hue = iter / (float) maxIter;
        int rgb = java.awt.Color.HSBtoRGB(hue, 0.7f, 1.0f);
        return 0xFF000000 | (rgb & 0xFFFFFF);
    }
}


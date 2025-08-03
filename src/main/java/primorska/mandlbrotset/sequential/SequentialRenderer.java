
/*package primorska.mandlbrotset.sequential;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import primorska.mandlbrotset.renderer.MandelbrotRenderer;
import java.awt.image.BufferedImage;

public class SequentialRenderer implements MandelbrotRenderer {

    @Override
    public void logHardwareUsage() {
        long usedMemoryMB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        long maxMemoryMB = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        System.out.println("SequentialRenderer: Memory used = " + usedMemoryMB + " MB / Max = " + maxMemoryMB + " MB");
    }

    @Override
    public void render(GraphicsContext gc, int width, int height,
                       double minX, double maxX, double minY, double maxY,
                       double zoomFactor) {

        long startTime = System.nanoTime();

        gc.clearRect(0, 0, width, height);

        double rangeX = (maxX - minX) / zoomFactor;
        double rangeY = (maxY - minY) / zoomFactor;

        for (int px = 0; px < width; px++) {
            for (int py = 0; py < height; py++) {
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

                gc.getPixelWriter().setColor(px, py, color);
            }
        }

        long endTime = System.nanoTime();
        System.out.printf("Sequential Mandelbrot drawn in %.2f ms%n", (endTime - startTime) / 1_000_000.0);
    }
}*/
/*
package primorska.mandlbrotset.sequential;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import primorska.mandlbrotset.renderer.MandelbrotRenderer;

import java.awt.image.BufferedImage;

public class SequentialRenderer implements MandelbrotRenderer {

    @Override
    public void logHardwareUsage() {
        long usedMemoryMB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        long maxMemoryMB = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        System.out.println("SequentialRenderer: Memory used = " + usedMemoryMB + " MB / Max = " + maxMemoryMB + " MB");
    }

    @Override
    public void render(GraphicsContext gc, int width, int height,
                       double minX, double maxX, double minY, double maxY,
                       double zoomFactor) {

        long startTime = System.nanoTime();

        gc.clearRect(0, 0, width, height);

        double rangeX = (maxX - minX) / zoomFactor;
        double rangeY = (maxY - minY) / zoomFactor;

        for (int px = 0; px < width; px++) {
            for (int py = 0; py < height; py++) {
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

                gc.getPixelWriter().setColor(px, py, color);
            }
        }

        long endTime = System.nanoTime();
        System.out.printf("Sequential Mandelbrot drawn in %.2f ms%n", (endTime - startTime) / 1_000_000.0);
    }

    // --- New method for benchmarking (no GUI) ---
    public BufferedImage renderToImage(int width, int height,
                                       double minX, double maxX,
                                       double minY, double maxY,
                                       int maxIter) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        double rangeX = (maxX - minX);
        double rangeY = (maxY - minY);

        for (int px = 0; px < width; px++) {
            for (int py = 0; py < height; py++) {
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

                image.setRGB(px, py, rgb);
            }
        }

        return image;
    }
}
*/
/*
package primorska.mandlbrotset.sequential;

import java.awt.image.BufferedImage;
import java.awt.Color;

public class SequentialRenderer implements MandelbrotRenderer {

    @Override
    public void logHardwareUsage() {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Sequential Renderer: using " + cores + " core(s)");
        System.out.flush();
    }

    public void renderToImage(BufferedImage img, int width, int height,
                              double minX, double maxX, double minY, double maxY,
                              int maxIter) {
        logHardwareUsage();

        for (int y = 0; y < height; y++) {
            double cy = minY + (maxY - minY) * y / height;
            for (int x = 0; x < width; x++) {
                double cx = minX + (maxX - minX) * x / width;

                int iter = mandelbrot(cx, cy, maxIter);
                int color = Color.HSBtoRGB(iter / 256f, 1, iter > 0 ? 1 : 0);
                img.setRGB(x, y, color);
            }
        }
    }

    private int mandelbrot(double cx, double cy, int maxIter) {
        double zx = 0.0;
        double zy = 0.0;
        int iter = 0;
        while (zx * zx + zy * zy < 4.0 && iter < maxIter) {
            double temp = zx * zx - zy * zy + cx;
            zy = 2.0 * zx * zy + cy;
            zx = temp;
            iter++;
        }
        return iter;
    }
}
*/
package primorska.mandlbrotset.sequential;

import javafx.scene.canvas.GraphicsContext;
import primorska.mandlbrotset.renderer.MandelbrotRenderer;

import java.awt.image.BufferedImage;

public class SequentialRenderer implements MandelbrotRenderer {

    @Override
    public void render(GraphicsContext gc, int width, int height,
                       double minX, double maxX,
                       double minY, double maxY,
                       double zoomFactor) {
        // Your render implementation here
    }

    /*@Override
    public BufferedImage renderToImage(int width, int height,
                                       double minX, double maxX,
                                       double minY, double maxY,
                                       int maxIter) {
        // Your image rendering logic here
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }*/
    @Override
    public BufferedImage renderToImage(int width, int height,
                                       double minX, double maxX,
                                       double minY, double maxY,
                                       int maxIter) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int px = 0; px < width; px++) {
            for (int py = 0; py < height; py++) {
                // Map pixel to complex plane coordinates
                double x0 = minX + px * (maxX - minX) / (width - 1);
                double y0 = minY + py * (maxY - minY) / (height - 1);

                double x = 0;
                double y = 0;
                int iteration = 0;

                while (x * x + y * y <= 4 && iteration < maxIter) {
                    double xtemp = x * x - y * y + x0;
                    y = 2 * x * y + y0;
                    x = xtemp;
                    iteration++;
                }

                // Color based on iteration count (simple grayscale)
                int color;
                if (iteration == maxIter) {
                    color = 0xFF000000; // black
                } else {
                    int c = 255 - (iteration * 255 / maxIter);
                    color = (0xFF << 24) | (c << 16) | (c << 8) | c; // grayscale
                }

                image.setRGB(px, py, color);
            }
        }

        return image;
    }
}

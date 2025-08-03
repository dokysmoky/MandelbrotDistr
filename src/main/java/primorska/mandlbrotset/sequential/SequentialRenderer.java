
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

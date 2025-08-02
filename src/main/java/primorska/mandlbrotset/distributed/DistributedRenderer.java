
/*package primorska.mandlbrotset.distributed;

import mpi.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class DistributedRenderer {

    public static void main(String[] args) throws Exception {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int width = 800;
        int height = 600;
        int maxIter = 1000;

        double minX = -2.5, maxX = 1.5;
        double minY = -1.5, maxY = 1.5;

        double zoomFactor = 1.0;

        int rowsPerProc = height / size;
        int startY = rank * rowsPerProc;
        int endY = (rank == size - 1) ? height : startY + rowsPerProc;

        int[] pixels = new int[(endY - startY) * width];

        for (int py = startY; py < endY; py++) {
            for (int px = 0; px < width; px++) {
                double x0 = minX + px * (maxX - minX) / width;
                double y0 = minY + py * (maxY - minY) / height;

                double x = 0.0, y = 0.0;
                int iteration = 0;

                while (x * x + y * y <= 4 && iteration < maxIter) {
                    double xtemp = x * x - y * y + x0;
                    y = 2 * x * y + y0;
                    x = xtemp;
                    iteration++;
                }

                int color;
                if (iteration == maxIter) {
                    color = 0x000000;
                } else {
                    float hue = 280f - (280f * iteration / maxIter);
                    java.awt.Color c = java.awt.Color.getHSBColor(hue / 360f, 0.8f, 1.0f - 0.8f * iteration / maxIter);
                    color = (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue();
                }

                pixels[(py - startY) * width + px] = color;
            }
        }

        int[] fullPixels = null;
        int[] recvCounts = new int[size];
        int[] displs = new int[size];

        for (int i = 0; i < size; i++) {
            int sY = i * rowsPerProc;
            int eY = (i == size - 1) ? height : sY + rowsPerProc;
            recvCounts[i] = (eY - sY) * width;
            displs[i] = sY * width;
        }

        if (rank == 0) {
            fullPixels = new int[width * height];
        }

        MPI.COMM_WORLD.Gatherv(pixels, 0, pixels.length, MPI.INT,
                fullPixels, 0, recvCounts, displs, MPI.INT, 0);

        if (rank == 0) {
            System.out.println("Master received full image array, length: " + fullPixels.length);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    image.setRGB(x, y, fullPixels[y * width + x]);
                }
            }

            try {
                //File outputfile = new File("mandelbrot_distributed.png");
                File outputfile = new File("C:/Users/Korisnik/Desktop/mandelbrot_distributed.png");
                ImageIO.write(image, "png", outputfile);
                System.out.println("Saved image to mandelbrot_distributed.png");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        MPI.Finalize();
    }
}
*/
package primorska.mandlbrotset.distributed;

import mpi.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class DistributedRenderer {

    /**
     * Distributed render method.
     * Returns BufferedImage only on rank 0, other ranks return null.
     */
    public static BufferedImage renderDistributed(int width, int height,
                                                  double minX, double maxX, double minY, double maxY,
                                                  int maxIter) throws Exception {

        MPI.Init(new String[0]);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int rowsPerProc = height / size;
        int startY = rank * rowsPerProc;
        int endY = (rank == size - 1) ? height : startY + rowsPerProc;

        int[] pixels = new int[(endY - startY) * width];

        for (int py = startY; py < endY; py++) {
            for (int px = 0; px < width; px++) {
                double x0 = minX + px * (maxX - minX) / width;
                double y0 = minY + py * (maxY - minY) / height;

                double x = 0.0, y = 0.0;
                int iteration = 0;

                while (x * x + y * y <= 4 && iteration < maxIter) {
                    double xtemp = x * x - y * y + x0;
                    y = 2 * x * y + y0;
                    x = xtemp;
                    iteration++;
                }

                int color;
                if (iteration == maxIter) {
                    color = 0x000000;
                } else {
                    float hue = 280f - (280f * iteration / maxIter);
                    java.awt.Color c = java.awt.Color.getHSBColor(hue / 360f, 0.8f, 1.0f - 0.8f * iteration / maxIter);
                    color = (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue();
                }

                pixels[(py - startY) * width + px] = color;
            }
        }

        int[] fullPixels = null;
        int[] recvCounts = new int[size];
        int[] displs = new int[size];

        for (int i = 0; i < size; i++) {
            int sY = i * rowsPerProc;
            int eY = (i == size - 1) ? height : sY + rowsPerProc;
            recvCounts[i] = (eY - sY) * width;
            displs[i] = sY * width;
        }

        if (rank == 0) {
            fullPixels = new int[width * height];
        }

        MPI.COMM_WORLD.Gatherv(pixels, 0, pixels.length, MPI.INT,
                fullPixels, 0, recvCounts, displs, MPI.INT, 0);

        BufferedImage image = null;

        if (rank == 0) {
            System.out.println("Master received full image array, length: " + fullPixels.length);

            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    image.setRGB(x, y, fullPixels[y * width + x]);
                }
            }
        }

        MPI.Finalize();

        return image;
    }


    /**
     * Standalone main for testing, saves the image to desktop.
     */
    public static void main(String[] args) throws Exception {
        int width = 800;
        int height = 600;
        int maxIter = 1000;
        double minX = -2.5, maxX = 1.5;
        double minY = -1.5, maxY = 1.5;

        BufferedImage image = renderDistributed(width, height, minX, maxX, minY, maxY, maxIter);

        if (image != null) {
            File outputfile = new File("C:/Users/Korisnik/Desktop/mandelbrot_distributed.png");
            ImageIO.write(image, "png", outputfile);
            System.out.println("Saved image to mandelbrot_distributed.png");
        }
    }
}

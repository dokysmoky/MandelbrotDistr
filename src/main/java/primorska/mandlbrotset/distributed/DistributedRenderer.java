/*package primorska.mandlbrotset.distributed;

import mpi.*;
import java.awt.image.BufferedImage;

public class DistributedRenderer {


    public static BufferedImage renderDistributed(int width, int height,
                                                  double minX, double maxX,
                                                  double minY, double maxY,
                                                  int maxIter) throws Exception {
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int rowsPerRank = height / size;
        int startRow = rank * rowsPerRank;
        int endRow = (rank == size - 1) ? height : startRow + rowsPerRank;

        // Each rank computes its chunk of pixels
        int[] localPixels = new int[(endRow - startRow) * width];

        for (int y = startRow; y < endRow; y++) {
            for (int x = 0; x < width; x++) {
                double zx = minX + x * (maxX - minX) / width;
                double zy = minY + y * (maxY - minY) / height;

                int iter = 0;
                double zx2 = zx;
                double zy2 = zy;
                while (zx2 * zx2 + zy2 * zy2 < 4 && iter < maxIter) {
                    double tmp = zx2 * zx2 - zy2 * zy2 + zx;
                    zy2 = 2 * zx2 * zy2 + zy;
                    zx2 = tmp;
                    iter++;
                }
                int idx = (y - startRow) * width + x;
                localPixels[idx] = ColorMapping.toRGB(iter, maxIter);
            }
        }

        // Prepare arrays for gathering results on rank 0
        int[] allPixels = null;
        if (rank == 0) {
            allPixels = new int[width * height];
        }

        int[] recvCounts = new int[size];
        int[] displs = new int[size];

        for (int i = 0; i < size; i++) {
            int sRow = i * rowsPerRank;
            int eRow = (i == size - 1) ? height : sRow + rowsPerRank;
            recvCounts[i] = (eRow - sRow) * width;
            displs[i] = sRow * width;
        }

        // Gather pixel arrays from all ranks into allPixels on rank 0
        MPI.COMM_WORLD.Gatherv(localPixels, 0, localPixels.length, MPI.INT,
                allPixels, 0, recvCounts, displs, MPI.INT, 0);

        BufferedImage image = null;

        if (rank == 0) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    image.setRGB(x, y, allPixels[y * width + x]);
                }
            }
        }

        return image;
    }

    public class ColorMapping {
        public static int toRGB(int iter, int maxIter) {
            int color = iter == maxIter ? 0 : java.awt.Color.HSBtoRGB((float) iter / maxIter, 0.8f, 1);
            return color;
        }
    }

    public static long benchmarkDistributed(int width, int height,
                                            double minX, double maxX,
                                            double minY, double maxY,
                                            int maxIter) throws MPIException {

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int rowsPerProc = height / size;
        int startY = rank * rowsPerProc;
        int endY = (rank == size - 1) ? height : startY + rowsPerProc;

        int[] pixels = new int[(endY - startY) * width];

        long start = 0;
        if (rank == 0) start = System.currentTimeMillis();

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

        long elapsed = 0;
        if (rank == 0) {
            elapsed = System.currentTimeMillis() - start;
            System.out.println("Distributed benchmark completed in " + elapsed + " ms");
        }

        return elapsed;
    }
}*/

package primorska.mandlbrotset.distributed;

import mpi.*;
import java.awt.image.BufferedImage;

public class DistributedRenderer {

    public static BufferedImage renderDistributed(int width, int height,
                                                  double minX, double maxX,
                                                  double minY, double maxY,
                                                  int maxIter) throws MPIException {
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int rowsPerRank = height / size;
        int startRow = rank * rowsPerRank;
        int endRow = (rank == size - 1) ? height : startRow + rowsPerRank;

        // Compute pixels for this rank's chunk
        int[] localPixels = new int[(endRow - startRow) * width];

        for (int y = startRow; y < endRow; y++) {
            for (int x = 0; x < width; x++) {
                double zx = minX + x * (maxX - minX) / width;
                double zy = minY + y * (maxY - minY) / height;

                int iter = 0;
                double zx2 = zx;
                double zy2 = zy;
                while (zx2 * zx2 + zy2 * zy2 < 4 && iter < maxIter) {
                    double tmp = zx2 * zx2 - zy2 * zy2 + zx;
                    zy2 = 2 * zx2 * zy2 + zy;
                    zx2 = tmp;
                    iter++;
                }
                int idx = (y - startRow) * width + x;
                localPixels[idx] = ColorMapping.toRGB(iter, maxIter);
            }
        }

        // Prepare arrays for gathering at rank 0
        int[] allPixels = null;
        if (rank == 0) {
            allPixels = new int[width * height];
        }

        int[] recvCounts = new int[size];
        int[] displs = new int[size];

        for (int i = 0; i < size; i++) {
            int sRow = i * rowsPerRank;
            int eRow = (i == size - 1) ? height : sRow + rowsPerRank;
            recvCounts[i] = (eRow - sRow) * width;
            displs[i] = sRow * width;
        }

        // Gather all chunks into allPixels on rank 0
        MPI.COMM_WORLD.Gatherv(localPixels, 0, localPixels.length, MPI.INT,
                allPixels, 0, recvCounts, displs, MPI.INT, 0);

        if (rank == 0) {
            // Construct final BufferedImage using a single setRGB call for efficiency
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            image.setRGB(0, 0, width, height, allPixels, 0, width);
            return image;
        }

        return null;
    }

    public static class ColorMapping {
        public static int toRGB(int iter, int maxIter) {
            if (iter == maxIter) {
                return 0x000000;  // black for points inside Mandelbrot set
            }
            // Use HSB color mapping for smooth color gradient
            float hue = (float) iter / maxIter;
            return java.awt.Color.HSBtoRGB(hue, 0.8f, 1.0f);
        }
    }
}

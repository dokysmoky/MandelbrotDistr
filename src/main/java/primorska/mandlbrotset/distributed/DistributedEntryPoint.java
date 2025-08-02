/*package primorska.mandlbrotset.distributed;

import mpi.MPI;
import java.awt.image.BufferedImage;

public class DistributedEntryPoint {
    public static void main(String[] args) throws Exception {

        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        if (rank == 0) {
            System.out.println("Running distributed Mandelbrot render...");
        }

        int width = 800;
        int height = 600;
        double minX = -2.5;
        double maxX = 1.5;
        double minY = -1.5;
        double maxY = 1.5;
        int maxIter = 1000;

        BufferedImage img = DistributedRenderer.renderDistributed(width, height, minX, maxX, minY, maxY, maxIter);

        // Save or display image only on rank 0 if needed
        if (rank == 0 && img != null) {
            System.out.println("Image rendered successfully.");
            // optionally: save image here
        }

        MPI.Finalize();
    }
}
*/

package primorska.mandlbrotset.distributed;
import mpi.MPI;

public class DistributedEntryPoint {
    public static void main(String[] args) {
        try {
            MPI.Init(args);

            int rank = MPI.COMM_WORLD.Rank();
            if (rank == 0) {
                System.out.println("Running distributed Mandelbrot render...");
            }

            int width = 800;
            int height = 600;
            double minX = -2.5;
            double maxX = 1.5;
            double minY = -1.5;
            double maxY = 1.5;
            int maxIter = 1000;

            var image = DistributedRenderer.renderDistributed(width, height, minX, maxX, minY, maxY, maxIter);

            if (rank == 0 && image != null) {
                System.out.println("Image rendered successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                MPI.Init(args);

                int rank = MPI.COMM_WORLD.Rank();
                if (rank == 0) {
                    System.out.println("Running distributed Mandelbrot render...");
                }

                int width = 800;
                int height = 600;
                double minX = -2.5;
                double maxX = 1.5;
                double minY = -1.5;
                double maxY = 1.5;
                int maxIter = 1000;

                var image = DistributedRenderer.renderDistributed(width, height, minX, maxX, minY, maxY, maxIter);

                if (rank == 0 && image != null) {
                    System.out.println("Image rendered successfully.");
                }

                // Finalize only on master
                if (rank == 0) {
                    MPI.Finalize();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

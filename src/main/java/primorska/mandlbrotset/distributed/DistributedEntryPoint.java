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

                // System.out.println("Image rendered successfully.");
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
package primorska.mandlbrotset.distributed;

import mpi.MPI;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DistributedEntryPoint {
    public static void main(String[] args) {
        boolean mpiInitialized = false;

        try {
            MPI.Init(args);
            mpiInitialized = true;

            int rank = MPI.COMM_WORLD.Rank();
            System.out.println("Rank: " + rank + " started.");
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

            BufferedImage image = DistributedRenderer.renderDistributed(width, height, minX, maxX, minY, maxY, maxIter);
            System.out.println("Rank: " + rank + ", image is null: " + (image == null));

            MPI.COMM_WORLD.Barrier();

            if (rank == 0 && image != null) {
                System.out.println("Rank 0: About to save image...");
                try {
                    String userHome = System.getProperty("user.home");
                    File outputFile = new File(userHome + File.separator + "Desktop" + File.separator + "mandelbrot_distributed_output.png");
                    ImageIO.write(image, "png", outputFile);
                    System.out.println("Image saved to: " + outputFile.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("Failed to save image: " + e.getMessage());
                    e.printStackTrace();
                }
            }else if(rank == 0) {
                System.out.println("Rank 0 image is null, skipping save.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
if (mpiInitialized) {
                    MPI.Finalize();
                }


            } catch (Exception e) {
                System.err.println("MPI Finalize failed: " + e.getMessage());
            }
        }
        MPI.Finalize();
    }
}
*/
package primorska.mandlbrotset.distributed;

import mpi.MPI;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DistributedEntryPoint {

    public static void main(String[] args) {
        boolean mpiInitialized = false;

        try {
            MPI.Init(args);
            mpiInitialized = true;

            int rank = MPI.COMM_WORLD.Rank();
            System.out.println("Rank: " + rank + " started.");
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

            BufferedImage image = DistributedRenderer.renderDistributed(width, height, minX, maxX, minY, maxY, maxIter);
            System.out.println("Rank: " + rank + ", image is null: " + (image == null));

            MPI.COMM_WORLD.Barrier(); // Sync before saving and finalize

            if (rank == 0 && image != null) {
                System.out.println("Rank 0: About to save image...");
                try {
                    String userHome = System.getProperty("user.home");
                    File outputFile = new File(userHome + File.separator + "Desktop" + File.separator + "mandelbrot_distributed_output.png");
                    ImageIO.write(image, "png", outputFile);
                    System.out.println("Image saved to: " + outputFile.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("Failed to save image: " + e.getMessage());
                    e.printStackTrace();
                }
            } else if (rank == 0) {
                System.out.println("Rank 0 image is null, skipping save.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (mpiInitialized) {
                    MPI.COMM_WORLD.Barrier(); // Final sync before finalize
                    MPI.Finalize();
                }
            } catch (Exception e) {
                System.err.println("MPI Finalize failed: " + e.getMessage());
            }
        }
    }
}

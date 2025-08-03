/*package primorska.mandlbrotset;


public class MainApp {
    public static void main(String[] args) {
        MandelbrotApp.launch(MandelbrotApp.class, args);
    }
}*/

/*package primorska.mandlbrotset;

import mpi.MPI;

public class MainApp {
    public static void main(String[] args) {
        try {
            MPI.Init(args);
            int rank = MPI.COMM_WORLD.Rank();

            if (rank == 0) {
                // Only rank 0 launches the GUI app
                MandelbrotApp.launch(MandelbrotApp.class, args);
            } else {
                // All other ranks idle (wait for distributed calls)
                while (true) {
                    Thread.sleep(1000);
                }
            }

            MPI.Finalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
/*package primorska.mandlbrotset;

import mpi.MPI;
public class MainApp {
    public static void main(String[] args) {
        try {
            MPI.Init(args);
            int rank = MPI.COMM_WORLD.Rank();

            if (rank == 0) {
                MandelbrotApp.launch(MandelbrotApp.class, args);
                // Wait for GUI to close, then finalize
                MPI.COMM_WORLD.Barrier();
            } else {
                // Wait at barrier until rank 0 GUI exits
                MPI.COMM_WORLD.Barrier();
            }

            MPI.Finalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/
/*package primorska.mandlbrotset;

import mpi.MPI;

public class MainApp {
    public static void main(String[] args) {
        try {
            MPI.Init(args);
            int rank = MPI.COMM_WORLD.Rank();

            if (rank == 0) {
                Thread fxThread = new Thread(() -> MandelbrotApp.launch(MandelbrotApp.class, args));
                fxThread.setDaemon(false);
                fxThread.start();

                // Wait for the JavaFX thread to exit before proceeding
                fxThread.join();
                MPI.COMM_WORLD.Barrier();
            } else {
                // Just wait at the barrier
                MPI.COMM_WORLD.Barrier();
            }

            MPI.Finalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
/*
package primorska.mandlbrotset;

import mpi.MPI;
public class MainApp {
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();

        if (rank == 0) {
            MandelbrotApp.launch(MandelbrotApp.class, args);
        } else {
            // Worker ranks sit idle until renderDistributed() is called via MPI
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                // or handle interruption appropriately
            }

        }

        MPI.Finalize();
    }
}*/
/*package primorska.mandlbrotset;

import mpi.MPI;

public class MainApp {
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();

        // Replace this with your actual rendering call and parameters
        BufferedImage image = MandelbrotApp.renderDistributed(
                800, 800, -2.0, 1.0, -1.5, 1.5, 1000
        );

        if (rank == 0) {
            MandelbrotApp.setRenderedImage(image);
            MandelbrotApp.launch(MandelbrotApp.class, args);
        }

        MPI.Finalize();
    }
}*/
/*
package primorska.mandlbrotset;

import mpi.MPI;
import java.awt.image.BufferedImage;
import primorska.mandlbrotset.distributed.DistributedRendererWrapper;

public class MainApp {
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();

        BufferedImage image = null;

        if (rank == 0) {
            // Create DistributedRendererWrapper instance and render image
            DistributedRendererWrapper renderer = new DistributedRendererWrapper();
            int width = 800, height = 600;
            double minX = -2.5, maxX = 1.5;
            double minY = -1.5, maxY = 1.5;
            int maxIter = 500;

            // Distributed rendering - this should internally handle MPI workers communication
            image = renderer.renderToImage(width, height, minX, maxX, minY, maxY, maxIter);

            MandelbrotApp.setRenderedImage(image);
            MandelbrotApp.launch(MandelbrotApp.class, args);
        } else {
            // Workers run their MPI distributed tasks
            DistributedRendererWrapper workerRenderer = new DistributedRendererWrapper();
            //workerRenderer.waitForTasks();  // You should implement or call a method that waits/handles tasks
        }

        MPI.Finalize();
    }
}*/
package primorska.mandlbrotset;
import primorska.mandlbrotset.distributed.DistributedRendererWrapper;

import mpi.MPI;

public class MainApp {
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();

        if (rank == 0) {
            // Only rank 0 launches JavaFX app
            MandelbrotApp.launch(MandelbrotApp.class, args);
        } else {
            // Other ranks enter a waiting loop to handle distributed rendering
            DistributedRendererWrapper worker = new DistributedRendererWrapper();
            worker.runWorkerLoop();
        }

        MPI.Finalize();
    }
}



package primorska.mandlbrotset.benchmark;

import primorska.mandlbrotset.renderer.MandelbrotRenderer;
import primorska.mandlbrotset.sequential.SequentialRenderer;
import primorska.mandlbrotset.parallel.ParallelRenderer;
import primorska.mandlbrotset.distributed.DistributedRendererWrapper;

import mpi.MPI;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BenchmarkRunner {

    private static void runBenchmark() throws InterruptedException, IOException {
        // Define image sizes to test
        int[] sizes = {1000, 2000, 3000, 4000};

        PrintWriter csvWriter = null;

        // Only rank 0 writes to CSV and prints status
        if (MPI.COMM_WORLD.Rank() == 0) {
            csvWriter = new PrintWriter(new FileWriter("benchmark_results.csv"));
            csvWriter.println("width,height,sequential,parallel,distributed");
        }

        for (int size : sizes) {
            int width = size;
            int height = size;

            if (MPI.COMM_WORLD.Rank() == 0) {
                System.out.println("Running benchmark for size: " + width + "x" + height);
            }

            MandelbrotRenderer sequential = new SequentialRenderer();
            MandelbrotRenderer parallel = new ParallelRenderer();
            MandelbrotRenderer distributed = new DistributedRendererWrapper();

            // Sequential
            long start = System.nanoTime();
            BufferedImage seqImage = ((SequentialRenderer) sequential)
                    .renderToImage(width, height, -2.5, 1.5, -1.5, 1.5, 500);
            long sequentialTimeMs = (System.nanoTime() - start) / 1_000_000;

            // Parallel
            start = System.nanoTime();
            BufferedImage parImage = ((ParallelRenderer) parallel)
                    .renderToImage(width, height, -2.5, 1.5, -1.5, 1.5, 500);
            long parallelTimeMs = (System.nanoTime() - start) / 1_000_000;

            // Distributed
            start = System.nanoTime();
            BufferedImage distImage = ((DistributedRendererWrapper) distributed)
                    .renderToImage(width, height, -2.5, 1.5, -1.5, 1.5, 500);
            long distributedTimeMs = (System.nanoTime() - start) / 1_000_000;

            if (MPI.COMM_WORLD.Rank() == 0) {
                System.out.printf("Size %dx%d: Seq = %d ms, Par = %d ms, Dist = %d ms%n",
                        width, height, sequentialTimeMs, parallelTimeMs, distributedTimeMs);

                csvWriter.printf("%d,%d,%d,%d,%d%n", width, height,
                        sequentialTimeMs, parallelTimeMs, distributedTimeMs);
                csvWriter.flush();
            }

            // Synchronize all MPI processes before next iteration
            MPI.COMM_WORLD.Barrier();
        }

        if (MPI.COMM_WORLD.Rank() == 0) {
            csvWriter.close();
            System.out.println("Benchmarking complete. Results saved to benchmark_results.csv");
        }
    }

    public static void main(String[] args) {
        MPI.Init(args);
        try {
            runBenchmark();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            MPI.Finalize();
        }
    }
}

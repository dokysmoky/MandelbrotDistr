package primorska.mandlbrotset.distributed;

import mpi.MPI;

public class DistributedRenderer {
    public static void main(String[] args) {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        System.out.printf("Hello from process %d of %d%n", rank, size);

        MPI.Finalize();
    }
}

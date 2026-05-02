package uz.abdukarimov.virtualthreads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadComparison {

    static void simulateIoTask() throws InterruptedException {
        Thread.sleep(100); // DB so'rov, HTTP call simulate
    }

    static void main() throws Exception {

        int tasks = 10_000;

        // ── Platform thread pool ───────────────────────────────
        long start1 = System.currentTimeMillis();
        try (ExecutorService pool =
                     Executors.newFixedThreadPool(200)) { // max 200 thread

            List<Future<?>> f = new ArrayList<>();
            for (int i = 0; i < tasks; i++) {
                f.add(pool.submit(() -> {
                    simulateIoTask();
                    return null;
                }));
            }
            for (Future<?> future : f) future.get();
        }
        long pt = System.currentTimeMillis() - start1;

        // ── Virtual thread ─────────────────────────────────────
        long start2 = System.currentTimeMillis();
        try (ExecutorService vPool =
                     Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<?>> f = new ArrayList<>();
            for (int i = 0; i < tasks; i++) {
                f.add(vPool.submit(() -> {
                    simulateIoTask();
                    return null;
                }));
            }
            for (Future<?> future : f) future.get();
        }
        long vt = System.currentTimeMillis() - start2;

        System.out.printf("Platform thread pool : %dms%n", pt);
        System.out.printf("Virtual thread       : %dms%n", vt);
        System.out.printf("Tezlashish           : %.1fx%n", (double) pt / vt);

        // Taxminiy natija (I/O og'ir ilovada):
        // Platform thread pool :  5200ms  (200 thread, navbat kutadi)
        // Virtual thread       :   120ms  (10k thread parallel)
        // Tezlashish           :  43.3x
    }
}

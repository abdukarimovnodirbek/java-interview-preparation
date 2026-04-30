package uz.abdukarimov.atomics;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CyclicBarrierDemo {

    // ── 1. Bosqichli hisoblash ────────────────────────────────
    public static void parallelComputation() throws Exception {
        int workers = 4;
        int[] partialResults = new int[workers];
        int[] finalResults = new int[3]; // 3 ta bosqich
        AtomicInteger phase = new AtomicInteger(0);

        // Barrier action — hammasi yetganda bir marta bajariladi
        Runnable barrierAction = () -> {
            int p = phase.getAndIncrement();
            int sum = 0;
            for (int r : partialResults) sum += r;
            finalResults[p] = sum;
            System.out.printf("=== Bosqich %d tugadi, yig'indi: %d ===%n",
                    p + 1, sum);
        };

        CyclicBarrier barrier = new CyclicBarrier(workers, barrierAction);
        ExecutorService pool = Executors.newFixedThreadPool(workers);

        for (int w = 0; w < workers; w++) {
            final int wId = w;
            pool.submit(() -> {
                try {
                    // Bosqich 1
                    partialResults[wId] = wId * 10;
                    System.out.printf("Worker-%d bosqich-1: %d%n",
                            wId, partialResults[wId]);
                    barrier.await(); // barcha kelguncha kutadi

                    // Bosqich 2 — avtomatik reset bo'ldi!
                    partialResults[wId] = wId * 20;
                    System.out.printf("Worker-%d bosqich-2: %d%n",
                            wId, partialResults[wId]);
                    barrier.await();

                    // Bosqich 3
                    partialResults[wId] = wId * 30;
                    System.out.printf("Worker-%d bosqich-3: %d%n",
                            wId, partialResults[wId]);
                    barrier.await();

                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);

        System.out.println("\nBarcha bosqich natijalari:");
        for (int i = 0; i < finalResults.length; i++) {
            System.out.printf("  Bosqich %d: %d%n", i + 1, finalResults[i]);
        }
    }

    // ── 2. CyclicBarrier vs CountDownLatch farqi ──────────────
    /*
     CountDownLatch:
       - Bir martalik, reset bo'lmaydi
       - Bir yo'nalish: N → 0
       - Turli sondagi threadlar ishlatishi mumkin
       - Qachon: "N task tugasin, keyin davom et"

     CyclicBarrier:
       - Qayta ishlatiladigan (cyclic!)
       - Hammasi BIR nuqtaga yetganda davom etadi
       - Bir xil sondagi threadlar
       - Qachon: "Hammasi shu bosqichni tugatsin, keyin navbatiga"
    */
}
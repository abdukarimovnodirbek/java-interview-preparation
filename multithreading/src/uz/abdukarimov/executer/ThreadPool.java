package uz.abdukarimov.executer;

import java.time.LocalTime;
import java.util.concurrent.*;

public class ThreadPool {

    static void main() throws InterruptedException {
        // ══════════════════════════════════════════════════════════════
        // 3.1 FixedThreadPool
        // ══════════════════════════════════════════════════════════════
        // Aniq N ta thread — hammasi doimiy ishlaydi
        ExecutorService fixed = Executors.newFixedThreadPool(4);

        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            fixed.submit(() -> {
                System.out.printf("Task-%d → [%s]%n",
                        taskId, Thread.currentThread().getName());
                Thread.sleep(500);
                return taskId * 10;
            });
        }

        fixed.shutdown();
        fixed.awaitTermination(10, TimeUnit.SECONDS);
        // 4 ta thread, 10 ta task — har biri navbat bilan bajariladi

        // ══════════════════════════════════════════════════════════════
        // 3.2 CachedThreadPool
        // ══════════════════════════════════════════════════════════════
        // Thread soni avtomatik — kerak bo'lsa yaratadi, bo'sh tursa o'chiradi
        ExecutorService cached = Executors.newCachedThreadPool();

        for (int i = 1; i <= 20; i++) {
            final int id = i;
            cached.submit(() -> {
                System.out.println("Task-" + id + " → " +
                        Thread.currentThread().getName());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        // 20 ta task bir vaqtda kelsa — 20 ta thread yaratiladi!
        // ⚠️ Ko'p task bo'lsa xavfli — OutOfMemoryError mumkin

        cached.shutdown();

        // ══════════════════════════════════════════════════════════════
        // 3.3 SingleThreadExecutor
        // ══════════════════════════════════════════════════════════════
        // Faqat 1 ta thread — tartib kafolatlanadi
        ExecutorService single = Executors.newSingleThreadExecutor();

        single.submit(() -> System.out.println("1-bosqich: Ma'lumot yuklash"));
        single.submit(() -> System.out.println("2-bosqich: Qayta ishlash"));
        single.submit(() -> System.out.println("3-bosqich: Saqlash"));
        // Har doim bu tartibda bajariladi!

        single.shutdown();

        // ══════════════════════════════════════════════════════════════
        // 3.4 ScheduledThreadPool
        // ══════════════════════════════════════════════════════════════
        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(2);

        // 1. Bir marta, 3 soniyadan keyin
        scheduler.schedule(
                () -> System.out.println("Bir martalik task"),
                3, TimeUnit.SECONDS
        );

        // 2. Har 2 soniyada (oldingi task tugamasdan ham)
        scheduler.scheduleAtFixedRate(
                () -> System.out.println("Fixed rate: " + LocalTime.now()),
                0,  // boshlang'ich kechikish
                2,  // davr
                TimeUnit.SECONDS
        );

        // 3. Oldingi task tugagandan 2 soniya keyin
        scheduler.scheduleWithFixedDelay(
                () -> System.out.println("Fixed delay: " + LocalTime.now()),
                0, 2, TimeUnit.SECONDS
        );

        Thread.sleep(10_000);
        scheduler.shutdown();

        // ══════════════════════════════════════════════════════════════
        // 4. ThreadPoolExecutor — To'liq Nazorat
        // ══════════════════════════════════════════════════════════════
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,                              // corePoolSize
                6,                              // maximumPoolSize
                30L,                            // keepAliveTime
                TimeUnit.SECONDS,               // keepAliveTime birligi
                new ArrayBlockingQueue<>(10),   // workQueue
                _ -> null,      // threadFactory
                new ThreadPoolExecutor.CallerRunsPolicy() // rejectionHandler
        );
    }

    // To'g'ri shutdown pattern
    public static void shutdownGracefully(ExecutorService pool) {
        pool.shutdown();                      // yangi task qabul qilmaydi
        try {
            if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
                pool.shutdownNow();           // kuch bilan to'xtatish
                if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.err.println("Pool to'xtamadi!");
                }
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

package uz.abdukarimov.atomics;

import java.util.Map;
import java.util.concurrent.*;

public class CountDownLatchDemo {

    // ── 1. Asosiy ishlatish ────────────────────────────────────
    public static void basicExample() throws InterruptedException {

        int taskCount = 5;
        CountDownLatch latch = new CountDownLatch(taskCount);
        ExecutorService pool = Executors.newFixedThreadPool(taskCount);

        System.out.println("Barcha tasklar boshlanmoqda...");

        for (int i = 1; i <= taskCount; i++) {
            final int id = i;
            pool.submit(() -> {
                try {
                    System.out.printf("Task-%d boshlandi [%s]%n",
                            id, Thread.currentThread().getName());
                    Thread.sleep(id * 200L); // turli vaqt
                    System.out.printf("Task-%d tugadi%n", id);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown(); // har doim finally da!
                }
            });
        }

        latch.await();            // 0 ga yetguncha kutadi
        // latch.await(10, TimeUnit.SECONDS); // timeout bilan
        System.out.println("Barcha tasklar tugadi! Davom etilmoqda...");
        pool.shutdown();
    }

    // ── 2. Parallel ma'lumot yuklash ──────────────────────────
    public static Map<String, Object> loadDashboard(String userId)
            throws InterruptedException {

        Map<String, Object> results = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(4);
        ExecutorService pool = Executors.newFixedThreadPool(4);

        pool.submit(() -> {
            try {
                results.put("user", fetchUser(userId));
            } finally {
                latch.countDown();
            }
        });
        pool.submit(() -> {
            try {
                results.put("orders", fetchOrders(userId));
            } finally {
                latch.countDown();
            }
        });
        pool.submit(() -> {
            try {
                results.put("balance", fetchBalance(userId));
            } finally {
                latch.countDown();
            }
        });
        pool.submit(() -> {
            try {
                results.put("notifs", fetchNotifications(userId));
            } finally {
                latch.countDown();
            }
        });

        boolean allDone = latch.await(5, TimeUnit.SECONDS);
        if (!allDone) {
            System.err.println("Timeout! Faqat " +
                    results.size() + " ta yuklandi");
        }
        pool.shutdown();
        return results;
    }

    // ── 3. Start signal — hammani bir vaqtda boshlash ─────────
    public static void startRace() throws InterruptedException {

        int runners = 8;
        CountDownLatch startSignal = new CountDownLatch(1); // starter
        CountDownLatch finishLine = new CountDownLatch(runners);

        for (int i = 1; i <= runners; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    System.out.printf("Runner-%d tayyor, signal kutmoqda%n", id);
                    startSignal.await(); // hammasi shu yerda turadi
                    System.out.printf("Runner-%d yugurdi!%n", id);
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.printf("Runner-%d finish!%n", id);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLine.countDown();
                }
            }).start();
        }

        Thread.sleep(500);
        System.out.println("Tayor... START!");
        startSignal.countDown();  // hammasi bir vaqtda starts!

        finishLine.await();
        System.out.println("Poyga tugadi!");
    }

    private static String fetchUser(String id) {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        return "User:" + id;
    }

    private static String fetchOrders(String id) {
        try {
            Thread.sleep(150);
        } catch (Exception e) {
        }
        return "Orders:" + id;
    }

    private static Double fetchBalance(String id) {
        try {
            Thread.sleep(80);
        } catch (Exception e) {
        }
        return 1_500_000.0;
    }

    private static Integer fetchNotifications(String id) {
        try {
            Thread.sleep(60);
        } catch (Exception e) {
        }
        return 5;
    }
}
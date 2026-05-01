package uz.abdukarimov.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// ThreadMXBean bilan dastur ichida deadlock aniqlash
public class DeadlockDetector {

    private final ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "DeadlockDetector");
                t.setDaemon(true);
                return t;
            });

    public void startMonitoring(long intervalSeconds) {
        scheduler.scheduleAtFixedRate(
                this::detectDeadlock,
                intervalSeconds,
                intervalSeconds,
                TimeUnit.SECONDS
        );
        System.out.println("Deadlock monitor ishga tushdi");
    }

    private void detectDeadlock() {
        // Deadlock bo'lgan thread ID larini qaytaradi
        long[] deadlockedIds = mxBean.findDeadlockedThreads();

        if (deadlockedIds == null) return; // deadlock yo'q

        System.err.println("\n!!! DEADLOCK ANIQLANDI !!!");
        System.err.println("Vaqt: " + LocalDateTime.now());

        ThreadInfo[] threadInfos =
                mxBean.getThreadInfo(deadlockedIds, true, true);

        for (ThreadInfo info : threadInfos) {
            System.err.println("\nThread: " + info.getThreadName());
            System.err.println("Holat: " + info.getThreadState());
            System.err.println("Kutayotgan lock: " +
                    info.getLockName());
            System.err.println("Lock egasi: " +
                    info.getLockOwnerName());

            System.err.println("Stack trace:");
            for (StackTraceElement ste : info.getStackTrace()) {
                System.err.println("  at " + ste);
            }
        }

        // Alert yuborish, log yozish, yoki dasturni to'xtatish
        notifyOncall("Deadlock aniqlandi! " +
                deadlockedIds.length + " ta thread bloklangan");
    }

    private void notifyOncall(String message) {
        System.err.println("ALERT: " + message);
        // Email, Slack, PagerDuty...
    }

    public void stop() {
        scheduler.shutdown();
    }

    // ── Test ──────────────────────────────────────────────────
    static void main() throws Exception {

        DeadlockDetector detector = new DeadlockDetector();
        detector.startMonitoring(3); // har 3 soniyada tekshir

        // Deadlock yaratamiz
        Object lockA = new Object();
        Object lockB = new Object();

        new Thread(() -> {
            synchronized (lockA) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                synchronized (lockB) { /* ... */ }
            }
        }, "T1-deadlock").start();

        new Thread(() -> {
            synchronized (lockB) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                synchronized (lockA) { /* ... */ }
            }
        }, "T2-deadlock").start();

        Thread.sleep(10_000); // 10 soniya kuting
        detector.stop();
    }
}
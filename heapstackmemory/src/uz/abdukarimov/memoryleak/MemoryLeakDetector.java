package uz.abdukarimov.memoryleak;

import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MemoryLeakDetector {

    private static final long WARN_THRESHOLD_MB = 100;
    private static final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "MemoryMonitor");
                t.setDaemon(true);
                return t;
            });

    private static long lastHeapMB = 0;

    public static void startMonitoring() {
        scheduler.scheduleAtFixedRate(() -> {
            Runtime rt = Runtime.getRuntime();
            long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1_048_576;
            long maxMB = rt.maxMemory() / 1_048_576;
            long growthMB = usedMB - lastHeapMB;

            System.out.printf("[Memory] used=%dMB max=%dMB " +
                            "growth=+%dMB (%.0f%%)%n",
                    usedMB, maxMB, growthMB,
                    (double) usedMB / maxMB * 100);

            if (growthMB > WARN_THRESHOLD_MB) {
                System.err.println("OGOHLANTIRISH: Heap " +
                        growthMB + "MB o'sdi — memory leak bo'lishi mumkin!");
                triggerHeapDump();
            }

            lastHeapMB = usedMB;
        }, 30, 30, TimeUnit.SECONDS);
    }

    private static void triggerHeapDump() {
        try {
            String path = "/tmp/auto_heap_" +
                    System.currentTimeMillis() + ".hprof";
            // MXBean orqali heap dump
            com.sun.management.HotSpotDiagnosticMXBean bean =
                    ManagementFactory.newPlatformMXBeanProxy(
                            ManagementFactory.getPlatformMBeanServer(),
                            "com.sun.management:type=HotSpotDiagnostic",
                            com.sun.management.HotSpotDiagnosticMXBean.class);
            bean.dumpHeap(path, true); // true = faqat tirik ob'ektlar
            System.out.println("Heap dump saqlandi: " + path);
        } catch (Exception e) {
            System.err.println("Heap dump xato: " + e.getMessage());
        }
    }
}
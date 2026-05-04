package uz.abdukarimov.memory;

// ── Runtime xotira monitoringi ────────────────────────────────
public class MemoryMonitor {
    public static void printMemoryStatus() {
        Runtime rt = Runtime.getRuntime();
        long total = rt.totalMemory() / 1024 / 1024;
        long free = rt.freeMemory() / 1024 / 1024;
        long used = total - free;
        long max = rt.maxMemory() / 1024 / 1024;

        System.out.printf(
                "Heap: used=%dMB / total=%dMB / max=%dMB (%.0f%%)%n",
                used, total, max, (double) used / max * 100);
    }
}
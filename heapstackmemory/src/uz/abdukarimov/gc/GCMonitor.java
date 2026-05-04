package uz.abdukarimov.gc;

// GC Logging flaglari (tashxis uchun)
// -Xlog:gc*              → batafsil GC log
// -Xlog:gc=info          → qisqa GC log
// -verbose:gc            → oddiy GC info

// GC ni kodda chaqirish (faqat maslahat — JVM bajarmasligi mumkin)
// System.gc();
// Runtime.getRuntime().gc();

// GC ma'lumotlarini runtime da olish

import java.lang.management.*;

public class GCMonitor {
    public static void printGCInfo() {
        for (GarbageCollectorMXBean gc :
                ManagementFactory.getGarbageCollectorMXBeans()) {
            System.out.printf("GC: %-30s count=%-6d time=%dms%n",
                    gc.getName(),
                    gc.getCollectionCount(),
                    gc.getCollectionTime());
        }

        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memory.getHeapMemoryUsage();
        System.out.printf("Heap: used=%dMB max=%dMB%n",
                heap.getUsed() / 1024 / 1024,
                heap.getMax() / 1024 / 1024);
    }
}
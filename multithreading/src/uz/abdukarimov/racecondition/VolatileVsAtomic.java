package uz.abdukarimov.racecondition;

import java.util.concurrent.atomic.AtomicInteger;

public class VolatileVsAtomic {
    volatile int counter = 0;

    // ── volatile YETARLI holatlar ──────────────────────────────

    // 1. Flag — bitta thread yozadi, boshqalar o'qiydi
    volatile boolean stopFlag = false;

    // 2. Singleton double-checked locking
    private static volatile VolatileVsAtomic instance;

    public static VolatileVsAtomic getInstance() {
        if (instance == null) {                    // 1-tekshiruv (lock yo'q)
            synchronized (VolatileVsAtomic.class) {
                if (instance == null) {            // 2-tekshiruv (lock bilan)
                    instance = new VolatileVsAtomic();
                }
            }
        }
        return instance;
    }

    // ── volatile YETARSIZ holatlar ─────────────────────────────

    // ❌ counter++ — atomic EMAS!
    // (read + increment + write = 3 qadam)
    public void unsafeIncrement() {
        counter++;  // race condition bor!
    }

    // ✅ To'g'ri: synchronized ishlatish
    public synchronized void safeIncrement() {
        counter++;
    }

    // ✅ Yoki AtomicInteger
    AtomicInteger atomicCounter = new AtomicInteger(0);

    public void atomicIncrement() {
        atomicCounter.incrementAndGet(); // atomic, lock yo'q
    }
}

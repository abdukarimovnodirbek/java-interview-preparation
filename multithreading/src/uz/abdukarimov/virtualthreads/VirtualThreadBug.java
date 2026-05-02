package uz.abdukarimov.virtualthreads;

import java.util.concurrent.locks.ReentrantLock;

public class VirtualThreadBug {
    // ── Pinning holat 1: synchronized blok ichida I/O ─────────────
// ❌ YOMON — virtual thread pinned bo'ladi!
    public synchronized void badMethod() {
        callDatabase(); // bu yerda carrier thread bloklanadi!
    }

    private void callDatabase() {
    }

    // ✅ YAXSHI — ReentrantLock ishlatish
    private final ReentrantLock lock = new ReentrantLock();

    public void goodMethod() {
        lock.lock();
        try {
            callDatabase(); // virtual thread park bo'ladi — OK!
        } finally {
            lock.unlock();
        }
    }

// ── Pinning holat 2: native method ichida ─────────────────────
// JNI/native calls ham pinning qiladi — bu o'zgartirib bo'lmaydi

// ── Pinning ni aniqlash ────────────────────────────────────────
// JVM flag bilan:
// -Djdk.tracePinnedThreads=full
// yoki short:
// -Djdk.tracePinnedThreads=short

// ── Virtual Thread uchun qoidalar ─────────────────────────────
// 1. synchronized → ReentrantLock ga almashtirish
// 2. ThreadLocal → ScopedValue (Java 21) ishlatish
// 3. Thread.sleep() → OK (park qiladi, pinning emas)
// 4. CPU-intensive ish uchun platform thread afzal
// 5. BlockingQueue → OK (virtual thread bilan ishlaydi)

// ── ScopedValue (Java 21) — ThreadLocal o'rniga ───────────────
// ThreadLocal virtual thread bilan ishlaydi,
// lekin ScopedValue tezroq va xavfsizroq

    static final ScopedValue<String> USER_ID = ScopedValue.newInstance();

    public static void handleRequest(String userId) {
        ScopedValue.where(USER_ID, userId).run(() -> {
            processRequest();
        });
    }

    public static void processRequest() {
        String uid = USER_ID.get(); // istalgan joyda o'qish
        System.out.println("Foydalanuvchi: " + uid);
    }
}

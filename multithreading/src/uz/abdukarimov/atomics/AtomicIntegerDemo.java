package uz.abdukarimov.atomics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {
    static void main() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);

// ── Asosiy operatsiyalar ───────────────────────────────────────
        counter.get();                    // 0  — qiymatni o'qish
        counter.set(10);                  // qiymatni o'rnatish
        counter.getAndSet(20);            // eski qaytaradi, yangisini o'rnatadi → 10

// ── Increment / Decrement ──────────────────────────────────────
        counter.incrementAndGet();        // ++i → 21
        counter.getAndIncrement();        // i++ → 21, lekin 21 qaytaradi
        counter.decrementAndGet();        // --i
        counter.getAndDecrement();        // i--

// ── Qo'shish ──────────────────────────────────────────────────
        counter.addAndGet(5);             // counter += 5, yangi qaytaradi
        counter.getAndAdd(5);             // counter += 5, eski qaytaradi

// ── CAS to'g'ridan ────────────────────────────────────────────
        boolean ok = counter.compareAndSet(26, 100); // expected=26, new=100
        System.out.println(ok);    // true (26 bo'lsa)

// ── updateAndGet / accumulateAndGet (Java 8+) ─────────────────
        counter.updateAndGet(n -> n * 2);              // n → n*2
        counter.accumulateAndGet(3, Integer::sum);     // n → n+3

// ── Thread-safe counter misoli ────────────────────────────────
        AtomicInteger hits = new AtomicInteger();

        ExecutorService pool = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 10_000; i++) {
            pool.submit(hits::incrementAndGet);
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println(hits.get()); // har doim 10000
    }
}

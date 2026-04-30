package uz.abdukarimov.atomics;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

public class AtomicIntegerArrayDemo {
    static void main() {
        // ── AtomicIntegerArray — thread-safe massiv ───────────────────
        AtomicIntegerArray scores = new AtomicIntegerArray(10);
        scores.set(0, 100);
        scores.incrementAndGet(3);        // scores[3]++
        scores.addAndGet(5, 50);          // scores[5] += 50
        scores.compareAndSet(7, 0, 200);  // scores[7]: 0→200

// ── LongAdder — yuqori contention uchun tezroq ────────────────
// AtomicLong: barcha thread bir hujayraga yozadi (contention!)
// LongAdder: har thread o'z hujayraga yozadi, yig'ish oxirida

        LongAdder requestCount = new LongAdder();

// Ko'p thread parallel increment qiladi — contention yo'q
        requestCount.increment();    // thread-local cell
        requestCount.add(5);

        long total = requestCount.sum();       // barcha cell lar yig'indisi
        requestCount.reset();                  // nolga qaytarish
        long sumAndReset = requestCount.sumThenReset();

// LongAccumulator — custom operatsiya bilan
        LongAccumulator maxValue = new LongAccumulator(Long::max, Long.MIN_VALUE);
        maxValue.accumulate(100);
        maxValue.accumulate(500);
        maxValue.accumulate(200);
        System.out.println(maxValue.get()); // 500

// Qachon nima ishlatish:
// AtomicInteger/Long  → o'qish ko'p, yozish kam
// LongAdder           → yozish ko'p (counter, statistika)
// LongAccumulator     → max/min/custom yig'ish
    }
}

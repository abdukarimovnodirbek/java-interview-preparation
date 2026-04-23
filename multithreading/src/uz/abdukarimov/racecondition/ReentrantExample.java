package uz.abdukarimov.racecondition;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantExample {
    private final ReentrantLock lock = new ReentrantLock();

    public void outer() {
        lock.lock();            // count: 1
        try {
            System.out.println("outer: " + lock.getHoldCount()); // 1
            inner();            // bir xil thread qayta kirishi mumkin!
        } finally {
            lock.unlock();      // count: 0
        }
    }

    public void inner() {
        lock.lock();            // count: 2 (deadlock EMAS!)
        try {
            System.out.println("inner: " + lock.getHoldCount()); // 2
        } finally {
            lock.unlock();      // count: 1
        }
    }
}

// synchronized ham reentrant — bir xil xulq
// ── synchronized ─────────────────────────────────────────────
// + Sodda, kam kod
// + JVM tomonidan optimallashtirilgan
// + try-finally shart emas
// - tryLock yo'q
// - timeout yo'q
// - interrupt yo'q
// - bir nechta Condition yo'q

//synchronized (obj) {
//        // ish
//        }

// ── ReentrantLock ─────────────────────────────────────────────
// + tryLock() — kutmasdan sinash
// + tryLock(time) — timeout bilan
// + lockInterruptibly() — interrupt ga javob
// + bir nechta Condition
// + isFair(true) — FIFO tartib
// - try-finally SHART (unlock unutilsa — deadlock!)
// - Ko'proq kod

//        lock.lock();
//  try { /* ish */ } finally { lock.unlock(); }
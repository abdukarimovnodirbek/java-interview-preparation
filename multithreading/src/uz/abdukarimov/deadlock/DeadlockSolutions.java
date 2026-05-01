package uz.abdukarimov.deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockSolutions {

    private final ReentrantLock lockA = new ReentrantLock();
    private final ReentrantLock lockB = new ReentrantLock();

    // ── Yechim 1: Tartibli lock olish ─────────────────────────
    // Har doim A → B tartibida (hech qachon B → A)
    public void safeMethod1() {
        synchronized (lockA) {       // har doim AVVAL A
            synchronized (lockB) {   // keyin B
                System.out.println("Ishlamoqda — deadlock yo'q");
            }
        }
    }

    // ── Yechim 2: tryLock timeout bilan ───────────────────────
    public boolean safeMethod2() throws InterruptedException {
        boolean gotA = false, gotB = false;
        try {
            gotA = lockA.tryLock(300, TimeUnit.MILLISECONDS);
            gotB = lockB.tryLock(300, TimeUnit.MILLISECONDS);

            if (gotA && gotB) {
                System.out.println("Ikkala lock olindi");
                return true;
            }
            return false; // ololmadim — finally da bo'shatiladi

        } finally {
            if (gotA) lockA.unlock();
            if (gotB) lockB.unlock();
        }
    }

    // ── Yechim 3: System.identityHashCode bilan tartib ────────
    // Object ID si kichik bo'lganini birinchi oling
    public static void orderedLock(Object obj1, Object obj2,
                                   Runnable task) {
        Object first = System.identityHashCode(obj1)
                < System.identityHashCode(obj2) ? obj1 : obj2;
        Object second = first == obj1 ? obj2 : obj1;

        synchronized (first) {
            synchronized (second) {
                task.run();
            }
        }
    }

    // ── Yechim 4: Lock-free — AtomicReference ─────────────────
    // Lock umuman ishlatmaslik
    private final AtomicReference<String> sharedState =
            new AtomicReference<>("initial");

    public void lockFreeUpdate(String newValue) {
        String current;
        do {
            current = sharedState.get();
        } while (!sharedState.compareAndSet(current, newValue));
        // CAS — lock kerak emas!
    }
}
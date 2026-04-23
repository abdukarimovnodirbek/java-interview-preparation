package uz.abdukarimov.racecondition;

public class Counter {
    private int count = 0;

    // ── 1. Synchronized instance metod ────────────────────────
    public synchronized void increment() {
        count++;   // this.monitor lock ostida
    }

    // ── 2. Synchronized blok — kamroq lock (tezroq) ───────────
    public void incrementBlock() {
        // lock oldingi ish ...
        synchronized (this) {
            count++;    // faqat zarur qism lock ostida
        }
        // lock keyingi ish ...
    }

    // ── 3. Alohida lock object — yaxshi amaliyot ──────────────
    private final Object lock = new Object();

    public void incrementLock() {
        synchronized (lock) {
            count++;
        }
    }

    // ── 4. Static synchronized ────────────────────────────────
    private static int total = 0;

    public static synchronized void addTotal(int n) {
        total += n;   // Counter.class monitor
    }

    public int getCount() { return count; }
}
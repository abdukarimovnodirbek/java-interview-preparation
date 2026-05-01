package uz.abdukarimov.deadlock;

public class DeadlockDemo {

    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();

    // ── Klassik deadlock: T1 = A→B, T2 = B→A ─────────────────
    public static void createDeadlock() {

        Thread thread1 = new Thread(() -> {
            System.out.println("T1: LOCK_A ni olmoqchi...");
            synchronized (LOCK_A) {
                System.out.println("T1: LOCK_A olindi, LOCK_B kutmoqda");
                try {
                    Thread.sleep(100);
                } // T2 LOCK_B olguncha kutamiz
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                synchronized (LOCK_B) {   // ← T2 bu lockni ushlab turibdi!
                    System.out.println("T1: LOCK_B olindi (bu hech qachon chiqmaydi)");
                }
            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            System.out.println("T2: LOCK_B ni olmoqchi...");
            synchronized (LOCK_B) {
                System.out.println("T2: LOCK_B olindi, LOCK_A kutmoqda");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                synchronized (LOCK_A) {   // ← T1 bu lockni ushlab turibdi!
                    System.out.println("T2: LOCK_A olindi (bu hech qachon chiqmaydi)");
                }
            }
        }, "Thread-2");

        thread1.start();
        thread2.start();

        // Dastur shu yerda abadiy qotib qoladi!
        // jstack <pid> yoki VisualVM bilan tekshirish kerak
    }

    static void main() {
        System.out.println("Deadlock yaratilmoqda...");
        createDeadlock();
        System.out.println("Bu qator hech qachon chiqmaydi!");
    }
}
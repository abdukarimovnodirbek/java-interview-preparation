package uz.abdukarimov.lifecycle;

public class LifecycleDemo {
    static void main() throws InterruptedException {

        Object lock = new Object();

        Thread t = new Thread(() -> {
            try {
                synchronized (lock) {
                    lock.wait(2000);  // WAITING ga o'tadi
                }
                Thread.sleep(1000);   // TIMED_WAITING
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        System.out.println(t.getState()); // NEW

        t.start();
        Thread.sleep(100);
        System.out.println(t.getState()); // WAITING

        synchronized (lock) {
            lock.notifyAll();
        }
        Thread.sleep(100);
        System.out.println(t.getState()); // TIMED_WAITING

        t.join();
        System.out.println(t.getState()); // TERMINATED
    }
}

//        ┌────────────────┬──────────────────────────────────────────────────┐
//        │ Holat          │ Sabab                                            │
//        ├────────────────┼──────────────────────────────────────────────────┤
//        │ NEW            │ new Thread() — hali start() chaqirilmagan        │
//        │ RUNNABLE       │ start() chaqirilgan, CPU da ishlayapti/kutayapti │
//        │ BLOCKED        │ synchronized lock ni kutayapti                   │
//        │ WAITING        │ wait() / join() — muddatsiz kutish               │
//        │ TIMED_WAITING  │ sleep(n) / wait(n) / join(n) — muddatli kutish   │
//        │ TERMINATED     │ run() tugadi yoki exception otildi               │
//        └────────────────┴──────────────────────────────────────────────────┘
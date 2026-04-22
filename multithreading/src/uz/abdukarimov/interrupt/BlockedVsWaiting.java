package uz.abdukarimov.interrupt;

public class BlockedVsWaiting {

    static final Object lock = new Object();

    // BLOCKED — synchronized lock kutish
    static void blockedDemo() {
        Runnable task = () -> {
            synchronized (lock) {          // lock olishga harakat
                // agar boshqa thread lock da bo'lsa → BLOCKED
                System.out.println(Thread.currentThread().getName()
                        + " lock oldi");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Thread t1 = new Thread(task, "T1");
        Thread t2 = new Thread(task, "T2"); // T1 tugamaguncha BLOCKED

        t1.start();
        t2.start();
    }

    // WAITING — wait() chaqirilganda
    static void waitingDemo() throws InterruptedException {
        Thread waiter = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("Waiter kutmoqda...");
                    lock.wait();            // → WAITING (muddatsiz)
                    System.out.println("Waiter davom etmoqda");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread notifier = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (lock) {
                System.out.println("Notifier xabar bermoqda");
                lock.notify();              // waiterni uyg'otish
            }
        });

        waiter.start();
        Thread.sleep(100);
        System.out.println("Waiter holati: " + waiter.getState()); // WAITING
        notifier.start();
    }
}
